---
重大更新：   \
1 去除用户表和用户组表，相应的管理移交Spring Security管理 ， 特别强调DemoApplicationConfiguration.java类中用户创建过程，相应的前缀不能修改，这是工作流代码里面强耦合写死的东西！ \
2 每一个方法，都要执行 securityUtil.logInAs(username);    \


---
3 这是给有基础的人看的，我不想写太多的东西了，代码不多。\
4 运行步骤，将项目根目录中的文件“新版工作流演示.postman_collection.json”导入postman中，按照前缀的顺序执行即可。\
5 可能部分的任务实例ID值，需要去数据库查询哈！或者根据之前的接口请求返回值获取，这就是懂得人自然懂，懒得写了。