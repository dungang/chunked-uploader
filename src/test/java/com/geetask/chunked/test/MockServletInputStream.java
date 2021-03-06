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

import java.io.ByteArrayInputStream;
import java.io.IOException;


import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

/**
 * @author dungang <a href="mailto:dungang@126.com">dungang</a>
 * 
 *         2018年7月11日
 */
class MockServletInputStream extends ServletInputStream {

	private ByteArrayInputStream is;

	public MockServletInputStream(byte[] myBytes) {

		is = new ByteArrayInputStream(myBytes);

	}

	@Override
	public boolean isFinished() {
		return is.available() <= 0 ? true : false;
		// return (lastIndexRetrieved == myBytes.length - 1);
	}

	@Override
	public boolean isReady() {
		return isFinished();
	}

	@Override
	public void setReadListener(ReadListener readListener) {

	}

	@Override
	public int read() throws IOException {
		return is.read();
	}
}
