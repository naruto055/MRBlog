package com.edu.zua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.zua.constant.Constants;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.dto.TagListDto;
import com.edu.zua.domain.entity.Tag;
import com.edu.zua.domain.enums.AppHttpCodeEnum;
import com.edu.zua.domain.vo.PageVO;
import com.edu.zua.domain.vo.TagListVO;
import com.edu.zua.domain.vo.TagVO;
import com.edu.zua.service.TagService;
import com.edu.zua.mapper.TagMapper;
import com.edu.zua.utils.BeanCopyUtils;
import com.edu.zua.utils.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author wenqunsheng
* @description 针对表【sg_tag(标签)】的数据库操作Service实现
* @createDate 2024-04-06 23:16:19
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

    @Override
    public ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()), Tag::getName, tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()), Tag::getRemark, tagListDto.getRemark());
        // 分页查询
        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);
        List<TagListVO> tagListVOList = BeanCopyUtils.copyBeanList(page.getRecords(), TagListVO.class);
        // 封装数据返回
        return ResponseResult.okResult(new PageVO(tagListVOList, page.getTotal()));
    }

    @Override
    public ResponseResult saveTag(TagListDto tagListDto) {
        Long userId = SecurityUtil.getUserId();
        Tag tag = BeanCopyUtils.copyBean(tagListDto, Tag.class);
        tag.setCreateBy(userId);
        tag.setCreateTime(new Date());
        tag.setUpdateBy(userId);
        tag.setUpdateTime(new Date());
        boolean save = save(tag);
        return save ? ResponseResult.okResult() : ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    @Override
    public ResponseResult removeTag(Integer id) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getId, id);
        boolean remove = remove(queryWrapper);
        return remove ? ResponseResult.okResult() : ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    @Override
    public ResponseResult getTag(Integer id) {
        Tag tag = getById(id);
        return ResponseResult.okResult(tag);
    }

    @Override
    public ResponseResult editTag(Tag tag) {
        boolean edit  = updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllTag() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getName, Tag::getId);
        List<Tag> list =  list(queryWrapper);
        List<TagVO> result = BeanCopyUtils.copyBeanList(list, TagVO.class);
        return ResponseResult.okResult(result);
    }

    @Override
    public void batchDelTag(String ids) {
        List<String> list = Arrays.asList(ids.split(","));
        List<Long> idsList = list.stream().map(Long::parseLong)
                .collect(Collectors.toList());
        removeBatchByIds(idsList);
    }
}




