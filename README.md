# Chunked Uploader

分片文件上传，本项目是根据AJAX文件上传前端框架 fex-team/webuploader 开发的后台，如果您的前端组件也满足该组件的上传机制，也是可以通用的。

> 本项目适合`分片上传图片`的场景，如果您是普通上传文件，本项目暂时不支持，建议直接使用您的项目所使用的framework提供的文件上传更加高效。

## 注意事项

* 不支持删除文件（下个版本开发）
* 不支持列出文件（下个版本开发）

## maven

`
		<dependency>
			<groupId>com.geetask</groupId>
			<artifactId>chunked-uploader</artifactId>
			<version>0.0.1</version>
			<!--exclusions>
				<exclusion>
					<groupId>org.slf4j</groupId>
					<artifactId>slf4j-api</artifactId>
				</exclusion>
				<exclusion>
					<groupId>org.slf4j</groupId>
					<artifactId>slf4j-log4j12</artifactId>
				</exclusion>
				<exclusion>
					<groupId>javax.servlet</groupId>
					<artifactId>javax.servlet-api</artifactId>
				</exclusion>
			</exclusions-->
		</dependency>
`

## 分片的表单参数

项目现在只支持formdata的参数
	
|参数	|required	|说明											|
|-------|:---------	|:----------------------------------------------|
|uuid	|必须		|本窗口删除图片的回话id，注意不是sessionId			|
|id		|必须		|分片文件的Id，webuploader使用的格式是：WU_FILE_0	|
|name	|必须		|原始文件名称,包括文件后缀							|
|size	|必须		|原始文件的大小									|
|type	|必须		|文件类型,image/jpeg								|
|chunks	|分片时必须	|分片总数量										|
|chunk	|分片时必须	|本次请求的分片的序号，从0开始						|
	
	
	