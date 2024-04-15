package org.loong.service;
import com.baomidou.mybatisplus.extension.service.IService;
import org.loong.domain.ResponseResult;
import org.loong.domain.entity.Link;
import org.loong.domain.vo.PageVo;

/**
 * 友链(Link)表服务接口
 *
 * @author loong
 * @since 2024-03-17 20:33:45
 */
public interface LinkService extends IService<Link> {
   ResponseResult getAllLink();

    PageVo selectLinkPage(Link link, Integer pageNum, Integer pageSize);
}
