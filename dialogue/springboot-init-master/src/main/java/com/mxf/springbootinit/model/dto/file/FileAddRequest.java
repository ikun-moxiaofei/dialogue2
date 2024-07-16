package com.mxf.springbootinit.model.dto.file;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户创建请求
 *
 */
@Data
public class FileAddRequest implements Serializable {

    private String filename;
    private MultipartFile[] multipartFiles; // 用于接收上传的文件
    private String filesize;
    private Date filedate;

}