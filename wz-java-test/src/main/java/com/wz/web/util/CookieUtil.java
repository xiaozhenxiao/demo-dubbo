package com.wz.web.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    private static final String COOKIE_NAME = "authentication";

    private static Cookie getCookie(HttpServletRequest request) {
        Cookie cookies[] = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (COOKIE_NAME.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }

    public static String getCookieValue(HttpServletRequest request) {
        Cookie cookie = getCookie(request);
        return cookie != null ? cookie.getValue() : null;
    }

    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String value, int maxAge) {
        Cookie cookie = new Cookie(COOKIE_NAME, value);
        cookie.setPath(getPath(request));
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response) {
        setCookie(request, response, null, 0);
    }

    private static String getPath(HttpServletRequest request) {
        String path = request.getContextPath();
        return (path == null || path.length() == 0) ? "/" : path;
    }
}
