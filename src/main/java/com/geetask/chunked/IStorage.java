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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author dungang <dungang@126.com>
 * 
 *         2018年7月10日
 */
public interface IStorage {

	/**
	 * 每次开始分片上传文件的时，要先获取初始化的参数
	 * 
	 * @param initRequest InitRequest
	 * @param request     HttpServletRequest
	 * @param response    HttpServletResponse
	 * @return InitResponse
	 */
	public InitResponse initChunkUpload(InitRequest initRequest, HttpServletRequest request,
			HttpServletResponse response);

	/**
	 * 写入文件
	 * 
	 * @param chunkRequest ChunkRequest 参数对象
	 * @param request      HttpServletRequest
	 * @param response     HttpServletResponse
	 * @return ChunkResponse
	 * @throws IOException
	 */
	public ChunkResponse write(ChunkRequest chunkRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException;

	/**
	 * 根据相对路径删除文件
	 * 
	 * @param key String
	 * @return boolean
	 */
	public boolean delete(String key);

}
