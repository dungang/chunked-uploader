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
import java.io.InputStream;

/**
 * @author dungang <dungang@126.com>
 * 
 *         2018年7月10日
 */
public interface IStorage {

	/**
	 * 写入文件
	 * 
	 * @param param      Param 参数对象
	 * @param pathSuffix String 文件保存的目录后缀
	 * @return String
	 */
	public String write(InputStream inputStream, Param param, String pathSuffix) throws IOException;

	/**
	 * 根据相对路径删除文件
	 * @param fileRelativePath
	 * @return
	 */
	public boolean delete(String fileRelativePath);

}
