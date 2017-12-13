package com.kira.web.service;

import com.kira.web.bean.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author kira
 */
@Path("/account")
public class AccountService {
    @Path("/login")
    @GET
    public String get()
    {
        return "You get the login";
    }
    @Path("/login")
    @POST
    //指定请求与返回的响应体为Json
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User post()
    {
        User user = new User();
        user.setName("kira");
        user.setAge(20);
        return user;
    }
}
