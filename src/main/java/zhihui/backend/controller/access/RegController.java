package zhihui.backend.controller.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import zhihui.backend.pojo.ResultData;
import zhihui.backend.pojo.User;
import zhihui.backend.service.UserServiceImpl;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * 注册控制器
 * @author CHENPrime-Coder
 */
@RestController
public class RegController {

    private final UserServiceImpl userService;


    @Autowired
    public RegController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/reg")
    public ResultData<String> reg(@RequestBody User user) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        // 验证参数完整性
        if (user.getUserEmail() == null || user.getUsername() == null || user.getUserPassword() == null ||
                user.getUserMajor() == null || user.getUserGrade() == null) {
            return ResultData.success("请求参数不完整", null);
        }

        ResultData<String> insertResult = userService.insertUser(user);

        return insertResult;
    }

}
