package com.dekalong.gqqtmonitor.util.pageutil;

import java.util.Collections;
import java.util.List;

public class Page<E> implements java.io.Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;
	private int pageShow = 2; 
	@SuppressWarnings("unused")
	private int totalPage=0;
	private int totalCount=0;
	private int start=0;
	private int nowPage=0;
	
	private List<E> result = Collections.emptyList();
	
	public int getStart() {
		start = (getNowPage()-1)*getPageShow();
		if(start<0){
			start = 0;
		}
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getPageShow() {
		return pageShow;
	}
	public void setPageShow(int pageShow) {
		this.pageShow = pageShow;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public List<E> getResult() {
		return result;
	}
	public void setResult(List<E> result) {
		this.result = result;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}
	public int getTotalPage() {
		return  (int)Math.ceil(totalCount*1.0/pageShow);
	}
	public int getNowPage() {
		if(nowPage<=0){nowPage = 1;}
		if(nowPage>getTotalPage()&&totalCount!=0){nowPage = getTotalPage();}
		return nowPage;
	}
	@Override
	public String toString() {
		return "Page [pageShow=" + pageShow + ", totalPage=" + getTotalPage()
				+ ", totalCount=" + totalCount + ", nowPage=" + nowPage
				+ ", result=" + result + "]";
	}
}