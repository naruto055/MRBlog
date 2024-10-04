package com.edu.zua.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.edu.zua.annotation.SystemLog;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.dto.LinkDto;
import com.edu.zua.domain.entity.BlogLink;
import com.edu.zua.domain.enums.AppHttpCodeEnum;
import com.edu.zua.domain.vo.PageVO;
import com.edu.zua.exception.SystemException;
import com.edu.zua.service.BlogLinkService;
import org.apache.poi.util.StringUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Resource
    private BlogLinkService linkService;

    /**
     * 分页查询所有友链
     * @param linkDto
     * @return
     */
    @SystemLog(businessName = "分页查询所有友链")
    @GetMapping("/list")
    public ResponseResult getAllLinkByPage(LinkDto linkDto) {
        PageVO pageVO = linkService.getAllLinkByPage(linkDto);
        return ResponseResult.okResult(pageVO);
    }

    /**
     * 新增友链
     * @param link
     * @return
     */
    @SystemLog(businessName = "新增友链")
    @PostMapping
    public ResponseResult addLink(@RequestBody BlogLink link) {
        if (!StringUtils.hasText(link.getName())) {
            throw new SystemException(AppHttpCodeEnum.TAGNAME_NOT_NULL);
        }
        linkService.save(link);
        return ResponseResult.okResult();
    }

    /**
     * 根据id查询友链详情
     * @param id
     * @return
     */
    @SystemLog(businessName = "查询友链详情")
    @GetMapping("/{id}")
    public ResponseResult getLinkById(@PathVariable Long id) {
        return ResponseResult.okResult(linkService.getById(id));
    }

    /**
     * 修改link
     * @param link
     * @return
     */
    @SystemLog(businessName = "修改友链")
    @PutMapping
    public ResponseResult editLink(@RequestBody BlogLink link) {
        LambdaQueryWrapper<BlogLink> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlogLink::getId, link.getId());
        linkService.update(link, queryWrapper);
        return ResponseResult.okResult();
    }

    /**
     * 删除友链
     * @param ids
     * @return
     */
    @SystemLog(businessName = "删除友链")
    @DeleteMapping("/{id}")
    public ResponseResult delLink(@PathVariable(name = "id") String ids) {
        linkService.delLink(ids);
        return ResponseResult.okResult();
    }
}
