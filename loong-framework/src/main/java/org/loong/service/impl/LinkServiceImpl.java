package org.loong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.loong.constants.SystemConstants;
import org.loong.domain.ResponseResult;
import org.loong.domain.entity.Link;
import org.loong.domain.vo.LinkVo;
import org.loong.domain.vo.PageVo;
import org.loong.mapper.LinkMapper;
import org.loong.service.LinkService;
import org.loong.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 友链(Link)表服务实现类
 *
 * @author loong
 * @since 2024-03-17 20:33:45
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {
    @Override
    public ResponseResult getAllLink() {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links = list(queryWrapper);
        //转换vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        return ResponseResult.successResult(linkVos);
    }

    @Override
    public PageVo selectLinkPage(Link link, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(StringUtils.hasText(link.getName()), Link::getName, link.getName());
        queryWrapper.eq(Objects.nonNull(link.getStatus()), Link::getStatus, link.getStatus());

        Page<Link> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);

        //转换成VO
        List<Link> categories = page.getRecords();

        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(categories);
        return pageVo;
    }
}
