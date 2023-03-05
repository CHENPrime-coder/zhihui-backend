package zhihui.backend.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zhihui.backend.pojo.ResultData;
import zhihui.backend.service.FileService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * 文件控制器
 * @author CHENPrime-Coder
 */
@RequestMapping("/file")
@RestController
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResultData<Object> uploadImage(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("fileType") Integer fileType,
                                                      @RequestParam("operationID") String operationID,
                                                      HttpServletRequest request) throws IOException {
        if (file == null || file.getOriginalFilename() == null) {
            return ResultData.success("上传的文件或文件名为空");
        }

        Map<String, Object> result = null;
        result = fileService.uploadFile(file, fileType, operationID, request);

        return ResultData.success(result);
    }

}
