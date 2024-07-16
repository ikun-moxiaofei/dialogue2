# javascript高级

## 一Proxy使用

### defineProperty(ES5)

* 监听对象的操作

```
Object.defineProperty(obj,"name",{
set:function(newValue){


},

get:function(){


}

})
```

* 缺陷：

  ```
  1.defineProperty初衷不是监听一个对象的属性
  2.只能监听设置和获取过程，但是新增属性，删除属性，它无能为力
  ```





### Proxy(ES6)

1.代理proxy

2.步骤

```
//1.创建代理
  const objProxy = new Proxy(obj,{
        get: function (target,key) {
            console.log(`获取到${key}`);
            return target[key]
        },
        set: function (target,key,value) {
            console.log(`监听到新的${key}值`,value);
            target[key] = value;
        }
    })
```

![image-20240227202848472](D:\.typora图片\image-20240227202848472.png)

3.一共有十三个捕获器

![image-20240227203858921](D:\.typora图片\image-20240227203858921.png)

### Reflect

1.对对象本身来进行操作和管理

2.为何出现？

![image-20240227211805883](D:\.typora图片\image-20240227211805883.png)

3.常见的方法（13个）





4.好处

* 返回一个布尔值，判断是否操作成功
* 代理对象，不再操作原对象
* receiver就是外面的proxy（特殊场景），Reflect最后一个参数，可以决定对象访问器this.指向



## 二promise的使用

1.创建

```
const a = new Promise()
```



2.使用

```
a.then(value=>{

}).then(err=>{

})
```



3.好处

```
1.解决异步请求代码
2.Promise是一个类，需要的时候进行回调
```



4.常用例子

```

      	 function exe(counter) {
            const promise= new Promise((resolve,reject) => {
                setTimeout(() => {
                    if(counter > 0){
                    let total = 0;
                    for (let i = 0; i < 100; i++) {
                        total += i;
                    }
                    resolve(total)
                    }else{
                    reject(`${counter}有问题`)
                    }
            },3000)})
            return promise
        }
       
        exe(-100).then(value=>{
            console.log(value);
        }).catch(err=>{
            console.log(err);
        })
```





### 状态区分

1.三种状态

* 待定

  创建后为待定状态

* 完成

  执行resolve()时执行

* 拒绝

​		执行reject()时执行

2.状态改变后，就不会更改，也不能执行函数来改变状态



### resolve参数

1.resolve(promise)

```
resolve(new Promise())
```

如果传入一个promise，那么当前的Promise状态会由传入的Promise来决定



2.resolve(thenable)

```
resolve({
	name:"kobe",
	then : function(reslove){
	resolve(1111)
	}
})

由111决定
```



3.then 和 catch

```

```





### 链式调用

1.顾名思义

```
promise.then().then()
第二个then的this是第一个的return 
```

2.示例

```
 promise.then(res => {
        console.log("第一个then方法",res);
        return "bbbbbb"
    }).then((res)=>{
        console.log("第二个then方法", res); //打印出bbbbbb
        
    }).catch(err =>{

    })
```

3.catch调用

```
promise.then(res=>{
	throw new Error("异常");   //这行之后还会执行吗，会的，只会执行catch返回
}).catch(err=>{
	console.log(err)
})
```

4.yield

```
暂停 暂时性中断
```



### finally方法

1.无论怎么样都会执行的代码

```
promise.then(res=>{
	throw new Error("异常");   //这行之后还会执行吗，会的，只会执行catch返回
}).catch(err=>{
	console.log(err)
}).finally(()=>{
	console.log("都会执行")
})
```







### resolve方法

1.多用于有一个现成的内容，但是希望通过转换为Promise来使用

2.

```
Promise.resolve(arr).then(res=>{
	

})

相当于什么呢
new Promise((resolve,_)=>{
	resolve(arr)
})
```



### reject方法

1.想直接返回，更加简洁了，同上resolve方法



### all方法

1.![image-20240302180336029](D:/.typora图片/image-20240302180336029.png)

2.状态决定

当所有的promise实例resolve时，返回一个数组结果

