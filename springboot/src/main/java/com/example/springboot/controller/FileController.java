package com.example.springboot.controller;

import cn.hutool.core.io.FileUtil;
import com.example.springboot.common.AuthAccess;
import com.example.springboot.common.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${ip:localhost}")
    String ip;
    @Value("${server.port}")
    String port;

    private static final String ROOT_PATH = System.getProperty("user.dir") + File.separator + "files"; //文件存储的目录 C:\Users\Chan9zero\Desktop\biyesheji-1\project-1\files

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename(); //文件的原始名称 aaa.png
        String mainName = FileUtil.mainName(originalFilename);//aaa
        String extName = FileUtil.extName(originalFilename); //png
        if(!FileUtil.exist(ROOT_PATH)){
            FileUtil.mkdir(ROOT_PATH); //如果当前文件的父级目录不存在，就创建
        }
        if(FileUtil.exist(ROOT_PATH + File.separator+ originalFilename)){ //如果当前上传的文件已经存在， 重命名一个文件名称
            originalFilename = System.currentTimeMillis() + "_" + mainName + "." + extName;
        }
        File saveFile = new File(ROOT_PATH + File.separator + originalFilename);
        file.transferTo(saveFile); // 存储文件到本地磁盘
        String url = "http://" + ip + ":" + port + "/file/download/" + originalFilename;
        return Result.success(url); //返回文件的链接
    }

    @AuthAccess
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws IOException{
        //response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8")); //下载预览的文件
        response.addHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(fileName, "utf-8")); //支持预览的文件预览，不支持预览的文件就下载
        String filePath = ROOT_PATH + File.separator + fileName;
        if(!FileUtil.exist(filePath)){
            return;
        }
        byte[] bytes = FileUtil.readBytes(filePath);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes); //文件的字节流数组
        outputStream.flush();
        outputStream.close();
    }
}
