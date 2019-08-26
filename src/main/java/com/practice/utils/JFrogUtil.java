package com.practice.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.practice.http.HttpClient;
import com.practice.http.RequestException;

public class JFrogUtil {

	public List<String> getJFrogRepoWithJarsFQN(String url, String body, Map<String, String> requestHeaders)
			throws RequestException {

		HttpClient hc = new HttpClient();
		List<String> listOfFQN = new ArrayList<>();

		try {
			String response = hc.callPOSTRequest(url, body, requestHeaders);
			//System.out.println(response);
			JSONObject obj = new JSONObject(response);
			JSONArray arr = obj.getJSONArray("results");

			for (int i = 0; i < arr.length(); i++) {
				String repo = arr.getJSONObject(i).getString("repo");
				String path = arr.getJSONObject(i).getString("path");
				String name = arr.getJSONObject(i).getString("name");

				if (name.endsWith("jar")) {
					String fqn = repo + "/" + path + "/" + name;
					//System.out.println(fqn);
					listOfFQN.add(fqn);
				}
			}

		} catch (Exception e) {
			throw new RequestException("Error while calling JFROG POST API:", e);
		}

		return listOfFQN;
	}

	public int getJFrogRepoDownLoadCount(String url, Map<String, String> requestHeaders) throws RequestException {

		HttpClient hc = new HttpClient();
		int downLoadCount = 0;
		String getresponse = hc.callGetRequest(url, requestHeaders);
		//System.out.println(getresponse);
		
		JSONObject getobj = new JSONObject(getresponse);
		downLoadCount = (int) getobj.get("downloadCount");
		return downLoadCount;
		
	}

}