如果中途有一个reject则会立即返回reject结果，而不会返回resolve的数组

3.用处

一次性发送多个网络请求！





### allSettled方法

1.会在所有的promise实例都要结果时才会返回

2.返回结果为数组，数组里面不单单只是结果，而是对象，可以做到所有实例都有结果





### race方法

1.返回最快实例方法的结果

2.特点：会等到一个promise有结果，无论结果是什么（reject 或者 fulfilled）



### any方法

1.只有最快的a正确结果才会返回，不使用失败结果

2.若全部失败结果，返回一个错误



## 三异步处理

### 异步 函数

1.异步函数默认情况也是同步执行的

异步函数返回值是一个promise，可以执行then,catch

异步函数的返回值会被Promise.resolve()包裹

2.如果异步函数抛出异常，会自己抛出，而不会是浏览器抛出的

### await关键字

await会返回一个promise，等到promise函数fufilled时才会执行异步函数，相当于代码阻塞



### 进程和线程

1.进程：计算机已经运行的程序

2.线程：操作系统能够运行的调度的最小单位，通常被包含于进程中--------------------------------------------------------------



### 微任务和宏任务

1.宏任务macrotask

宏任务队列

```
计时器(setTimeout) setInterval
ajax
dom监听
UI Rendering
window.requestAnimationFrame
```

2.微任务microtask

微任务队列

```
promise的回调then
Mutation Observer API
queueMicrotask
```

3.顺序：先完成微任务队列，再完成宏任务队列

4.await代码顺序

```jsx
async function async1(){
	console.log("async1 start")
	await async2()
	console.log("async1 end")
}
async function async2(){
	console.log("async2")
}
```

首先来说,异步函数和函数一样，会先放到队列里边执行，await后的代码会先阻塞，但当调用async时，会返回一个then，因此await之后的代码会放到微任务里面







### 抛出异常

1.抛出异常的几种方式：

```
throw "hhhhh"
throw {error:"错误信息"}
throw new Error("我是错误信息")   //会给函数位置
```

![image-20240306201456918](D:/.typora图片/image-20240306201456918.png)







## 四Storage 

### localStorage 和  sessionStorage

1.localStorage:本地存储，永久性存储

 sessionStorage:会话存储，提供的本次对话存储

2.二者区别

![image-20240306220638780](D:/.typora图片/image-20240306220638780.png)

页面内 _self

页面外 _blank

3.常见的属性和方法

* localStorage.length
* localStorage.removeItem()
* localStorage.clear()
* localStorage.key(0)



4.封装

storage本身不能存储对象类型，除非你这样

```
let obj = {}
JSON.stringify(obj)
```

因此封装的好处来了

可以在封装函数内进行这种操作 ，扩展很多功能

![image-20240307093238630](D:/.typora图片/image-20240307093238630.png)





## 五this

### this的指向

```js
//
function foo(){
    
}

vat obj = {aaa:"why"}
obj.aaa = foo
obj.aaa()


```



this跟绑定位置无关，在运行时绑定this，跟调用方式有关



### 绑定规则

1.默认绑定

* 独立函数调用时，指向window
* 间接函数引用时，指向window

* 严格模式时，指向undefined







2.隐式绑定

如果函数调用时，前面存在调用它的对象，就会将this绑定至这个对象

```js
function fn() {
    console.log(this.name);
};
let obj = {
    name: '听风是风',
    func: fn
};
obj.func() //听风是风
```







3.new绑定

```js 
function foo(){
      console.log(this)
    
      this.name = 'why'
 }

//此时的this指向的为这个foo函数对象
new foo()

//
1.创建新的空对象
2.将this指向这个空对象
3.执行函数体中的代码
4.没有显示返回非空对象时，默认返回这个对象
//
```



4.显示绑定

语法

```js
     function foo(){
        console.log(this.name);
     }
     let obj = {
        name:"why"
     }
	//调用foo函数，并强制使其this指向
     foo.call(obj)
	 foo.apply(obj)
```



call()与apply()的区别

首先，两者都是调用函数的方式

传参方法不一样

