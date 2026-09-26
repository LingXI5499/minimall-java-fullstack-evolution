package com.lingxi.minimall.exception;

import org.springframework.http.HttpStatus;

/** 可预期的业务失败，携带对应 HTTP 状态供统一异常处理器使用。 */
public class BusinessException extends RuntimeException {
    private final HttpStatus status;
    public BusinessException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
    public HttpStatus getStatus() { return status; }
}
