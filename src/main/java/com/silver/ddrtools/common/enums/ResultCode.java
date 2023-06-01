package com.silver.ddrtools.common.enums;

/**
 * @ClassName ResultCode
 * @Description TODO
 * @Author silver
 * @Date 2022/7/16 17:47
 * @Version 1.0
 **/

public enum ResultCode {
    SUCCESS(200, "success"),
    FAIL(400, "fail"),
    UNAUTHORIZED(401, "unauthorized"),
    NOT_THIS_USER(100101, "用户名不存在"),
    PASSWORD_ERROR(100102, "账号或密码错误"),
    NOT_FOUND(404, "not found"),
    SYSTEM_ERROR(500, "internal server error"),
    INTERNAL_SERVER_ERROR(500, "internal server error"),
    UPDATEPASSWORD_SUCCESS(200,"密码修改成功"),
    REGISTER_ERROR(100104,"不可以为空"),
    REGISTER_ERROR_NAME(100105,"用户名已存在"),
    REGISTER_SUCCESS(200,"注册成功"),
    VERIFY_SUCCESS(200,"verify成功"),
    VERIFY_ERROR(100106,"verify失败"),
    NOT_THIS_MAP(200101,"地图未找到"),
    IMG_TYPE_ERROR(300101,"上传失败，文件格式不正确"),
    IMG_SIZE_ERROR(300102,"上传失败，文件超过限制大小5M"),
    DATA_ALREADY_EXIST(300103,"已经有这个吃的了"),
    NOT_THIS_PLAYER(200102,"玩家未找到");




    private int code;
    private String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
