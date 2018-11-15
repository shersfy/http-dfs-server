package org.shersfy.fs.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ImageController extends BaseController{
	
	@RequestMapping("/")
	public ModelAndView index() {
		return new ModelAndView("index");
	}
    
    @RequestMapping("/imgs/{path}")
    public void viewImgs(@PathVariable("path") String path) throws IOException {
    	File img = new File("C:/data/imgs/"+path);
    	if (!img.isFile()) {
    		getResponse().getWriter().write("file not exist:"+img.getAbsolutePath());
    		return;
    	}
    	getResponse().setContentType("image/png");
    	InputStream input = new FileInputStream(img);
		IOUtils.copy(input, getResponse().getOutputStream());
		input.close();
		getResponse().getOutputStream().close();
    }

}
