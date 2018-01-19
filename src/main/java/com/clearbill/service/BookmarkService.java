package com.clearbill.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.clearbill.dto.Bookmark;
import com.clearbill.dto.UserBookmark;
import com.clearbill.util.JedisUtils;
import com.clearbill.util.ResultUtil;
import com.clearbill.util.ResultVo;

@Service
public class BookmarkService {
	
	/**
	 * 获取用户下所有书签
	 * @param userName
	 * @return
	 */
	public ResultVo getBookmarkByUser(String userName){
		Map<String,String> bookmarkMap = JedisUtils.getInstall().hgetAll(userName);
		//书签信息分组解析到map
		Map<String,List<Bookmark>> categoryMap = new TreeMap<String, List<Bookmark>>();
		List<Bookmark> otherBookmark = new ArrayList<Bookmark>();
		for(Map.Entry<String, String> bookmarkEntry : bookmarkMap.entrySet()){
			Bookmark bookmark = JSONObject.parseObject(bookmarkEntry.getValue(),Bookmark.class);
			String category = bookmark.getCategory();
			if(StringUtils.isEmpty(category)){
				otherBookmark.add(bookmark);
			}
			else{
				List<Bookmark> bookmarkList = categoryMap.get(category);
				if(bookmarkList==null){
					bookmarkList = new ArrayList<Bookmark>();
					categoryMap.put(category, bookmarkList);
				}
				bookmarkList.add(bookmark);
			}
		}
		//分组书签
		List<UserBookmark> userBookmarkList = new ArrayList<UserBookmark>();
		for (Map.Entry<String, List<Bookmark>> categoryEntry : categoryMap.entrySet()) {
			UserBookmark userBookmark = new UserBookmark();
			userBookmark.setCategory(categoryEntry.getKey());
			userBookmark.setBookmarkList(categoryEntry.getValue());
			userBookmarkList.add(userBookmark);
		}
		//未分组书签
		if(otherBookmark.size()>0){
			UserBookmark userBookmark = new UserBookmark();
			userBookmark.setCategory("未分组");
			userBookmark.setBookmarkList(otherBookmark);
			userBookmarkList.add(userBookmark);
		}
		return ResultUtil.success(userBookmarkList);
	}
	
	/**
	 * 新增书签信息
	 * @param userName
	 * @param bookmark
	 * @return
	 */
	public ResultVo addBookmark(String userName,Bookmark bookmark){
		bookmark.setId(DigestUtils.md5Hex(String.valueOf(System.currentTimeMillis())));
		JedisUtils.getInstall().hset(userName,bookmark.getId(),JSON.toJSONString(bookmark));
		return ResultUtil.success();
	}
	
	/**
	 * 更新书签信息
	 * @param userName
	 * @param bookmark
	 * @return
	 */
	public ResultVo updateBookmark(String userName,Bookmark bookmark){
		String idValue = JedisUtils.getInstall().hget(userName,bookmark.getId());
		if(StringUtils.isEmpty(idValue)){
			return ResultUtil.error("id错误");
		}
		JedisUtils.getInstall().hset(userName,bookmark.getId(),JSON.toJSONString(bookmark));
		return ResultUtil.success();
	}
	
	/**
	 * 删除书签
	 * @param userName
	 * @param bookmark
	 * @return
	 */
	public ResultVo delBookmark(String userName,Bookmark bookmark){
		JedisUtils.getInstall().hdel(userName,bookmark.getId());
		return ResultUtil.success();
	}
	
}
