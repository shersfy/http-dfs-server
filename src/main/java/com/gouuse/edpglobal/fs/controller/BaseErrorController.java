package com.gouuse.edpglobal.fs.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gouuse.datahub.commons.beans.Result;

@Controller
public class BaseErrorController extends BaseController 
	implements ErrorController{
	
	@Override
	public String getErrorPath() {
		return "/error";
	}
	
	@RequestMapping("/error")
	@ResponseBody
	public Result error() {
		Result res = new Result();
		
		HttpStatus status = HttpStatus.valueOf(getResponse().getStatus());
		res.setCode(status.value());
		res.setMsg(status.getReasonPhrase());

		Object error = getRequest().getAttribute("javax.servlet.error.message");
		if (error!=null && StringUtils.isNotBlank(error.toString())) {
			StringBuffer msg = new StringBuffer(res.getMsg());
			msg.append("; ").append(error.toString());
			res.setMsg(msg.toString());
		}
		res.setModel(getRequest().getAttribute("javax.servlet.error.request_uri"));

		return res;
	}
	
}
