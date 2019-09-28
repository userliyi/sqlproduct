import lombok.Data;

import java.util.List;

@Data
public class ImportBody {
    /**
     * 权限点id
     */
    private Integer id;

    /**
     * 权限点中文名称
     */
    private String  zhName;

    /**
     * 权限点英文名称
     */
    private String  enName;

    /**
     *父级权限id
     */
    private Integer parentId;

    /**
     * 父级层级
     */
    private String  parentLevel;

    /**
     * 子层级
     */
    private String  level;

    /**
     * 大角色id
     */
    private Integer bigRole;

    /**
     * 创建者
     */
    private String  creator;

    /**
     * 创建时间
     */
    private String creatTime;

    /**
     * 子级结点
     */
    private List<ImportBody> child;
}
