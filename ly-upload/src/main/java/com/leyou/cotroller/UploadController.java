package com.leyou.cotroller;

import com.leyou.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author cbh
 * @PackageName:com.leyou.cotroller
 * @ClassName:UploadController
 * @Description:
 * @date 2021-01-29 12:23
 */
@RestController
public class UploadController {

	@Autowired
	private UploadService uploadService;

	@PostMapping("/image")
	public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file){
		String imageUrl = uploadService.uploadImage(file);
		return ResponseEntity.ok(imageUrl);
	}

	@GetMapping("/signature")
	public ResponseEntity<Map<String, String>> getOssSignature(){
		Map<String, String> resultMap = uploadService.getOssSignature();
		return ResponseEntity.ok(resultMap);
	}
}
