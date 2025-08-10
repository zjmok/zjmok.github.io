---
layout: post
tag: Android
---

# MVC

**Model**
- 表示应用程序的核心数据和业务逻辑
- 负责处理数据的获取、存储和处理。通常包含对数据库的操作和与后端交互的逻辑
- 通常是纯粹的数据结构，不包含任何用户界面逻辑

**View**
- 用户界面部分，负责呈现数据
- 直接显示模型的数据并将用户的输入转发给控制器
- 通常是 UI 组件，比如按钮、文本框等

**Controller**
- 作为模型和视图之间的桥梁
- 处理用户输入并更新模型或视图
- 控制器接收来自视图的输入，然后更新模型或视图，通常是通过调用模型的函数来更新数据

优点是 清晰的分层结构，模型、视图和控制器是独立  
缺点是 在复杂的应用中，控制器可能变得非常庞大和复杂，会随着业务的复杂而逐渐臃肿膨胀，难以维护，耦合性高  

# MVP

依赖接口通信的解耦方式

- **Model** 负责业务逻辑和数据管理
- **View** 负责UI界面展示及用户操作交互，Activity Fragment 实现 View 接口，不直接与模型交互
- **Presenter** 通过 Model 获取数据，处理大部分逻辑，调用 View 接口 的 UI 更新 函数，降低 Model 与 View 的耦合性

使用 P 把 MVC 的 M 直接传递给 V 解耦了，并替代 C，就成了 MVP

但 V 和 P 是直接交互的

缺点是 要创建大量的接口，V 与 P 耦合性过高，更适合大型项目

# MVVM

- **Model** 负责数据处理部分。Repository DataSource 等
- **View** 用户界面，显示数据并接收用户的输入。Activity Fragment 等
- **ViewModel** ViewModel 连接 M 与 V 的桥梁，VM 与 M 直接交互，通过观察者模式（LiveData 或 Flow 等） 间接把数据更新通知给 View 以实现解耦

M 负责处理数据存储网络传输,  
V 持有 VM 的对象, 作为观察者, 根据 VM 数据的更新去处理 UI,  
VM 不持有 V 对象，作为被观察者, 负责处理逻辑和调用获取数据函数等,  

MVP 把 P 与 V 直接调用的方式，改成 P 提供数据给 V 监听，就差不多成了 MVVM

使用 DataBinding 的项目 可以实现双向绑定 缺点是 `DataBinding` 构建时间需要更久，数据流更复杂，不易测试

官方已不推荐使用 `DataBinding` 现在推荐使用 `ViewBinding`

显然 `ViewBinding` 淘汰了 `DataBinding` `anko` `kotlin-android-extensions`

DataBinding 虽然已被弃用，但 DataBinding 只是实现 MVVM 双向绑定的一种方式。  
使用 ViewBinding 或 Compose 一样可以实现 MVVM 双向绑定。  
例如 TextField 通过 onValueChange 方法与 UserViewModel 进行双向绑定。  
当用户输入时，ViewModel 中的用户信息将被更新，从而触发 UI 更新  

# MVI

Model-View-Intent

- **Model**: UseCase Repository DataSource。负责业务逻辑处理和数据管理

- **View**: Composable 或 Activity Fragment 等。负责显示 UI 和响应 State，根据用户操作触发 Intent

- **Intent**: Intent 层。用户的交互动作和意图，被传递给 ViewModel 来处理逻辑

在 MVI 模式中，ViewModel 扮演的是中介角色，它负责处理 Intent 并更新 State。严格来说，ViewModel 既不属于 Model 也不完全属于 View，而是位于两者之间，连接用户的操作（Intent）与应用的数据和业务逻辑（Model）

是一种响应式和流式的处理思想，是单向数据流的

将意图事件（用户操作），通过函数转换为特定Model（状态），将其结果反馈给用户（渲染界面）。

典型关系图: `UserIntent ----> UserViewModel ----> GetUserUseCase ----> UserRepository ----> DataSource (Remote/Local)`
