---
layout: post
tags: Harmony
---

## 状态管理

状态管理 <https://developer.huawei.com/consumer/cn/doc/harmonyos-guides-V5/arkts-state-management-V5>

### V1 稳定版

- **@State**：@State装饰的变量拥有其所属**组件的状态**，可以作为其子组件单向和双向同步的数据源。当其数值改变时，会引起相关组件的渲染刷新。

- **@Prop**：@Prop装饰的变量可以和父组件建立**单向同步**关系，@Prop装饰的变量是可变的，但修改不会同步回父组件。

- **@Link**：@Link装饰的变量可以和父组件建立**双向同步**关系，子组件中@Link装饰变量的修改会同步给父组件中建立双向数据绑定的数据源，父组件的更新也会同步给@Link装饰的变量。

- **@Provide/@Consume**：@Provide/@Consume装饰的变量用于跨组件层级（多层组件）同步状态变量，可以不需要通过参数命名机制传递，通过alias（别名）或者属性名绑定。

- **@Observed**：@Observed装饰class，需要观察多层**嵌套场景的class**需要被@Observed装饰。单独使用@Observed没有任何作用，需要和@ObjectLink、@Prop联用。

- **@ObjectLink**：@ObjectLink装饰的变量接收@Observed装饰的class的实例，应用于观察多层嵌套场景，和父组件的数据源构建**双向同步**。

---

#### 管理组件拥有的状态

1. @State

@State 的观察能力只有一层，如果状态值是对象，而他的属性又是对象，obj.obj.value 变化时不会被观测到

状态管理V1中，会给@Observed装饰的类对象以及使用状态变量装饰器如@State装饰的Class、Date、Map、Set、Array添加一层代理用于观测一层属性或API调用产生的变化

如果 list 是一个对象数组，给状态赋值 this.list[0]，多次赋值时也会刷新 UI，原因：当再次赋值 list[0] 时，状态值已经是一个 Proxy 类型，而 list[0] 是 Object 类型，判断是不相等的，因此会触发赋值和刷新。
解决办法1是 @Observed，给对应的类增加了 @Observed 装饰器后，list[0] 已经是 Proxy 类型了，这样再次赋值时，相同的对象，就不会触发刷新。
解决办法2是 `UIUtils.getTarget()`，先传入 Proxy 对象的状态值 UIUtils.getTarget(this.stateData) 获取到 Object 判断是否相等再进行赋值

不允许在build里改状态变量，`Text(`${this.count++}`)`，后果是运行时报错，这将可能造成死循环。

通过静态方法赋值的话，需要添加代理层 `let score1 = this.score`
```
class Score {
  value: number;
  constructor(value: number) {
    this.value = value;
  }

  static changeScore1(score:Score) {
    score.value += 1;
  }
}

// 在 build 赋值
// 通过赋值添加 Proxy 代理
let score1 = this.score;
Score.changeScore1(score1);
```

2. @Prop

父组件 @State 的状态传递给子组件 @Prop 的状态，父组件的状态改变会同步（覆盖）子组件的状态，反之则不会，单向同步

```
// 父组件
@State parentSelectedDate: Date = new Date('2021-08-08');
// build
DateComponent({ selectedDate:this.parentSelectedDate })

// 子组件
@Prop selectedDate: Date = new Date('');
```

3. @Link

父组件中 @State, @StorageLink 和 @Link 和子组件 @Link 可以建立双向数据同步。
- 子组件的 @Link 状态会将 this 注册给父组件，从而监听到父组件状态的更新
- @Link更新后，调用父组件的@State包装类的set方法，将更新后的数值同步回父组件

@Link 不允许定义为 number 类型（@Link item : number），并在父组件中用 @State 数组中每个数据项创建子组件

通过静态方法赋值的话，需要添加代理层 `let score1 = this.score`

```
// 父组件
@State parentSelectedDate: Date = new Date('2021-08-08');
// build
DateComponent({ selectedDate:this.parentSelectedDate })

// 子组件
@Link selectedDate: Date;
```

4. @Provide 和 @Consume

@Provide 和 @Consume，应用于与后代组件的双向数据同步，应用于状态数据在多个层级之间传递的场景
```
// 通过相同的变量名绑定
@Provide a: number = 0;
@Consume a: number;

// 通过相同的变量别名绑定
@Provide('a') b: number = 0;
@Consume('a') c: number;
```

@Provide 和 @Consume 是一对多的

5. @Observed 和 @ObjectLink

