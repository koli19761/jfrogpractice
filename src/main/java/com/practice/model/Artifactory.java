package com.practice.model;

public class Artifactory implements Comparable<Artifactory> {

	private String name;
	private int downLoadCount;
	
	/**
	 * 
	 * @param name
	 * @param downLoadCount
	 */
	public Artifactory(String name, int downLoadCount) {
		super();
		this.name = name;
		this.downLoadCount = downLoadCount;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the downLoadCount
	 */
	public int getDownLoadCount() {
		return downLoadCount;
	}
	/**
	 * @param downLoadCount the downLoadCount to set
	 */
	public void setDownLoadCount(int downLoadCount) {
		this.downLoadCount = downLoadCount;
	}
	
	
	public int compareTo(Artifactory that) {
        return Integer.compare(this.downLoadCount, that.downLoadCount);
    }    
	
	
}
