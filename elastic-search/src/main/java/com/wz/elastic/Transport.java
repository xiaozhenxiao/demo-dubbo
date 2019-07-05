package com.wz.elastic;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * ElasticSearch 7.2.0
 */
public class Transport {
    public static void main(String[] args) throws IOException {
        Settings settings = Settings.builder()
                .put("cluster.name", "xiao_first_index")
                .put("client.transport.sniff", true)
                .put("client.transport.ping_timeout", "5s")
                .put("client.transport.nodes_sampler_interval", "5s")
                .build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300))
                .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9301))
                .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9302));

        index(client);

        GetResponse getResponse = client.prepareGet("join_index", "_doc", "1").get();
        Map<String, Object> result = getResponse.getSource();
        System.out.println("======================Get=======================");
        System.out.println(JSON.toJSONString(result));

        MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
                .add("join_index", "_doc", "1")
                .add("join_index", "_doc", "2", "3", "4")
                .add("first_index", "_doc", "M4nnrGsBDfq_EtOLelc0")
                .get();

        System.out.println("======================MultiGet=======================");
        for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
            GetResponse response = itemResponse.getResponse();
            if (response.isExists()) {
                String json = response.getSourceAsString();
                System.out.println(json);
            }
        }
        search(client);

        try {
            update(client);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // on shutdown
        client.close();
    }

    public static void index(TransportClient client) throws IOException {
        Random random = new Random();
        IndexResponse response = client.prepareIndex("first_index", "_doc", "110")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("firstName", "wang zhen " + random.nextInt(30))
                        .field("lastName", "xiao xiao " + random.nextInt(30))
                        .field("age", random.nextInt(30))
                        .field("high", 1.5 + random.nextInt(200))
                        .endObject()
                ).get();
        System.out.println("====================index======================");
        System.out.println("index:" + JSON.toJSONString(response));
    }

    public static void update(TransportClient client) throws IOException, ExecutionException, InterruptedException {
        /**第一种**/
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("first_index");
        updateRequest.type("_doc");
        updateRequest.id("M4nnrGsBDfq_EtOLelc0");
        updateRequest.doc(jsonBuilder()
                .startObject()
                .field("high", new Random(180).nextFloat())
                .endObject());
        UpdateResponse updateResponse = client.update(updateRequest).get();
        System.out.println("update result:" + updateResponse.status());

        /**第二种**/
        updateResponse = client.prepareUpdate("twitter", "_doc", "1")
                .setDoc(jsonBuilder()
                        .startObject()
                        .field("tags", "tags message")
                        .field("age", new Random().nextInt(30))
                        .endObject())
                .get();
        System.out.println("update 2 result:" + updateResponse.status());
    }

    public static void search(TransportClient client) {
        SearchResponse response = client.prepareSearch("first_index")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(termQuery("firstName", "wang"))                 // Query
                .setPostFilter(QueryBuilders.rangeQuery("age").from(3).to(25))     // Filter
                .setFrom(0).setSize(10).setExplain(true)
                .get();
        System.out.println("==========================search===========================");
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }

    public static void scroll(TransportClient client) {
        QueryBuilder qb = termQuery("firstName", "wang");

        SearchResponse scrollResp = client.prepareSearch("first_index")
                .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                .setScroll(new TimeValue(60000))
                .setQuery(qb)
                .setSize(100).get(); //max of 100 hits will be returned for each scroll
        //Scroll until no hits are returned
        do {
            for (SearchHit hit : scrollResp.getHits().getHits()) {
                //Handle the hit...
            }

            scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
        } while (scrollResp.getHits().getHits().length != 0); // Zero hits mark the end of the scroll and the while loop.
    }

    public static void aggregation(TransportClient client) {
        SearchResponse sr = client.prepareSearch()
                .setQuery(QueryBuilders.matchAllQuery())
                .addAggregation(
                        AggregationBuilders.terms("agg1").field("field")
                )
                .addAggregation(
                        AggregationBuilders.dateHistogram("agg2")
                                .field("birth")
                                .calendarInterval(DateHistogramInterval.YEAR)
                ).get();

        // Get your facet results
        Terms agg1 = sr.getAggregations().get("agg1");
        Histogram agg2 = sr.getAggregations().get("agg2");
    }

}
