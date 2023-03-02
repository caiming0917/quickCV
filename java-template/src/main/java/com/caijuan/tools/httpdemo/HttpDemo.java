package com.caijuan.tools.httpdemo;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @author cai juan
 * &#064;date  2023/2/23 19:23
 */
public class HttpDemo {
    public static void main(String[] args) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        HttpClient client = getClient();
        String url = "http://localhost" + ":" + "38080" + "/";
        HttpPost httpPost = new HttpPost(url);
        AuthRequestVo authRequestVo = new AuthRequestVo("name", "ps");
        HttpEntity httpEntity = new StringEntity(JSONObject.toJSONString(authRequestVo), Charset.forName("UTF-8"));
        httpPost.setEntity(httpEntity);
        HttpResponse response = client.execute(httpPost);
        String resultStr = EntityUtils.toString(response.getEntity());
        System.out.println(resultStr);
    }

    private static HttpClient getClient() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(), NoopHostnameVerifier.INSTANCE);
        CloseableHttpClient client = HttpClients.custom()
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .build();
        return client;
    }
}
