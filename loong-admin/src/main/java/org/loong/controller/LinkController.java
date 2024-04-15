package org.loong.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import org.loong.domain.ResponseResult;
import org.loong.domain.entity.Category;
import org.loong.domain.entity.Link;
import org.loong.domain.vo.ExcelCategoryVo;
import org.loong.domain.vo.PageVo;
import org.loong.enums.AppHttpCodeEnum;
import org.loong.service.CategoryService;
import org.loong.service.LinkService;
import org.loong.utils.BeanCopyUtils;
import org.loong.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取友链列表
     */
    @GetMapping("/list")
    public ResponseResult list(Link link, Integer pageNum, Integer pageSize) {
        PageVo pageVo = linkService.selectLinkPage(link, pageNum, pageSize);
        return ResponseResult.successResult(pageVo);
    }

    @PostMapping
    public ResponseResult add(@RequestBody Link link) {
        linkService.save(link);
        return ResponseResult.successResult();
    }

    @DeleteMapping("/{ids}")
    public ResponseResult delete(@PathVariable List<Long> ids) {
        linkService.removeByIds(ids);
        return ResponseResult.successResult();
    }

    @PutMapping
    public ResponseResult edit(@RequestBody Link link) {
        linkService.updateById(link);
        return ResponseResult.successResult();
    }

    @PutMapping("/changeLinkStatus")
    public ResponseResult changeLinkStatus(@RequestBody Link link) {
        linkService.updateById(link);
        return ResponseResult.successResult();
    }


    @GetMapping(value = "/{id}")
    public ResponseResult getInfo(@PathVariable Long id) {
        Link link = linkService.getById(id);
        return ResponseResult.successResult(link);
    }

    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx", response);
            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
}

