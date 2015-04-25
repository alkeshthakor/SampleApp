package com.st.samudratech.modal;

import java.io.Serializable;

public class ItemData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String title;
    public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public ItemData(String title){         
        this.title = title;
    }
}
