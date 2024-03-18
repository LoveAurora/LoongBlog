package org.loong.controller;

import org.loong.domain.ResponseResult;
import org.loong.service.ArticleService;
import org.loong.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/getCategoryList")
    public ResponseResult CategoryList(HttpServletResponse response) {
        response.setHeader("Content-Type", "text/html;charset=UTF-8");
        return categoryService.getCategoryList();
    }

}
