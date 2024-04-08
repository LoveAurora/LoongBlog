package org.loong.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.loong.domain.ResponseResult;
import org.loong.domain.entity.Category;
import org.loong.domain.vo.CategoryVo;

import java.util.List;

/**
 * 分类表(Category)表服务接口
 *
 * @author loong
 * @since 2024-03-17 13:16:08
 */

public interface CategoryService extends IService<Category> {
    ResponseResult getCategoryList();
    List<CategoryVo> listAllCategory();

}

