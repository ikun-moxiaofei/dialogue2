# uniapp小程序开发

2017.1微信小程序横空出世，打开了小程序的大门

* 大厂竞争格局你有我也得有
* 使用更加方便
* 小程序可以为App添加新的功能需求

微信小程序：

WXML,WXSS,JavaScript

框架：uniapp/taro



## 一初始化预备

uniapp跨平台

1.选择文件->新建->项目

2.Vue-Cli 

```
npm install -g @vue/cli@4
```

3.手动打开微信开发者工具调试

```
找到项目unpackage/dist/dev/mp-weixin打开即可
```









## 二uniapp语法学习

### 目录框架

1.main.js

作用：

```
定义全局组件
初始化vue实例
定义全局属性
安装插件：如pinia、vuex等等
```

2.App.vue

作用：应用的生命周期，编写全局样式，定义全局数据globalData

应用的生命周期仅能在a

全局数据定义

* App.vue里面导入全局样式，作用每一个界面

* 或者uni.scss可以应用于每一个界面，没有scoped

3.page.json

配置各种全局的东西，例如tabBar,globalStyle,pages



### 常用内置组件

1.view

2.text

3.button 

```
type 

```

4.image

默认高度320px,宽度240px 

```
mode
```

5.scrollview

```
使用shuxian
```

6.swiper轮播图

```
 
```





### 扩展组件

1.uni-forms

表单组件

```
问：如何自定义表单组件的属性
答：
:deep(.className){}     //修改扩展组件
：global(.className){}   //全局修改

```









### 生命周期

* onLoad

加载时触发



* unonload

挂载时触发







### API

路由API

* 打开新页面

  ```
  uni.navigateTo({})
  ```

* tab切换

  ```
  uni.switchTab({})
  ```

  



### 页面通讯

1.url传递

在跳转页面时直接带上参数

```
uni.navigateTo({
      url:"page/page?name=liu"
})
```



2.eventChannel

```
//传递
uni.navigateTo({
	success(res){
		res.eventChannel.emit("content",data:"fdafadsf")

}
})
```



3.事件总线

```
//触发部分（传递）
uni.$emit("fff",{
data:{
	desc:"这是事件总线的传递"
}
})

//接收部分
uni.$on("content",(value)=>{
	
})
```



## 三注意事项

1.尺寸问题

750*1334 

rpx

2.建议使用option Api,setup有些是不支持的

3.全局样式

```
:global(){
	dispalt:none;
}
```

