package com.edu.zua.controller;

import com.edu.zua.annotation.SystemLog;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.service.BlogLinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/link")
@Api(tags = "友情链接", description = "友链相关接口")
public class LinkController {

    @Resource
    private BlogLinkService blogLinkService;

    /**
     * 查询所有友链
     * @return
     */
    @SystemLog(businessName = "查询所有友链")
    @ApiOperation(value = "查询友链", notes = "查询所有友情链接")
    @GetMapping("/getAllLink")
    public ResponseResult getAllLink() {
        return blogLinkService.getAllLink();
    }
}
