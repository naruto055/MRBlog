package com.edu.zua.controller;

import com.edu.zua.annotation.SystemLog;
import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.dto.TagListDto;
import com.edu.zua.domain.entity.Tag;
import com.edu.zua.service.TagService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Resource
    private TagService tagService;

    /**
     * 分页查询所有博客标签
     * @return
     */
    @SystemLog(businessName = "查询所有标签")
    @GetMapping("/list")
    public ResponseResult list(Integer pageSize, Integer pageNum, TagListDto tagListDto) {
        return tagService.pageTagList(pageNum, pageSize, tagListDto);
    }

    /**
     * 新增标签
     * @param tagListDto
     * @return
     */
    @SystemLog(businessName = "添加标签")
    @PostMapping
    public ResponseResult addTag(@RequestBody TagListDto tagListDto) {
        return tagService.saveTag(tagListDto);
    }

    /**
     * 删除标签
     * @param id
     * @return
     */
    // @DeleteMapping("/{id}")
    // public ResponseResult delTag(@PathVariable("id") Integer id) {
    //     return tagService.removeTag(id);
    // }
    @SystemLog(businessName = "删除标签")
    @DeleteMapping("/{id}")
    public ResponseResult batchDelTag(@PathVariable("id") String ids) {
        tagService.batchDelTag(ids);
        return ResponseResult.okResult();
    }

    /**
     * 根据id获取标签
     * @param id
     * @return
     */
    @SystemLog(businessName = "查询某个标签详情")
    @GetMapping("/{id}")
    public ResponseResult getTag(@PathVariable("id") Integer id) {
        return tagService.getTag(id);
    }

    /**
     * 修改标签
     * @param tag
     * @return
     */
    @SystemLog(businessName = "修改标签")
    @PutMapping
    public ResponseResult editTag(@RequestBody Tag tag) {
        return tagService.editTag(tag);
    }

    /**
     * 查询所有的标签名
     * @return
     */
    @SystemLog(businessName = "查询所有标签")
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag() {
        return tagService.listAllTag();
    }
}
