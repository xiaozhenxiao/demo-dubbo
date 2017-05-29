package com.wz.http;

import com.google.common.collect.Maps;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 * wangzhen23
 * 2017/5/24.
 */
public class HttpClientTest {
    private ClientConnectionManager clientConnectionManager;
    private DefaultHttpClient client;
    private static Logger log = LoggerFactory.getLogger(HttpClientTest.class);
    private SchemeRegistry schemeRegistry;
    private int maxPerRoute = 10;//每个路由(目标机器)的最大连接数
    private int maxTotal = 10;//最大连接数
    private static int connectionTimeout = 10; //连接超时时间
    private static int soTimeOut = 5000;//响应超时时间
    private int connTTL = 10;//max lifetime
    private int retryCount;//重试次数

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClientTest httpClient = new HttpClientTest();
//        DefaultHttpClient hclient = httpClient.getHttpClient();
//        HttpParams params = hclient.getParams();
//        HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);

        int num = 0;
        for (int i = 0; i < 10; i++) {
            num++;
            HttpUriRequest method = new HttpPost("http://blog.csdn.net/fly_time2012/article/details/50771296");
            method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, soTimeOut);
            Thread thread = new Thread(new HttpTask(httpClient.getHttpClient(), method, System.currentTimeMillis()));
            thread.start();
//            Thread.sleep(100L);
        }
        System.out.println("============================END==========================="+num);
    }

    protected static class HttpTask implements Runnable {
        private DefaultHttpClient client;
        private HttpUriRequest method;
        private Long systemTime;
        private static int num = 0;

        public HttpTask(DefaultHttpClient client, HttpUriRequest method, Long systemTime) {
            this.client = client;
            this.method = method;
            this.systemTime = systemTime;
        }

        @Override
        public void run() {
            try {
                HttpParams params = client.getParams();
                HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);
                HttpResponse response = client.execute(method);
                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode != HttpStatus.SC_OK) {
                    log.info((num++ + "------------------Method failed: " + response.getStatusLine()));
                    return;
                }
                String message = EntityUtils.toString(response.getEntity(), "GBK");
                System.out.println(num++ + "=============================" + message.length() + "**************" + (System.currentTimeMillis()-systemTime));
            } catch (IOException e) {
                e.printStackTrace();
                log.error("occur error! ", e);
            }
        }
    }

    protected final DefaultHttpClient getHttpClient() {
        //synchronized？
        if (client == null) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>init http client");
            client = this.createHttpClient();
        }
        ThreadSafeClientConnManager clientManager = (ThreadSafeClientConnManager) (client.getConnectionManager());
        log.info("============= httpclient ===============" + client.getClass());
        log.info("============= ConnectionsInPool ===============" + clientManager.getConnectionsInPool());
        return client;
    }

    protected DefaultHttpClient createHttpClient() {
        final HttpParams params = new BasicHttpParams();
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        final ClientConnectionManager clientConnectionManager = this.createClientConnectionManager(params);
        client = new DefaultHttpClient(clientConnectionManager, params);
        client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(retryCount, false));
        return client;
    }

    protected ClientConnectionManager createClientConnectionManager(HttpParams params) {
        if (this.clientConnectionManager != null) {
            return this.clientConnectionManager;
        }
        final ThreadSafeClientConnManager threadSafeClientConnManager = new ThreadSafeClientConnManager(this.getSchemeRegistry(), connTTL,
                TimeUnit.SECONDS);
        threadSafeClientConnManager.setMaxTotal(maxTotal);
        threadSafeClientConnManager.setDefaultMaxPerRoute(maxPerRoute);
        return threadSafeClientConnManager;
    }

    public ClientConnectionManager getClientConnectionManager() {
        return clientConnectionManager;
    }

    public void setClientConnectionManager(ClientConnectionManager clientConnectionManager) {
        this.clientConnectionManager = clientConnectionManager;
    }

    public DefaultHttpClient getClient() {
        return client;
    }

    public void setClient(DefaultHttpClient client) {
        this.client = client;
    }

    public SchemeRegistry getSchemeRegistry() {
        this.schemeRegistry = new SchemeRegistry();
        Map<String, Scheme> items = Maps.newHashMap();
        Scheme scheme = new Scheme("http", 80, PlainSocketFactory.getSocketFactory());
        items.put("http", scheme);
        this.schemeRegistry.setItems(items);
        return this.schemeRegistry;
    }

    public void setSchemeRegistry(SchemeRegistry schemeRegistry) {
        this.schemeRegistry = new SchemeRegistry();
        Map<String, Scheme> items = Maps.newHashMap();
        Scheme scheme = new Scheme("http", 80, PlainSocketFactory.getSocketFactory());
        items.put("http", scheme);
        this.schemeRegistry.setItems(items);
    }

    public int getMaxPerRoute() {
        return maxPerRoute;
    }

    public void setMaxPerRoute(int maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getSoTimeOut() {
        return soTimeOut;
    }

    public void setSoTimeOut(int soTimeOut) {
        this.soTimeOut = soTimeOut;
    }

    public int getConnTTL() {
        return connTTL;
    }

    public void setConnTTL(int connTTL) {
        this.connTTL = connTTL;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
}