package org.loong.service;

import org.loong.domain.ResponseResult;
import org.loong.domain.dto.AddArticleDto;
import org.loong.domain.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import org.loong.domain.vo.PageVo;

public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();


    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult addArticle(AddArticleDto article);

    ResponseResult<PageVo> getArticleList(Long pageNum, Long pageSize, String title, String summary);

    ResponseResult getArticleInfo(Long id);

    ResponseResult updateArticle(Article article);
}