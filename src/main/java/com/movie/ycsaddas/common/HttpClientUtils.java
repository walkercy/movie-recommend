package com.movie.ycsaddas.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * http请求工具类
 *
 * @author walker
 * @date 2018/05/19
 */
@Slf4j
public class HttpClientUtils {
	public static String get(String uri) throws Exception {
		HttpGet get = new HttpGet(uri);
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(getSSLFactory()).build();
		CloseableHttpResponse response = httpClient.execute(get);
		String result = null;
		if (response != null) {
			result = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
		} else {
			log.error("response is null");
		}
		return result;
	}

	private static SSLConnectionSocketFactory getSSLFactory() throws Exception {
		return new SSLConnectionSocketFactory(getSSLContext(), new String[] {"SSLv3", "TLSv1.2", "TLSv1"}, null, new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
	}

	private static SSLContext getSSLContext() throws Exception {
		SSLContext ctx = SSLContext.getInstance("SSL");
		TrustManager tx = new X509TrustManager() {
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
		};
		ctx.init(null, new TrustManager[] {tx}, null);
		return ctx;
	}
}
