package org.loong.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论表(SgComment)表实体类
 *
 * @author makejava
 * @since 2024-03-08 15:28:23
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sg_comment")
public class Comment {
    
    private Long id;
    //评论类型（0代表文章评论，1代表友链评论）
    private String type;
    //文章id
    private Long articleId;
    //根评论id，如果是根评论则为-1,如果有子评论，其他子评论的root_id就是根评论的id
    private Long rootId;
    //评论内容
    private String content;
    //所回复的目标评论的用户的userid，如果是根评论则为-1
    private Long toCommentUserId;
    //回复目标评论的id
    private Long toCommentId;

    //当前评论的创建人id
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;

}
