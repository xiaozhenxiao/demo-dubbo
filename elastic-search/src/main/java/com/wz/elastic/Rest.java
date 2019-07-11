package com.wz.elastic;

import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.sniff.Sniffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * REST CLIENT
 * wangzhen23
 * 2019/7/4.
 */
public class Rest {
    private static final Logger log = LoggerFactory.getLogger(Rest.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("user", "password"));

        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200, "http"),
                new HttpHost("localhost", 9201, "http"),
                new HttpHost("localhost", 9202, "http")
        ).setRequestConfigCallback(requestConfigBuilder ->
                requestConfigBuilder
                        .setConnectTimeout(5000)
                        .setSocketTimeout(60000)
        ).setHttpClientConfigCallback(httpClientBuilder -> {
                    httpClientBuilder.disableAuthCaching();//Disable preemptive authentication
                    return httpClientBuilder.setDefaultIOReactorConfig(
                            IOReactorConfig.custom().
                                    setIoThreadCount(1).
                                    build());
//                            .setDefaultCredentialsProvider(credentialsProvider);添加基础认证
                }
        ).build();
        Sniffer sniffer = Sniffer.builder(restClient)
                .setSniffIntervalMillis(60000) //默认5分钟
                .build();


        Request request = new Request(
                "GET",
                "/first_index/_doc/M4nnrGsBDfq_EtOLelc0");
        Response response = restClient.performRequest(request);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            log.info("==============================get================================");
            /*BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }*/
            log.info(EntityUtils.toString(response.getEntity()));
        } else {
            log.info("===================get error============================");
        }

        asyncRequest(restClient, request);

        TimeUnit.SECONDS.sleep(120);
        sniffer.close();
        restClient.close();
    }

    /**
     * @param restClient
     * @param request
     */
    public static void asyncRequest(RestClient restClient, Request request) {
        restClient.performRequestAsync(request, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                log.info("==============================async get================================");
                try {
                    log.info(EntityUtils.toString(response.getEntity()));
                    /*BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    String line;
                    while (true) {
                        if (((line = reader.readLine()) == null)) {
                            break;
                        }
                        System.out.println(line);
                    }*/
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception exception) {
                log.info("===================async get error============================");
            }
        });
    }
}
