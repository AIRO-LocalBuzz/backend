package backend.airo.api.resolver;

import backend.airo.api.annotation.JwtTokenParsing;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class JwtTokenParsingArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JwtTokenParsing.class)
                && String.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, @NotNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        JwtTokenParsing meta = parameter.getParameterAnnotation(JwtTokenParsing.class);
        if (meta == null) {
            throw new IllegalStateException("@JwtTokenParsing not present");
        }

        HttpServletRequest req = webRequest.getNativeRequest(HttpServletRequest.class);
        String raw = (req != null) ? req.getHeader(meta.header()) : null;

        if (!StringUtils.hasText(raw)) {
            if (meta.required()) {
                throw new MissingRequestHeaderException(meta.header(), parameter);
            } else {
                return null;
            }
        }

        String token = sanitize(raw, meta.stripBearerPrefix());
        if (!StringUtils.hasText(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Empty bearer token");
        }

        return sanitize(raw, meta.stripBearerPrefix());
    }

    private static String sanitize(String raw, boolean stripBearer) {
        String s = raw.trim();
        if (stripBearer && s.regionMatches(true, 0, "Bearer ", 0, 7)) s = s.substring(7).trim();
        if (s.length() >= 2 && s.startsWith("\"") && s.endsWith("\"")) {
            s = s.substring(1, s.length() - 1);
        }
        return s;
    }

}
