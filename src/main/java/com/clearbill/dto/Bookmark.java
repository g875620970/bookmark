package com.clearbill.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

import com.clearbill.dto.groups.DelBookmark;
import com.clearbill.dto.groups.UpdateBookmark;

/**
 * 书签信息
 * @author v-gongwenguo
 *
 */
public class Bookmark {
	/**
	 * id
	 */
	@NotNull(message="id不能为空",groups={UpdateBookmark.class,DelBookmark.class})
	@Size(min=20,max=32,message="id错误",groups={UpdateBookmark.class,DelBookmark.class})
	private String id;
	/**
	 * 类别
	 */
	@Size(max=6,message="类别长度不能超过6")
	private String category;
	/**
	 * 书签名称
	 */
	@NotNull(message="书签名称不能为空")
	@Size(min=1,max=20,message="名称长度应为1~20")
	private String name;
	/**
	 * 书签地址
	 */
	@NotNull(message="书签地址不能为空")
	@URL(message="书签地址要为url")
	private String url;
	/**
	 * 备注
	 */
	@Size(max=100,message="备注长度不能超过100")
	private String remark;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
