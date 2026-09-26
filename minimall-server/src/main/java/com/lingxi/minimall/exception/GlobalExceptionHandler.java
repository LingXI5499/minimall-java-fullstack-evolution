package com.lingxi.minimall.exception;

import com.lingxi.minimall.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** 把参数、业务和意外异常分别转换成稳定的 JSON 与 HTTP 状态码。 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Void>> business(BusinessException e) {
        log.warn("Business error: {}", e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(Result.error(e.getStatus().value(), e.getMessage()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
    public ResponseEntity<Result<Void>> validation(Exception e) {
        String message = e instanceof MethodArgumentNotValidException invalid
                ? invalid.getBindingResult().getAllErrors().getFirst().getDefaultMessage()
                : e.getMessage();
        return ResponseEntity.badRequest().body(Result.error(400, message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Result<Void>> json(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(Result.error(400, "JSON 格式错误"));
    }

    /** 没有对应的接口或静态资源属于客户端路径错误，不能记成服务器故障。 */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Result<Void>> notFound(NoResourceFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Result.error(404, "请求路径不存在"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> unexpected(Exception e) {
        log.error("Unexpected request error", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.error(500, "服务器内部错误"));
    }
}
