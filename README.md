# zscat-platform

# qq群 群号: 171826977
#### 项目介绍

集成最新的dubbo https://github.com/alibaba/dubbo-spring-boot-starter
zscat-platform是高效率，低封装，面向学习型，面向微服的开源Java EE开发框架。

zscat-platform是在SpringBoot基础上搭建的一个Java基础开发平台，MyBatis为数据访问层，ApacheShiro为权限授权层，Ehcahe对常用数据进行缓存,dubbo为数据远程调用。

zscat-platform主要定位于后台管理系统学习交流，已内置后台管理系统的基础功能和高效的代码生成工具， 包括：系统权限组件、数据权限组件、数据字典组件、核心工具组件、视图操作组件、工作流组件、代码生成等。 前端界面风格采用了结构简单、性能优良、页面美观大气的Twitter Bootstrap页面展示框架。 采用分层设计、双重验证、提交数据安全编码、密码加密、访问验证、数据权限验证。 使用Maven做项目管理，提高项目的易开发性、扩展性。

zscat-platform目前包括以下四大模块，系统管理（SYS）模块、 内容管理（CMS）模块、在线办公（OA）模块、代码生成（GEN）模块。 系统管理模块 ，包括企业组织架构（用户管理、机构管理、区域管理）、 菜单管理、角色权限管理、字典管理等功能； 内容管理模块 ，包括内容管理（文章、链接），栏目管理、站点管理、 公共留言、文件管理、前端网站展示等功能； 在线办公模块 ，提供简单的请假流程实例；代码生成模块 ，完成重复的工作。

zscat-platform 提供了常用工具进行封装，包括日志工具、缓存工具、服务器端验证、数据字典、当前组织机构数据 （用户、机构、区域）以及其它常用小工具等。另外还提供一个强大的在线 代码生成 工具。

内置功能
用户管理：用户是系统操作者，该功能主要完成系统用户配置。
机构管理：配置系统组织机构（公司、部门、小组），树结构展现，可随意调整上下级。
区域管理：系统城市区域模型，如：国家、省市、地市、区县的维护。
菜单管理：配置系统菜单，操作权限，按钮权限标识等。
角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
字典管理：对系统中经常使用的一些较为固定的数据进行维护，如：是否、男女、类别、级别等。
操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询。
连接池监视：监视当期系统数据库连接池状态，可进行分析SQL找出系统性能瓶颈。
工作流引擎：实现业务工单流转、在线流程设计器。
技术选型
1、后端

服务治理 dubbo
核心框架：Spring Boot
安全框架：Apache Shiro
模板引擎：Thymeleaf
持久层框架：MyBatis
数据库连接池：Alibaba Druid
缓存框架：Ehcache 、Redis
日志管理：SLF4J
工具类：Apache Commons、Jackson
2、前端

JS框架：jQuery
客户端验证：JQuery Validation
富文本在线编辑：summernote
数据表格：bootstrapTable
弹出层：layer
树结构控件：jsTree
4、平台

服务器中间件：SpringBoot内置
数据库支持：目前仅提供MySql数据库的支持，但不限于数据库
开发环境：Java、Eclipse Java EE 、Maven 、Git
安全考虑
开发语言：系统采用Java 语言开发，具有卓越的通用性、高效性、平台移植性和安全性。
分层设计：（数据库层，数据访问层，业务逻辑层，展示层）层次清楚，低耦合，各层必须通过接口才能接入并进行参数校验（如：在展示层不可直接操作数据库），保证数据操作的安全。
双重验证：用户表单提交双验证：包括服务器端验证及客户端验证，防止用户通过浏览器恶意修改（如不可写文本域、隐藏变量篡改、上传非法文件等），跳过客户端验证操作数据库。
安全编码：用户表单提交所有数据，在服务器端都进行安全编码，防止用户提交非法脚本及SQL注入获取敏感数据等，确保数据安全。
密码加密：登录用户密码进行SHA1散列加密，此加密方法是不可逆的。保证密文泄露后的安全问题。
强制访问：系统对所有管理端链接都进行用户身份权限验证，防止用户直接填写url进行访问。

#### 软件架构
软件架构说明



