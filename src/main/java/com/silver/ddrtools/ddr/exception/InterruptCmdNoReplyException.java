package com.silver.ddrtools.ddr.exception;

/**
 * @ClassName UserInputCannotFormatException
 * @Description TODO
 * @Author Tsui
 * @Date 2022/12/29 16:58
 * @Version 1.0
 **/

public class InterruptCmdNoReplyException extends Exception{
    public InterruptCmdNoReplyException(String message) {
        super(message);
    }
}
