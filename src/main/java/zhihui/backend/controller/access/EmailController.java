package zhihui.backend.controller.access;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zhihui.backend.pojo.ResultData;
import zhihui.backend.service.EmailService;
import zhihui.backend.service.UserServiceImpl;
import zhihui.backend.util.RandomUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 邮件相关处理
 * @author CHENPrime-Coder
 */
@Slf4j
@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;
    private final UserServiceImpl userService;
    private final static String EMAIL_PARAM_NAME = "email";
    private final static String CODE_PARAM_NAME = "code";

    @Autowired
    public EmailController(EmailService emailService, UserServiceImpl userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    @GetMapping("/send")
    public void send(@RequestParam("addr") String addr) {
        emailService.sendVerifyEmail(addr);
    }

    /**
     * 邮件注册验证码发送
     */
    @GetMapping("/reg/send")
    public String sendReg(@RequestParam("addr") String addr) {
        Boolean checkResult = userService.checkEmail(addr);

        // 如果邮件重复了
        if (! checkResult) {
            return "邮件已重复";
        }
        // 邮件未重复
        emailService.sendVerifyEmail(addr);

        return null;
    }

    @PostMapping("/verify")
    public String verify(@RequestBody HashMap<String, Object> body) {
        if (body.get(EMAIL_PARAM_NAME) == null || body.get(CODE_PARAM_NAME) == null) {
            return "请求参数不完整";
        }

        Boolean verifyResult = emailService.verifyCode(body.get(EMAIL_PARAM_NAME).toString(),
                body.get(CODE_PARAM_NAME).toString());
        // 验证失败
        if (! verifyResult) {
            return "验证失败，验证码错误";
        }

        return "验证成功";
    }

}
