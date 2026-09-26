package com.lingxi.minimall.audit;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/** 管理员商品操作的横切日志：只记录操作者和动作，不记录请求体或密码。 */
@Aspect
@Component
public class ProductOperationAspect {
    private static final Logger log = LoggerFactory.getLogger(ProductOperationAspect.class);

    @AfterReturning("execution(* com.lingxi.minimall.controller.ProductController.create(..)) || " +
            "execution(* com.lingxi.minimall.controller.ProductController.update(..)) || " +
            "execution(* com.lingxi.minimall.controller.ProductController.delete(..))")
    public void record(JoinPoint call) {
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        log.info("ADMIN OPERATION user={} action={}", user == null ? "unknown" : user.getName(),
                call.getSignature().getName());
    }
}
