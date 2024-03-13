package org.loong.controller;


import org.loong.domain.entity.Article;
import org.loong.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    //Todo: Autowired注解是Spring的注解，用于自动装配，可以对类成员变量、方法及构造函数进行标注，完成自动装配工作
    //注入公共模块的ArticleService接口
    private ArticleService articleService;

    @GetMapping("/list")
    //Article是公共模块的实体类
    public List<Article> test() {
        //查询数据库的所有数据
        return articleService.list();
    }

}