package com.mxf.springbootinit.model.dto.user;

import lombok.Data;

@Data
public class UserVerificationCode {

    private Integer verificationCodeId;

    private String verificationCode;

}
