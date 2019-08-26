package com.practice;

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
 * only "jars" file type
 * 
 * 
 * @author lokeshkohli
 *
 */
public class Main {

	public static void main(String[] args) {

		System.out.println("JFROG Artifactory top count ");

		String postApiUrl = "http://104.155.149.184/artifactory/api/search/aql";
		String getApiUrl = "http://104.155.149.184/artifactory/api/storage/";
		String apiKey = "AKCp5dL3Da6i7VUJc5sLGXHzaLENQvSr2uoVT9puZJtfFUKUJLrS5BqvLLc42EBL5g3zc5i21";
		String repoName = "jcenter-cache";

		String body = "items.find({ \"repo\":{\"$eq\":\"" + repoName + "\"}})";

		Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("X-JFrog-Art-API", apiKey);
		requestHeaders.put("Content-Type", "text/plain");

		JFrogUtil jutil = new JFrogUtil();
		try {

			List<String> listOfFileNames = jutil.getJFrogRepoWithJarsFQN(postApiUrl, body, requestHeaders);

			System.out.println("The Max Heap is ");
			int m = 5;
			MinHeap<Artifactory> minHeap = new MinHeap<Artifactory>(m + 1);

			for (String fileName : listOfFileNames) {

				String url = getApiUrl + fileName + "?stats";
				int count = jutil.getJFrogRepoDownLoadCount(url, requestHeaders);
				Artifactory artifactory = new Artifactory(fileName, count);
				minHeap.insert(artifactory);

				// remove minimum if m+1 entries on the PQ
				if (minHeap.size() > m)
					minHeap.delMin();
			}

			// print entries on PQ in reverse order
			Stack<Artifactory> stack = new Stack<Artifactory>();
			for (Artifactory ar : minHeap) {
				stack.push(ar);
			}

			Artifactory top = stack.pop();
			System.out.println(top.getName() + ":" + top.getDownLoadCount());
			Artifactory secondtop = stack.pop();
			System.out.println(secondtop.getName() + ":" + secondtop.getDownLoadCount());

		} catch (RequestException e) {
			System.err.println("Error" + e.getMessage());
		}

		System.exit(-1);
	}

}