```js
//数组形式
foo.apply("apply",["kobe,30,1.98"])
//多参数形式
foo.call("call","james",25,2.05)
```





另外一个函数bind()

```js
 function foo(name,age){
 	console.log(this)
 }
 
 var obj = {name:"why"}
 var bar = foo.bind(obj,"why",18)
 bar()
```





### 内置函数的思考

内置函数的this指向需要经验判断

```js
names.forEach(()=>{
    console.log("foeEach",this);
   },"aaaa")
 forEach第二个参数为this绑定
```





### 优先级

bind/apply绑定>隐式绑定>默认绑定

new绑定>隐式绑定>默认绑定

new绑定不能和call,apply一块使用。可以和bind一块使用,new绑定优先级更高



new bind apply/call



### 箭头函数的this

只有一行代码时，这行代码会作为返回值默认返回



### this查找规则

对象不是作用域，代码块会形成作用域

若没找见的话，在外面层层寻找

```js
  var obj = {
        name:"obj",
        foo:()=>{
            var bar = () => {
                console.log("bar",this);
            }
            return bar
        }
    }

    var fn = obj.foo()
    fn.apply("bbb")
```



### 使用实例

之前的话this使用，你要在外层传递

```js
_this = this
```



箭头函数用法

```js
fucntion request(res,callback){
	var results = [abb,dfasdf]
    callback(results)


}



var obj = {
	names:{ },
	network:function(){
		request("/names",(res)=>{
            this.names = [].concat(res)
        })
	}
}
```



 

## 六浏览器原理

### 解析过程

![image-20240326163517781](D:/.typora图片/image-20240326163517781.png)

解析一：HtmL解析

生成DOM树



解析二：生成CSS规则

遇到link元素下载css文件，单独线程解析CSS，不影响DOM解析

上成CSSOM树



解析三：构建Render Tree

Render Tree 需要DOM Tree和 CSSOM Tree



解析四：布局和绘制

布局是呈现所有结点的高度，宽度和位置信息



解析五：绘制

将可见部分进行绘制，例如文本颜色边框阴影替换元素



### 回流和重绘

回流：结点大小重新计算，引发Layout

例如：

* 改变了布局（width,height)
* 改变了size
* DOM结构发生改变（）

* getComputedStyle方法获取尺寸位置信息



重绘：

* 修改边框颜色，样式





### 合成图层

标准流中的内容都是被绘制在一个图层中的(render layer)

每个合成层是单独渲染的

一些特殊的属性，可以启动新的合成层启动GPU加速



```
可以生成新的图层的属性
3D transforms
video canvas iframe
opacity 动画转换
position : fixed
animation transition 设置了opacity transform

```



分层可以提高性能，但是以内存管理为代价的



### script元素的处理

遇到js代码时，就会停止解析，等待js代码的下载，因为js代码会进行dom的操作，如果你这样处理的话，会造成严重的回流和重绘。

但是随着Vue,React的兴起，js代码体积变大 





defer 

* defer执行下载时不会阻塞DOM Tree的构建过程

* 如果脚本提前下载好，会在先执行defer中的代码

* 多个defer保证顺序，推荐放到head元素中提前下载

```js
<script src="./dsf.css" defer>
    </script>
```





async

* async脚本不能保证顺序
* async不会保证在DOMContentLoaded事件之前或者之后执行





### v8引擎暂时先逃过（js执行原理）







## 七js执行原理

### 作用域和作用域链

AO VO

作用域的提升

先去AO里面找，找不见的话沿着作用域链去找

```
函数作用域链是在定义的位置确定的
```





## 八js内存管理和闭包

### js的内存管理

* 内存管理会有如下生命周期：

   1.分配申请你需要的内存

   2.使用分配的内存

   3.不需要时，释放内存

* 垃圾回收机制（Garbage Collection  GC）

 回收机制一：引用计数

```js
会在内存中reeainCount进行引用计数
当为0时就释放

弊端是产生循坏引用
such as:
obj1 = {}
obj2 = {}
obj1.info = obj2
obj2.info = obj1
这样的话不手动删除，两个对象无法销毁



```

 回收机制二：标记清除

 可达性

 根对象,垃圾回收器定期从这个根开始，找到所有从根开始有引用的对象

 



