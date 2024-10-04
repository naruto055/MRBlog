package com.edu.zua.service;

import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.dto.TagListDto;
import com.edu.zua.domain.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author wenqunsheng
* @description 针对表【sg_tag(标签)】的数据库操作Service
* @createDate 2024-04-06 23:16:19
*/
public interface TagService extends IService<Tag> {

    ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult saveTag(TagListDto tagListDto);

    ResponseResult removeTag(Integer id);

    ResponseResult getTag(Integer id);

    ResponseResult editTag(Tag tag);

    ResponseResult listAllTag();

    void batchDelTag(String ids);
}
