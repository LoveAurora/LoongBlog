package org.loong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.loong.domain.ResponseResult;
import org.loong.domain.dto.AddTagDto;
import org.loong.domain.dto.TagListDto;
import org.loong.domain.entity.Tag;
import org.loong.domain.vo.PageVo;
import org.loong.domain.vo.TagUpdateVo;
import org.loong.domain.vo.TagVo;
import org.loong.service.TagService;
import org.loong.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        return tagService.pageTagList(pageNum, pageSize, tagListDto);
    }

    @PostMapping()
    public ResponseResult addTag(@RequestBody AddTagDto tagDto) {
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        tagService.save(tag);
        return ResponseResult.successResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable Long id) {
        tagService.removeById(id);
        return ResponseResult.successResult();
    }

    @GetMapping("/{id}")
    public ResponseResult getTag(@PathVariable Long id) {
        Tag tag = tagService.getById(id);
        return ResponseResult.successResult(tag);
    }

    @PutMapping()
    public ResponseResult updateTag(@RequestBody TagUpdateVo updateVo) {
        Tag tag = BeanCopyUtils.copyBean(updateVo,Tag.class);
        tagService.updateById(tag);
        return ResponseResult.successResult();
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagVo> list = tagService.listAllTag();
        return ResponseResult.successResult(list);
    }
}
