package org.shersfy.fs.beans;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class Page<T> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 当前页码 **/
	private int pageNo   = 1;
	/** 分页大小 **/
	private int pageSize = 20;
	/** 总行数 **/
	private long sumRow;
	/** 每页数据 **/
	private List<T> data;
	
	public Page() {}
	
	public Page(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getSumRow() {
		return sumRow;
	}

	public void setSumRow(long sumRow) {
		this.sumRow = sumRow;
	}

	/**
	 * 总页数
	 * @return
	 */
	public long getSumPage() {
		return sumRow % pageSize == 0? sumRow / pageSize : sumRow / pageSize + 1;
	}

	/**
	 * 分页起始位置
	 * @return
	 */
	public long getStartIndex() {
		return pageNo<=0?0:(pageNo - 1) * pageSize;
	}

	/***
	 * 是否最后一页
	 * @return
	 */
	public boolean isLastPage() {
		return pageNo == this.getSumPage();
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
