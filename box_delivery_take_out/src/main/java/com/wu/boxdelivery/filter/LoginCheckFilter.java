package com.wu.boxdelivery.filter;

import com.alibaba.fastjson.JSON;
import com.wu.boxdelivery.common.BaseContext;
import com.wu.boxdelivery.common.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.filters.ExpiresFilter;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebFault;
import java.io.IOException;

/**
 * @description:
 * @author: wxj
 * filter过滤器
 * 检查用户是否完成登录
 **/
@Slf4j
@WebFilter(filterName = "LoginCheckFilter",urlPatterns ="/*" )//定义过滤器名称，配置拦截那些路径,拦截那些请求路径
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持统配符
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;//向下转型
        HttpServletResponse response=(HttpServletResponse) servletResponse;

        //1.获取本次请求的URI
        String requestURI =request.getRequestURI();

        log.info("拦截到请求：{}",requestURI);
        //d定义不需要处理的请求路径
        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };
        //2.判断本次请求是否需要处理，通过封装方法处理
        boolean check = check(urls, requestURI);
        //如果不需要处理，则直接放行
        if (check){

            log.info("本次请求：{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //4.判断登录状态，如果已经登录，则直接放行
        if(request.getSession().getAttribute("employee")!=null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));
            //设置当前用户登录id
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
           /* long id=Thread.currentThread().getId();
            log.info("线程id为:{}",id);*/
            filterChain.doFilter(request,response);
            return;
        }
        log.info("用户未登录");
        //5.如果未登录则返回未登录的结果，通过输出流向客户端页面来相应数据,通过response对象响应
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));//写json数据
        return;
    }
/*
        log.info("拦截到请求：{}",request.getRequestURI());
        filterChain.doFilter(request,response);//放行
    }//包下的过滤器
    */
    /**
     * @description:
     * @author: wxj
     * 路径匹配，检查本次请求是否需要放行
     * @param: requestURI
     **/
    public boolean check(String[] urls,String requestURI){
        for (String url:urls){
            boolean match = PATH_MATCHER.match(url, requestURI);//匹配url数组元素
            if (match){
                return  true;//匹配上了
            }
        }
        return false;
    }
}
