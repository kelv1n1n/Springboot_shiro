package com.springboot_shiro.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置类
 */

@Configuration
public class ShiroConfig {

    /**
     * 创建ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager")DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        //1、设置安全管理器
        filterFactoryBean.setSecurityManager(securityManager);

        //2、添加shiro的内置过滤器 实现url拦截
        //  常见的过滤器：
        //          anon: 无需认证（登录）就可以访问
        //          authc: 必须认证才能访问
        //          user: 如果使用了 记住我 的功能可以直接访问
        //          perms: 该资源必须得到资源权限才可以访问
        //          role: 该资源必须得到角色权限才可以访问

        //认证过滤器
        Map<String, String> filterMap = new LinkedHashMap<String, String>();//保证集合元素顺序 用LinkedHashMap
        filterMap.put("/add","authc");
        filterMap.put("/update","authc");

        //哪个访问路径部拦截就给他 anon 的权限

        //授权过滤器 perms/role
        //当shiro拦截授权后，shiro会默认跳转到未授权的页面
        filterMap.put("/add","perms[user:add]");
        filterMap.put("/update","perms[user:update]");

        //修改跳转的登录页面 默认是:login.jsp
        filterFactoryBean.setLoginUrl("/toLogin");

        //设置未授权的页面 默认报401错误
        filterFactoryBean.setUnauthorizedUrl("/unAuth");

        filterFactoryBean.setFilterChainDefinitionMap(filterMap);

        return filterFactoryBean;
    }


    /**
     * 创建DefaultWebSecurityManager
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联realm
        securityManager.setRealm(userRealm);
        return securityManager;
    }


    /**
     * 创建realm
     */
    @Bean(name = "userRealm")
    public UserRealm getRealm(){
        return new UserRealm();
    }


    /**
     * 配置shiroDialect,用于thymeleaf和shiro标签配合使用
     */
    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }

}
