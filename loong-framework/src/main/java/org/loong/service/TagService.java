package org.loong.service;
import com.baomidou.mybatisplus.extension.service.IService;
import org.loong.domain.ResponseResult;
import org.loong.domain.dto.TagListDto;
import org.loong.domain.entity.Tag;
import org.loong.domain.vo.PageVo;
import org.loong.domain.vo.TagUpdateVo;
import org.loong.domain.vo.TagVo;

import java.util.List;

/**
 * 标签(Tag)表服务接口
 *
 * @author loong
 * @since 2024-03-17 20:49:47
 */
public interface TagService extends IService<Tag> {
    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    List<TagVo> listAllTag();
}
