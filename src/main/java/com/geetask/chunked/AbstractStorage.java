/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.geetask.chunked;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dungang <dungang@126.com>
 * 
 *         2018年7月10日
 */
public abstract class AbstractStorage implements IStorage {

	private static Logger logger = LoggerFactory.getLogger(AbstractStorage.class);
	
	/**
	 * 文件存储的基础路径
	 */
	private String baseDir = "";

	/**
	 * 上传的目录
	 */
	private String uploaderDir = "uploader";

	/**
	 * 获取文件的后组
	 * 
	 * @param fileName
	 * @return
	 */
	protected String fileExtension(String fileName) {
		return fileName.substring(fileName.indexOf("."), fileName.length());
	}

	public static ArrayList<String> getFiles(String path) {

		File file = new File(path);
		String[] files = file.list();
		ArrayList<String> names = new ArrayList<String>();
		for (String name : files) {
			if (name.equals(".") || name.equals("..")) {
				continue;
			}
			names.add(name);
		}
		return names;
	}

	/**
	 * 
	 * @param inputStream
	 * @param dirSuffix   保存文件base目录的后缀,比如 2018/06/08
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public String upload(InputStream inputStream, String dirSuffix, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		if (request.getContentLength() > 0) {

			Param param = new Param();
			param.setId(request.getParameter("id"));
			param.setUuid(request.getParameter("uuid"));
			param.setName(request.getParameter("name"));
			param.setType(request.getParameter("type"));
			param.setChunk(null == request.getParameter("chunk") ? 0 : Integer.valueOf(request.getParameter("chunk")));
			param.setChunks(
					null == request.getParameter("chunks") ? 0 : Integer.valueOf(request.getParameter("chunks")));
			param.setSize(Integer.valueOf(request.getParameter("size")));
			param.setExtension(fileExtension(param.getName()));
			param.setSaveBaseDir(uploaderDir);
			logger.debug("web uploader param :" + param.toString());
			return write(inputStream, param, dirSuffix);
		}
		return null;
	}

	public String getUploaderDir() {
		return uploaderDir;
	}

	public void setUploaderDir(String uploaderDir) {
		this.uploaderDir = uploaderDir;
	}

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

}
