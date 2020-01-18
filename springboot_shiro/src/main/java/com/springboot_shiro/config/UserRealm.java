package com.springboot_shiro.config;

import com.springboot_shiro.model.User;
import com.springboot_shiro.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 自动应用户realm
 */

public class UserRealm extends AuthorizingRealm {


    @Autowired
    UserService userService;

    /**
     * 执行授权逻辑
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("模拟执行授权逻辑");

        //1、给资源进行授权
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

//        info.addStringPermission("user:add");

        //到数据库查询当前用户的权限字段是什么
        //获取当前用户的id
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        System.out.println("授权中------当前认证过用户的id:" + user.getId());

        User u = userService.findById(user.getId());
        info.addStringPermission(u.getPerms());

        return info;
    }


    /**
     * 执行认证逻辑
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("模拟执行认证逻辑");
        //编写shiro 判断逻辑，判断用户名和密码
        //用户名
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;

        //查询数据库用户名
        User user = userService.findByName(usernamePasswordToken.getUsername());

        if (user == null){
            //用户名不存在
            return null;//shiro底层会抛出一个UnknownAccountException异常
        }
        //密码 不需要判断 只需要返回AuthenticationInfo 的一个子类
        //                         有三个参数：
        //                              第一个是返回信息给login方法  就是Principal 传递认证完的对象给上面的授权
        //                              第二个是数据库的密码
        //                              第三个是shiro的名字
        return new SimpleAuthenticationInfo(user,user.getPassword(),"");
    }
}
