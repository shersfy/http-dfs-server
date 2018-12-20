package com.gouuse.edpglobal.fs.filter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.gouuse.edpglobal.fs.config.FsConfigProperties;
import com.gouuse.edpglobal.fs.service.FileManager;

@Component
public class ViewFilesInterceptor implements HandlerInterceptor {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ViewFilesInterceptor.class);
    
    public static String[] patterns = {"/fs/image/**", "/fs/text/**"};
    
	@Autowired
	private FileManager fileManager;
	
	@Autowired
	private FsConfigProperties fsConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
    	
    	String url = request.getRequestURI();
    	LOGGER.info("load resource url: {}", request.getRequestURL());
    	
    	String path = "/fs";
    	path = url.substring(url.indexOf(path)+path.length());
    	
    	InputStream input   = null;
		OutputStream output = null;
		response.setContentType(ContentType.create("text/plain", Consts.UTF_8).toString());
		try {
			output = response.getOutputStream();
			if (fileManager.exist(path)) {
				input = fileManager.getInputStream(path);
			} else {
				String warn = String.format("file not exist '%s'", path);
				LOGGER.warn(warn);
				if (StringUtils.startsWith(path, "/image")) {
					input = fsConfig.getDefaultImage().getInputStream();
				} else {
					input = new ByteArrayInputStream(warn.getBytes());
				}
			}

			if (StringUtils.startsWith(path, "/image")) {
				response.setContentType(ContentType.IMAGE_PNG.toString());
			}
			else if (StringUtils.endsWithIgnoreCase(path, "html")){
				response.setContentType(ContentType.create("text/html", Consts.UTF_8).toString());
			}
			else if (StringUtils.endsWithIgnoreCase(path, "xml")){
				response.setContentType(ContentType.create("text/xml", Consts.UTF_8).toString());
			}
			IOUtils.copy(input, output);

		} catch (Exception e) {
			LOGGER.error("", e);
			IOUtils.write("view file error:"+path, output);
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}

        return false;
    }

}
