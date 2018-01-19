package com.clearbill.dto;

import java.util.List;

public class UserBookmark {
	
	/**
	 * 分组
	 */
	private String category;
	/**
	 * 书签list
	 */
	private List<Bookmark> bookmarkList;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<Bookmark> getBookmarkList() {
		return bookmarkList;
	}

	public void setBookmarkList(List<Bookmark> bookmarkList) {
		this.bookmarkList = bookmarkList;
	}
	
}
