package org.loong.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.loong.domain.entity.Comment;

import java.util.Date;
import java.util.List;

/**
 * @author loong
 * @date 2023/7/25 0025 13:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVo {

    private Long id;
    //文章id
    private Long articleId;
    //根评论id -1代表根评论 否则代表回复的评论id
    private Long rootId;
    //评论内容
    private String content;
    //发根评论的userid -1代表根评论 否则代表回复的人的id
    private Long toCommentUserId;
    //发根评论的userName
    private String toCommentUserName;
    //回复目标评论id
    private Long toCommentId;
    //当前评论的创建人id
    private Long createBy;

    private Date createTime;

    //评论是谁发的
    private String username;
    // 子评论
    private List<CommentVo> children;

}