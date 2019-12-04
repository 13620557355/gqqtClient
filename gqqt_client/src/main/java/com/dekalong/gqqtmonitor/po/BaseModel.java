package com.dekalong.gqqtmonitor.po;

import java.io.Serializable;

import com.dekalong.gqqtmonitor.util.pageutil.Page;
/**
 * 
 * @author Long
 *公共model类，所有model类的uuid和分页的page都在这里
 *
 */
@SuppressWarnings({"rawtypes"})
public class BaseModel implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;
	private Integer uuid;
	private Integer manUuidSearch;
	private Integer custUuidSearch;
	public Integer getUuid() {
		return uuid;
	}
	public void setUuid(Integer uuid) {
		this.uuid = uuid;
	}
	
	private Page page = new Page();

	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}	

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseModel other = (BaseModel) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}
	public Integer getManUuidSearch() {
		return manUuidSearch;
	}
	public void setManUuidSearch(Integer manUuidSearch) {
		this.manUuidSearch = manUuidSearch;
	}
	public Integer getCustUuidSearch() {
		return custUuidSearch;
	}
	public void setCustUuidSearch(Integer custUuidSearch) {
		this.custUuidSearch = custUuidSearch;
	}

}
