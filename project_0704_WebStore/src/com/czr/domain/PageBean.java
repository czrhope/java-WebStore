package com.czr.domain;

public class PageBean {
	
	//总页数
	private int totalPage;
	//当前是第几页
	private int pageIndex;
	//每页显示个数
	private int pageContent;
	//总记录数
	private int totalRecord;
	
	public PageBean() {};
	
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getPageContent() {
		return pageContent;
	}
	public void setPageContent(int pageContent) {
		this.pageContent = pageContent;
	}
	public int getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}
	
	
}
