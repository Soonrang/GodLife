//package com.example.rewardservice.user.domain.vo;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Embeddable;
//import lombok.AccessLevel;
//import lombok.EqualsAndHashCode;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.util.Objects;
//import java.util.regex.Pattern;
//
//@EqualsAndHashCode
//@Embeddable
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Getter
//public class Email {
//
//    private static final String EMAIL_REGEX = "^([\\w\\.\\_\\-])*[a-zA-Z0-9]+([\\w\\.\\_\\-])*([a-zA-Z0-9])+([\\w\\.\\_\\-])+@([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]{2,8}$";
//
//    @Column(name = "user_email", nullable = false, unique = true)
//    private String value;
//
//    public Email(String value) {
//
//    }
//
//    private void validate(final String value) {
//        if(Objects.isNull(value)) {
//            throw new NullPointerException("이메일 값은 null이 불가합니다.");
//        }
//
//        if(isNotMatchEmailFrom(value)) {
//            throw new IllegalArgumentExceptio n("허용되지 않은 이메일 형식입니다.");
//        }
//    }
//
//    private boolean isNotMatchEmailFrom(final String value) {
//        return !Pattern.matches(EMAIL_REGEX, value);
//    }
//
//
//}
