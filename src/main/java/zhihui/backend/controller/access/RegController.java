package zhihui.backend.controller.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import zhihui.backend.pojo.ResultData;
import zhihui.backend.pojo.User;
import zhihui.backend.service.UserServiceImpl;

import javax.servlet.http.HttpServletResponse;

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
    public ResultData<String> reg(@RequestBody User user, HttpServletResponse response) {
        // 验证参数完整性
        if (user.getUserEmail() == null || user.getUsername() == null || user.getUserPassword() == null ||
                user.getUserMajor() == null || user.getUserGrade() == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ResultData.error("请求参数不完整");
        }

        ResultData<String> insertResult = userService.insertUser(user);

        return insertResult;
    }

}
