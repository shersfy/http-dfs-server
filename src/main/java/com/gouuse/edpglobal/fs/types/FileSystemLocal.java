package com.gouuse.edpglobal.fs.types;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.gouuse.datahub.commons.exception.DatahubException;
import com.gouuse.datahub.commons.meta.FileMeta;
import com.gouuse.datahub.commons.utils.FileUtil;

/**
 * 本地文件系统
 * @author pengy
 * @date 2018年12月17日
 */
public class FileSystemLocal extends FileSystemBase {

	@Override
	public String download(String path, String localDir) throws DatahubException {
		super.download(path, localDir);
		LOGGER.info("copy file {}-->{}", path, localDir);
		try {
			FileUtils.copyFileToDirectory(new File(path), new File(localDir));
		} catch (Exception e) {
			throw new DatahubException(e, "copy file from local file system error: %s", path);
		}
		String file = FileUtil.concat(FilenameUtils.getName(path), localDir);
		return file;
	}

	@Override
	public String upload(String path, InputStream input) throws DatahubException {
		if(StringUtils.isBlank(path) || input == null){
			return null;
		}
		try {

			File file = new File(path);
			if(file.isFile()){
				file.delete();
			}
			if(!file.getParentFile().isDirectory()){
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
			FileUtils.copyInputStreamToFile(input, file);
			return path;
		} catch (Exception e) {
			throw new DatahubException(e, "write input stream to local file system error: %s", path);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	@Override
	public void delete(String path) throws DatahubException {
		if(StringUtils.isBlank(path)){
			return;
		}
		LOGGER.debug("delete file {}", path);
		if(!FileUtils.deleteQuietly(new File(path))){
			String err = String.format("delete file error from local file system: %s", path);
			throw new DatahubException(err);
		}
	}

	@Override
	public long getFileSize(String path) {
		if(StringUtils.isBlank(path)){
			return 0;
		}
		long size = 0;
		File file = new File(path);
		if(file.isFile()){
			size = file.length();
		}
		return size;
	}

	@Override
	public boolean exist(String path) throws DatahubException {
		return new File(path).exists();
	}

	@Override
	public List<FileMeta> listFiles(String path) throws DatahubException {
		
		List<FileMeta> list = new ArrayList<>();
		Collection<File> files = FileUtils.listFiles(new File(path), null, false);
		files.forEach(file->{
			FileMeta meta = new FileMeta(file.getAbsolutePath());
			meta.setFile(file.isFile());
			meta.setDirectory(file.isDirectory());
			meta.setSize(file.length());
			list.add(meta);
		});
		return list;
	}
	
	@Override
	public InputStream getInputStream(String path) throws DatahubException {
		try {
			return new FileInputStream(path);
		} catch (Exception e) {
			throw new DatahubException(e);
		}
		
	}

}
