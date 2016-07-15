package com.wz.web.interceptor;

import com.wz.web.util.CookieUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

public class PrivilegeInterceptor extends HandlerInterceptorAdapter {

    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = CookieUtil.getCookieValue(request);
        if (StringUtils.isNotBlank(token)) {
//            BrandUser user = brandUserService.check(token);
//            if (user != null) {
//                request.setAttribute("REMOTE_USER", user);
                return true;
//            }
        }
        response.sendRedirect("/login?ref=" + URLEncoder.encode(String.valueOf(request.getRequestURL()), "UTF-8"));
        return false;
    }
}
