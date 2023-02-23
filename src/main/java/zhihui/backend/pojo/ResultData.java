package zhihui.backend.pojo;

import lombok.Data;
import zhihui.backend.constant.ResponseStateConstant;

/**
 * 返回内容封装
 * @param <E> data 格式
 * @author CHENPrime-Coder
 */
@Data
public class ResultData<E> {

    private Integer code;
    private String message;
    private E data;
    private Long timestamp;

    public ResultData() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 请求成功
     * @param data 请求体
     * @return 统一封装返回值
     * @param <E> 请求体格式
     */
    public static <E> ResultData<E> success(E data) {
        ResultData<E> resultData = new ResultData<>();
        resultData.setCode(ResponseStateConstant.STATE_OK.getCode());
        resultData.setMessage(ResponseStateConstant.STATE_OK.getMessage());
        resultData.setData(data);
        return resultData;
    }

    /**
     * 请求失败
     * @param code 状态码
     * @param message 信息
     * @return 统一封装返回值
     * @param <E> 请求体格式
     */
    public static <E> ResultData<E> error(Integer code, String message) {
        ResultData<E> resultData = new ResultData<>();
        resultData.setCode(code);
        resultData.setMessage(message);
        return resultData;
    }
}
