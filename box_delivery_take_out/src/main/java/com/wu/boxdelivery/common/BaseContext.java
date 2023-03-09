package com.wu.boxdelivery.common;
/**
 * 基于ThreadLocal封装工具类，用于保存和获取当前登录用户id
 * */
public class BaseContext {//每个线程保存
    private  static ThreadLocal<Long>threadLocal=new ThreadLocal<>();
    /**
     * 设置值
     * @param id
     * */
    public static   void setCurrentId(Long id){
        threadLocal.set(id);
    }
    /**
     * 获取值
     * @return
     * */
    public static Long getCurrentId(){
        return threadLocal.get();
    }

}
