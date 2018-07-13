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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dungang <a href="mailto:dungang@126.com">dungang</a>
 * 
 *         2018年7月10日
 */
public class FileStorage extends AbstractStorage {

	private static Logger logger = LoggerFactory.getLogger(FileStorage.class);

	public InitResponse initChunkUpload(InitRequest initRequest, HttpServletRequest request,
			HttpServletResponse response) {
		InitResponse initResponse = new InitResponse();
		initResponse.setUploadId(UUID.randomUUID().toString());
		String extension = this.fileExtension(initRequest.getName());
		String fileName = DigestUtils.md5Hex(initResponse.getUploadId() + initRequest.getTimestamp());
		Path keyPath = Paths.get(getUploaderDir()).resolve(initRequest.getDirSuffix()).resolve(fileName + extension);
		initResponse.setKey(keyPath.toString());
		return initResponse;
	}

	@SuppressWarnings("resource")
	public ChunkResponse write(ChunkRequest chunkRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		InputStream inputStream = request.getInputStream();
		RandomAccessFile rf = null;
		FileChannel channel = null;
		FileChannel resultFileChannel = null;
		ChunkResponse chunkResponse = new ChunkResponse();
		chunkResponse.setKey(chunkRequest.getKey());
		chunkResponse.setUploadId(chunkRequest.getUploadId());
		
		try {
			Path keyPath = Paths.get(chunkRequest.getKey());
			Path keyAbPath = keyPath.toAbsolutePath();
			Path parentPath = keyPath.getParent().toAbsolutePath();
			String chunks = chunkRequest.getChunks();

			if (false == Files.exists(parentPath, LinkOption.NOFOLLOW_LINKS)) {
				try {
					Files.createDirectories(parentPath);
				} catch (FileAlreadyExistsException e) {
					e.printStackTrace();
					logger.warn("direct existed: " + parentPath.toString());
				}

			}

			// 没有分片时，执行直接保存文件
			if (null == chunks || chunks.equals("0")) {
				Files.copy(inputStream, keyAbPath, StandardCopyOption.REPLACE_EXISTING);
				chunkResponse.setCompleted(true);
				return chunkResponse;
			} else {
				// 否则执行，先保存每个分片，最后合并分片
				Path chunksPath = parentPath.resolve(chunkRequest.getUploadId()).toAbsolutePath();
				if (false == Files.exists(chunksPath, LinkOption.NOFOLLOW_LINKS)) {
					try {
						Files.createDirectories(chunksPath);
					} catch (FileAlreadyExistsException e) {
						e.printStackTrace();
						logger.warn("direct existed: " + chunksPath.toString());
					}
				}

				Files.copy(inputStream, chunksPath.resolve(chunkRequest.getChunk()),
						StandardCopyOption.REPLACE_EXISTING);

				ArrayList<String> files = getFiles(chunksPath.toString());
				int chunksNum = Integer.valueOf(chunks);
				if (files.size() == chunksNum) {

					resultFileChannel = new FileOutputStream(keyAbPath.toFile(), true).getChannel();
					for (int i = 0; i < chunksNum; i++) {
						FileChannel blk = new FileInputStream(chunksPath.resolve(i + "").toAbsolutePath().toFile())
								.getChannel();
						resultFileChannel.transferFrom(blk, resultFileChannel.size(), blk.size());
						blk.close();
					}

					for (int i = 0; i < chunksNum; i++) {
						File file = chunksPath.resolve("" + i).toAbsolutePath().toFile();
						if (file.exists()) {
							file.delete();
						}
					}
					Files.deleteIfExists(chunksPath);
					chunkResponse.setCompleted(true);
					return chunkResponse;

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			logger.warn("File load fail.", e);
		} finally {
			if (null != rf) {
				rf.close();
			}
			if (null != channel) {
				channel.close();
			}
			if (null != resultFileChannel) {
				resultFileChannel.close();
			}
			inputStream.close();
		}
		chunkResponse.setCompleted(false);
		return chunkResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.geetask.webuploader.IStorage#delete(java.lang.String)
	 */
	public boolean delete(String fileRelativePath) {
		File file = Paths.get(getBaseDir()).resolve(fileRelativePath).toFile();
		if (file.isFile()) {
			return file.delete();
		}
		return false;
	}

}
