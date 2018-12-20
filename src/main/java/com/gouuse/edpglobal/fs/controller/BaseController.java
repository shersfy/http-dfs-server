package com.gouuse.edpglobal.fs.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.gouuse.datahub.commons.beans.Result.ResultCode;

public class BaseController {

	protected static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);
	protected static final int SUCESS = ResultCode.SUCESS;
	protected static final int FAIL	  = ResultCode.FAIL;

	private static ThreadLocal<HttpServletRequest> THREAD_LOCAL_REQUEST = new ThreadLocal<>();
	private static ThreadLocal<HttpServletResponse> THREAD_LOCAL_RESPONSE = new ThreadLocal<>();

	@ModelAttribute
	public void setRequestAndResponse(HttpServletRequest request, HttpServletResponse response) {
		THREAD_LOCAL_REQUEST.set(request);
		THREAD_LOCAL_RESPONSE.set(response);
	}

	public HttpServletRequest getRequest() {
		return THREAD_LOCAL_REQUEST.get();
	}

	public HttpServletResponse getResponse() {
		return THREAD_LOCAL_RESPONSE.get();
	}

	public String getRemoteAddr() {
		String remoteAddr = getRequest().getRemoteAddr();
		remoteAddr = "0:0:0:0:0:0:0:1".equals(remoteAddr)?"127.0.0.1":remoteAddr;
		return remoteAddr;
	}
	public String getRemoteHost() {
		String remoteAddr = getRequest().getRemoteHost();
		remoteAddr = "0:0:0:0:0:0:0:1".equals(remoteAddr)?"localhost":remoteAddr;
		return remoteAddr;
	}

}
