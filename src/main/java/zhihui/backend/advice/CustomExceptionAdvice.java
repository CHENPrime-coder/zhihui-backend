package zhihui.backend.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import zhihui.backend.constant.ResponseStateConstant;
import zhihui.backend.pojo.ResultData;

/**
 * 全局异常处理
 * @author CHENPrime-Coder
 */
@Slf4j
@RestControllerAdvice
public class CustomExceptionAdvice {

    /**
     * 全局异常处理
     * @param e 异常
     * @return 响应
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultData<String> exception(Exception e) {
        log.error("发生异常 message="+e.getMessage()+" ; exception="+e);

        return ResultData.error(e.getMessage());
    }

}
