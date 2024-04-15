package org.loong.controller;

import org.loong.domain.ResponseResult;
import org.loong.domain.dto.AddArticleDto;
import org.loong.domain.entity.Article;
import org.loong.domain.vo.PageVo;
import org.loong.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult addArticle(@RequestBody AddArticleDto addArticleDto) {
        return articleService.addArticle(addArticleDto);
    }

    @GetMapping("/list")
    public ResponseResult<PageVo> getArticleList(Long pageNum, Long pageSize, String title, String summary) {
        return articleService.getArticleList(pageNum, pageSize, title, summary);
    }

    @DeleteMapping("/{ids}")
    public ResponseResult delete(@PathVariable List<Long> ids) {
        articleService.removeByIds(ids);
        return ResponseResult.successResult();
    }

    @GetMapping("/{id}")
    public ResponseResult updateArticle(@PathVariable Long id) {
        return articleService.getArticleInfo(id);
    }

    @PutMapping()
    public ResponseResult updateArticles(@RequestBody Article article) {

        return articleService.updateArticle(article);
    }
}

