# TS学习

指明类型是最关键的

### 1.基本类型

number

string

boolean

any

tuple

object

void

never

unknown 不能直接付赋值给其他变量



```
let a : number
```

* 也可以直接判断

```
let a = false //会直接规定为boolean类型
```

* 多用于函数参数规定，而js函数不考虑函数的类型

```
function sum(a:number,b:number):number{
	return a + b;
}
```

如果不指定类型，则默认为any类型



* object

```tsx
let b = {
	name : string
    age: number
    grade: number
    work?:number
    
}

let d(a:number,b:number) =>number;
任意属性的书写：
[propName : string] : any
```

* array

```
let e:string[]; //字符串数组
let f:number[]; //数字数组
let g :Array<number>; 
```



* tuple 

```
固定长度的数组
let h = [string,number];

```















### 2.更多规定

或者其一

```
let c: boolean | string;

let a: "male" | "female"


```

unknown赋值

```
e : unknown;
e = "hh"

if (typeof e === "string"){
	s = e;
}
```

类型断言：

```
s = e as string;
s = <string> e
```









### 3.编译配置

1.tsconfig.json

```
 "include":{
	 "./src/**/*"    **任意目录 *任意文件
  }
 ""
 
 

```



### 4面向对象

泛型：

不用any，可以跳过类型检查

```
function fn<T>(a:T){
	return 10;
}

function fn2<T,K>(a:T,b:K):T{
	console.log(b);
	return a;
}

fn2(123,"hello");
```





5

