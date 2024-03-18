package org.loong.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.loong.domain.entity.Tag;
import org.loong.mapper.TagMapper;
import org.loong.service.TagService;
import org.springframework.stereotype.Service;
/**
 * 标签(Tag)表服务实现类
 *
 * @author loong
 * @since 2024-03-17 20:49:47
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
}
