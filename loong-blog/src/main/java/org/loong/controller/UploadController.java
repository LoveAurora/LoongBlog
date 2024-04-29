package org.loong.controller;

import org.loong.domain.ResponseResult;
import org.loong.service.UploadService;
import org.loong.service.impl.IOCUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {
    @Autowired
    private UploadService uploadService;
    @Autowired
    private IOCUpload iocUpload;

    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile img){
        return uploadService.uploadImg(img);
        }

    @PostMapping("/iocupload")
    public ResponseResult uploadFile(MultipartFile file){
        return iocUpload.uploadFile(file);
    }
}
