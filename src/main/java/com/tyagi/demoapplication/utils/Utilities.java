package com.tyagi.demoapplication.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

@Service
public class Utilities {

    /* Convert Multi-Part File to File */
    public File converMultipartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (Exception e) {
//            log.info("Error converting MultipartFile to File" + e);
        }

        return convertedFile;
    }
}
