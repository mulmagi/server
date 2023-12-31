package shop.mulmagi.app.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import shop.mulmagi.app.domain.Announcement;
import shop.mulmagi.app.domain.Message;

public interface S3UploadService {
	List<String> uploadsAWS(List<MultipartFile> multipartFile);
	String uploadAWS(MultipartFile file);
	String createFileName(String fileName);
	void deleteAWSFile(Announcement announcement);
	String uploadAWSChatImg(MultipartFile img);
	void deleteAWSChatImg(Message message);

}
