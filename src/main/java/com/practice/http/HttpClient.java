package com.practice.http;

import static org.asynchttpclient.Dsl.asyncHttpClient;
import static org.asynchttpclient.Dsl.get;
import static org.asynchttpclient.Dsl.post;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;

public class HttpClient {

	private final AsyncHttpClient asyncHttpClient;

	public HttpClient() {
		asyncHttpClient = asyncHttpClient();
	}

	/**
	 * Simple Http Client Method to make GET Request
	 * 
	 * @param url
	 * @return
	 */
	public String callGetRequest(String url,Map<String, String> requestheaders) throws RequestException {

		Request request = get(url).setSingleHeaders(requestheaders).build();
		ListenableFuture<Response> whenResponse = asyncHttpClient.executeRequest(request);
		Response response = null;
		try {
			response = whenResponse.get();
		} catch (InterruptedException e) {
			throw new RequestException("Get Request Failed", e.getCause());
		} catch (ExecutionException e) {
			throw new RequestException("Get Request Failed", e.getCause());
		}

		return response.getResponseBody();

	}

	/**
	 * 
	 * Simple Http Client Method to make POST Request
	 * 
	 * @param url
	 * @param body
	 * @return
	 */
	public String callPOSTRequest(String url, String body, Map<String, String> requestheaders) throws RequestException {

		Request request = post(url).setSingleHeaders(requestheaders).setBody(body).build();
		ListenableFuture<Response> whenResponse = asyncHttpClient.executeRequest(request);
		Response response = null;
		try {
			response = whenResponse.get();
		} catch (InterruptedException e) {
			throw new RequestException("Post Request Failed", e.getCause());
		} catch (ExecutionException e) {
			throw new RequestException("Post Request Failed", e.getCause());
		}

		return response.getResponseBody();

	}
	
}
