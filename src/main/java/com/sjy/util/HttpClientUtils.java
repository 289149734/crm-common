package com.sjy.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpClientUtils {
	public String getUrl(String url, Map<String, String> map) {
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

	public String postForm(String url, Map<String, String> params) {
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

	public String postForm(String url, String contentType, Map<String, String> params) {
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

	public String putForm(String url, String contentType, Map<String, String> params) {
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

	public String getForm(String url, Map<String, String> params) {
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

	public String getForm(String url) {
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

	public String getForm(String url, String contentType, Map<String, String> params) {
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
}
