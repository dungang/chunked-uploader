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

import java.io.InputStream;

/**
 * @author dungang <a href="mailto:dungang@126.com">dungang</a>
 * 
 *         2018年7月11日
 */
public class ChunkRequest {

	/**
	 * 当前页面文件的编号
	 */
	private String id;
	
	/**
	 * 每个文件上传的Id，全局唯一，由服务端生成。
	 * 一个文件包含多个分片，但是只有一个id
	 */
	private String uploadId;

	/**
	 * 当前页面回话id
	 */
	private String uuid;

	/**
	 * 文件的原始名称
	 */
	private String name;
	
	/**
	 * 如果分片，分片的个数
	 * 如果只有一个分片，没有此参数
	 *
	 */
	private String chunks;
	
	/**
	 * 分片的序号， 从0开始
	 */
	private String chunk;
	
    /**
     *	 分片文件的类型，如image/jpeg
     */
	private String type;
	
	
	/**
	 * 完整文件的大小
	 */
	private String size;
	
	/**
	 * 保存的基础目录
	 */
	private String saveBaseDir;
	
	
	/**
	 * 文件后缀
	 */
	private String extension;
	
	
	private InputStream inputStream;
	
	/**
	 * 文件存储最终的文件路径
	 */
	private String key;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getUuid() {
		return uuid;
	}


	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getChunks() {
		return chunks;
	}


	public void setChunks(String chunks) {
		this.chunks = chunks;
	}


	public String getChunk() {
		return chunk;
	}


	public void setChunk(String chunk) {
		this.chunk = chunk;
	}


	public String getSize() {
		return size;
	}


	public void setSize(String size) {
		this.size = size;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getExtension() {
		return extension;
	}


	public void setExtension(String extension) {
		this.extension = extension;
	}


	public String getSaveBaseDir() {
		return saveBaseDir;
	}


	public void setSaveBaseDir(String saveBaseDir) {
		this.saveBaseDir = saveBaseDir;
	}


	public InputStream getInputStream() {
		return inputStream;
	}


	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}


	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	public String getUploadId() {
		return uploadId;
	}


	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}


	@Override
	public String toString() {
		return "ChunkRequest [id=" + id + ", name=" + name + ", chunks=" + chunks + ", chunk=" + chunk + ", type="
				+ type + ", size=" + size + ", key=" + key + "]";
	}


	

}
