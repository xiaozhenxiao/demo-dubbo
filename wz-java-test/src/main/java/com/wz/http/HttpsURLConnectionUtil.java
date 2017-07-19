package com.wz.http;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * wangzhen23
 * 2017/7/18.
 */
public class HttpsURLConnectionUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpsURLConnectionUtil.class);

    public static String sendSSlRequest(String requestUrl) throws IOException {
        HostnameVerifier DO_NOT_VERIFY = (hostname, session) -> true;
        URL url = new URL(requestUrl);
        // trust all hosts
        trustAllHosts();

        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5 * 1000);
        conn.setHostnameVerifier(DO_NOT_VERIFY);
        return IOUtils.toString(conn.getInputStream(), "UTF-8");
    }

    /**
     * Trust every server - dont check for any certificate
     */
    private static void trustAllHosts() {
        final String TAG = "trustAllHosts";
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                logger.info(TAG, "checkClientTrusted");
            }
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                logger.info(TAG, "checkServerTrusted");
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
