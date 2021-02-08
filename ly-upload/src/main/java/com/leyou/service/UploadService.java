package com.leyou.service;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.leyou.common.constants.LyConstants;
import com.leyou.common.exception.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.config.OssProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author cbh
 * @PackageName:com.leyou.service
 * @ClassName:UploadService
 * @Description:
 * @date 2021-01-29 12:23
 */
@Service
public class UploadService {

	@Autowired
	private OssProperties ossProps;

	@Autowired
	private OSS ossClient;

	public String uploadImage(MultipartFile multipartFile) {
		try {
			BufferedImage image = ImageIO.read(multipartFile.getInputStream());
			if (null == image) {
				//该文件不是图片
				throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
		}
		//1.1获取文件原名
		String filename = multipartFile.getOriginalFilename();

		//1.2获取后缀名
		String extName = filename.substring(filename.lastIndexOf("."));

		//1.3使用uuid作为文件名
		String fileName = new StringBuilder(UUID.randomUUID().toString()).append(extName).toString();

		try {
			multipartFile.transferTo(new File(LyConstants.IMAGE_PATH));
			return LyConstants.IMAGE_PATH + fileName;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LyException(ExceptionEnum.FILE_UPLOAD_ERROR);
		}
	}

	public Map<String, String> getOssSignature() {
		try {
			long expireTime = ossProps.getExpireTime();
			long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
			Date expiration = new Date(expireEndTime);
			// PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
			PolicyConditions policyConds = new PolicyConditions();
			policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, ossProps.getMaxFileSize());
			policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, ossProps.getDir());

			String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
			byte[] binaryData = postPolicy.getBytes("utf-8");
			String encodedPolicy = BinaryUtil.toBase64String(binaryData);
			String postSignature = ossClient.calculatePostSignature(postPolicy);

			Map<String, String> respMap = new LinkedHashMap<String, String>();
			respMap.put("accessId", ossProps.getAccessKeyId());
			respMap.put("policy", encodedPolicy);
			respMap.put("signature", postSignature);
			respMap.put("dir", ossProps.getDir());
			respMap.put("host", ossProps.getHost());
			respMap.put("expire", String.valueOf(expireEndTime));
			// respMap.put("expire", formatISO8601Date(expiration));

			JSONObject jasonCallback = new JSONObject();
			jasonCallback.put("callbackUrl", "");
			jasonCallback.put("callbackBody",
					"filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
			jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
			String base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().getBytes());
			respMap.put("callback", base64CallbackBody);

			return respMap;
		}catch (Exception e){
			System.out.println(e.getMessage());
			throw new LyException(ExceptionEnum.INVALID_NOTIFY_SIGN);
		}finally {
			ossClient.shutdown();
		}
	}
}
