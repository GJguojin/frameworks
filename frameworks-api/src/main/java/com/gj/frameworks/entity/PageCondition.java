package com.gj.frameworks.entity;

import java.io.Serializable;

import com.gj.frameworks.support.TypeAliases;

/**
 * 查询条件对象基类
 */
public class PageCondition implements Serializable, TypeAliases, Pagable {

	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_PAGE_NO = 1;
	public static final int DEFAULT_PAGE_SIZE = 10;

	protected int pageNo = DEFAULT_PAGE_NO;
	protected int pageSize = DEFAULT_PAGE_SIZE;
	protected boolean autoCount = true;

	public PageCondition() {
	}

	public PageCondition(int pageNo, int pageSize) {
		this.pageNo = pageNo < 1 ? DEFAULT_PAGE_NO : pageNo;
		this.pageSize = pageSize < 2 ? DEFAULT_PAGE_SIZE : pageSize;
	}

	@Override
	public int getOffset() {
		return (pageNo - 1) * pageSize;
	}

	@Override
	public int getLimit() {
		return pageSize;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 查询对象时是否自动另外执行count查询获取总记录数, 默认为false.
	 */
	@Override
	public boolean isAutoCount() {
		return autoCount;
	}

	/**
	 * 查询对象时是否自动另外执行count查询获取总记录数.
	 */
	public void setAutoCount(final boolean autoCount) {
		this.autoCount = autoCount;
	}

	public int getPageNo() {
		return pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

}
