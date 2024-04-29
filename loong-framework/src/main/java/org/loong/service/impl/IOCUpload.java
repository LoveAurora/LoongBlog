package org.loong.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.loong.domain.ResponseResult;
import org.loong.enums.AppHttpCodeEnum;
import org.loong.handler.exception.SystemException;
import org.loong.utils.PathUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;


@Service("")
@Data
@Slf4j
@ConfigurationProperties(prefix = "ossioc")
public class IOCUpload {
    private String accessKey;
    private String secretKey;
    private String bucket;

    public ResponseResult uploadFile(MultipartFile file) {
        //判断文件类型
        //获取原始文件名
        String originFileName = file.getOriginalFilename();
        //对原始文件名进行判断
        if (!originFileName.endsWith(".txt") && !originFileName.endsWith(".pdf") && !originFileName.endsWith(".docx")) {
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        //如果判断通过上传文件到OSS
        String filePath = PathUtils.generateFilePath(originFileName);
        JSONObject res = uploadOss(file, filePath);

        return ResponseResult.successResult(res);
    }

    private JSONObject uploadOss(MultipartFile imgFile, String filePath) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;
        try {
            InputStream inputStream = imgFile.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response =
                        uploadManager.put(inputStream, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                log.info("putRet.key", putRet.key);
                log.info("putRet.hash", putRet.hash);

                // 构建 JSON 对象
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("url", "http://sb8wgssza.hb-bkt.clouddn.com/" + putRet.key);

                // 发送 POST 请求到远程服务器
                HttpClient httpClient = HttpClients.createDefault();
                HttpPost httpPost = new HttpPost("http://192.168.3.33:5000/receive");
                StringEntity entity = new StringEntity(jsonObject.toString());
                httpPost.setEntity(entity);
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse responseEntity = httpClient.execute(httpPost);
                if (responseEntity != null) {
                    // 处理服务器返回的 JSON 数据
                    HttpEntity entityResponse = responseEntity.getEntity();
                    String responseString = EntityUtils.toString(entityResponse, "UTF-8");
                    log.info("Received response: " + responseString);

                    // 使用 Fastjson 解析 JSON 字符串
                    JSONObject serverResponseJson = JSONObject.parseObject(responseString);
                    return serverResponseJson;
                }
//
//                HttpEntity serverEntity = responseEntity.getEntity();
//                String serverResponseString = EntityUtils.toString(serverEntity);

//                log.info("Received response: " + serverResponseString);

                // 使用 Fastjson 解析 JSON 字符串
//                JSONObject serverResponseJson = JSONObject.parseObject(serverResponseString);

                // 处理服务器返回的数据

                return null;
                // return "http://sb8wgssza.hb-bkt.clouddn.com/" + key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    log.error("上传失败", ex2.toString());
                }
            }
        } catch (Exception ex) {
            log.info("上传失败");
        }
        return null;
    }
}
