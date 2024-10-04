package com.edu.zua.service.impl;

import com.edu.zua.domain.ResponseResult;
import com.edu.zua.domain.enums.AppHttpCodeEnum;
import com.edu.zua.exception.SystemException;
import com.edu.zua.service.UploadService;
import com.edu.zua.utils.PathUtil;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
@Data
@ConfigurationProperties(prefix = "oss")
public class UploadServiceImpl implements UploadService {

    private String accessKey;

    private String secretKey;
    private String bucket;

    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        // 判断文件类型或文件大小
        // 获取原始文件名
        // 对原始文件名进行判断
        String filename = img.getOriginalFilename();
        if ( !(   filename.endsWith(".png") || filename.endsWith(".jpg") || filename.endsWith(".jpeg")  ) ) {
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }

        // 如果判断通过，上传文件到OSS
        String path = PathUtil.generateFilePath(filename);
        String url = uploadOSS(img, path);
        return ResponseResult.okResult(url);
    }

    private String uploadOSS(MultipartFile imgFile, String path) {
        // 构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);

        // 默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = path;

        try {

            InputStream inputStream = imgFile.getInputStream();

            // 创建凭证
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(inputStream, key, upToken, null, null);
                // 解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return "http://scqv6jvm6.hn-bkt.clouddn.com/" + key;
            } catch (QiniuException ex) {
                ex.printStackTrace();
                if (ex.response != null) {
                    System.err.println(ex.response);

                    try {
                        String body = ex.response.toString();
                        System.err.println(body);
                    } catch (Exception ignored) {
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "www";
    }
}
