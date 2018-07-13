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

/**
 * @author dungang <a href="mailto:dungang@126.com">dungang</a>
 * 
 *         2018年7月13日
 */
public class InitRequest {

	/**
	 * 文件类型 image/jpeg
	 */
	private String type;

	/**
	 * 文件名称， xxx.jpg
	 */
	private String name;

	/**
	 * 目录后缀 比如 2018-06-08
	 */
	private String dirSuffix;
	
	/**
	 * 客户端生成的时间戳
	 */
	private String timestamp;
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDirSuffix() {
		return dirSuffix;
	}

	public void setDirSuffix(String dirSuffix) {
		this.dirSuffix = dirSuffix;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}