- ├── mall-common -- 工具类，通用类
- ├── zscat-label --【22181  9090】 blog dubbo服务 单独的zscat-label数据库
-     |-- label-api     label接口
-     |-- label-service label服务
- ├── zscat-blog-- 【22182  9093】 blog dubbo服务 单独的zscat-blog数据库
-     |-- blog-api      blog接口
-     |-- blog-service  blog服务
- ├── zscat-shop--  blog dubbo服务 单独的zscat-shop数据库
-     |-- goods-api      商品接口
-     |-- goods-service 【22183  9096】 商品服务
-     |-- user-api      会员接口
-     |-- user-service 【22184  9097】 会员服务
-     |-- order-api      订单接口
-     |-- order-service 【22185  9098】 订单服务
- ├── zscat-manager-vue-- 【9099】后台管理系统 zscat-platform数据库 springboot shiro,springsession
- ├── zscat-manager-vue-session-- 【9092】后台管理系统 zscat-platform数据库 springboot shiro,springsession
- ├── zscat-web 【9094】-- 前端项目
- ├── zscat-admin-- 【9091】 后台管理系统  springboot vue



#### 安装教程


### 注意

模型类无get,set方法，采用lombok注解方式实现。 需要安装插件

- 1. 创建数据库zscat-label，zscat-blog，zscat-platform
- 2.启动zookeep
- 3.启动label-service，运行springboot的主类
- 4.启动blog-service，运动springboot的主类
- 4.启动user-service，运动springboot的主类
- 4.启动goods-service，运动springboot的主类
- 4.启动order-service，运动springboot的主类
- 5.启动zscat-manager-vue，运动springboot的主类，http://localhost:9099/index  账号 zhuan 123456
- 5.启动zscat-manager-vue-springsession，运动springboot的主类，http://localhost:9092/index  账号 zhuan 123456

- 6.启动zscat-web，运动springboot的主类     http://localhost:9094/blog/index http://localhost:9094/diaShop/index
- 7.启动zscat-admin，运动springboot的主类 http://localhost:9091/index  账号 admin 123456
- 

#### 使用说明


- blog   22182  9093
- label  22181 9090
- admin 9091
- web   9094 
- manager 9095
- manager-vue 9099
- goods  22183 9096
- user  22184 9097
- order  22185 9098
- boot-monitor 8080

 **zscat-manager-vue 演示
** ![输入图片说明](https://images.gitee.com/uploads/images/2018/1222/224203_0aa544e6_134431.jpeg "登陆.jpg")
![输入图片说明](https://images.gitee.com/uploads/images/2018/1222/224218_7ccc01f0_134431.png "商品列表.png")


**springsession接入和演示**
**演示**
- 1.启动zookeep
- 2.启动user-service，运动springboot的主类
- 3.启动goods-service，运动springboot的主类
- 4.启动order-service，运动springboot的主类
- 5.启动zscat-manager-vue，运动springboot的主类，http://localhost:9099/index  账号 zhuan 123456
- 6.启动zscat-manager-vue-springsession，运动springboot的主类，http://localhost:9092/index 

登陆zscat-manager-vue ，运行zscat-manager-vue-springsession的时候不需要登陆

**接入**
添加 com.zscat.platform.config.WebConfig;
添加配置 cookie.domian.name=localhost

#### 参与贡献

1. Fork 本项目
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request

### 微信交流群
![输入图片说明](https://images.gitee.com/uploads/images/2018/1202/213120_80b3755c_134431.jpeg "platform.jpg")
### 群主微信
![输入图片说明](https://images.gitee.com/uploads/images/2018/1202/213146_d8dcc5b2_134431.jpeg "zscat.jpg")

###  请作者喝杯咖啡

![输入图片说明](https://git.oschina.net/uploads/images/2017/0829/203712_6694b4c1_134431.jpeg "weixin.jpg")
![输入图片说明](https://git.oschina.net/uploads/images/2017/0829/203723_5567bd56_134431.jpeg "alipay.jpg")
###  **- - 关注公众号 ，有更多的资料** 
![输入图片说明](https://images.gitee.com/uploads/images/2018/0923/103015_3df65a8a_134431.jpeg "qrcode_for_gh_ad5fa85786aa_344.jpg")
 **_

### 
待做功能
_** 

![输入图片说明](https://images.gitee.com/uploads/images/2019/0120/093834_eef64601_134431.png "屏幕截图.png")