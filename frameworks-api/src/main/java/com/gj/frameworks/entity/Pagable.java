package com.gj.frameworks.entity;

public interface Pagable {

	int getOffset();
	
	int getLimit();
	
	boolean isAutoCount();
}
