// java
package backend.airo.api.test.support;

import backend.airo.domain.user.User;
import backend.airo.domain.user.enums.ProviderType;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UserPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  org.springframework.web.bind.support.WebDataBinderFactory binderFactory) {
        // 더미 User 인스턴스 생성 (User의 빌더 또는 생성자에 맞게 수정)
        return new User(
                1L, // ID
                "testuser@example.com",// email
                "Test User", // username
                "testuser", // username
                "010-1234-5678",
                null,
                ProviderType.KAKAO,
                "kakao12345" // providerId
        );
    }
}