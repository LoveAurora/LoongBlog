package org.loong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.loong.domain.entity.Article;
import org.loong.mapper.ArticleMapper;
import org.loong.service.ArticleService;
import org.springframework.stereotype.Service;


@Service
//ServiceImpl是mybatisPlus官方提供的
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
}