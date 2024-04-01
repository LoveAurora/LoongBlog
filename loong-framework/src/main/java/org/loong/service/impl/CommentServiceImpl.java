package org.loong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.loong.domain.ResponseResult;
import org.loong.domain.entity.Comment;
import org.loong.domain.vo.CommentVo;
import org.loong.domain.vo.PageVo;
import org.loong.handler.exception.SystemException;
import org.loong.mapper.CommentMapper;
import org.loong.service.CommentService;
import org.loong.service.UserService;
import org.loong.constants.SystemConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.loong.utils.BeanCopyUtils;
import org.springframework.util.StringUtils;
import org.loong.enums.AppHttpCodeEnum;
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
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(CommentType),Comment::getArticleId, articleId);
        //对评论区的某条评论的rootID进行判断，如果为-1，就表示是根评论。
        queryWrapper.eq( Comment::getRootId, SystemConstants.COMMENT_ROOT);
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
            List<CommentVo> childCommentVoList =getChildren(commentVo.getId());
            commentVo.setChildren(childCommentVoList);
        }

        return ResponseResult.successResult(new PageVo(commentVoList, page.getTotal()));
    }



    public List<CommentVo> getChildren(Long rootId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper();
        //对rootId进行判断，作用是得到指定的子评论
        queryWrapper.eq(Comment::getRootId, rootId);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        // 某一条评论的全部子评论
        List<Comment> comments = list(queryWrapper);
        //调用xxToCommentList方法，转成commentVo的list
        List<CommentVo> commentVoList =xxToCommentList(comments);
        return commentVoList;
    }
    //封装响应返回。CommentVo、BeanCopyUtils、ResponseResult、PageVo是我们写的类
    // 将传进来的comment转换成commentVo
    private List<CommentVo> xxToCommentList(List<Comment> list) {
        //获取评论区的所有评论
        List<CommentVo> commentVoList = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //遍历。由于封装响应好的数据里面没有username字段，所以我们还不能返回给前端。这个遍历就是用来得到username字段
        for (CommentVo commentVo : commentVoList) {
            //通过createBy查询用户的昵称并赋值
            //通过toCommentUserId查询用户的昵称并赋值
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
            //如果toCommentUserId不为-1才进行查询,为-1表明为根评论
            if (commentVo.getToCommentUserId() != -1) {
                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toCommentUserName);
            }
        }
        return commentVoList;
    }
    @Override
    public ResponseResult addComment(Comment comment) {
        //评论内容不能为空
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.successResult();
    }

}


