package org.loong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.loong.domain.ResponseResult;
import org.loong.domain.dto.TagListDto;
import org.loong.domain.entity.Tag;
import org.loong.domain.vo.PageVo;
import org.loong.domain.vo.TagUpdateVo;
import org.loong.domain.vo.TagVo;
import org.loong.enums.AppHttpCodeEnum;
import org.loong.mapper.TagMapper;
import org.loong.service.TagService;
import org.loong.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author loong
 * @since 2024-03-17 20:49:47
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    /**
     * 分页查询标签列表
     *
     * @param pageNum    当前页码
     * @param pageSize   每页的记录数
     * @param tagListDto 查询条件
     * @return 分页查询结果
     */
    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        // 创建查询条件
        LambdaQueryWrapper<Tag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 如果标签名不为空，添加标签名的查询条件
        lambdaQueryWrapper.eq(StringUtils.hasText(tagListDto.getName()), Tag::getName, tagListDto.getName());
        // 如果备注不为空，添加备注的查询条件
        lambdaQueryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()), Tag::getRemark, tagListDto.getRemark());

        // 创建分页对象
        Page<Tag> page = new Page<>();
        // 设置当前页码
        page.setCurrent(pageNum);
        // 设置每页的记录数
        page.setSize(pageSize);
        // 执行分页查询
        page(page, lambdaQueryWrapper);

        // 创建分页结果对象
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        // 返回查询结果
        return ResponseResult.successResult(pageVo);
    }


    @Override
    public List<TagVo> listAllTag() {
        //使用了 LambdaQueryWrapper<Tag>，这里的泛型参数 Tag 就指定了查询的表。
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Tag::getId, Tag::getName);
        List<Tag> list = list(wrapper);
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(list, TagVo.class);
        return tagVos;
    }


}