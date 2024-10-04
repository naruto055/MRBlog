package com.edu.zua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.zua.constant.Constants;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.dto.LinkDto;
import com.edu.zua.domain.entity.BlogLink;
import com.edu.zua.domain.vo.LinkVO;
import com.edu.zua.domain.vo.PageVO;
import com.edu.zua.mapper.BlogLinkMapper;
import com.edu.zua.service.BlogLinkService;
import com.edu.zua.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 友链(BlogLink)表服务实现类
 *
 * @author makejava
 * @since 2024-04-03 21:27:34
 */
@Service("blogLinkService")
public class BlogLinkServiceImpl extends ServiceImpl<BlogLinkMapper, BlogLink> implements BlogLinkService {

    /**
     * 查询所有友链
     * @return
     */
    @Override
    public ResponseResult getAllLink() {
        // 查询所有审核通过的友链
        LambdaQueryWrapper<BlogLink> queryWrapper = new LambdaQueryWrapper<>();
        // mybatis-plus中的条件查询
        queryWrapper.eq(BlogLink::getStatus, Constants.LINK_STATUS_NORMAL);
        List<BlogLink> linkList = list(queryWrapper);

        // 转化为vo
        List<LinkVO> linkVo = BeanCopyUtils.copyBeanList(linkList, LinkVO.class);

        return ResponseResult.okResult(linkVo);
    }

    @Override
    public PageVO getAllLinkByPage(LinkDto linkDto) {
        LambdaQueryWrapper<BlogLink> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(linkDto.getStatus()), BlogLink::getStatus, linkDto.getStatus());
        queryWrapper.like(StringUtils.hasText(linkDto.getName()), BlogLink::getName, linkDto.getName());
        Page<BlogLink> page = new Page<>();
        page(page, queryWrapper);
        PageVO pageVO = new PageVO(page.getRecords(), page.getTotal());
        return pageVO;
    }

    @Override
    public void delLink(String ids) {
        List<String> list = Arrays.asList(ids.split(","));
        List<Long> collect = list.stream().map(id -> Long.parseLong(id))
                .collect(Collectors.toList());
        removeBatchByIds(collect);
    }
}

