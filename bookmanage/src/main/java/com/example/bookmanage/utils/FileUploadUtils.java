package com.example.bookmanage.utils;

import java.io.File;

public class FileUploadUtils {

    //静态资源路径
    public final static String IMG_PATH_PREFIX = "D:\\研一\\练习\\bookmanage-vue\\src\\assets\\image";

    public static File getImgDirFile() {
        String fileDirPath = new String(IMG_PATH_PREFIX);

        File fileDir = new File(fileDirPath);
        if (!fileDir.exists()) {

            fileDir.mkdirs();
        }
        return fileDir;
    }
}
