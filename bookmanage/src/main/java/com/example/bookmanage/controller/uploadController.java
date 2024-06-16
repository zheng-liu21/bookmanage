package com.example.bookmanage.controller;

import com.example.bookmanage.utils.FileUploadUtils;
import com.example.bookmanage.utils.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@RestController
@RequestMapping("/upload")
public class uploadController {

    @PreAuthorize("@ss.hasPermi('sys:user:uploadImage')")
    @RequestMapping("/uploadImage")
    public ResultUtil uploadImage(@RequestParam("file") MultipartFile imgFile, HttpServletRequest request, HttpSession session) {

        // 生成唯一文件名
        String uuid = UUID.randomUUID().toString().trim();
        String filename = imgFile.getOriginalFilename();
        int index = filename.indexOf(".");
        String fileNames = uuid + filename;

        // 调用FileUploadUtils工具类将图片存放到服务器上
        File fileDir = FileUploadUtils.getImgDirFile();
        Map<String, Object> map = new HashMap<>();
        try {
            // 构建真实的文件路径
            File newFile = new File(fileDir.getAbsolutePath() + File.separator + fileNames);
            imgFile.transferTo(newFile);
            map.put("fileName","profile\\"+fileNames);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回图片路径
       return ResultUtil.ok().data(map);
    }
}
