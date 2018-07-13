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
package com.geetask.chunked.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import com.geetask.chunked.ChunkResponse;
import com.geetask.chunked.FileStorage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author dungang <a href="mailto:dungang@126.com">dungang</a>
 * 
 *         2018年7月11日
 */
public class FileUploaderTest {

	/**
	 * 测试上传不分片的文件上传
	 * 
	 * @throws IOException
	 */
	@Test
	public void testSingleUploadFile() throws IOException {
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i < 10000; i++) {
			sbf.append("FileUploaderTest");
		}
		byte[] bytes = sbf.toString().getBytes();

		String uuid = "dhdhslhdhdhdhdhd";

		HttpServletRequest mockRequest = mock(HttpServletRequest.class);
		HttpServletResponse mockResponse = mock(HttpServletResponse.class);
		when(mockRequest.getParameter("uploadId")).thenReturn(uuid);
		when(mockRequest.getParameter("key")).thenReturn("uploader/test/ddddds.txt");
		when(mockRequest.getParameter("type")).thenReturn("text/plain");
		when(mockRequest.getParameter("name")).thenReturn("test.txt");
		when(mockRequest.getParameter("size")).thenReturn(bytes.length + "");
		when(mockRequest.getInputStream()).thenReturn(new MockServletInputStream());
		when(mockRequest.getContentLength()).thenReturn(1000);
		FileStorage storage = new FileStorage();
		ChunkResponse rst = storage.upload(mockRequest.getInputStream(), mockRequest, mockResponse);
		Path filePath = Paths.get(mockRequest.getParameter("key")).toAbsolutePath();
		assertTrue(rst.isCompleted());
		assertTrue(filePath.toFile().exists());
		Files.deleteIfExists(filePath);
		Files.deleteIfExists(Paths.get("uploader/test").toAbsolutePath());
		Files.deleteIfExists(Paths.get("uploader").toAbsolutePath());

	}

	/**
	 * 测试分片的文件上传
	 * 
	 * @throws IOException
	 */
	@Test
	public void testChunkUploadFile() throws IOException {

		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i < 10000; i++) {
			sbf.append("FileUploaderTest");
		}
		byte[] bytes = sbf.toString().getBytes();

		String uuid = "dhdhslhdhdhdhdhd";
		for (int k = 0; k < 3; k++) {
			HttpServletRequest mockRequest = mock(HttpServletRequest.class);
			HttpServletResponse mockResponse = mock(HttpServletResponse.class);
			when(mockRequest.getParameter("uploadId")).thenReturn(uuid);
			when(mockRequest.getParameter("key")).thenReturn("uploader/test/ddddds.txt");
			when(mockRequest.getParameter("type")).thenReturn("text/plain");
			when(mockRequest.getParameter("name")).thenReturn("test.txt");
			when(mockRequest.getParameter("chunks")).thenReturn("3");
			when(mockRequest.getParameter("chunk")).thenReturn("" + k);
			when(mockRequest.getParameter("size")).thenReturn(bytes.length + "");
			when(mockRequest.getInputStream()).thenReturn(new MockServletInputStream());
			when(mockRequest.getContentLength()).thenReturn(1000);
			FileStorage storage = new FileStorage();

			ChunkResponse rst = storage.upload(mockRequest.getInputStream(), mockRequest, mockResponse);

			if (k < 2) {
				assertFalse(rst.isCompleted());
			} else {
				Path filePath = Paths.get(mockRequest.getParameter("key")).toAbsolutePath();
				assertTrue(rst.isCompleted());
				assertTrue(filePath.toFile().exists());
				Files.deleteIfExists(filePath);
				Files.deleteIfExists(Paths.get("uploader/test").toAbsolutePath());
				Files.deleteIfExists(Paths.get("uploader").toAbsolutePath());
			}
		}

	}

	/**
	 * 测试删除上传成功的文件
	 * 
	 * @throws IOException
	 */
	@Test
	public void testDeleteUploadFile() throws IOException {
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i < 10000; i++) {
			sbf.append("FileUploaderTest");
		}
		byte[] bytes = sbf.toString().getBytes();

		String uuid = "dhdhslhdhdhdhdhd";

		HttpServletRequest mockRequest = mock(HttpServletRequest.class);
		HttpServletResponse mockResponse = mock(HttpServletResponse.class);
		when(mockRequest.getParameter("uploadId")).thenReturn(uuid);
		when(mockRequest.getParameter("key")).thenReturn("uploader/test/ddddds.txt");
		when(mockRequest.getParameter("type")).thenReturn("text/plain");
		when(mockRequest.getParameter("name")).thenReturn("test.txt");
		when(mockRequest.getParameter("size")).thenReturn(bytes.length + "");
		when(mockRequest.getInputStream()).thenReturn(new MockServletInputStream());
		when(mockRequest.getContentLength()).thenReturn(1000);
		FileStorage storage = new FileStorage();
		ChunkResponse rst = storage.upload(mockRequest.getInputStream(), mockRequest, mockResponse);
		assertTrue(storage.delete(rst.getKey()));
		Files.deleteIfExists(Paths.get("uploader/test").toAbsolutePath());
		Files.deleteIfExists(Paths.get("uploader").toAbsolutePath());

	}
}
