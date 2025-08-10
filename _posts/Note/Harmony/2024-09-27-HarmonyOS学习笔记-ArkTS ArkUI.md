---
layout: post
tags: Harmony
---

# HarmonyOS Next

## ArkTS 语言

ArkTS 语言介绍 <https://developer.huawei.com/consumer/cn/doc/harmonyos-guides-V5/introduction-to-arkts-V5>

- 除了基本类型，自定义类，还有 "null"、"undefined"(未定义、未初始化) 

- Union 类型，即联合类型
```
type Animal = Cat | Dog | Frog | number
// 相当于 可 null
let error: Error | null = null;
```

- 可选参数, `name?: string` 或 `coeff: number = 2`，后者跟 Kotlin 类似
```
function hello(name?: string) {
  if (name == undefined) {
    console.log('Hello!');
  } else {
    console.log(`Hello, ${name}!`);
  }
}
```

- 可变参数，`...numbers: number[]`，对比 Java `...numbers: number[]`，对比 Kotlin `vararg numbers: Int`
```
function sum(...numbers: number[]): number {
  let res = 0;
  for (let n of numbers)
    res += n;
  return res;
}

sum() // 返回0
sum(1, 2, 3) // 返回6
```

- 函数类型，跟 Kotlin 类似但又不同
```
type trigFunc = (x: number) => number // 这是一个函数类型
```

- 函数重载（构造函数也可以），跟 Java 类似，但定义函数时有不同
```
function foo(x: number): void;            /* 第一个函数定义 */
function foo(x: string): void;            /* 第二个函数定义 */
function foo(x: number | string): void {  /* 函数实现 */
}

foo(123);     //  OK，使用第一个定义
foo('aa'); // OK，使用第二个定义
```

- 对象字面量
```
class C {
  n: number = 0
  s: string = ''
}

let c: C = {n: 42, s: 'foo'}; // 可以代替new表达式 `new C(42, 'foo')`
```

- Record类型的对象字面量
```
/ 泛型Record<K, V>用于将类型（键类型）的属性映射到另一个类型（值类型）。
// 常用对象字面量来初始化该类型的值
let map: Record<string, number> = {
  'John': 25,
  'Mary': 21,
}

map['John']; // 25
```

- 泛型支持默认值 `class Base <T = SomeType> { }`

- 非空断言 `a!.value`，对比 Kotlin 是 `a!!.value`

- 空值合并运算符 `a ?? b`，对比 Kotlin 是 `a ?: b`

- 导出，可以使用关键字export导出顶层的声明。未导出的声明名称被视为私有名称，只能在声明该名称的模块中使用
```
export class Point {
  x: number = 0
  y: number = 0
  constructor(x: number, y: number) {
    this.x = x;
    this.y = y;
  }
}
export let Origin = new Point(0, 0);
export function Distance(p1: Point, p2: Point): number {
  return Math.sqrt((p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y));
}
```

- 导入，导入声明用于导入从其他模块导出的实体，并在当前模块中提供其绑定
```
// 太多了，看文档吧
```



## ArkUI

MVVM 应用示例 <https://developer.huawei.com/consumer/cn/doc/harmonyos-guides-V5/arkts-mvvm-V5#mvvm%E5%BA%94%E7%94%A8%E7%A4%BA%E4%BE%8B>

### 组件

- 行列与分栏
  
  - Column
    沿垂直方向布局的容器组件。
  
  - ColumnSplit
    垂直方向分隔布局容器组件，将子组件纵向布局，并在每个子组件之间插入一根横向的分割线。
  
  - Row
    沿水平方向布局的容器组件。
  
  - RowSplit
    水平方向分隔布局容器组件，将子组件横向布局，并在每个子组件之间插入一根纵向的分割线。
  
  - SideBarContainer
    提供侧边栏可以显示和隐藏的侧边栏容器组件，通过子组件定义侧边栏和内容区，第一个子组件表示侧边栏，第二个子组件表示内容区。
  
- 堆叠Flex与栅格
  
  - Stack
    堆叠容器组件，子组件按照顺序依次入栈，后一个子组件覆盖前一个子组件。
  
  - Flex
    以弹性方式布局子组件的容器组件。
  
  - GridContainer
    纵向排布栅格布局容器组件，仅在栅格布局场景中使用。
  
  - GridRow
    栅格容器组件，仅可以和栅格子组件(GridCol)在栅格布局场景中使用。
  
  - GridCol
    栅格子组件，必须作为栅格容器组件(GridRow)的子组件使用。
  
  - RelativeContainer
    相对布局组件，用于复杂场景中元素对齐的布局。
  
- 列表与宫格
  
  - List
    列表包含一系列相同宽度的列表项，适合连续、多行呈现同类数据，例如图片和文本。
    
  - ListItem
    用来展示具体列表项，必须配合List来使用。
    
  - ListItemGroup
    用来展示分组列表项的组件，必须配合List组件来使用。
    
  - Grid
    网格容器组件，由“行”和“列”分割的单元格所组成，通过指定“项目”所在的单元格做出各种各样的布局。
    
  - GridItem
    网格容器中单项内容容器。
  
- 滚动与滑动
  
  - Scroll
    可滚动的容器组件，当子组件的布局尺寸超过父组件的尺寸时，内容可以滚动。
    
  - Swiper
    滑块视图容器组件，提供子组件滑动轮播显示的能力。
    
  - WaterFlow
    瀑布流容器组件，由“行”和“列”分割的单元格所组成，通过容器自身的排列规则，将不同大小的“项目”自上而下，如瀑布般紧密布局。
    
  - FlowItem
    瀑布流组件WaterFlow的子组件，用来展示瀑布流具体item。
  
