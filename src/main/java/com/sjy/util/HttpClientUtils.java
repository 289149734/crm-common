package com.sjy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpClientUtils {
	public static String getUrl(String url, Map<String, String> map) {
		if (map == null)
			return url;
		try {
			url += "?";
			List<String> params = new ArrayList<>(map.size());
			map.forEach((key, val) -> {
				params.add(key + "=" + val);
			});
			url += StringUtil.join(params, "&");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}

	public static String postForm(String url, Map<String, String> params) {
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		// 创建httppost
		HttpPost httppost = new HttpPost(url);

		// httppost.addHeader("Content-type",
		// "application/json; charset=utf-8");
		// httppost.setHeader("Accept", "application/json");

		// 创建参数队列
		List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
		if (params != null) {
			for (String key : params.keySet()) {
				formparams.add(new BasicNameValuePair(key, params.get(key)));
			}
		}
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			log.debug("executing request " + httppost.getURI());
			HttpResponse response = closeableHttpClient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String responseStr = EntityUtils.toString(entity, "UTF-8");
				log.debug("--------------------------------------");
				log.debug("Response content: {}", responseStr);
				log.debug("--------------------------------------");
				return responseStr;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (closeableHttpClient != null) {
				try {
					closeableHttpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static String postForm(String url, Map<String, String> headerData, Map<String, String> params) {
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		// 创建httppost
		HttpPost httppost = new HttpPost(url);

		if (headerData != null) {
			headerData.forEach((key, val) -> {
				httppost.addHeader(key, val);
			});
		}

		// 创建参数队列
		List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
		if (params != null) {
			for (String key : params.keySet()) {
				formparams.add(new BasicNameValuePair(key, params.get(key)));
			}
		}
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			log.debug("executing request " + httppost.getURI());
			HttpResponse response = closeableHttpClient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String responseStr = EntityUtils.toString(entity, "UTF-8");
				log.debug("--------------------------------------");
				log.debug("Response content: {}", responseStr);
				log.debug("--------------------------------------");
				return responseStr;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (closeableHttpClient != null) {
				try {
					closeableHttpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static String postForm(String url, String contentType, Map<String, String> params) {
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		// 创建httppost
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("Content-type", contentType);
		// httppost.setHeader("Accept", "application/json");

		// 创建参数队列
		List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
		if (params != null) {
			for (String key : params.keySet()) {
				formparams.add(new BasicNameValuePair(key, params.get(key)));
			}
		}
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			log.debug("executing request " + httppost.getURI());
			HttpResponse response = closeableHttpClient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String responseStr = EntityUtils.toString(entity, "UTF-8");
				log.debug("--------------------------------------");
				log.debug("Response content: {}", responseStr);
				log.debug("--------------------------------------");
				return responseStr;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (closeableHttpClient != null) {
				try {
					closeableHttpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static String putForm(String url, String contentType, Map<String, String> params) {
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		// 创建httppost
		HttpPut httpput = new HttpPut(url);
		httpput.setHeader("Content-type", contentType);
		// httppost.setHeader("Accept", "application/json");

		try {
			// 创建参数队列
			List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
			if (params != null) {
				for (String key : params.keySet()) {
					formparams.add(new BasicNameValuePair(key, params.get(key)));
				}
				UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
				httpput.setEntity(uefEntity);
			}

			log.debug("executing request " + httpput.getURI());
			HttpResponse response = closeableHttpClient.execute(httpput);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String responseStr = EntityUtils.toString(entity, "UTF-8");
				log.debug("--------------------------------------");
				log.debug("Response content: {}", responseStr);
				log.debug("--------------------------------------");
				return responseStr;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (closeableHttpClient != null) {
				try {
					closeableHttpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static String getForm(String url, Map<String, String> params) {
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		// 创建httppost
		HttpGet httpget = new HttpGet(getUrl(url, params));
		try {
			log.debug("executing request " + httpget.getURI());
			HttpResponse response = closeableHttpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String responseStr = EntityUtils.toString(entity, "UTF-8");
				log.debug("--------------------------------------");
				log.debug("Response content: {}", responseStr);
				log.debug("--------------------------------------");
				return responseStr;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (closeableHttpClient != null) {
				try {
					closeableHttpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static Boolean getFormForFile(String url, Map<String, String> params, File file) {
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		// 创建httppost
		HttpGet httpget = new HttpGet(getUrl(url, params));
		try {
			log.debug("executing request " + httpget.getURI());
			HttpResponse response = closeableHttpClient.execute(httpget);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					FileUtils.copyInputStreamToFile(entity.getContent(), file);
					return true;
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (closeableHttpClient != null) {
				try {
					closeableHttpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public static String getForm(String url) {
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		// 创建httppost
		HttpGet httpget = new HttpGet(url);
		try {
			log.debug("executing request " + httpget.getURI());
			HttpResponse response = closeableHttpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			String returnStr = EntityUtils.toString(entity, "UTF-8");
			if (entity != null) {
				log.debug("--------------------------------------");
				log.debug("Response content: " + returnStr);
				log.debug("--------------------------------------");
			}
			return returnStr;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (closeableHttpClient != null) {
				try {
					closeableHttpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static String getForm(String url, String contentType, Map<String, String> params) {
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		// 创建httppost
		HttpGet httpget = new HttpGet(getUrl(url, params));
		httpget.setHeader("Content-type", contentType);
		// httpget.addHeader("Accept", "application/json; charset=utf-8");

		try {
			log.debug("executing request " + httpget.getURI());
			HttpResponse response = closeableHttpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			String returnStr = EntityUtils.toString(entity, "UTF-8");
			if (entity != null) {
				log.debug("--------------------------------------");
				log.debug("Response content: " + returnStr);
				log.debug("--------------------------------------");
			}
			return returnStr;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (closeableHttpClient != null) {
				try {
					closeableHttpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static JSONObject sendHttpPost(String url, JSONObject json) {
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		// 创建httppost
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("Content-type", "application/json; charset=utf-8");
		httppost.addHeader("Accept", "application/json; charset=utf-8");

		try {
			StringEntity r_entity = new StringEntity(json.toString(), "UTF-8");// 解决中文乱码问题
			httppost.setEntity(r_entity);
			log.debug("executing request " + httppost.getURI());
			HttpResponse response = closeableHttpClient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String responseStr = EntityUtils.toString(entity, "UTF-8");
				log.debug("--------------------------------------");
				log.debug("Response content: {}", responseStr);
				log.debug("--------------------------------------");
				return JSON.parseObject(responseStr);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (closeableHttpClient != null) {
				try {
					closeableHttpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static String sendHttpPost(String url, String data) {
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		// 创建httppost
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("Content-type", "application/json; charset=utf-8");
		httppost.addHeader("Accept", "application/json; charset=utf-8");

		try {
			StringEntity r_entity = new StringEntity(data, "UTF-8");// 解决中文乱码问题
			httppost.setEntity(r_entity);
			log.debug("executing request " + httppost.getURI());
			HttpResponse response = closeableHttpClient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String responseStr = EntityUtils.toString(entity, "UTF-8");
				log.debug("--------------------------------------");
				log.debug("Response content: {}", responseStr);
				log.debug("--------------------------------------");
				return responseStr;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (closeableHttpClient != null) {
				try {
					closeableHttpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static Boolean sendHttpPostForFile(String url, String data, File file) {
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		// 创建httppost
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("Content-type", "application/json; charset=utf-8");
		httppost.addHeader("Accept", "application/json; charset=utf-8");

		try {
			if (StringUtil.isNotBlank(data)) {
				StringEntity r_entity = new StringEntity(data, "UTF-8");// 解决中文乱码问题
				httppost.setEntity(r_entity);
			}
			log.debug("executing request " + httppost.getURI());
			HttpResponse response = closeableHttpClient.execute(httppost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					FileUtils.copyInputStreamToFile(entity.getContent(), file);
					return true;
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (closeableHttpClient != null) {
				try {
					closeableHttpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	/**
	 * 方法描述: HTTPS双向认证 作 者： yiming.zhang 日 期： 2015年4月7日-下午10:34:11
	 * 
	 * @param url
	 * @param arrayToXml
	 * @param mch_id
	 *            微信商户号
	 * @return
	 * @throws Exception
	 *             返回类型： Map<String,String>
	 */
	public static Map<String, String> sslResquest(String url, String arrayToXml, String mch_id, String certFileName) {
		try {
			String xmlStr = sslResquestNew(url, arrayToXml, mch_id, certFileName);
			return XMLUtil.doXMLParse(xmlStr);
		} catch (Exception e) {
			log.error("sslResquest请求失败", e);
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static String sslResquestNew(String url, String arrayToXml, String mch_id, String certFileName) {
		CloseableHttpClient httpclient = null;
		try {
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			FileInputStream instream = new FileInputStream(new File(certFileName));
			try {
				// 指定PKCS12的密码(商户ID)
				keyStore.load(instream, mch_id.toCharArray());
			} finally {
				instream.close();
			}
			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mch_id.toCharArray()).build();
			// Allow TLSv1 protocol only
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" },
					null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new StringEntity(arrayToXml, "UTF-8"));
			log.info("executing request {}", httpPost.getRequestLine());
			CloseableHttpResponse response = httpclient.execute(httpPost);
			String xmlStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			log.info("executing response {}", xmlStr);
			response.close();
			return xmlStr;
		} catch (Exception e) {
			log.error("sslResquestNew请求失败", e);
		} finally {
			if (httpclient != null) {
				try {
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
