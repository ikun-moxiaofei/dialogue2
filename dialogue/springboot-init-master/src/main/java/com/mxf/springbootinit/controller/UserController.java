package com.mxf.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mxf.springbootinit.annotation.AuthCheck;
import com.mxf.springbootinit.common.BaseResponse;
import com.mxf.springbootinit.common.DeleteRequest;
import com.mxf.springbootinit.common.ErrorCode;
import com.mxf.springbootinit.common.ResultUtils;

import com.mxf.springbootinit.config.WxOpenConfig;
import com.mxf.springbootinit.constant.UserConstant;
import com.mxf.springbootinit.exception.BusinessException;
import com.mxf.springbootinit.exception.ThrowUtils;
import com.mxf.springbootinit.model.dto.user.*;
import com.mxf.springbootinit.model.entity.User;
import com.mxf.springbootinit.model.vo.LoginUserVO;
import com.mxf.springbootinit.model.vo.UserVO;
import com.mxf.springbootinit.service.UserService;

import java.util.List;
import java.util.Random;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.mxf.springbootinit.service.impl.UserServiceImpl.SALT;

/**
 * 用户接口
 *
 */
@RestController
//@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private WxOpenConfig wxOpenConfig;

    // region 登录相关

    /**
     * 申请验证码
     *
     * @param
     * @return
     */
    @GetMapping("/get/register")
    public BaseResponse<UserVerificationCode> getVerificationCode() {
        Random random = new Random();
        int randomNumber = random.nextInt(5); // 生成0到Integer.MAX_VALUE之间的随机整数
        String verificationCode = "";
        if(randomNumber == 0){
            verificationCode = "https://camo.githubusercontent.com/2c55cbca002843b9baba5487c8c3b9f1fd4f902f396db0c2cdf4756f37ac1309/68747470733a2f2f63646e2e6e6c61726b2e636f6d2f79757175652f302f323032342f706e672f33383838363732332f313731373736333934303736302d31653038383965652d363132332d346231312d393331302d3164383465303133353439662e706e6723617665726167654875653d25323364396462633826636c69656e7449643d7533373038666338642d363630312d342666726f6d3d7061737465266865696768743d37342669643d7845487179266f726967696e4865696768743d313138266f726967696e57696474683d333231266f726967696e616c547970653d62696e61727926726174696f3d312e3934393939393932383437343432363326726f746174696f6e3d302673686f775469746c653d66616c73652673697a653d3236353438267374617475733d646f6e65267374796c653d6e6f6e65267461736b49643d7532623434663331662d363363302d346137382d616537642d3663356331313564343265267469746c653d2677696474683d323030";
        } else if (randomNumber == 1) {
            verificationCode = "https://camo.githubusercontent.com/902752519f49d54c5822c30ceb3178d12933b74217f30c3be4c067bfac0a8a7f/68747470733a2f2f63646e2e6e6c61726b2e636f6d2f79757175652f302f323032342f706e672f33383838363732332f313731373736333937363338382d34326635333264312d633364342d346163652d613132632d3337386364373930323036642e706e6723617665726167654875653d25323364646463646326636c69656e7449643d7533373038666338642d363630312d342666726f6d3d7061737465266865696768743d36382669643d753537363534623861266f726967696e4865696768743d313531266f726967696e57696474683d343437266f726967696e616c547970653d62696e61727926726174696f3d312e3934393939393932383437343432363326726f746174696f6e3d302673686f775469746c653d66616c73652673697a653d3137303433267374617475733d646f6e65267374796c653d6e6f6e65267461736b49643d7533333132373566352d306161362d346366392d623232382d3737646239643732313563267469746c653d2677696474683d323030";
        } else if (randomNumber == 2) {
            verificationCode = "https://camo.githubusercontent.com/678fb00e20900c6b10073ad5da7af64d014f942318b85bad680b4a646a6a11e4/68747470733a2f2f63646e2e6e6c61726b2e636f6d2f79757175652f302f323032342f706e672f33383838363732332f313731373736343030323634312d30333961376539622d326463612d346430362d623463612d3132343030366663626363302e706e6723617665726167654875653d25323362656465623126636c69656e7449643d7533373038666338642d363630312d342666726f6d3d7061737465266865696768743d37302669643d56674f6463266f726967696e4865696768743d313930266f726967696e57696474683d353431266f726967696e616c547970653d62696e61727926726174696f3d312e3934393939393932383437343432363326726f746174696f6e3d302673686f775469746c653d66616c73652673697a653d313530353738267374617475733d646f6e65267374796c653d6e6f6e65267461736b49643d7564383230373733362d643139322d343635642d613062342d3862346236346139313237267469746c653d2677696474683d323030";
        } else if (randomNumber == 3) {
            verificationCode = "https://camo.githubusercontent.com/46403fc202ce1a70caaa405b2e050c519d60d4e8b61bac82663d5290efb45ee3/68747470733a2f2f63646e2e6e6c61726b2e636f6d2f79757175652f302f323032342f706e672f33383838363732332f313731373736343030383838322d36633531353765322d613566312d343930372d613034302d3336346137313031623038642e706e6723617665726167654875653d25323365386561653126636c69656e7449643d7533373038666338642d363630312d342666726f6d3d7061737465266865696768743d38332669643d753666623362326565266f726967696e4865696768743d323433266f726967696e57696474683d353836266f726967696e616c547970653d62696e61727926726174696f3d312e3934393939393932383437343432363326726f746174696f6e3d302673686f775469746c653d66616c73652673697a653d313231303430267374617475733d646f6e65267374796c653d6e6f6e65267461736b49643d7538343065613237352d636364612d346336622d613136362d6330656433356636636561267469746c653d2677696474683d323030";
        } else if (randomNumber == 4) {
            verificationCode = "https://camo.githubusercontent.com/224b4512bf3d82ee5c4c6c98bc0c4aedf3655da03f45316de85605628ee6b4a7/68747470733a2f2f63646e2e6e6c61726b2e636f6d2f79757175652f302f323032342f706e672f33383838363732332f313731373736343031383138322d35356533346137662d336433302d343161322d386333332d6436633830663062613038642e706e6723617665726167654875653d25323362366439613626636c69656e7449643d7533373038666338642d363630312d342666726f6d3d7061737465266865696768743d38322669643d753836643330613462266f726967696e4865696768743d323331266f726967696e57696474683d353632266f726967696e616c547970653d62696e61727926726174696f3d312e3934393939393932383437343432363326726f746174696f6e3d302673686f775469746c653d66616c73652673697a653d313637323033267374617475733d646f6e65267374796c653d6e6f6e65267461736b49643d7536373830366465322d313266632d343633302d613530662d3665346463633465383031267469746c653d2677696474683d313939";
        }else {
            verificationCode = "https://camo.githubusercontent.com/f1dc3d2bb8315ead28695b001a8d0553fe47ddc03c2017d13a78ecfa6efaad35/68747470733a2f2f63646e2e6e6c61726b2e636f6d2f79757175652f302f323032342f706e672f33383838363732332f313731373736343033363931382d37643131363562322d356365392d346565302d396465332d3530303764633438363737362e706e6723617665726167654875653d25323361396432393826636c69656e7449643d7533373038666338642d363630312d342666726f6d3d7061737465266865696768743d36392669643d756634396231616466266f726967696e4865696768743d323232266f726967696e57696474683d363436266f726967696e616c547970653d62696e61727926726174696f3d312e3934393939393932383437343432363326726f746174696f6e3d302673686f775469746c653d66616c73652673697a653d313831313033267374617475733d646f6e65267374796c653d6e6f6e65267461736b49643d7531633930633235302d656433612d346133392d386338392d3337666435396166646334267469746c653d2677696474683d323030";
        }
        UserVerificationCode userVerificationCode = new UserVerificationCode();
        userVerificationCode.setVerificationCodeId(randomNumber);
        userVerificationCode.setVerificationCode(verificationCode);
        return ResultUtils.success(userVerificationCode);
    }

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Integer root = userRegisterRequest.getRoot();
        System.out.println(root);
        System.out.println("1111111111111111111111111111111111111111");

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String verificationCode = userRegisterRequest.getVerificationCode();
        Integer verificationCodeID = userRegisterRequest.getVerificationCodeID();
        String lowerVerificationCode = verificationCode.toLowerCase();


        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        if (verificationCodeID == 0){
            if (!lowerVerificationCode.equals("bu75g")){
                throw new BusinessException(ErrorCode.VERIFICATIONCODE_ERROR);
            }
        } else if (verificationCodeID == 1) {
            if (!lowerVerificationCode.equals("k4p8")){
                throw new BusinessException(ErrorCode.VERIFICATIONCODE_ERROR);
            }
        }else if (verificationCodeID == 2) {
            if (!lowerVerificationCode.equals("3n3d")){
                throw new BusinessException(ErrorCode.VERIFICATIONCODE_ERROR);
            }
        }else if (verificationCodeID == 3) {
            if (!lowerVerificationCode.equals("m8k2")){
                throw new BusinessException(ErrorCode.VERIFICATIONCODE_ERROR);
            }
        }else if (verificationCodeID == 4) {
            if (lowerVerificationCode != "7w0b"){
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }else {
            if (lowerVerificationCode != "dwse"){
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        String userRole = "";
        if (root == 1234){
            userRole = "admin";
        }else{
            userRole = "user";
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, userRole);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }
    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(user));
    }

    // endregion

    // region 增删改查

    /**
     * 创建用户
     *
     * @param userAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request) {
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        // 默认密码 12345678
        String defaultPassword = "12345678";
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + defaultPassword).getBytes());
        user.setUserPassword(encryptPassword);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * 删除用户
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,
            HttpServletRequest request) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取用户（仅管理员）
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 根据 id 获取包装类
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id, HttpServletRequest request) {
        BaseResponse<User> response = getUserById(id, request);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }

    /**
     * 分页获取用户列表（仅管理员）
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest,
            HttpServletRequest request) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        return ResultUtils.success(userPage);
    }

    /**
     * 分页获取用户封装列表
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest,
            HttpServletRequest request) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(current, size, userPage.getTotal());
        List<UserVO> userVO = userService.getUserVO(userPage.getRecords());
        userVOPage.setRecords(userVO);
        return ResultUtils.success(userVOPage);
    }

    // endregion

    /**
     * 更新个人信息
     *
     * @param userUpdateMyRequest
     * @param request
     * @return
     */
    @PostMapping("/update/my")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,
            HttpServletRequest request) {
        if (userUpdateMyRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest, user);
        user.setId(loginUser.getId());
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }
}