其他回收机制

1.标记整理

回收期间同时将保留的对象搬运到汇集的连续内存空间，从而整合空闲空间，避免内存碎片化

2.分代收集

将对象分为两组，新的和旧的

3.增量收集

将垃圾工作分成几部分来做，然后将这些部分逐一进行处理

4.闲时收集

cpu空闲时运行垃圾回收



### 闭包(closure)

 闭包的理解：

1.闭包就是一个函数和它周围状态（词法环境）的引用捆绑在一起

2.也就是说，闭包让你可以在内层函数中访问到外层函数的作用域

3.在js中，每当创建一个函数，闭包就会在函数创建的同时被创建出来



函数柯里化：

是将函数从调用方式：`f(a,b,c)`变换成调用方式：`f(a)(b)(c)`的过程。柯里化不会调用函数，它只是对函数进行转换。



















## 九防抖和节流

### 防抖

```js
    const input = document.querySelector("input")

        function xjbDebounce(fn,delay){
            let timer = null
            const _debounce = () =>{
                if(timer)clearTimeout(timer)

                timer = setTimeout(()=>{
                    fn()
                },delay)
            }

            return _debounce
        }

        input.oninput = xjbDebounce(function(){
            console.log("发送网路请求！")
        },1000)
```













## 十函数增强

### 属性参数：

常用的就只有两个

```js
//参数的个数，需要注意的是length的个数是未赋值参数的个数
foo.length  


//函数的名字
foo.name


function demo(...arg){
    
}
```





### arguments

类数组对象(array-like)

箭头函数里面没有arguments



是一个可迭代对象

```js
function foo(){
	console.log(arguments)
    console.log(arguments.length)
}
```



arguments转Array

```js
方法一                    
            let arr = []
             for(let arg of arguments){
                arr.push(arg)
             }

 方法三   ES6
             let arr = Array.from(arguments)
             console.log(arr);

            let arr = [...arguments]
            
 方法二  slice转
 var n = [].slice.apply(arguments)
       

```





### 函数的剩余参数(rest)

是ES6新增的，希望替代arguments

剩余参数必须写到最后

rest是一个真正的数组

```js
function foo(a,b,...rest){
    console.log(rest);
}

foo(20,43,43,34,34)
```





### 纯函数

* 相同输入，相同输出
* 不能产生副作用（不改变外部变量等）



### 柯里化函数

更加方便的函数复用

```js
  function foo(x,y,z){
            return x + y + z
        }

        function hy(fn){
            function cuurryFn(...args){
                //参数够了的话直接
                //否则进行第二类操作
                if(args.length >= fn.length){
                      fn(...args)
                }else{
                    return function(...newArgs){
                        return cuurryFn(...args.concat(newArgs))
                    }
                }
            }
            return cuurryFn
        }

        var fooed = hy(foo)
        fooed(10)(20)(30)
```





### 自动柯里化函数







## 十一迭代器(iterable)

### 定义

![image-20240523135359766](D:/.typora图片/image-20240523135359766.png)

### 将对象变为可迭代的

```js
    const infos = {
            friends:["kobe","james","curry"],
            [Symbol.iterator](){
                let index = 0
                const infosItreable = {
                    next:()=>{
                        if(index < this.friends.length){
                       return 				 	{done:false,value:this.friends[index++]}
                          
                   			}else{
                            return {done:true}
                        }
                    }
                }
                return infosItreable
            }
     }
```

可以使用for of 遍历可迭代对象。

如果想要遍历对象的属性可以这样：

```js
  const fo = {
            name:"fds",
            age:16,
            [Symbol.iterator](){
                const entries = Object.entries(this)
                let index = 0
                const iterator = {
                    next:()=>{
                        if(index < entries.length){
                            return {done:false,value:entries[index++]}
                        }else{
                            return {done:true}
                        }
                    }
                }
                return iterator
            }
        }

        for(let item of fo){
            const [key,value] = item
            console.log(key,value);
        }
```



### 可迭代对象

