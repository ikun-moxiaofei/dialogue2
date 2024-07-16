# Mysql学习(自2023.12.28)

## 一Mysql基本安装与启动

1.启动两种方式：

* 终端输入services.msc  点击启动和停止

* 输入(以管理员身份运行)

```
启动： net start mysql80
停止: net stop mysql80

```



2.连接mysql的两种方式:

* 找到mysql 8.0 command line client

* 命令行输入

  ```
  mysql [-h 127.0.0.1][-p 3306] -u root -p
  ```

3.

关系型数据库

数据模型

连接DMBS -> 创建数据库 ->





## 二基础操作

1.通用语法：

SQL分类：

![image-20231228130453272](D:\.typora图片\image-20231228130453272.png)



### DDL

1.操作

![image-20231228130740311](D:\.typora图片\image-20231228130740311.png)



2.操作表结构

* 表的基本操作

![image-20231228131758121](D:/.typora图片/image-20231228131758121.png)

* 表的创建

​	![image-20231228132103683](D:/.typora图片/image-20231228132103683.png)

* 数据类型







3.修改表结构：

* add 添加字段：

```mysql
alter table 表名 add 字段名 类型 [comment 注释]
```

* modify 修改字段:

```mysql
修改数据类型
alter table 表名 modify 字段名 新数据类型（长度）
修改字段名和字段类型
alter table 表名 change 旧字段名 新字段名 类型（长度） [comment 注释][约束]
```

* drop 删除字段：

```mysql
alter table 表名 drop 字段名;
```

* 修改表名:

```mysql
alter table 表名 rename to 新表名;
```

* 删除表

```
drop table 表名;
truncate table 表名; 删除指定表并且重新创建
```





### DML

添加：insert

```mysql
insert into 表名 (字段1，字段2) values(值1，值2)
插入一条数据
insert into 表名 values(值1，值2)
插入多条数据
insert into 表名 values(值1，值2)(值1，值2)(值1，值2)
```

修改:  update

```
update 表名 set 字段1=值1 字段2=值2 [where 条件]
```

删除：delete

```
delete from 表名 [where 条件]
```



### DQL



查询 select

```
select 字段列表
from 表名列表
where 条件列表
group by 分组字段列表
having 分组后条件列表
orderby 排序后查询
limt 分页查询
```

* 基本查询

1.查询多个字段

```
查询指定
select 字段1，字段2，字段3 ... from 表名
查询全部
select * from 表名

```

2.设置别名

```
select 字段1 [as 别名1] 字段2 [as 别名2] from 表名
```

3.去除重复记录

```
select distinct 字段列表 [as 别名1] from 别名
```

* 条件查询

```
select * from where age = 18;
```

条件有哪些？![image-20231228203813876](D:/.typora图片/image-20231228203813876.png)

模糊匹配

_ 匹配的时几位数字

```
select * from emp where idcard like '_ _ _ _ _ _ _ _ x';
```





* 聚合函数

​	常见的聚合函数：

​	count 数量

​	max 最大值

​	min 最小值

​	avg 平均值

​	sum 求和



```
 select 聚合函数（字段列表） from 表名；
```

注意：所有的null值不参与聚合函数的运算





* 分组查询

  groupby

```mysql
select 字段列表 from 表名 [where 条件] group by 分组字段名 [having 分组后过滤条件]
```

![image-20231228205225837](D:/.typora图片/image-20231228205225837.png)



* 排序查询

```mysql
select 字段列表 from 表名 order by 字段1 排序方式1，字段2 排序方式2；
```

​		asc （升序)

​		desc (降序)

* 分页查询

​		limit

```
select 字段列表 from 表名 limit 起始索引，查询记录数;
```







DQL语句执行顺序：

![image-20231229164707650](D:/.typora图片/image-20231229164707650.png)



### DCL

控制数据库的访问权限

* 用户管理

![image-20231229165557539](D:/.typora图片/image-20231229165557539.png)

* 权限控制



​	  常用权限

​	![image-20231229170722431](D:/.typora图片/image-20231229170722431.png)





![image-20231229171115745](D:/.typora图片/image-20231229171115745.png)



## 函数

1.常见内置函数

* 字符串函数

  ![image-20231231111303288](D:/.typora图片/image-20231231111303288.png)

​		

```mysql
	update 表名 set 属性 = lpad(workno,5,'0')
```



* 数值函数

  ![image-20231231113326691](D:/.typora图片/image-20231231113326691.png)



* 日期函数

  ![image-20231231113920692](D:/.typora图片/image-20231231113920692.png)

​			



​			



* 流程控制函数

  ![image-20231231114902843](D:/.typora图片/image-20231231114902843.png)
