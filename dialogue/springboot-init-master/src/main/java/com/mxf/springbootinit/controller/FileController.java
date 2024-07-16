package com.mxf.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mxf.springbootinit.annotation.AuthCheck;
import com.mxf.springbootinit.common.BaseResponse;
import com.mxf.springbootinit.common.DeleteRequest;
import com.mxf.springbootinit.common.ErrorCode;
import com.mxf.springbootinit.common.ResultUtils;
import com.mxf.springbootinit.config.WxOpenConfig;
import com.mxf.springbootinit.constant.FileConstant;
import com.mxf.springbootinit.constant.FileConstant;
import com.mxf.springbootinit.constant.UserConstant;
import com.mxf.springbootinit.exception.BusinessException;
import com.mxf.springbootinit.exception.ThrowUtils;
import com.mxf.springbootinit.model.dto.file.FileAddRequest;

import java.io.BufferedReader;
import java.io.File;

import com.mxf.springbootinit.model.dto.file.FileQueryRequest;
//import com.mxf.springbootinit.model.entity.File;


import com.mxf.springbootinit.model.dto.file.FileQueryRequest_;
import com.mxf.springbootinit.model.dto.user.UserQueryRequest;
import com.mxf.springbootinit.model.entity.User;
import com.mxf.springbootinit.model.vo.UserVO;
import com.mxf.springbootinit.service.FileService;
import com.mxf.springbootinit.service.FileService;
import com.mxf.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

//import static com.sun.jna.ELFAnalyser.ArmAeabiAttributesTag.File;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;


/**
 * 用户接口
 *
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Resource
    private FileService fileService;

    @Resource
    private UserService userService;

    /**
     * 创建用户
     *
//     * @param fileAddRequest
//     * @param request
     * @return
     */

    public static void sendPostRequest(String url, String data) {
        try {
            // 创建URL对象
            URL obj = new URL(url);
            // 打开连接
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // 设置请求方法
            con.setRequestMethod("POST");
            // 设置请求头信息
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            // 发送POST请求必须设置
            con.setDoOutput(true);

            // 获取输出流并写入数据
            try(DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.writeBytes(data);
                wr.flush();
            }

            // 获取响应码
            int responseCode = con.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);

            // 如果响应码是200，则读取响应内容
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try(BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    // 打印结果
                    System.out.println(response.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/add")
    public Object addFile(MultipartFile[] multipartFiles,String id) {
        String rootPath = "/home/dir";
//         String rootPath = "D:\\zhuomian\\dialogue2\\dialogue\\springboot-init-master\\src\\main\\java\\com\\mxf\\springbootinit";

        File fileDir = new File(rootPath);
        if (!fileDir.exists() && !fileDir.isDirectory()) {
            fileDir.mkdirs();
        }
        com.mxf.springbootinit.model.entity.File file_ = new  com.mxf.springbootinit.model.entity.File();
//        User user = userService.getLoginUser(request);
        file_.setFilename(multipartFiles[0].getOriginalFilename());
        file_.setUserid(Long.valueOf(id));
        file_.setCreatetime(new Date());
        file_.setState(0);
        file_.setFilesize(String.valueOf(multipartFiles[0].getSize()));

        if (!fileDir.exists() && !fileDir.isDirectory()) {
            fileDir.mkdirs();
        }
        String storagePath = "";
        try {
            if (multipartFiles != null && multipartFiles.length > 0) {
                for (int i = 0; i < multipartFiles.length; i++) {
                    try {
                        //以原来的名称命名,覆盖掉旧的，这里也可以使用UUID之类的方式命名，这里就没有处理了
                        storagePath = rootPath + multipartFiles[i].getOriginalFilename();
                        System.out.println("上传的文件：" + multipartFiles[i].getName() + "," + multipartFiles[i].getContentType() + "," + multipartFiles[i].getOriginalFilename()
                                + "，保存的路径为：" + storagePath);
                        // 3种方法： 第1种
//                        Streams.copy(multipartFiles[i].getInputStream(), new FileOutputStream(storagePath), true);
                        // 第2种
//                        Path path = Paths.get(storagePath);
//                        Files.write(path,multipartFiles[i].getBytes());
                        // 第3种
                        multipartFiles[i].transferTo(new File(storagePath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = "http://localhost:8000/vector_store";
        String userName = id;
        String fileName = file_.getFilename();

        String jsonString = String.format("{\"file_path\": \"%s\", \"user_name\": \"%s\", \"file_name\": \"%s\"}", storagePath, userName, fileName);

        sendPostRequest(url, jsonString);

        boolean result = fileService.save(file_);
        return ResultUtils.success(result);

    }

    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return dotIndex > 0 ? filename.substring(dotIndex) : "";
    }


    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteFile(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     *
     * @param fileQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<com.mxf.springbootinit.model.entity.File>> listFileByPage(@RequestBody FileQueryRequest fileQueryRequest) {
        if (fileQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = fileQueryRequest.getCurrent();
        long size = Math.min(fileQueryRequest.getPageSize(), 20L); // 限制每页大小不超过20
        // 使用fileService的page方法进行分页查询
        Long id_ = Long.valueOf(fileQueryRequest.getUserid());
        FileQueryRequest_ fileQueryRequest_ = new FileQueryRequest_();

        BeanUtils.copyProperties(fileQueryRequest,fileQueryRequest);
        fileQueryRequest_.setUserid(id_);

        Page<com.mxf.springbootinit.model.entity.File> filePage = fileService.page(new Page<>(current, size), fileService.getQueryWrapper(fileQueryRequest_));
        return ResultUtils.success(filePage); // 直接返回分页结果
    }

}
