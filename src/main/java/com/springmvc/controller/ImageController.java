package com.springmvc.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 静态图片兜底：直接从运行时 webapps/ROOT/images/web 读取，避免资源映射异常导致的 404。
 */
@Controller
public class ImageController {

    @GetMapping("/assets/web/{fileName:.+}")
    public ResponseEntity<Resource> serveWebImages(@PathVariable("fileName") String fileName,
                                                   HttpServletRequest request) throws Exception {
        ServletContext context = request.getServletContext();
        // 实际文件依然保存在 /images/web 下，这里仅换一个不与 Tomcat 默认 /images 冲突的访问路径。
        String basePath = context.getRealPath("/images/web/");
        Path filePath = Paths.get(basePath).resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }
        String contentType = context.getMimeType(filePath.toString());
        if (contentType == null) {
            try {
                contentType = Files.probeContentType(filePath);
            } catch (Exception ignored) {
            }
        }
        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }
}
