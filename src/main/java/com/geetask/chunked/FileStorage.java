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
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dungang <dungang@126.com>
 * 
 *         2018年7月10日
 */
public class FileStorage extends AbstractStorage {

	private static Logger logger = LoggerFactory.getLogger(FileStorage.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.geetask.webuploader.IStorage#write(java.io.InputStream,
	 * com.geetast.webuploader.Param, java.lang.String)
	 */
	@SuppressWarnings("resource")
	public String write(InputStream inputStream, Param param, String dirSuffix) throws IOException {

		RandomAccessFile rf = null;
		FileChannel channel = null;
		FileChannel resultFileChannel = null;
		try {
			Path basePath = Paths.get(getBaseDir());
			Path savePath = Paths.get(param.getSaveBaseDir()).resolve(dirSuffix);
			Path path = basePath.resolve(savePath).toAbsolutePath();

			String fileId = DigestUtils.md5Hex(param.getUuid() + param.getId());
			int chunks = param.getChunks();

			if (false == Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
				try {
					Files.createDirectories(path);
				} catch(FileAlreadyExistsException e) {
					e.printStackTrace();
					logger.warn("direct existed: "+ path.toString());
				}
				
			}

			// 没有分片时，执行直接保存文件
			if (chunks == 0) {
				Files.copy(inputStream, path.resolve(fileId + param.getExtension()),
						StandardCopyOption.REPLACE_EXISTING);
				return savePath.resolve(fileId + param.getExtension()).toString();
			} else {
				// 否则执行，先保存每个分片，最后合并分片

				if (false == Files.exists(path.resolve(fileId), LinkOption.NOFOLLOW_LINKS)) {
					try {
						Files.createDirectories(path.resolve(fileId));
					} catch (FileAlreadyExistsException e) {
						e.printStackTrace();
						logger.warn("direct existed: "+ path.resolve(fileId).toString());
					}
				}

				Files.copy(inputStream, path.resolve(fileId + "/" + param.getChunk()),
						StandardCopyOption.REPLACE_EXISTING);

				ArrayList<String> files = getFiles(path.resolve(fileId + "/").toAbsolutePath().toString());

				if (files.size() == chunks) {

					Path rPath = path.resolve(fileId + param.getExtension()).toAbsolutePath();
					resultFileChannel = new FileOutputStream(rPath.toFile(), true).getChannel();
					for (int i = 0; i < chunks; i++) {
						FileChannel blk = new FileInputStream(path.resolve(fileId + "/" + i).toAbsolutePath().toFile())
								.getChannel();
						resultFileChannel.transferFrom(blk, resultFileChannel.size(), blk.size());
						blk.close();
					}

					for (int i = 0; i < chunks; i++) {
						File file = path.resolve(fileId + "/" + i).toAbsolutePath().toFile();
						if (file.exists()) {
							file.delete();
						}
					}
					Files.deleteIfExists(path.resolve(fileId));
					return savePath.resolve(fileId + param.getExtension()).toString();

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

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.geetask.webuploader.IStorage#delete(java.lang.String)
	 */
	public boolean delete(String savePath) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.geetask.webuploader.IStorage#list(java.lang.String, int, int)
	 */
	public List<String> list(String savePath, int start, int size) {
		// TODO Auto-generated method stub
		return null;
	}

}
