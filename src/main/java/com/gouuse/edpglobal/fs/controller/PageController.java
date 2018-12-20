package com.gouuse.edpglobal.fs.controller;

import java.io.IOException;

import org.apache.http.Consts;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController extends BaseController {
	
	@RequestMapping("/")
	public void index() throws IOException {
		getResponse().setContentType(ContentType.create("text/html", Consts.UTF_8).toString());
		getResponse().getWriter().write("<h2 style='text-align: center; margin-top: 30px;'>Welcome EDP Distributed File System Server</h2>");
		getResponse().getWriter().close();
	}

}
