package org.loong.controller;


import org.loong.constants.SystemConstants;
import org.loong.domain.ResponseResult;
import org.loong.domain.entity.Comment;
import org.loong.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 35238
 * @date 2023/7/25 0025 13:14
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    //CommentService是我们在loong-framework工程写的类
    private CommentService commentService;

    @GetMapping("/commentList")
    //ResponseResult是我们在loong-framework工程写的类
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
    }
    @PostMapping
    public ResponseResult addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }
    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList( Integer pageNum, Integer pageSize){
        return commentService.linkCommentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }
}