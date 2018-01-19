package com.clearbill.web;

import javax.validation.groups.Default;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.clearbill.dto.Bookmark;
import com.clearbill.dto.UserInfo;
import com.clearbill.dto.groups.DelBookmark;
import com.clearbill.dto.groups.UpdateBookmark;
import com.clearbill.exception.ValidateException;
import com.clearbill.service.BookmarkService;
import com.clearbill.util.ResultVo;

@Controller
public class BookmarkController extends BaseController{

	@Autowired
	BookmarkService bookmarkService;
	
	/**
	 * 书签页
	 * @param userName
	 * @return
	 */
	@RequestMapping(value="/bookmarks",method=RequestMethod.GET)
	public ModelAndView bookmarkList(){
		return new ModelAndView("bookmark/bookmarks");
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
		return bookmarkService.getBookmarkByUser(userName);
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
		return bookmarkService.addBookmark(userInfo.getUserName(), bookmark);
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
		return bookmarkService.updateBookmark(userInfo.getUserName(), bookmark);
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
		return bookmarkService.delBookmark(userInfo.getUserName(), bookmark);
	}

}
