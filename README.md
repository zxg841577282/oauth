v3版本：


└── oauth_auth
    ├── config      认证服务器相关配置信息
    ├── other       自定义异常输出及其他
    │   ├── provider 自定义登录方式逻辑处理
    │   └── TokenGranter 自定义登录方式
    └── service     继承UserDetailsService的实现
└── oauth_client
    ├── config      资源服务器相关配置信息
    └── controller  测试控制器
└── oauth_common
    ├── dao         dao层
    ├── data        实体类层(包括入参和反参模型,个人喜好这样写)
    │   ├── constant
    │   ├── dto
    │   ├── entity
    │   └── vo
    ├── other       其他公共部分
    │   └── token 自定义登录方式的token生成策略
    └── util        公共工具类
└── other           项目外其他资料


主要maven依赖及版本
springboot  |   mybatis-plus    |   oauth2          ｜   redis
2.5.0       |   3.4.2           |   2.5.1.RELEASE   ｜   2.5.0


主要实现功能及介绍
本次版本在V2版本的基础上，新增了第三方登录方式。第三方接入api为justAuth，参考地址：https://www.justauth.cn/

参考V2版本，新增第三方登录的相关配置文件
ThirdProvider，ThirdCustomTokenGranter，ThirdAuthenticationToken
在AuthorizationServerConfig的getTokenGranters添加第三方登录模式
在SecurityConfig的configure中添加ThirdProvider依赖

第三方登录接入流程(个人理解，有错误欢迎文明指出)
访问/third/render/gitee (此处以gitee为例，其他平台同理)

跳转至对应平台登录页面
![img.png](other/img.png)

登录完后，跳转至对应平台授权页面
![img_1.png](other/img_1.png)

确认授权后，调用接口/third/callback/gitee，其中AuthResponse返回为成功，则会包含第三方的access_token,
是第三方的access_token(用来访问第三方的接口的，如用户信息)，由于justAuth中已经封装好了获取第三方用户信息，
所以不需要我们自己写了，因此获取到用户信息，接下来要接自己系统中的用户逻辑(此部分自己视业务实现)，
生成自身系统的access_token
![img_2.png](other/img_2.png)


关于资源端如何验证token的正确性，请前往V1分支的readme.me查看