package org.loong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.loong.domain.entity.ArticleTag;
import org.loong.mapper.ArticleTagMapper;
import org.loong.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author Aurora
 * @since 2024-03-18 14:03:23
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
