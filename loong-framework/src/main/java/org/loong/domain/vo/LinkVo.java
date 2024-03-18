package org.loong.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 友链(Link)表实体类
 *
 * @author loong
 * @since 2024-03-17 20:49:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sg_link")
public class LinkVo {
    @TableId
    private String name;
    private String logo;
    private String description;
    //网站地址
    private String address;
    //审核状态 (0代表审核通过，1代表审核未通过，2代表未审核)
}
