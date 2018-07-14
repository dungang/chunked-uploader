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
 * @author dungang <a href="mailto:dungang@126.com">dungang</a>
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
	 * @param fileName String
	 * @return String
	 */
	protected String fileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * 获取目录的文件列表
	 * @param path String
	 * @return ArrayList
	 */
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
	 * 初始化上传文件的参数
	 * @param dirSuffix String
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return InitResponse
	 * @return InitResponse
	 */
	public final InitResponse initUpload(String dirSuffix, HttpServletRequest request, HttpServletResponse response) {
		InitRequest initRequest = new InitRequest();
		initRequest.setName(request.getParameter("name"));
		initRequest.setType(request.getParameter("type"));
		initRequest.setTimestamp(request.getParameter("timestamp"));
		initRequest.setDirSuffix(dirSuffix);
		return this.initChunkUpload(initRequest, request, response);
	}

	/**
	 * 处理上传的文件分片
	 * @param inputStream InputStream
	 * @param chunkSize long
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ChunkResponse
	 * @throws IOException 
	 */
	public final ChunkResponse upload(InputStream inputStream, long chunkSize, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		if (request.getContentLength() > 0) {
			ChunkRequest chunkRequest = new ChunkRequest();
			chunkRequest.setUploadId(request.getParameter("uploadId"));
			chunkRequest.setChunk(request.getParameter("chunk"));
			chunkRequest.setChunks(request.getParameter("chunks"));
			chunkRequest.setName(request.getParameter("name"));
			chunkRequest.setType(request.getParameter("type"));
			chunkRequest.setSize(request.getParameter("size"));
			chunkRequest.setKey(request.getParameter("key"));
			chunkRequest.setChunkSize(chunkSize);
			chunkRequest.setInputStream(inputStream);
			logger.debug("web uploader param :" + chunkRequest.toString());
			return write(chunkRequest, request, response);
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
