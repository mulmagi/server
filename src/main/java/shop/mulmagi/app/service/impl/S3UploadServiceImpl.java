package shop.mulmagi.app.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import shop.mulmagi.app.domain.Announcement;
import shop.mulmagi.app.domain.Message;
import shop.mulmagi.app.service.S3UploadService;

@Service
@RequiredArgsConstructor
public class S3UploadServiceImpl implements S3UploadService {
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;
	private final AmazonS3 amazonS3;

	@Override
	public List<String> uploadsAWS(List<MultipartFile> multipartFile){
		List<String> fileUrlList = new ArrayList<>();

		String fileName;
		for (MultipartFile file : multipartFile) {
			fileName = createFileName(file.getOriginalFilename());
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentLength(file.getSize());
			objectMetadata.setContentType(file.getContentType());

			try (InputStream inputStream = file.getInputStream()) {
				amazonS3.putObject(new PutObjectRequest(bucket + "/notice/file", fileName, inputStream, objectMetadata)
					.withCannedAcl(CannedAccessControlList.PublicRead));
				fileUrlList.add(amazonS3.getUrl(bucket + "/notice/file", fileName).toString());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return fileUrlList;
	}

	@Override
	public String uploadAWS(MultipartFile file){
		String fileUrl = "";

		String fileName = createFileName(file.getOriginalFilename());
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(file.getSize());
		objectMetadata.setContentType(file.getContentType());

		try (InputStream inputStream = file.getInputStream()) {
			amazonS3.putObject(new PutObjectRequest(bucket + "/notice/file", fileName, inputStream, objectMetadata)
				.withCannedAcl(CannedAccessControlList.PublicRead));
			fileUrl = amazonS3.getUrl(bucket + "/notice/file", fileName).toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return fileUrl;
	}

	@Override
	public String createFileName(String fileName){
		return UUID.randomUUID().toString().concat(fileName);
	}

	@Override
	public void deleteAWSFile(Announcement announcement) {
		try {
			String imgUrl = announcement.getImgUrl();
			String fileUrl = announcement.getFileUrl();
			if(imgUrl != null){
				String decodedImgUri = URLDecoder.decode(imgUrl, "UTF-8");
				String imgName = decodedImgUri.substring(decodedImgUri.lastIndexOf('/') + 1);
				amazonS3.deleteObject(bucket + "/notice/file", imgName);
			}
			if(fileUrl != null){
				String decodedFileUri = URLDecoder.decode(fileUrl, "UTF-8");
				String fileName = decodedFileUri.substring(decodedFileUri.lastIndexOf('/') + 1);
				amazonS3.deleteObject(bucket + "/notice/file", fileName);
			}
		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public String uploadAWSChatImg(MultipartFile img){
		String imgUrl = "";

		String imgName = createFileName(img.getOriginalFilename());
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(img.getSize());
		objectMetadata.setContentType(img.getContentType());

		try (InputStream inputStream = img.getInputStream()) {
			amazonS3.putObject(new PutObjectRequest(bucket + "/chat/img", imgName, inputStream, objectMetadata)
				.withCannedAcl(CannedAccessControlList.PublicRead));
			imgUrl = amazonS3.getUrl(bucket + "/chat/img", imgName).toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return imgUrl;
	}

	@Override
	public void deleteAWSChatImg(Message message) {
		try {
			String imgUrl = message.getContent();
			if(imgUrl != null){
				String decodedImgUri = URLDecoder.decode(imgUrl, "UTF-8");
				String imgName = decodedImgUri.substring(decodedImgUri.lastIndexOf('/') + 1);
				amazonS3.deleteObject(bucket + "/chat/img", imgName);
			}
		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}
