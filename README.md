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

