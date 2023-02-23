package zhihui.backend.controller.access;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zhihui.backend.pojo.ResultData;
import zhihui.backend.service.EmailService;
import zhihui.backend.util.RandomUtils;

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

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/send")
    public void send(@RequestParam("addr") String addr) {
        emailService.sendVerifyEmail(addr);
    }

}
