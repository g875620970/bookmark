package com.clearbill.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.clearbill.dto.Bookmark;
import com.clearbill.dto.UserBookmark;
import com.clearbill.dto.UserInfo;
import com.clearbill.dto.groups.DelBookmark;
import com.clearbill.dto.groups.UpdateBookmark;
import com.clearbill.exception.ValidateException;
import com.clearbill.util.RedisUtils;
import com.clearbill.util.ResultUtil;
import com.clearbill.util.ResultVo;

@Controller
public class BookmarkController {

	/**
	 * 书签页
	 * @param userName
	 * @return
	 */
	@RequestMapping(value="/bookmarks/{userName}",method=RequestMethod.GET)
	public ModelAndView bookmarkList(@PathVariable String userName){
		return new ModelAndView("bookmarks","userName",userName);
	}

	/**
	 * 获取用户下所有书签信息
	 * @param userName
	 * @param password
	 * @return
	 * @throws ValidateException 
	 */
	@RequestMapping(value="/getBookmarks",method=RequestMethod.GET)
	@ResponseBody
	public ResultVo getBookmarkByUser(@Validated UserInfo userInfo,BindingResult validateResult) throws ValidateException{
		validateParam(validateResult);
		String userName = userInfo.getUserName();
		Map<String,String> bookmarkMap = RedisUtils.getInstall().hgetAll(userName);
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
			userBookmark.setScore(0d);
			userBookmark.setCategory(categoryEntry.getKey());
			userBookmark.setBookmarkList(categoryEntry.getValue());
			userBookmarkList.add(userBookmark);
		}
		//未分组书签
		if(otherBookmark.size()>0){
			UserBookmark userBookmark = new UserBookmark();
			userBookmark.setScore(100d);
			userBookmark.setCategory("未分组");
			userBookmark.setBookmarkList(otherBookmark);
			userBookmarkList.add(userBookmark);
		}
		return ResultUtil.success(userBookmarkList);
	}

	/**
	 * 新建书签
	 * @param session
	 * @param bookmark
	 * @param validateResult
	 * @return
	 * @throws ValidateException 
	 */
	@RequestMapping(value="/addBookmark",method=RequestMethod.POST)
	@ResponseBody
	public ResultVo addBookmark(@Validated UserInfo userInfo,@Validated(Default.class) Bookmark bookmark,BindingResult validateResult) throws ValidateException{
		validateParam(validateResult);
		String userName = userInfo.getUserName();
		//书签信息
		bookmark.setId(DigestUtils.md5Hex(String.valueOf(System.currentTimeMillis())));
		RedisUtils.getInstall().hset(userName,bookmark.getId(),JSON.toJSONString(bookmark));
		return ResultUtil.success();
	}

	/**
	 * 更新书签
	 * @param bookmark
	 * @param validateResult
	 * @return
	 * @throws ValidateException 
	 */
	@RequestMapping(value="/updateBookmark",method=RequestMethod.POST)
	@ResponseBody
	public ResultVo updateBookmark(@Validated UserInfo userInfo,@Validated({UpdateBookmark.class,Default.class})Bookmark bookmark,BindingResult validateResult) throws ValidateException{
		validateParam(validateResult);
		String userName = userInfo.getUserName();
		String idValue = RedisUtils.getInstall().hget(userName,bookmark.getId());
		if(StringUtils.isEmpty(idValue)){
			return ResultUtil.error("id错误");
		}
		RedisUtils.getInstall().hset(userName,bookmark.getId(),JSON.toJSONString(bookmark));
		return ResultUtil.success();
	}

	/**
	 * 删除书签
	 * @param bookmark
	 * @param validateResult
	 * @return
	 * @throws ValidateException 
	 */
	@RequestMapping(value="/delBookmark",method=RequestMethod.POST)
	@ResponseBody
	public ResultVo delBookmark(@Validated UserInfo userInfo,@Validated({DelBookmark.class})Bookmark bookmark,BindingResult validateResult) throws ValidateException{
		validateParam(validateResult);
		String userName = userInfo.getUserName();
		RedisUtils.getInstall().hdel(userName,bookmark.getId());
		return ResultUtil.success();
	}

	/**
	 * 错误信息
	 * @param errors
	 * @throws ValidateException 
	 */
	private void validateParam(BindingResult validateResult) throws ValidateException{
		if(validateResult.hasErrors()){
			for (ObjectError error : validateResult.getAllErrors()) {
				System.out.println(error.getDefaultMessage());
			}
			throw new ValidateException();
		}
	}

	/**
	 * 异常处理
	 * @param request
	 * @param ex
	 * @return
	 */
	@ExceptionHandler
	@ResponseBody
	public ResultVo exp(HttpServletRequest request, Exception ex) {
		if(ex instanceof ValidateException){
			return ResultUtil.error("参数错误");
		}
		return ResultUtil.error("服务器异常");
	}

}
