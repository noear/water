package waterapp.models.water;

import lombok.Getter;

/**
 * @Author:Fei.chu
 * @Date:Created in 17:46 2018/04/20
 * @Description:代码片段查询结果实体类
 */
@Getter
public class CodeQueryModel {

    public int pid;
    public int id;

    public int code_type;
    public String tag;
    public String name;
    public String code;
    public String note;
}
