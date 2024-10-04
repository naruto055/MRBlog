package com.edu.zua.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.edu.zua.annotation.SystemLog;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.dto.AddCategoryDto;
import com.edu.zua.domain.dto.GetAllCategoryDto;
import com.edu.zua.domain.entity.BlogCategory;
import com.edu.zua.domain.enums.AppHttpCodeEnum;
import com.edu.zua.domain.vo.BlogCategoryVO;
import com.edu.zua.domain.vo.ExcelCategoryVO;
import com.edu.zua.domain.vo.PageVO;
import com.edu.zua.exception.SystemException;
import com.edu.zua.handler.exception.GlobalExceptionHandler;
import com.edu.zua.service.BlogCategoryService;
import com.edu.zua.utils.BeanCopyUtils;
import com.edu.zua.utils.WebUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Resource
    private BlogCategoryService categoryService;

    /**
     * 写博文的时候，获取所有分类
     * @return
     */
    @SystemLog(businessName = "获取所有分类")
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory() {
        List<BlogCategory> categoryList = categoryService.listAllCategory();
        return ResponseResult.okResult(categoryList);
    }

    /**
     * 导出所有分类
     * @param response
     * @throws IOException
     */
    @PreAuthorize("@ps.hasPermission('content:catergory:export')")
    @GetMapping("/export")
    public void exportCategory(HttpServletResponse response) throws IOException {
        try {
            // 设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx", response);
            // 获取需要导出的数据
            List<BlogCategory> result = categoryService.listAllCategory();
            List<ExcelCategoryVO> excelCategoryVOS = BeanCopyUtils.copyBeanList(result, ExcelCategoryVO.class);

            // 将数据写入到临时输出流
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // 把数据写入到excel中
            EasyExcel.write(outputStream, ExcelCategoryVO.class)
                    .autoCloseStream(Boolean.FALSE)
                    .sheet("分类导出")
                    .doWrite(excelCategoryVOS);
            // 将临时输出流的内容写入到 response.getOutputStream()
            ServletOutputStream servletOutputStream = response.getOutputStream();
            outputStream.writeTo(servletOutputStream);
            outputStream.close();
            servletOutputStream.flush();
        } catch (Exception e) {
            // 如果出现异常需要返回json
            // 重置response
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    /**
     * 分页查询分类的列表信息
     * @param getAllCategoryDto
     * @return
     */
    @SystemLog(businessName = "分页查询所有分类")
    @GetMapping("list")
    public ResponseResult getAllCategory(GetAllCategoryDto getAllCategoryDto) {
        PageVO pageVO = categoryService.getAllCategory(getAllCategoryDto);
        return ResponseResult.okResult(pageVO);
    }

    /**
     * 新增分类
     * @param blogCategory
     * @return
     */
    @SystemLog(businessName = "新增分类")
    @PostMapping
    public ResponseResult addCategory(@RequestBody BlogCategory blogCategory) {
        if (!StringUtils.hasText(blogCategory.getName())) {
            throw new SystemException(AppHttpCodeEnum.CATEGORYNAME_NOT_NULL);
        }
        categoryService.save(blogCategory);
        return ResponseResult.okResult();
    }

    /**
     * 根据id查询分类详情
     * @param id
     * @return
     */
    @SystemLog(businessName = "查询分类详情")
    @GetMapping("/{id}")
    public ResponseResult getCategoryById(@PathVariable Long id) {
        BlogCategory category = categoryService.getById(id);
        return ResponseResult.okResult(category);
    }

    /**
     * 修改分类
     * @param category
     * @return
     */
    @SystemLog(businessName = "修改分类")
    @PutMapping
    public ResponseResult editCategory(@RequestBody AddCategoryDto category) {
        categoryService.updateById(BeanCopyUtils.copyBean(category, BlogCategory.class));
        return ResponseResult.okResult();
    }

    /**
     * 删除分类
     * @param ids
     * @return
     */
    @SystemLog(businessName = "删除分类")
    @DeleteMapping("/{id}")
    public ResponseResult delCategory(@PathVariable(value = "id") String ids) {
        categoryService.delCategory(ids);
        return ResponseResult.okResult();
    }


}
