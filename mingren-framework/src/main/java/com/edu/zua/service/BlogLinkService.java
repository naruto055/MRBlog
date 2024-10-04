package com.edu.zua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.dto.LinkDto;
import com.edu.zua.domain.entity.BlogLink;
import com.edu.zua.domain.vo.PageVO;

/**
 * 友链(BlogLink)表服务接口
 *
 * @author makejava
 * @since 2024-04-03 21:27:33
 */
public interface BlogLinkService extends IService<BlogLink> {

    ResponseResult getAllLink();

    PageVO getAllLinkByPage(LinkDto linkDto);

    void delLink(String ids);
}

