# Chunked Uploader

分片文件上传，本项目是根据AJAX文件上传前端框架 fex-team/webuploader 开发的后台，如果您的前端组件也满足该组件的上传机制，也是可以通用的。

> 本项目适合`分片上传图片`的场景，如果您是普通上传文件，本项目暂时不支持，建议直接使用您的项目所使用的framework提供的文件上传更加高效。

## 注意事项

* 不支持删除文件（完成开发）
* 不支持列出文件（废弃，通过读取磁盘获取文件的列表效率不高，不如用数据库存储文件的存储相对路径，通过数据查询更加方便，开发者可以自己实现）

## maven

```xml
<dependency>
	<groupId>com.geetask</groupId>
	<artifactId>chunked-uploader</artifactId>
	<version>1.0.2</version>
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
```

## 关于uploadId

* 全局唯一
* 最好每次打开上传的窗口重新生成
* 如果网站的上传图片并发不高，可以使用`UUID`来生成
* 如果并发高可以考虑面向服务的全局id生成器，比如`snowflake算法`

## 使用方法

> 1.实例化storage

|参数	|required	|说明											|
|-------|:---------	|:----------------------------------------------|
|name	|必须		|原始文件名称,包括文件后缀							|
|type	|必须		|文件类型,image/jpeg								|
|timestamp|必须		|时间戳								|

```java
	@Bean
	public AbstractStorage storage() {
		return new FileStorage();
	}
```

> 2. 获取文件上传的服务器参数

|参数	|required	|说明											|
|-------|:---------	|:----------------------------------------------|
|uploadId|必须		|每次发起上传文件前获取一个服务器`全局唯一`id，每个文件的uploadId是不同的|
|key	|必须		|是在服务器分配uploadId的时候同步返回的，是文件最终存储的路径|
|name	|必须		|原始文件名称,包括文件后缀							|
|size	|必须		|原始文件的大小									|
|type	|必须		|文件类型,image/jpeg								|
|chunks	|分片时必须	|分片总数量										|
|chunk	|分片时必须	|本次请求的分片的序号，从0开始						|

```java
	@Controller
	public class UploaderController {
		@Autowired
		private AbstractStorage storage;
		@PostMapping("/init")
		@ResponseBody
		public InitResponse initUpload(HttpServletRequest request, HttpServletResponse response) {
			return storage.initUpload("test", request, response);
		}
	}
```
结果类似
```json
{
	"uploadId":"370af9d2-c565-4b8e-9fa4-186d150affab",
	"key":"uploader\\test\\905cd7faa06cee41e0deb1a0502a868c.jpg"
}
```

> 3.处理上传的数据

```java
	@Controller
	public class UploaderController {
		@Autowired
		private AbstractStorage storage;
		@PostMapping("/uploader")
		@ResponseBody
		public ChunkResponse handle(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException {
			return storage.upload(file.getInputStream(),request, response);
		}
	}
```
结果
```json
{
	"key":"uploader\\test\\905cd7faa06cee41e0deb1a0502a868c.jpg",
	"uploadId":"370af9d2-c565-4b8e-9fa4-186d150affab",
	"completed":false //表示还没完成此文件所有的分片上传
}


{
	"key":"uploader\\test\\905cd7faa06cee41e0deb1a0502a868c.jpg",
	"uploadId":"370af9d2-c565-4b8e-9fa4-186d150affab",
	"completed":true //表示已完成此文件所有的分片上传
}
```
# Demo

[dungang/chunked-uploader-demo](https://github.com/dungang/chunked-uploader-demo)