- @Observed 装饰 class。需要放在 class 的定义前，使用new创建类对象。当前层的类被装饰，当前层的属性就能被观察到数据变化
- @ObjectLink 在非 @Entry 的组件使用，装饰被 @Observed 装饰的 class 实例。变量的属性是可以改变，但变量是只读

使用 @Observed 注解
```
class Child {
  public num: number;

  constructor(num: number) {
    this.num = num;
  }
}

@Observed
class Parent {
  public child: Child;
  public count: number;

  constructor(child: Child, count: number) {
    this.child = child;
    this.count = count;
  }
}
```

build() 中
```
@ObjectLink parent: Parent

// 赋值变化可以被观察到
this.parent.child = new Child(5)
this.parent.count = 5

// ClassA没有被@Observed装饰，其属性的变化观察不到
this.parent.child.num = 5
```

#### 管理应用拥有的状态

1. **LocalStorage**：页面级UI状态存储，通常用于UIAbility内、页面间的状态共享。可用于初始化组件状态（State Prop Link Provide）

LocalStorageProp 与组件状态**单向同步**（相对于只读）
  
LocalStorageLink 与组件状态**双向同步**（相当于可读写）

- 应用逻辑使用 LocalStorage

```
let para: Record<string,number> = { 'PropA': 47 };
let storage: LocalStorage = new LocalStorage(para); // 创建新实例并使用给定对象初始化
let propA: number | undefined = storage.get('PropA') // propA == 47
let link1: SubscribedAbstractProperty<number> = storage.link('PropA'); // link1.get() == 47
let link2: SubscribedAbstractProperty<number> = storage.link('PropA'); // link2.get() == 47
let prop: SubscribedAbstractProperty<number> = storage.prop('PropA'); // prop.get() == 47
link1.set(48); // two-way sync: link1.get() == link2.get() == prop.get() == 48
prop.set(1); // one-way sync: prop.get() == 1; but link1.get() == link2.get() == 48
link1.set(49); // two-way sync: link1.get() == link2.get() == prop.get() == 49
```

- 从 UI 内部使用 LocalStorage

```
// 创建 LocalStorage 新实例并使用给定对象初始化
let para: Record<string, number> = { 'PropA': 47 };
let storage: LocalStorage = new LocalStorage(para);
storage.setOrCreate('PropB', new PropB(50)); // 使用自定义对象初始化

// @LocalStorageProp变量装饰器与LocalStorage中的'PropA'属性建立单向绑定
@LocalStorageProp('PropA') storageProp1: number = 1;

// @LocalStorageLink变量装饰器与LocalStorage中的'PropA'属性建立双向绑定
@LocalStorageLink('PropA') childLinkNumber: number = 1;
```

2. **AppStorage**：特殊的单例LocalStorage对象，由UI框架在应用程序启动时创建，为应用程序UI状态属性提供中央存储。

AppStorage是单例，它的所有API都是静态的，使用方法类似于LocalStorage中对应的非静态方法。

- 从应用逻辑使用

```
// 初始化 赋值
AppStorage.setOrCreate('PropA', 47);
// 取值
let propA: number | undefined = AppStorage.get('PropA') // propA in AppStorage == 47, propA in LocalStorage == 17
let link1: SubscribedAbstractProperty<number> = AppStorage.link('PropA'); // link1.get() == 47
let link2: SubscribedAbstractProperty<number> = AppStorage.link('PropA'); // link2.get() == 47
let prop: SubscribedAbstractProperty<number> = AppStorage.prop('PropA'); // prop.get() == 47
// 赋值
link1.set(48); // two-way sync: link1.get() == link2.get() == prop.get() == 48
prop.set(1); // one-way sync: prop.get() == 1; but link1.get() == link2.get() == 48
link1.set(49); // two-way sync: link1.get() == link2.get() == prop.get() == 49
// 获取
AppStorage.get<number>('PropA') // == 49
link1.get() // == 49
link2.get() // == 49
prop.get() // == 49
```

- 从UI内部使用

```
// 赋值
AppStorage.setOrCreate('PropA', 47);
AppStorage.setOrCreate('PropB', new PropB(50));
// 获取
@StorageLink('PropA') storageLink: number = 1;
@StorageLink('PropB') storageLinkObject: PropB = new PropB(1);
```

3. **PersistentStorage**：持久化存储UI状态，通常和 AppStorage 配合使用，选择 AppStorage 存储的数据写入磁盘，以确保这些属性在应用程序重新启动时的值与应用程序关闭时的值相同。

在逻辑中使用
```
// 使用前初始化（直接放在 get 和 set 之前），查询到有值则会使用已存在的值，如果数据不存在才会赋值
PersistentStorage.persistProp<string>("cookies", "")

// get
let cookies = AppStorage.get<string>("cookies")

// save
AppStorage.setOrCreate("cookies", cookies)
```

