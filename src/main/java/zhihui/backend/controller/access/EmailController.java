package zhihui.backend.controller.access;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zhihui.backend.pojo.ResultData;
import zhihui.backend.service.EmailService;
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
    private final static String EMAIL_PARAM_NAME = "email";
    private final static String CODE_PARAM_NAME = "code";

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/send")
    public void send(@RequestParam("addr") String addr) {
        emailService.sendVerifyEmail(addr);
    }

    @PostMapping("/verify")
    public ResultData<String> verify(@RequestBody HashMap<String, Object> body, HttpServletResponse response) {
        if (body.get(EMAIL_PARAM_NAME) == null || body.get(CODE_PARAM_NAME) == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ResultData.error("请求参数不完整");
        }

        Boolean verifyResult = emailService.verifyCode(body.get(EMAIL_PARAM_NAME).toString(),
                body.get(CODE_PARAM_NAME).toString());
        // 验证失败
        if (! verifyResult) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ResultData.error("验证失败，验证码错误");
        }

        return ResultData.success("验证成功");
    }

}
