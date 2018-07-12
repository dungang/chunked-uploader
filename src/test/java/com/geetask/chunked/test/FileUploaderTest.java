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

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import com.geetask.chunked.FileStorage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author dungang <dungang@126.com>
 * 
 *         2018年7月11日
 */
public class FileUploaderTest {

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
		when(mockRequest.getParameter("uuid")).thenReturn(uuid);
		when(mockRequest.getParameter("id")).thenReturn("WU_FILE_0");
		when(mockRequest.getParameter("type")).thenReturn("text/plain");
		when(mockRequest.getParameter("name")).thenReturn("test.txt");
		when(mockRequest.getParameter("size")).thenReturn(bytes.length + "");
		when(mockRequest.getInputStream()).thenReturn(new MockServletInputStream());
		when(mockRequest.getContentLength()).thenReturn(1000);
		FileStorage storage = new FileStorage();

		String url = storage.upload(mockRequest.getInputStream(), "", mockRequest, mockResponse);

		String except = "uploader\\" + DigestUtils.md5Hex(uuid + "WU_FILE_0") + ".txt";
		assertEquals(except, url);
		Files.deleteIfExists(Paths.get(url).toAbsolutePath());
		Files.deleteIfExists(Paths.get("uploader").toAbsolutePath());

	}

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
			when(mockRequest.getParameter("uuid")).thenReturn(uuid);
			when(mockRequest.getParameter("id")).thenReturn("WU_FILE_0");
			when(mockRequest.getParameter("type")).thenReturn("text/plain");
			when(mockRequest.getParameter("name")).thenReturn("test.txt");
			when(mockRequest.getParameter("size")).thenReturn(bytes.length + "");
			when(mockRequest.getParameter("chunks")).thenReturn("3");
			when(mockRequest.getParameter("chunk")).thenReturn("" + k);
			when(mockRequest.getInputStream()).thenReturn(new MockServletInputStream());
			when(mockRequest.getContentLength()).thenReturn(1000);
			FileStorage storage = new FileStorage();

			String url = storage.upload(mockRequest.getInputStream(), "", mockRequest, mockResponse);

			if (k < 2) {
				assertNull(url);
			} else {

				String except = "uploader\\" + DigestUtils.md5Hex(uuid + "WU_FILE_0") + ".txt";
				assertEquals(except, url);
				Files.deleteIfExists(Paths.get(url).toAbsolutePath());
				Files.deleteIfExists(Paths.get("uploader").toAbsolutePath());
			}
		}

	}
}