- 导航
  
  - Navigator
    路由容器组件，提供路由跳转能力。
    
  - Navigation
    一般作为Page页面的根容器，通过属性设置来展示页面的标题栏、工具栏、导航栏等。
    
  - NavRouter
    导航组件，默认提供点击响应处理，不需要开发者自定义点击事件逻辑。
    
  - NavDestination
    作为NavRouter组件的子组件，用于显示导航内容区。
    
  - Stepper
    步骤导航器组件，适用于引导用户按照步骤完成任务的导航场景。
    
  - StepperItem
    Stepper组件的子组件。
    
  - Tabs
    通过页签进行内容视图切换的容器组件，每个页签对应一个内容视图。
    
  - TabContent
    仅在Tabs组件中使用，对应一个切换页签的内容视图。
  
- 按钮与选择
  
  - Button
    按钮组件，可快速创建不同样式的按钮。
    
  - Toggle
    组件提供勾选框样式、状态按钮样式及开关样式。
    
  - Checkbox
    提供多选框组件，通常用于某选项的打开或关闭。
    
  - CheckboxGroup
    多选框群组，用于控制多选框全选或者不全选状态。
    
  - DatePicker
    选择日期的滑动选择器组件。
    
  - TextPicker
    滑动选择文本内容的组件。
    
  - TimePicker
    滑动选择时间的组件。
    
  - Radio
    单选框，提供相应的用户交互选择项。
    
  - Rating
    提供在给定范围内选择评分的组件。
    
  - Select
    提供下拉选择菜单，可以让用户在多个选项之间选择。
    
  - Slider
    滑动条组件，通常用于快速调节设置值，如音量调节、亮度调节等应用场景。
    
  - Counter
    计数器组件，提供相应的增加或者减少的计数操作。
  
- 文本与输入
  
  - Text
    显示一段文本的组件。
    
  - Span
    作为Text组件的子组件，用于显示行内文本片段的组件。
    
  - Search
    搜索框组件，适用于浏览器的搜索内容输入框等应用场景。
    
  - TextArea
    多行文本输入框组件，当输入的文本内容超过组件宽度时会自动换行显示。
    
  - TextInput
    单行文本输入框组件。
    
  - PatternLock
    图案密码锁组件，以九宫格图案的方式输入密码，用于密码验证场景。手指在PatternLock组件区域按下时开始进入输入状态，手指离开屏幕时结束输入状态完成密码输入。
    
  - RichText
    富文本组件，解析并显示HTML格式文本。
  
- 图片视频与媒体
  
  - Image
    图片组件，支持本地图片和网络图片的渲染展示。
    
  - ImageAnimator
    提供逐帧播放图片能力的帧动画组件，可以配置需要播放的图片列表，每张图片可以配置时长。
    
  - Video
    用于播放视频文件并控制其播放状态的组件。
    
  - XComponent
    用于EGL/OpenGLES和媒体数据写入。
  
- 信息展示
  
  - DataPanel
    数据面板组件，用于将多个数据占比情况使用占比图进行展示。
    
  - Gauge
    数据量规图表组件，用于将数据展示为环形图表。
    
  - LoadingProgress
    用于显示加载动效的组件。
    
  - Marquee
    跑马灯组件，用于滚动展示一段单行文本，仅当文本内容宽度超过跑马灯组件宽度时滚动。
    
  - Progress
    进度条组件，用于显示内容加载或操作处理等进度。
    
  - QRCode
    用于显示单个二维码的组件。
    
  - TextClock
    通过文本将当前系统时间显示在设备上。支持不同时区的时间显示，最高精度到秒级。
    
  - TextTimer
    通过文本显示计时信息并控制其计时器状态的组件。
  
- 空白与分隔
  
  - Blank
    空白填充组件，在容器主轴方向上，空白填充组件具有自动填充容器空余部分的能力。仅当父组件为Row/Column时生效。
    
  - Divider
    分隔器组件，分隔不同内容块/内容元素。
  
- 画布与图形绘制
  
  - Canvas
    提供画布组件，用于自定义绘制图形。
    
  - Circle
    用于绘制圆形的组件。
    
  - Ellipse
    椭圆绘制组件。
    
  - Line
    直线绘制组件。
    
  - Polyline
    折线绘制组件。
    
  - Polygon
    多边形绘制组件。
    
  - Path
    路径绘制组件，根据绘制路径生成封闭的自定义形状。
    
  - Rect
    矩形绘制组件。
    
  - Shape
    作为绘制组件的父组件，实现类似SVG的效果，父组件中会描述所有绘制组件均支持的通用属性。
  
- 网络
  
  - Web
    提供具有网页显示能力的Web组件。
  
- 其他
  
  - ScrollBar
    滚动条组件，用于配合可滚动组件使用，如List、Grid、Scroll等。
    
  - Badge
    可以附加在单个组件上用于信息标记的容器组件。
    
  - AlphabetIndexer
    可以与容器组件联动用于按逻辑结构快速定位容器显示区域的索引条组件。
    
  - Panel
    可滑动面板，提供一种轻量的内容展示窗口，方便在不同尺寸中切换。
    
  - Refresh
    可以进行页面下拉操作并显示刷新动效的容器组件。
    
  - Menu
    以垂直列表形式显示的菜单，优先用于PC端的菜单开发。
    
  - MenuItem
    用来展示菜单Menu中具体的item菜单项。
    
  - MenuItemGroup
    用来展示菜单MenuItem的分组。
  
