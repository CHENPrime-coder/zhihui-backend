package zhihui.backend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 专业问答实体类
 * @author CHENPrime-Coder
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Questions {

    private Long questionId;
    private Long userId;
    private String questionTextBody;
    private String questionLabel;

    private Date questionCreateTime;
    private Date questionUpdateTime;
    private Date questionDeleteTime;

}
