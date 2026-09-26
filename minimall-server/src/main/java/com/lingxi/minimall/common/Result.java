package com.lingxi.minimall.common;

/**
 * 所有业务接口统一返回这个形状。
 * code 与 HTTP 状态码保持一致；data 放成功结果，失败时为空。
 */
public record Result<T>(int code, String message, T data) {
    public static <T> Result<T> success(T data) { return new Result<>(200, "success", data); }
    public static <T> Result<T> created(T data) { return new Result<>(201, "created", data); }
    public static Result<Void> error(int code, String message) { return new Result<>(code, message, null); }
}
