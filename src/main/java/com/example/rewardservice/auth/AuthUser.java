package com.example.rewardservice.auth;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
@Target: 메타 어노테이션으로 다른 어노테이션을 추가하는데 사용
ElementType을 명시하면 해당 타입에서만 커스텀 어노테이션 적용가능하다. *현재는 파라미터 타입으로 받아오도록 했다.
추가로 어노테이션 타입으로 사용하고 싶을 때 가능하도록 ANNOTATION_TYPE을 명시 (두 범위를 벗어나면 에러발생)

@Retention은 런타임에도 유지되는 것이다.
@Parameter은 필수가 아니지만 swagger ui 사용시 파라미터로 표현되지 않게 하는 것이다.

 */

@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(hidden = true)
@AuthenticationPrincipal(expression = "#this == 'annoymousUser' ? null : user")
public @interface AuthUser {
}
