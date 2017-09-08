package com.lld360.cnc.website.dto;

import java.net.URL;

import com.lld360.cnc.model.SoftDoc;
public class SoftDownloads {
	
	private SoftDoc soft;
	
	private URL url;

	
	public SoftDoc getSoft() {
		return soft;
	}

	public void setSoft(SoftDoc soft) {
		this.soft = soft;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}
	
	
}
