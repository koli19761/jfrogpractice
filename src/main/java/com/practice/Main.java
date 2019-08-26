package com.practice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.practice.heap.MinHeap;
import com.practice.http.RequestException;
import com.practice.model.Artifactory;
import com.practice.utils.JFrogUtil;

/**
 * Few Assumptions to the assignment
 * 
 * 1) JSON Structure or Schema is not changing 2) Download count considered for
 * only "jars" file type 3) This Solution creates MinHeap with fixed heapSize of
 * 5 i.e it will hold top 5 artifactories at any given point of time
 * 
 * 
 * @author lokeshkohli
 *
 */
public class Main {

	private static final String POST_API_URL = "http://104.155.149.184/artifactory/api/search/aql";
	private static final String GET_API_URL = "http://104.155.149.184/artifactory/api/storage/";
	private static final String API_KEY = "AKCp5dL3Da6i7VUJc5sLGXHzaLENQvSr2uoVT9puZJtfFUKUJLrS5BqvLLc42EBL5g3zc5i21";
	private static final int MAX_TOPN_SIZE = 5;
	private static String REPO_NAME = "jcenter-cache";

	public static void main(String[] args) {

		System.out.println("JFROG Artifactory top N ");

		try {

			Main m = new Main();
			List<Artifactory> topArtifcatories = m.getTopNArtifactory();
			for (Artifactory tp : topArtifcatories) {
				System.out.println(tp.getName() + ":" + tp.getDownLoadCount());
			}

		} catch (RequestException e) {
			System.err.println("Error" + e.getMessage());
		}

		System.exit(-1);
	}

	public List<Artifactory> getTopNArtifactory() throws RequestException {

		List<Artifactory> topNArtifactoryList = new ArrayList<>();

		String body = "items.find({ \"repo\":{\"$eq\":\"" + REPO_NAME + "\"}})";

		Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("X-JFrog-Art-API", API_KEY);
		requestHeaders.put("Content-Type", "text/plain");

		JFrogUtil jutil = new JFrogUtil();

		List<String> listOfFileNames = jutil.getJFrogRepoWithJarsFQN(POST_API_URL, body, requestHeaders);

		int heapSize = MAX_TOPN_SIZE;
		MinHeap<Artifactory> minHeap = new MinHeap<Artifactory>(heapSize + 1);

		for (String fileName : listOfFileNames) {

			String url = GET_API_URL + fileName + "?stats";
			int count = jutil.getJFrogRepoDownLoadCount(url, requestHeaders);
			Artifactory artifactory = new Artifactory(fileName, count);
			minHeap.insert(artifactory);

			if (minHeap.size() > heapSize) {
				minHeap.delMin();
			}
		}

		Stack<Artifactory> stack = new Stack<Artifactory>();
		for (Artifactory ar : minHeap) {
			stack.push(ar);
		}

		Artifactory top = stack.pop();
		topNArtifactoryList.add(top);
		Artifactory secondtop = stack.pop();
		topNArtifactoryList.add(secondtop);
		
		return topNArtifactoryList;

	}

}
