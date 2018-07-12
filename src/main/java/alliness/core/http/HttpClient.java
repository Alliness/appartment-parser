package alliness.core.http;

import javafx.util.Pair;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Apache Http client
 * GET, POST, PUT, DELETE
 * <p>
 * examples:
 * http.get("http://domain.com").execute().toString();
 * http.post("https://google.com", jsonData).execute().toJSON();
 */
public class HttpClient {

    public static final Logger log = Logger.getLogger(HttpClient.class);

    private CloseableHttpResponse response;
    private HttpUriRequest request;
    private String body;
    private List<BasicClientCookie> cookiesList;
    private URI url;
    private CookieStore cookieStore;
    private List<Pair<String, String>> userHeaders;
    private long responseTime;

    public HttpClient() {
        cookiesList = new ArrayList<>();
        userHeaders = new ArrayList<>();
    }

    /**
     * @param url target uri
     * @return this;
     */
    public HttpClient get(String url) {
        try {
            this.url = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        request = new HttpGet(url);
        return this;
    }

    public HttpClient get(URI url) {
        this.url = url;

        request = new HttpGet(url);
        return this;
    }

    /**
     * @param url      target uri
     * @param jsonData data json format
     * @return this;
     */
    public HttpClient post(String url, JSONObject jsonData) {
        try {
            this.url = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(jsonData.toString(), Charset.forName("UTF-8")));
        request = post;
        return this;
    }

    public HttpClient post(String url, String data) {
        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(data, Charset.forName("UTF-8")));
        request = post;
        return this;
    }

    public HttpClient post(URI url, String data) {
        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(data, Charset.forName("UTF-8")));
        request = post;
        return this;
    }

    public HttpClient post(URI url, JSONObject jsonData) {

        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(jsonData.toString(), Charset.forName("UTF-8")));
        request = post;
        return this;
    }

    /**
     * @param url      target uri
     * @param jsonData data json format
     * @return this;
     */
    public HttpClient put(String url, JSONObject jsonData) {
        try {
            this.url = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpPut put = new HttpPut(url);
        try {
            put.setEntity(new StringEntity(jsonData.toString()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        request = put;
        return this;
    }

    /**
     * @param url      target uri
     * @param jsonData data json format
     * @return this;
     */
    public HttpClient put(URI url, JSONObject jsonData) {
        this.url = url;

        HttpPut put = new HttpPut(url);
        try {
            put.setEntity(new StringEntity(jsonData.toString()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        request = put;
        return this;
    }

    /**
     * @param url target uri
     * @return this;
     */
    public HttpClient delete(String url) {
        try {
            this.url = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        request = new HttpDelete(url);
        return this;
    }

    /**
     * @param url target uri
     * @return this;
     */
    public HttpClient delete(URI url) {
        this.url = url;
        request = new HttpDelete(url);
        return this;
    }

    /**
     * @param key   header key
     * @param value header value
     * @return this;
     */
    public HttpClient addHeader(String key, String value) {
        userHeaders.add(new Pair<>(key, value));
        return this;
    }

    public HttpClient addHeader(HttpClientHeader header) {
        userHeaders.add(new Pair<>(header.key, header.value));
        return this;
    }

    public HttpClient clearHeaders() {
        userHeaders.clear();
        return this;
    }

    public HttpClient removeHeader(String key) {
        userHeaders.removeIf(stringStringPair -> stringStringPair.getKey().equals(key));
        return this;
    }

    /**
     * @param header header key
     * @return this;
     */
    public HttpClient addHeader(Header header) {

        request.addHeader(header);
        return this;
    }


    public HttpClient addCookies(String name, String value, Date expirationDate, String domain) {

        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setExpiryDate(expirationDate);
        cookie.setDomain(domain);
        if (!cookiesList.contains(cookie)) {
            cookiesList.add(cookie);
        }
        return this;
    }

    public HttpClient addCookies(String name, String value, String domain) {

        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setDomain(domain);
        if (!cookiesList.contains(cookie)) {
            cookiesList.add(cookie);
        }
        return this;
    }

    public HttpClient addCookies(String name, String value, Date expirationDate) {

        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setExpiryDate(expirationDate);
        if (!cookiesList.contains(cookie)) {
            cookiesList.add(cookie);
        }
        return this;
    }

    public HttpClient addCookies(String name, String value) {
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        if (!cookiesList.contains(cookie)) {
            cookiesList.add(cookie);
        }
        return this;
    }


    /**
     * Request execution method (get/post/put/delete/).execute()
     * After executing, can use response methods.
     *
     * @return this
     */
    public HttpClient execute() {
        log.info(String.format("[%s]: %s", request.getMethod(), request.getURI()));


        HttpClientBuilder builder = HttpClients.custom();
        builder.setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build());
        cookieStore = new BasicCookieStore();

        try {
            SSLContextBuilder ctxb = SSLContextBuilder.create();
            ctxb.loadTrustMaterial(new TrustSelfSignedStrategy());
            SSLContext ctx = ctxb.build();
            SSLConnectionSocketFactory sslf = new SSLConnectionSocketFactory(ctx, new DefaultHostnameVerifier());
            builder.setSSLSocketFactory(sslf);
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        }

        cookiesList.forEach(cookieStore::addCookie);
        userHeaders.forEach(stringStringPair -> request.addHeader(stringStringPair.getKey(), stringStringPair.getValue()));
        try {
            long startTime = System.currentTimeMillis();
            CloseableHttpClient client = builder
                    .setDefaultCookieStore(cookieStore)
                    .build();
            response = client.execute(request);

            responseTime = System.currentTimeMillis() - startTime;

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            HttpEntity entity = response.getEntity();
            body = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * if is able to convert response to JSONObject, it will return JSONObject
     *
     * @return JSONObject|null
     */
    public JSONObject toJSON() throws JSONException {
        return new JSONObject(body);
    }

    /**
     * @return response as string
     */
    public String toString() {

        return body;
    }

    /**
     * @return response code status
     */
    public int responseStatus() {

        return response.getStatusLine()
                .getStatusCode();
    }

    /**
     * @return response headers[]
     */
    public Header[] responseHeaders() {

        return response.getAllHeaders();
    }

    public CloseableHttpResponse getResponse() {

        return response;
    }

    public List<Cookie> getCookies() {
        return cookieStore.getCookies();
    }

    public String getURL() {
        return String.valueOf(request.getURI());
    }

    public HttpClient setOauthHeader(String token) {
        addHeader("Authorization", "Bearer " + token);
        return this;
    }

    public int getStatusCode() {
        return response.getStatusLine().getStatusCode();
    }

    public long getLastResponseTime() {
        return responseTime;
    }
}