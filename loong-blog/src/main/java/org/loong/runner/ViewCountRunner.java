package org.loong.runner;

import org.loong.domain.entity.Article;
import org.loong.mapper.ArticleMapper;
import org.loong.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 用于在应用启动时执行一次性任务的命令行处理器，主要功能是更新文章的浏览数。
 */
@Component
public class ViewCountRunner implements CommandLineRunner {
    // 注入ArticleMapper，用于数据库操作
    @Autowired
    private ArticleMapper articleMapper;
    // 注入RedisCache，用于缓存操作
    @Autowired
    private RedisCache redisCache;

    /**
     * 当Spring Boot应用启动完成后执行此方法。
     *
     * @param args 命令行参数，此处未使用
     * @throws Exception 可能抛出的异常
     */
    @Override
    public void run(String... args) throws Exception {
        // 从数据库中查询文章列表
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Integer> viewCountMap = articles.stream().collect(Collectors.toMap(article -> article.getId().toString(), article -> article.getViewCount().intValue()));
        // 存储到redis中
        redisCache.setCacheMap("article:viewCount", viewCountMap);

    }
}
