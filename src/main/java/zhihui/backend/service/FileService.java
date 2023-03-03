package zhihui.backend.service;

import lombok.Data;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件处理服务
 * @author CHENPrime-Coder
 */
@Service
@ConfigurationProperties(prefix = "file")
public class FileService {

    private String imagePath = "/zhihui/zhihui-image/";
    private String videoPath = "/zhihui/zhihui-video/";
    private String filePath = "/zhihui/zhihui-file/";

    /**
     * 文件存储至服务器
     * @param fileType 文件类型 file = "1",video = "2",picture = "3"
     * @return 上传结果
     */
    public Map<String, Object> uploadFile(MultipartFile file, Integer fileType, String opDate, HttpServletRequest request) throws IOException {
        Map<String, Object> result = new HashMap<>(1);

        // 文件名不会为空
        assert file.getOriginalFilename() != null;

        // 保存到服务器的文件名
        String filename = opDate+file.getName()+
                file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
        String nginxPath = "";
        String filepath = "";
        switch (fileType) {
            case 1:
                filepath = this.filePath+filename;
                nginxPath = "/zhihui-file/"+filename;
                break;
            case 2:
                filepath = this.videoPath+filename;
                nginxPath = "/zhihui-video/"+filename;
                break;
            case 3:
                filepath = this.imagePath+filename;
                nginxPath = "/zhihui-image/"+filename;
                break;
            default:
                throw new FileUploadException("文件类型找不到");
        }

        result.put("file-name", filename);
        result.put("full_url", "http://"+request.getServerName()+":"+request.getServerPort()+nginxPath);

        file.transferTo(new File(filepath));

        return result;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
