package org.loong.controller;

import org.loong.domain.ResponseResult;
import org.loong.service.impl.Pythonimpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/ioc")
public class IOCController {
    @Autowired
    private Pythonimpl pythonimpl;
    @GetMapping
    public ResponseResult ioc(){
        return  pythonimpl.IOC()  ;
    }

}
