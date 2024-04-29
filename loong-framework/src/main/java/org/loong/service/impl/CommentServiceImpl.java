package org.loong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.loong.constants.SystemConstants;
import org.loong.domain.ResponseResult;
import org.loong.domain.entity.Comment;
import org.loong.domain.entity.User;
import org.loong.domain.vo.CommentVo;
import org.loong.domain.vo.PageVo;
import org.loong.enums.AppHttpCodeEnum;
import org.loong.handler.exception.SystemException;
import org.loong.mapper.CommentMapper;
import org.loong.service.CommentService;
import org.loong.service.UserService;
import org.loong.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author Aurora
 * @since 2024-03-18 14:03:45
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    // 注入用户服务
    @Autowired
    private UserService userService;

    @Override
    public ResponseResult commentList(String CommentType, Long articleId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper();
        //对articleId进行判断，作用是得到指定的文章
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(CommentType), Comment::getArticleId, articleId);
        //对评论区的某条评论的rootID进行判断，如果为-1，就表示是根评论。
        queryWrapper.eq(Comment::getRootId, SystemConstants.COMMENT_ROOT);
        // 评论类型
        queryWrapper.eq(Comment::getType, CommentType);

        //分页查询。查的是整个评论区的每一条评论
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page = page(page, queryWrapper);
        //封装成CommentVo
        List<CommentVo> commentVoList = xxToCommentList(page.getRecords());

        // 查询评论区的所有子评论
        for (CommentVo commentVo : commentVoList) {
            //查询子评论
            List<CommentVo> childCommentVoList = getChildren(commentVo);
            commentVo.setChildren(childCommentVoList);
        }

        return ResponseResult.successResult(new PageVo(commentVoList, page.getTotal()));
    }


    public List<CommentVo> getChildren(CommentVo commentVo) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper();
        //对rootId进行判断，作用是得到指定的子评论
        queryWrapper.eq(Comment::getRootId, commentVo.getId());
        queryWrapper.orderByAsc(Comment::getCreateTime);
        // 某一条评论的全部子评论
        List<Comment> comments = list(queryWrapper);
        //调用xxToCommentList方法，转成commentVo的list
        List<CommentVo> commentVoList = xxToCommentList(comments);
        return commentVoList;
    }

    //封装响应返回。CommentVo、BeanCopyUtils、ResponseResult、PageVo是我们写的类
    // 将传进来的comment转换成commentVo
   // 定义一个方法将Comment列表转换为CommentVo列表
private List<CommentVo> xxToCommentList(List<Comment> list) {
    // 使用BeanCopyUtils工具类将Comment列表复制为CommentVo列表
    List<CommentVo> commentVoList = BeanCopyUtils.copyBeanList(list, CommentVo.class);

    // 遍历CommentVo列表
    for (CommentVo commentVo : commentVoList) {
        // 通过createBy字段获取用户信息
        User user = userService.getById(commentVo.getCreateBy());

        // 如果用户存在
        if (user != null) {
            // 获取用户昵称
            String nickName = user.getNickName();
            // 将用户昵称设置为CommentVo的username
            commentVo.setUsername(nickName);
        } else {
            // 如果用户不存在，将username设置为"Unknown User"
            commentVo.setUsername("用户已注销");
        }

        // 如果CommentVo的toCommentUserId字段不为-1（即该评论是子评论）
        if (commentVo.getToCommentUserId() != -1) {
            // 通过toCommentUserId字段获取被评论用户信息
            // 前端就显示 ** 回复 **
            User toCommentUser = userService.getById(commentVo.getToCommentUserId());

            // 如果被评论用户存在
            if (toCommentUser != null) {
                // 获取被评论用户昵称
                String toCommentUserName = toCommentUser.getNickName();
                // 将被评论用户昵称设置为CommentVo的toCommentUserName
                commentVo.setToCommentUserName(toCommentUserName);
            } else {
                // 如果被评论用户不存在，将toCommentUserName设置为"Unknown User"
                commentVo.setToCommentUserName("用户已注销");
            }
        }
    }
    // 返回转换后的CommentVo列表
    return commentVoList;
}

    @Override
    public ResponseResult addComment(Comment comment) {
        //评论内容不能为空
        if (!StringUtils.hasText(comment.getContent())) {
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.successResult();
    }

}