在 UI 中使用
```
PersistentStorage.persistProp<string>("supperUserInfo", "")

@Component
export struct PersonComponent {

  @StorageLink('supperUserInfo') supperUserInfo: string = "";
  
  build() {
    Text(this.supperUserInfo)
  }
  
}
```

4. **Environment**：应用程序运行的设备的环境参数，环境参数会同步到AppStorage中，可以和AppStorage搭配使用。

Environment 是只读的

键 | 数据类型 | 描述
--- | --- | ---
accessibilityEnabled | boolean | 获取无障碍屏幕读取是否启用。
colorMode | ColorMode | 色彩模型类型：选项为ColorMode.LIGHT: 浅色，ColorMode.DARK: 深色。
fontScale | number | 字体大小比例，范围: [0.85, 1.45]。
fontWeightScale | number | 字体粗细程度，范围: [0.6, 1.6]。
layoutDirection | LayoutDirection	布局方向类型：包括LayoutDirection.LTR: 从左到右，LayoutDirection.RTL: 从右到左。
languageCode | string | 当前系统语言值，取值必须为小写字母, 例如zh。

```
// 将设备的语言 code 存入 AppStorage，默认值为 en
Environment.envProp('languageCode', 'en');

// 在应用逻辑中读取系统语言值
const lang: SubscribedAbstractProperty<string> = AppStorage.prop('languageCode');

// 在 UI 中读取系统语言值
@StorageProp('languageCode') lang : string = 'en';
```

#### 其它状态管理

- @Watch：用于监听状态变量的变化。

```
@Component
struct TotalView {
  @Prop @Watch('onCountUpdated') count: number = 0;
  @State total: number = 0;
  // @Watch 回调
  onCountUpdated(propName: string): void {
    this.total += this.count;
  }

  build() {
    Text(`Total: ${this.total}`)
  }
}
```

- `$$` 运算符：给内置组件提供TS变量的引用，使得TS变量和内置组件的内部状态保持同步。

`$$` 绑定的变量变化时，会触发UI的同步刷新。使用场景，例如，TextInput组件的text参数

```
@Entry
@Component
struct TextInputExample {
  @State text: string = ''
  controller: TextInputController = new TextInputController()

  build() {
    Column({ space: 20 }) {
      Text(this.text)
      TextInput({ text: $$this.text, placeholder: 'input your word...', controller: this.controller })
        .placeholderColor(Color.Grey)
        .placeholderFont({ size: 14, weight: 400 })
        .caretColor(Color.Blue)
        .width(300)
    }.width('100%').height('100%').justifyContent(FlexAlign.Center)
  }
}
```

- @Track：应用于class对象的属性级更新。@Track装饰的属性变化时，只会触发该属性关联的UI更新。

@Track是class对象的属性装饰器。  
当一个class对象是状态变量时，@Track装饰的属性发生变化，
只会触发该属性关联的 UI 更新；而未被标记的属性不会使 UI 刷新。

- 自定义组件冻结：当自定义组件处于非激活状态时，状态变量将不响应更新。

### V2 试用版

- **@ObservedV2**：@ObservedV2装饰器装饰class，使得被装饰的class具有深度监听的能力。@ObservedV2和@Trace配合使用可以使class中的属性具有深度观测的能力。

- **@Trace**：@Trace装饰器装饰被@ObservedV2装饰的class中的属性，被装饰的属性具有深度观测的能力。

- **@ComponentV2**：使用@ComponentV2装饰的struct中能使用新的装饰器。例如：@Local、@Param、@Event、@Once、@Monitor、@Provider、@Consumer。

- **@Local**：@Local装饰的变量为组件内部状态，无法从外部初始化。

- **@Param**：@Param装饰的变量作为组件的输入，可以接受从外部传入初始化并同步。

- **@Once**：@Once装饰的变量仅初始化时同步一次，需要与@Param一起使用。

- **@Event**：@Event装饰方法类型，作为组件输出，可以通过该方法影响父组件中变量。

- **@Monitor**：@Monitor装饰器用于@ComponentV2装饰的自定义组件或@ObservedV2装饰的类中，能够对状态变量进行深度监听。

- **@Provider/@Consumer**：用于跨组件层级双向同步。

- **@Computed**：计算属性，在被计算的值变化的时候，只会计算一次。主要应用于解决UI多次重用该属性从而重复计算导致的性能问题。

- **!!语法**：双向绑定语法糖。
