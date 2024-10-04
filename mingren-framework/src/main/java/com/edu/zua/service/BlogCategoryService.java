package com.edu.zua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.dto.GetAllCategoryDto;
import com.edu.zua.domain.entity.BlogCategory;
import com.edu.zua.domain.vo.BlogCategoryVO;
import com.edu.zua.domain.vo.PageVO;

import java.util.List;

/**
 * 分类表(BlogCategory)表服务接口
 *
 * @author makejava
 * @since 2024-04-03 17:20:57
 */
public interface BlogCategoryService extends IService<BlogCategory> {

    ResponseResult getCategoryList();

    List<BlogCategory> listAllCategory();

    PageVO getAllCategory(GetAllCategoryDto getAllCategoryDto);

    void delCategory(String ids);
}