实现itreable protocol协议的对象就是可迭代对象

@@iterable方法

原生的有

* 数组
* Set
* arguments对象
* Map
* NodeList
* String



### 可迭代对象的应用

1.for of

2...运算符

3.yield*

4.解构赋值



### 自定义类的迭代



```js
cript>
        class Person{
            constructor(name,age,height,friends){
                this.name = name
                this.age = age
                this.height = height
                this.friends = friends
            }

            [Symbol.iterator](){
                let index = 0
                const iterator = {
                    next:()=>{
                        if(index < this.friends.length){
                            return { done: false, value: this.friends[index++] }
                        }else{
                            return {done:true}
                        }
                        
                    }
                }
                return iterator
            }
        }
```



### 中断迭代

```js
break return throw

    [Symbol.iterator](){
                let index = 0
                const iterator = {
                    next:()=>{
                        if(index < this.friends.length){
                            return { done: false, value: this.friends[index++] }
                        }else{
                            return {done:true}
                        }
                        
                    },
                    //中断
                    return: () =>{
                           return { done: true }
                    }
                }
                return iterator
            }

```





## 十二生成器

generator

![image-20240523150713052](D:/.typora图片/image-20240523150713052.png)



```js
   function* foo(){
            console.log("111");
            yield "Fdsaf"
            console.log("123");
            yield "fdsaf"
        }
        const generator = foo()
        console.log(generator.next());
         console.log(generator.next());
```





### 传参接收

```js
function* foo(nameffff){
          console.log("111",nameffff);
          const name = yield "Fdsaf"
          console.log("123",name);
          yield "fdsaf"
 }
 const generator = foo(namefff)  //第一次传参在这里
 console.log(generator.next());
 console.log(generator.next("第二次"));
```



### 结束生成器（）

```js
generator.return()


```





### 生成器替代迭代器

考虑如何做？

```js
  const nums = ["abv","fadsf","fads"]

        function* createArray(arr){
           for(let i = 0;i < arr.length;i++){
                yield arr[i]
           }
           //yield* arr 同上作用一般，语法糖写法
        }

        const Iterator = createArray(nums)
        console.log(Iterator.next());

以此可以避免复杂的迭代器写法
```



### yield*

后面跟随可迭代对象，进行挨个的迭代，语法糖写法





### yield替换类的实现

```js
 class Person{
            constructor(name,age,height,friends){
                this.name = name
                this.age = age
                this.height = height
                this.friends = friends
            }
			*[Symbol.iterator](){
				yield* this.friends
			}
        
}
```









## 十三浅拷贝与深拷贝

![image-20240527112938470](D:/.typora图片/image-20240527112938470.png)





### 浅拷贝

```js
1.let obj2 = {...obj1}  

2.let obj2 = Object.assign({},obj1)

3.let obj2 = _.clone(obj1)  //loadsh函数库方法

4.let arr2 = arr1.concat()

5.let arr2 = arr1.slice()
```







### 深拷贝

```js
1.JSON.parse(JSON.stringfy())
不能处理函数和正则，因为这两者基于JSON.stringify和JSON.parse处理后，得到的正则就不再是正则（变为空对象），得到的函数就不再是函数（变为null）了


2._.cloneDeep()
let obj2 = _.cloneDeep(obj1)

```



### 手写浅拷贝

```js
  function shallowClone(obj){
            let newObj = {}
            for(let i in obj){
                if(obj.hasOwnProperty(i)){
                    newObj[i] = obj[i]
                }
            }
            return newObj
        }
```





### 手写深拷贝

```js
  function deepClone(obj,hash = new WeakMap()){
            if(obj === null) return obj
            if(obj instanceof Date) return new Date(obj)
            if(obj instanceof RegExp) return new RegExp(obj)
            if(typeof obj !== "Object") return obj
            
            if(hash.get(obj)) return hash.get(obj)
            const newObj = new obj.constructor()
            hash.set(obj,newObj)
            for(let key of obj){
                newObj[key] = deepClone(obj[key],hash)
            }
            return newObj
 }
```







## 十四原型

### 获取原型

对象的原型获取方式（隐式）

