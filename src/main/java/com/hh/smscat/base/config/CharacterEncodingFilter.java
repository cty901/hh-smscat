package com.hh.smscat.base.config;//package com.zone.test.base.config;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * 2019/4/4 23:44
// *
// * @author owen pan
// */
//@WebFilter(urlPatterns = "/*", filterName = "CharacterEncodingFilter")
//public class CharacterEncodingFilter implements Filter {
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        if (isAjaxRequest(request)) {
//            response.setCharacterEncoding("UTF-8");
//        }
//        filterChain.doFilter(request, response);
//    }
//
//    @Override
//    public void destroy() {
//    }
//
//    /**
//     * 是否是Ajax异步请求
//     */
//    public static boolean isAjaxRequest(HttpServletRequest request) {
//
//        String accept = request.getHeader("accept");
//        if (accept != null && accept.indexOf("application/json") != -1) {
//            return true;
//        }
//
//        String xRequestedWith = request.getHeader("X-Requested-With");
//        if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1) {
//            return true;
//        }
//
//        String uri = request.getRequestURI();
//        if (uri.toLowerCase().indexOf(".json") >= 0 || uri.toLowerCase().indexOf(".xml") >= 0) {
//            return true;
//        }
//
//        String ajax = request.getParameter("__ajax");
//        if (uri.toLowerCase().indexOf("json") >= 0 || uri.toLowerCase().indexOf("xml") >= 0) {
//            return true;
//        }
//
//        return false;
//    }
//
//}
