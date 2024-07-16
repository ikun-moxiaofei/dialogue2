package com.mxf.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mxf.springbootinit.model.dto.file.FileQueryRequest;
import com.mxf.springbootinit.model.dto.file.FileQueryRequest_;
import com.mxf.springbootinit.model.dto.user.UserQueryRequest;
//import com.mxf.springbootinit.model.entity.File;
import com.mxf.springbootinit.model.entity.File;
import com.mxf.springbootinit.model.entity.User;
import com.mxf.springbootinit.model.vo.LoginUserVO;
import com.mxf.springbootinit.model.vo.UserVO;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务
 *
 */
public interface FileService extends IService<File> {

    QueryWrapper<File> getQueryWrapper(FileQueryRequest_ fileQueryRequest);

    List<File> getFileVO(List<File> fileList);

    File getFile(File file);

}
