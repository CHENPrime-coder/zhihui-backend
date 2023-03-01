package zhihui.backend.pojo;

import com.mysql.cj.jdbc.Blob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 文章实体类
 * @author CHENPrime-Coder
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    private Long articleId;
    private Long userId;
    private String articleTextTitle;
    private Blob articleTextBody;
    private String articleLabel;

    private Date articleCreatTime;
    private Date articleUpdateTime;
    private Date articleDeleteTime;

}
