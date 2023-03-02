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

    private Integer errCode;
    private String errMsg;
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
        resultData.setErrCode(0);
        resultData.setErrMsg(ResponseStateConstant.STATE_OK.getMessage());
        resultData.setData(data);
        return resultData;
    }

    /**
     * 请求失败
     * @param message 信息
     * @return 统一封装返回值
     * @param <E> 请求体格式
     */
    public static <E> ResultData<E> error(String message) {
        ResultData<E> resultData = new ResultData<>();
        resultData.setErrCode(1);
        resultData.setErrMsg(message);
        return resultData;
    }
}
