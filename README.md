### SpringBoot集成shiro权限框架小Demo

------

#### shiro 简洁

apache shiro是一个强大的且易用的Java安全框架，执行身份验证、授权、密码和回话管理。

#### shiro 体系结构

1. **Authentication 认证 --- 用户登录**
2. **Authorization  授权---用户具有哪些权限**
3. Cryptography  安全数据加密
4. Session Management  会话管理
5. web Integration web 系统集成
6. Interations 集成其他应用，spring、缓存框架

#### 创建springboot项目

#### 集成thymeleaf页面框架

在templates包下创建test页面进行测试能否访问该页面

#### shiro核心API

1. **Subject**---用户主体（把操作交给SecurityManager）
2. **SecurityManager**---安全管理器（关联realm）
3. **Realm**---shiro连接数据的桥梁

#### springboot 集合 shiro

##### 依赖

```
<!--导入shiro 权限框架-->
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-spring</artifactId>
    <version>1.4.2</version>
</dependency>

<!--thymeleaf对shiro的扩展坐标-->
<!--thymeleaf整合shiro标签-->
<dependency>
    <groupId>com.github.theborakompanioni</groupId>
    <artifactId>thymeleaf-extras-shiro</artifactId>
    <version>2.0.0</version>
</dependency>
```

##### 配置

- 先在config包下自定义Realm（比如一个用户）

```
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
		return null;
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
        return null;
    }
}

```

- config包下创建配置类

```
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
}
```

##### 使用shiro内置过滤器对页面进行拦截

- 在templates下创建一个user包，在user包创建add_user和update_user页面，然后修改test页面，实现点击跳转到上面两个页面

- 修改控制类controller

- 修改ShiroConfig

  ```
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
  }
  ```

  点击跳转拦截默认显示报错页面，说明成功拦截，修改默认跳转页面

  

- 添加login页面

- 修改ShiroConfig

  ```
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
          
          //修改跳转的登录页面 默认是:login.jsp
          filterFactoryBean.setLoginUrl("/toLogin");
          
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
  }
  ```

  到这里点击跳转会跳到登录页面