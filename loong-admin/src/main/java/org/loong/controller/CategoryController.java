package org.loong.controller;

import org.loong.domain.ResponseResult;
import org.loong.domain.entity.Category;
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
    public ResponseResult listAllCategory() {
        List<CategoryVo> list = categoryService.listAllCategory();
        return ResponseResult.successResult(list);
    }

    @DeleteMapping("/{ids}")
    public ResponseResult deleteCategory(@PathVariable List<Long> ids) {
        categoryService.removeByIds(ids);
        return ResponseResult.successResult();
    }

    @GetMapping("/list")
    public ResponseResult list(Category category, Integer pageNum, Integer pageSize) {
        return categoryService.selectCategoryPage(category, pageNum, pageSize);
    }

    @PostMapping
    public ResponseResult addCategory(@RequestBody Category category) {
        categoryService.save(category);
        return ResponseResult.successResult();
    }

    @GetMapping("/{id}")
    public ResponseResult getCategory(@PathVariable(value = "id") Long id) {
        return ResponseResult.successResult(categoryService.getById(id));
    }

    @PutMapping()
    public ResponseResult updateCategory(@RequestBody Category category) {
        categoryService.updateById(category);
        return ResponseResult.successResult();
    }
}
