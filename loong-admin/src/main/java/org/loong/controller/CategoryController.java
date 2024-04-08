package org.loong.controller;

import org.loong.domain.ResponseResult;
import org.loong.domain.vo.CategoryVo;
import org.loong.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        List<CategoryVo> list = categoryService.listAllCategory();
        return ResponseResult.successResult(list);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseResult delete(@PathVariable(value = "id")Long id){
        categoryService.removeById(id);
        return ResponseResult.successResult();
    }
}
