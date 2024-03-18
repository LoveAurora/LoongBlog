package org.loong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.loong.domain.entity.Comment;
import org.loong.mapper.CommentMapper;
import org.loong.service.CommentService;
import org.springframework.stereotype.Service;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author Aurora
 * @since 2024-03-18 14:03:45
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}
