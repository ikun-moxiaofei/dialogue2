package com.mxf.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 文件
 * @TableName file
 */
@TableName(value ="file")
@Data
public class File implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 创建用户id
     */
    private Long userid;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 文件状态，0是未向量化，1是已向量化
     */
    private Integer state;

    /**
     * 文件大小
     */
    private String filesize;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}