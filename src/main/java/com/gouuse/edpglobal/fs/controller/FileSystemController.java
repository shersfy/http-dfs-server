package com.gouuse.edpglobal.fs.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gouuse.datahub.commons.beans.Result;
import com.gouuse.datahub.commons.utils.AesUtil;
import com.gouuse.datahub.commons.utils.FileUtil;
import com.gouuse.edpglobal.fs.config.FsConfigProperties;
import com.gouuse.edpglobal.fs.service.FileManager;

@RestController
@RequestMapping("/fs")
public class FileSystemController extends BaseController {

	@Value("${spring.servlet.multipart.max-request-size}")
	private String maxSize;
	
	@Autowired
	private FsConfigProperties fsConfig;

	@Autowired
	private FileManager fileManager;

	@GetMapping("/file/list")
	public Result listFiles(@RequestParam(required=true)String path) {
		Result res = new Result();
		try {
			res.setModel(fileManager.listFiles(path));
		} catch (Exception e) {
			LOGGER.error("", e);
			res.setCode(FAIL);
			res.setMsg("list files error:"+path);
		}

		return res;
	}

	@PostMapping("/file/upload")
	public Result upload(@RequestParam(required=true)MultipartFile file, 
			@RequestParam(required=true)String app, 
			boolean useOrignFilename) {

		Result res = new Result();
		res.setMsg(String.format("maximum bytes size %s", maxSize));
		String path = "/%s/%s.%s";
		try {
			app = AesUtil.decryptHexStr(app, AesUtil.AES_SEED);
			List<String> apps = Arrays.asList(fsConfig.getPermitApps());
			if (!apps.contains(app)) {
				res.setCode(FAIL);
				res.setMsg("permission denied:"+app);
				return res;
			}
			
			String dir = file.getContentType().split("/")[0];
			dir = StringUtils.isBlank(dir)?"default":dir.toLowerCase();

			String filename = useOrignFilename?FilenameUtils.getBaseName(file.getOriginalFilename()):FileUtil.randomNameUUID();
			String ext = FilenameUtils.getExtension(file.getOriginalFilename());
			path = String.format(path, dir, filename, ext);

			String orign = "/%s/%s";
			orign = String.format(orign, app, file.getOriginalFilename());
			res.setModel(fileManager.upload(orign, path, file.getInputStream()));
		} catch (Exception e) {
			LOGGER.error("", e);
			res.setCode(FAIL);
			res.setMsg("upload error:"+file.getOriginalFilename());
		}

		return res;
	}

}
