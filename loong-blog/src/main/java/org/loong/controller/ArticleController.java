package org.loong.controller;


import org.loong.domain.ResponseResult;
import org.loong.domain.entity.Article;
import org.loong.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController

@RequestMapping("/article")
public class ArticleController {
    //Todo: Autowired注解是Spring的注解，用于自动装配，可以对类成员变量、方法及构造函数进行标注，完成自动装配工作
    //注入公共模块的ArticleService接口
    @Autowired
    private ArticleService articleService;

    @GetMapping("/hotArticleList")
    //Article是公共模块的实体类
    public ResponseResult hotArticleList() {
        // response.setHeader("Content-Type", "text/html;charset=UTF-8");
        //查询数据库的所有数据
        return articleService.hotArticleList();
    }

    @GetMapping("articleList")
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {

        return articleService.articleList(pageNum, pageSize, categoryId);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id) {
        return articleService.getArticleDetail(id);
    }


    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id) {
        return articleService.updateViewCount(id);
    }



}