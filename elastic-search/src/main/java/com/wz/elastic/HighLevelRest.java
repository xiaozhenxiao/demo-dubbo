package com.wz.elastic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.join.aggregations.Children;
import org.elasticsearch.join.aggregations.ChildrenAggregationBuilder;
import org.elasticsearch.join.aggregations.JoinAggregationBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.Filters;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.global.Global;
import org.elasticsearch.search.aggregations.bucket.global.GlobalAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.HistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Max;
import org.elasticsearch.search.aggregations.metrics.Sum;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.singletonMap;

/**
 * Java High Level REST Client
 *
 * @author wangzhen23
 * @date 2019/7/4.
 */
public class HighLevelRest {
    /**
     *
     */
    private static final Log log = LogFactory.getLog(HighLevelRest.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http"),
                        new HttpHost("localhost", 9202, "http")
                )
        );

        index(client);

        get(client);

        update(client);

        search(client);

        search2(client);

        TimeUnit.SECONDS.sleep(30);
        client.close();
    }

    public static IndexRequest index(RestHighLevelClient client) throws IOException {
        int i = new Random().nextInt(28);
        String jsonString = "{" +
                "\"firstName\": \"wang zhen " + i + "\"," +
                " \"lastName\": \"xiao xiao 8\"," +
                "\"age\": 22,  \"high\": 1" + i +
                "}";
        IndexRequest request = new IndexRequest("first_index")
                .id("1")
                .source(jsonString, XContentType.JSON)
//                .opType(DocWriteRequest.OpType.CREATE)
                ;

        /**同步请求**/
        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
        processIndexResponse(indexResponse);

        /**异步请求**/
        client.indexAsync(request, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                processIndexResponse(indexResponse);
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });


        /*Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "kimchy");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");
        IndexRequest indexRequest = new IndexRequest("posts")
                .id("1").source(jsonMap);*/

        /*try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject()
                    .field("user", "kimchy")
                    .timeField("postDate", new Date())
                    .field("message", "trying out Elasticsearch")
                    .endObject();
            IndexRequest indexRequest = new IndexRequest("posts")
                    .id("1").source(builder);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /*IndexRequest indexRequest = new IndexRequest("posts")
                .id("1")
                .source("user", "kimchy",
                        "postDate", new Date(),
                        "message", "trying out Elasticsearch");*/

        return request;
    }

    /**
     * 处理索引响应
     *
     * @param indexResponse
     */
    public static void processIndexResponse(IndexResponse indexResponse) {
        String index = indexResponse.getIndex();
        String id = indexResponse.getId();
        if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
            log.info("created document! " + "index:" + index + " id:" + id);
        } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
            log.info("updated document! " + "index:" + index + " id:" + id);
        }
        ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
        if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
            log.info("index success number is: " + shardInfo.getSuccessful());
        }
        if (shardInfo.getFailed() > 0) {
            for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                String reason = failure.reason();
                System.out.println("fail reason:" + reason);
            }
        }
    }

    public static void get(RestHighLevelClient client) throws IOException {
        GetRequest getRequest = new GetRequest("first_index", "1")
//                .version(2)    版本冲突
                ;
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        String index = getResponse.getIndex();
        String id = getResponse.getId();
        if (getResponse.isExists()) {
            long version = getResponse.getVersion();
            String sourceAsString = getResponse.getSourceAsString();
            Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
            byte[] sourceAsBytes = getResponse.getSourceAsBytes();
            log.info("index:" + index + " id:" + id + " version:" + version + " source:" + sourceAsString);
        } else {
            log.info("index:" + index + "id:" + id + " this id document not exist!");
        }
    }

    public static void update(RestHighLevelClient client) throws IOException {
        Map<String, Object> jsonMap = singletonMap("age", new Random().nextInt(20));  //update
//        Map<String, Object> jsonMap = new HashMap<>(); //noop
        UpdateRequest request = new UpdateRequest("twitter", "1")
                .doc(jsonMap)
//                .docAsUpsert(true)   insert
//                .upsert(jsonMap)  和脚本一起使用
                .fetchSource(true);
        try {
            UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);

            processUpdateResponse(updateResponse);
        } catch (ElasticsearchException e) {
            log.error(e.status());
            e.printStackTrace();
        }

    }

    public static void processUpdateResponse(UpdateResponse updateResponse) {
        String index = updateResponse.getIndex();
        String id = updateResponse.getId();
        long version = updateResponse.getVersion();
        if (updateResponse.getResult() == DocWriteResponse.Result.CREATED) {
            log.info("=========created document! " + "index:" + index + " id:" + id + " version:" + version);
        } else if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED) {
            log.info("=========updated document! " + "index:" + index + " id:" + id + " version:" + version);
        } else if (updateResponse.getResult() == DocWriteResponse.Result.NOOP) {
            log.info("=========noop! " + "index:" + index + " id:" + id + " version:" + version);
        }
        GetResult result = updateResponse.getGetResult();
        if (result.isExists()) {
            String sourceAsString = result.sourceAsString();
            Map<String, Object> sourceAsMap = result.sourceAsMap();
            byte[] sourceAsBytes = result.source();
            log.info("==========index:" + index + " id:" + id + " version:" + version + " source:" + sourceAsString);
        } else {
            log.info("index:" + index + "id:" + id + " this id document not exist!");
        }
    }

    public static void search(RestHighLevelClient client) throws IOException {
        SearchRequest searchRequest = new SearchRequest("first_index");

        constructSearchRequest(searchRequest);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);


        RestStatus status = searchResponse.status();
        TimeValue took = searchResponse.getTook();
        log.info("status:" + status + " took:" + took);

        SearchHits hits = searchResponse.getHits();  /** 搜索结果 **/
        Aggregations aggregations = searchResponse.getAggregations(); /** 聚合结果 **/

        processSearchResponse(hits);

        processAggregation(aggregations);
    }

    private static void processAggregation(Aggregations aggregations) {
        log.info("start#############################聚合#########################################");
        List<Aggregation> aggregationList = aggregations.asList();
        if (Objects.nonNull(aggregationList)) {
            aggregationList.forEach(agg -> {
                String type = agg.getType();
                if (type.equals(LongTerms.NAME) && agg.getName().equals("by_age")) {
                    for (Terms.Bucket ageBucket : ((Terms) agg).getBuckets()) {
                        long numberOfDocs = ageBucket.getDocCount();
                        Avg averageHigh = ageBucket.getAggregations().get("average_high");
                        log.info("--------------avgAge:" + ageBucket.getKeyAsString() + " =count:" + numberOfDocs + " averageHigh:" + averageHigh.getValue());
                    }
                } else if (agg.getType().equals(FilterAggregationBuilder.NAME) && agg.getName().equals("wang")) {
                    long numberOfDocs = ((Filter) agg).getDocCount();
                    Max maxAge = ((Filter) agg).getAggregations().get("max_age");
                    log.info("-----------------maxAge:" + agg.getName() + " =count:" + numberOfDocs + " maxAge:" + maxAge.getValue());
                } else if (agg.getType().equals(FiltersAggregationBuilder.NAME) && agg.getName().equals("firstName")) {
                    for (Filters.Bucket sumBucket : ((Filters) agg).getBuckets()) {
                        long numberOfDocs = sumBucket.getDocCount();
                        Sum sumAge = sumBucket.getAggregations().get("sum_age");
                        log.info("--------------sumAge:" + sumBucket.getKeyAsString() + " =count:" + numberOfDocs + " sumAge:" + sumAge.getValue());
                    }
                } else if (agg.getType().equals(HistogramAggregationBuilder.NAME) && agg.getName().equals("histogram_age")) {
                    for (Histogram.Bucket histogramBucket : ((Histogram) agg).getBuckets()) {
                        long numberOfDocs = histogramBucket.getDocCount();
                        log.info("--------------histogram:" + histogramBucket.getKeyAsString() + " =count:" + numberOfDocs);
                    }
                }else if (agg.getType().equals(GlobalAggregationBuilder.NAME) && agg.getName().equals("global-avgAge")){
                    long docCount = ((Global) agg).getDocCount();
                    Avg avg = ((Global) agg).getAggregations().get("avg-age");
                    log.info("----------global:" + docCount + " > avgAge:" + avg.getValue());
                }else if(agg.getType().equals(AvgAggregationBuilder.NAME) && agg.getName().equals("avg-age")){
                    log.info("----*----avgAge:" + ((Avg) agg).getValue());
                }
            });
        }
        log.info("end#############################聚合#########################################");
    }

    private static void processSearchResponse(SearchHits hits) {
        long total = hits.getTotalHits().value;
        SearchHit[] searchHits = hits.getHits();
        log.info("start=======search==========" + total);
        for (SearchHit hit : searchHits) {
            log.info("start*****************************高亮*********************************");
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField firstName = highlightFields.get("firstName");
            if (Objects.nonNull(firstName)) {
                Text[] fragments = firstName.fragments();
                String fragmentString = fragments[0].string();
                log.info("==firstName highlight " + fragmentString);
            }
            HighlightField lastName = highlightFields.get("lastName");
            if (Objects.nonNull(lastName)) {
                Text[] fragments = lastName.fragments();
                String fragmentString = fragments[0].string();
                log.info("==lastName highlight " + fragmentString);
            }
            log.info("end*****************************高亮*********************************");
            log.info(hit.getSourceAsString());
        }
        log.info("end=======search==========" + total);
    }

    private static void constructSearchRequest(SearchRequest searchRequest) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        /**构造查询请求**/
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(QueryBuilders.matchQuery("firstName", "wang zhen").operator(Operator.AND))
                .should(QueryBuilders.matchQuery("lastName", "xiao"))
        ;
        searchSourceBuilder.query(boolQueryBuilder)
                .from(0).size(20)
                .timeout(new TimeValue(60, TimeUnit.SECONDS));

        constructAggregation(searchSourceBuilder);

        searchRequest.source(searchSourceBuilder);
        log.info("--------------" + searchSourceBuilder.toString());

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field("firstName");
        highlightTitle.highlighterType("unified");
        highlightBuilder.field(highlightTitle);
        HighlightBuilder.Field highlightUser = new HighlightBuilder.Field("lastName");
        highlightBuilder.field(highlightUser);

        searchSourceBuilder.highlighter(highlightBuilder);
    }

    private static void constructAggregation(SearchSourceBuilder searchSourceBuilder) {
        /**构造聚合请求**/
        //terms agg
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("by_age").field("age");
        termsAggregationBuilder.subAggregation(AggregationBuilders.avg("average_high").field("high"));
        searchSourceBuilder.aggregation(termsAggregationBuilder);
        //filter agg
        FilterAggregationBuilder filterAggregationBuilder = AggregationBuilders.filter("wang", QueryBuilders.matchQuery("firstName", "wang"));
        filterAggregationBuilder.subAggregation(AggregationBuilders.max("max_age").field("age"));
        searchSourceBuilder.aggregation(filterAggregationBuilder);

        //filters agg
        FiltersAggregationBuilder filtersAggregationBuilder = AggregationBuilders.filters("firstName",
                QueryBuilders.matchQuery("firstName", "wang"),
                QueryBuilders.matchQuery("firstName", "zhen"));
        filtersAggregationBuilder.subAggregation(AggregationBuilders.sum("sum_age").field("age"));
        searchSourceBuilder.aggregation(filtersAggregationBuilder);

        //histogram
        HistogramAggregationBuilder histogramAggregationBuilder = AggregationBuilders.histogram("histogram_age").field("age").interval(5);
        searchSourceBuilder.aggregation(histogramAggregationBuilder);

        //global
        GlobalAggregationBuilder globalAggregationBuilder = AggregationBuilders.global("global-avgAge");
        globalAggregationBuilder.subAggregation(AggregationBuilders.avg("avg-age").field("age"));
        searchSourceBuilder.aggregation(globalAggregationBuilder);

        //avg
        AvgAggregationBuilder avgAggregationBuilder = AggregationBuilders.avg("avg-age").field("age");
        searchSourceBuilder.aggregation(avgAggregationBuilder);
    }


    public static void search2(RestHighLevelClient client) throws IOException {
        SearchRequest searchRequest = new SearchRequest("join_index");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        /**构造查询请求**/
        searchSourceBuilder.query(QueryBuilders.matchAllQuery())
                .from(0).size(100)
                .timeout(new TimeValue(60, TimeUnit.SECONDS));

        ChildrenAggregationBuilder childrenAggregationBuilder = JoinAggregationBuilders.children("answerChildren", "answer");
        childrenAggregationBuilder.subAggregation(AggregationBuilders.terms("child-terms").field("title.keyword"));

        searchSourceBuilder.aggregation(childrenAggregationBuilder);

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);


        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        RestStatus status = searchResponse.status();
        TimeValue took = searchResponse.getTook();
        log.info("status:" + status + " took:" + took);

        SearchHits hits = searchResponse.getHits();  /** 搜索结果 **/
        Aggregations aggregations = searchResponse.getAggregations(); /** 聚合结果 **/

        processSearchResponse(hits);

        Children children = aggregations.get("answerChildren");
        if(Objects.nonNull(children) && ChildrenAggregationBuilder.NAME.equals(children.getType())){
            String name = children.getName();
            long docCount = children.getDocCount();
            Terms terms = children.getAggregations().get("child-terms");
            for(Terms.Bucket bucket : terms.getBuckets()){
                long numberOfDocs = bucket.getDocCount();
                log.error("=======terms:" + bucket.getKeyAsString() + "> numberOfDocs:" + numberOfDocs);
            }
            log.info("!@#$%^&**&^%$#@!---name:" + name + " docCount:" + docCount);
        }
    }
}