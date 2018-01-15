package com.clearbill.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.groups.Default;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.clearbill.dto.Bookmark;
import com.clearbill.dto.groups.DelBookmark;
import com.clearbill.dto.groups.UpdateBookmark;
import com.clearbill.util.RedisUtils;
import com.clearbill.util.ResultUtil;

@RestController
public class BookmarkController {
	
	@RequestMapping(value="/getSession/${userName}",method=RequestMethod.GET)
	public String getSession(@PathVariable String userName){
		if(StringUtils.isEmpty(userName)){
			return ResultUtil.error("参数错误");
		}
//		String relSession = DigestUtils.md5Hex(userName+"&"+secret);
//		if(!relSession.equals(secret)){
//			return ResultUtil.error("用户验证失败");
//		}
		return ResultUtil.success(null);
	}
	
	/**
	 * 获取用户下所有书签信息
	 * @param userName
	 * @param password
	 * @return
	 */
	@RequestMapping(value="/getBookmarks/{userName}",method=RequestMethod.GET)
	public String getBookmarkByUser(@PathVariable String userName,String session){
		if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(session)){
			return ResultUtil.error("参数错误");
		}
		String relSession = DigestUtils.md5Hex(userName);
		if(!relSession.equals(session)){
			System.out.println(relSession);
			return ResultUtil.error("用户验证失败");
		}
		Map<String,String> bookmarkMap = RedisUtils.getInstall().hgetAll(userName);
		List<Bookmark> bookmarkList = new ArrayList<Bookmark>();
		for(Map.Entry<String, String> bookmarkEntry : bookmarkMap.entrySet()){
			Bookmark bookmark = JSONObject.parseObject(bookmarkEntry.getValue(),Bookmark.class);
			bookmarkList.add(bookmark);
		}
		return ResultUtil.success(bookmarkList);
	}
	
	/**
	 * 新建书签
	 * @param bookmark
	 * @param validateResult
	 * @return
	 */
	@RequestMapping(value="/addBookmark",method=RequestMethod.POST)
	public String addBookmark(@Validated(Default.class) Bookmark bookmark,BindingResult validateResult){
		if(validateResult.hasErrors()){
			outErrorMsg(validateResult.getAllErrors());
			return ResultUtil.error("参数错误");
		}
		bookmark.setId(DigestUtils.md5Hex(String.valueOf(System.currentTimeMillis())));
		RedisUtils.getInstall().hset(bookmark.getUserName(),bookmark.getId(),JSON.toJSONString(bookmark));
		return ResultUtil.success(bookmark);
	}
	
	/**
	 * 更新书签
	 * @param bookmark
	 * @param validateResult
	 * @return
	 */
	@RequestMapping(value="/updateBookmark",method=RequestMethod.POST)
	public String updateBookmark(@Validated({UpdateBookmark.class,Default.class})Bookmark bookmark,BindingResult validateResult){
		if(validateResult.hasErrors()){
			outErrorMsg(validateResult.getAllErrors());
			return ResultUtil.error("参数错误");
		}
		String value = RedisUtils.getInstall().hget(bookmark.getUserName(),bookmark.getId());
		if(StringUtils.isEmpty(value)){
			return ResultUtil.error("id错误");
		}
		RedisUtils.getInstall().hset(bookmark.getUserName(),bookmark.getId(),JSON.toJSONString(bookmark));
		return ResultUtil.success(null);
	}
	
	/**
	 * 删除书签
	 * @param bookmark
	 * @param validateResult
	 * @return
	 */
	@RequestMapping(value="/delBookmark",method=RequestMethod.POST)
	public String delBookmark(@Validated({DelBookmark.class})Bookmark bookmark,BindingResult validateResult){
		if(validateResult.hasErrors()){
			outErrorMsg(validateResult.getAllErrors());
			return ResultUtil.error("参数错误");
		}
		RedisUtils.getInstall().hdel(bookmark.getUserName(),bookmark.getId());
		return ResultUtil.success(null);
	}
	
	/**
	 * 错误信息
	 * @param errors
	 */
	private void outErrorMsg(List<ObjectError> errors){
		for (ObjectError error : errors) {
			System.out.println(error.getDefaultMessage());
		}
	}
	
}
