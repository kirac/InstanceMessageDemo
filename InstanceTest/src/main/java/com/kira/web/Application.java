package com.kira.web;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.kira.web.service.AccountService;

import org.glassfish.jersey.server.ResourceConfig;

import java.util.logging.Logger;

/**
 * @author kira
 */
public class Application extends ResourceConfig{
    public Application()
    {
        //注册逻辑处理
        packages(AccountService.class.getPackage().getName());
        //注册Json解析器
        register(JacksonJsonProvider.class);
        //注册日志打印输出
        register(Logger.class);
    }
}
