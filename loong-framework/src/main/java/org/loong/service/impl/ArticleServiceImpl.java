package org.loong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.loong.constants.SystemConstants;
import org.loong.domain.ResponseResult;
import org.loong.domain.dto.AddArticleDto;
import org.loong.domain.entity.Article;
import org.loong.domain.entity.ArticleTag;
import org.loong.domain.entity.Category;
import org.loong.domain.vo.ArticleDetailVo;
import org.loong.domain.vo.ArticleListVo;
import org.loong.domain.vo.HotArticleVo;
import org.loong.domain.vo.PageVo;
import org.loong.mapper.ArticleMapper;
import org.loong.service.ArticleService;
import org.loong.service.ArticleTagService;
import org.loong.service.CategoryService;
import org.loong.utils.BeanCopyUtils;
import org.loong.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表服务实现类
 *
 * @author loong
 * @since 2024-03-17 13:20:48
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleTagService articleTagService;

    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章 封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //只查询前十条
        Page<Article> page = new Page<>(1, 10);
        page(page, queryWrapper);
        List<Article> articles = page.getRecords();
        List<HotArticleVo> articleVos = new ArrayList<>();
        for (Article article : articles) {
            HotArticleVo vo = new HotArticleVo();
            BeanUtils.copyProperties(article, vo);
            articleVos.add(vo);
        }
        System.out.println("执行到这里");
        return ResponseResult.successResult(articleVos);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        // 查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 如果 有categoryId 就要查询时要和传入的相同
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId);

        // 状态是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        // 对isTop进行降序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);
        //分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, lambdaQueryWrapper);
        //查询categoryName
        List<Article> articles = page.getRecords();
        // 数据流写法
        articles.stream().map(article -> {
            Category category = categoryService.getById(article.getCategoryId());
            article.setCategoryName(category.getName());
            return article;
        }).collect(Collectors.toList());

        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos, page.getTotal());
        return ResponseResult.successResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        Article article = getById(id);
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if (category != null) {
            articleDetailVo.setCategoryName(category.getName());
        }
        return ResponseResult.successResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.incrementCacheMapValue("article:viewCount", id.toString(), 1);
        return ResponseResult.successResult();
    }

    @Override
    public ResponseResult addArticle(AddArticleDto articleDto) {
        // 将传入的 DTO 对象转换为 Article 实体对象
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        // 保存 Article 对象到数据库
        save(article);
        // 将 DTO 对象中的标签 ID 列表转换为 ArticleTag 对象列表
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        // 将 ArticleTag 对象列表保存到数据库，建立文章和标签的关联
        articleTagService.saveBatch(articleTags);
        // 返回成功的响应结果
        return ResponseResult.successResult();
    }

    @Override
    public ResponseResult<PageVo> getArticleList(Long pageNum, Long pageSize, String title, String summary) {
        // 创建查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 如果标题不为空，添加标题的模糊查询条件
        if (StringUtils.hasText(title)) {
            lambdaQueryWrapper.like(Article::getTitle, title);
        }
        // 如果摘要不为空，添加摘要的模糊查询条件
        if (StringUtils.hasText(summary)) {
            lambdaQueryWrapper.like(Article::getSummary, summary);
        }
        // 创建分页对象
        Page<Article> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        // 执行分页查询
        page(page, lambdaQueryWrapper);
        // 将查询结果封装为 PageVo 对象
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());

        // 返回成功的响应结果，包含分页查询的结果
        return ResponseResult.successResult(pageVo);
    }

    @Override
    public ResponseResult getArticleInfo(Long id) {
        // 创建查询条件，根据文章 ID 查询
        LambdaQueryWrapper<Article> articleQueryWrapper = new LambdaQueryWrapper<>();
        articleQueryWrapper.eq(Article::getId, id);
        // 执行查询，获取文章对象
        Article article = getOne(articleQueryWrapper);

        // 创建查询条件，根据文章 ID 查询关联的标签
        LambdaQueryWrapper<ArticleTag> tagQueryWrapper = new LambdaQueryWrapper<>();
        tagQueryWrapper.eq(ArticleTag::getArticleId, article.getId());
        // 执行查询，获取关联的标签列表
        List<ArticleTag> tags = articleTagService.list(tagQueryWrapper);
        // 将标签列表转换为标签 ID 列表，设置到文章对象中
        List<Long> collect = tags.stream().map(tag -> tag.getTagId()).collect(Collectors.toList());
        article.setTags(collect);

        // 返回成功的响应结果，包含文章的详细信息
        return ResponseResult.successResult(article);
    }

    @Override
    public ResponseResult updateArticle(Article article) {

        // 保存文章对象到数据库
        updateById(article);
        // 获取文章关联的标签 ID 列表
        List<Long> tags = article.getTags();
        // 创建查询条件，根据文章 ID 查询关联的标签
        LambdaQueryWrapper<ArticleTag> tagQueryWrapper = new LambdaQueryWrapper<>();
        tagQueryWrapper.eq(ArticleTag::getArticleId, article.getId());
        // 删除数据库中的旧标签关联
        articleTagService.remove(tagQueryWrapper);
        // 创建新的标签关联列表
        List<ArticleTag> tagList = new ArrayList<>();
        for (Long tagId : tags) {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticleId(article.getId());
            articleTag.setTagId(tagId);
            tagList.add(articleTag);
        }
        // 将新的标签关联列表保存到数据库
        articleTagService.saveBatch(tagList);

        // 返回空的响应结果
        return ResponseResult.successResult();
    }
}

