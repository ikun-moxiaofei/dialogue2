package com.mxf.springbootinit.service.impl;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mxf.springbootinit.common.ErrorCode;
import com.mxf.springbootinit.constant.CommonConstant;
import com.mxf.springbootinit.exception.BusinessException;
import com.mxf.springbootinit.mapper.FileMapper;
import com.mxf.springbootinit.model.dto.file.FileQueryRequest;
import com.mxf.springbootinit.model.dto.file.FileQueryRequest;
import com.mxf.springbootinit.model.dto.file.FileQueryRequest_;
import com.mxf.springbootinit.model.entity.File;
import com.mxf.springbootinit.model.entity.File;
import com.mxf.springbootinit.model.entity.User;
import com.mxf.springbootinit.model.vo.UserVO;
import com.mxf.springbootinit.service.FileService;
import com.mxf.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现
 *
 */
@Service
@Slf4j
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {
    @Override
    public QueryWrapper<File> getQueryWrapper(FileQueryRequest_ fileQueryRequest) {
        if (fileQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = 1L;
        if (fileQueryRequest.getId() != null) {
            id = Long.valueOf(fileQueryRequest.getId());
        }else {
            id = null;
        }

        String filename = fileQueryRequest.getFilename();
        Long userid = 1L;
        if (fileQueryRequest.getUserid() != null) {
            userid = Long.valueOf(fileQueryRequest.getUserid());
        }else{
            userid = null;
        }
        Date createtime = fileQueryRequest.getCreatetime();
        Integer state = fileQueryRequest.getState();
        int current = fileQueryRequest.getCurrent();
        int pageSize = fileQueryRequest.getPageSize();
        String sortField = fileQueryRequest.getSortField();
        String sortOrder = fileQueryRequest.getSortOrder();

        QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(filename), "filename", filename);
        queryWrapper.eq(userid != null, "userid", userid);
        queryWrapper.eq(createtime != null, "createtime", createtime);
        queryWrapper.eq(state != null, "state", state);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
    @Override
    public List<File> getFileVO(List<File> fileList) {
        // 如果没有VO类，这个方法可以直接返回fileList
        return fileList;
    }

    @Override
    public File getFile(File file) {
        // 如果没有VO类，这个方法可以直接返回file对象
        return file;
    }

}