1.obj.  __proto   _ _    

2.Object.getPrototypeOf(obj)



函数的原型（显示）

1.只有函数具有显式原型(prototype)

2.







### 显式原型

1.__proto _ _ 隐式原型

2.prototype 显示原型



### new操作符

步骤：

1.创建一个空对象

2.将这个对象赋值给this

3.将函数的显示原型赋值给对象的隐式原型

4.执行函数体内的代码

5.将这个对象返回





### 构造函数原型方法

```js
function Student(name,age){
            this.name = name
            this.age = age

        }
        Student.prototype.running = function(){
            console.log(this.name + ' running');
        }
        var stu1 = new Student("kobe",38)
        stu1.running()
```

将方法放到原型上，节省空间







### constructor

显示原型上的重要属性

Person = Person.prototype.constructor





### 内存表现

![image-20240529091055031](D:/.typora图片/image-20240529091055031.png)









### 自定义新原型对象

```js
>
            function Person() {

            }
            Person.prototype = {
                messaege:"Hello",
                running:function(){},
                eating:function(){},
                info:{name:"hhahaha"},
                constructor:Person
            }
            Object.defineProperty(Person.prototype,"constructor",{
                value:Person
            })
```



### 函数对象的原型与函数的关系



## 十五解构

数组的解构

1.基本使用

```js
 var names = ["abv","fsd"]
 var [name1,name2] = names
```

2.严格按照顺序解构





对象的解构

1.按照key值解构



2.对变量重命名

```js
var {height:w} = obj

```





应用

```js
//函数的参数直接解构
function get({x,y}){
        console.log(x);
        console.log(y);
 }
 get({x:10,y:30})
```





## 十六手写apply,call

函数的function.prototype上定义

apply 接收数组为第二个参数

```js
  function foo(name,age){
            console.log(this,name,age);
        }
      
        Function.prototype.hyapply = function(thisArg,otherArgs){
            thisArg = (thisArg === null || thisArg === undefined)? window : Object(thisArg)
            //给对象添加属性，将函数foo赋值给fn函数
            thisArg.fn = this
            thisArg.fn(...otherArgs)
            //删除这个函数
            delete thisArg.fn
        }
        let obj = {
            name:"fdf"
        }
        
        foo.hyapply(obj,["why",18])




		
```

call  接收参数个数

```js
   Function.prototype.hycall = function(thisArg,...otherArgs){
            thisArg =(thisArg == undefined || thisArg == null) ? window : Object(thisArg)
            thisArg.fn = this
            thisArg.fn(...otherArgs)
        }


        foo.hycall(obj,"why","18")
```





### 手写bind

```js
   function foo(name,age,height){
            console.log(this,name,age,height);
        }


       Function.prototype.hybind = function(thisArg,...otherArgs){
            thisArg = (thisArg == null || thisArg == undefined) ? window : Object(thisArg)
            thisArg.fn = this
            return (...newArgs)=>{
                var allArgs = [...otherArgs,...newArgs] 
                thisArg.fn(...allArgs)
            }
       }

       var newFoo = foo.hybind({name:"why"},18,"fd")
       newFoo(120)
```





## promise面试题

### 类型一：执行顺序

![image-20240321185549920](D:/.typora图片/image-20240321185549920.png)



总结：不要往微任务里边无限添加，否则宏任务会无限延迟

```js
   function requestData(url){
        return new Promise((resolve)=>{
            setTimeout(()=>{
                resolve(url)
            },2000)
        })
      }

      function getData(){
        console.log("getData start ");
        requestData("why".thne(res=>{
            console.log("then-res",res);
        }))
        console.log("getData end");
      }
        
      getData()
      console.log("script end");
```





async函数

```js
  function requestData(url){
        console.log("requestdata");
        return new Promise((resolve)=>{
            setTimeout(()=>{
                resolve(url)
            },2000)
        })
      }

      async function getData(){
        console.log("getData start ")
        const res = await requestData("why")
        console.log("thne1-res",res)
        console.log("getData end")
      }
      getData()
      console.log("script end");
```















## this面试题

```





```













