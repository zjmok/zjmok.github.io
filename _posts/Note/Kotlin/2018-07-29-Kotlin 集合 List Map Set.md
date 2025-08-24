---
layout: post
tags: Java Kotlin
---

### List

- `ArrayList` 可变数组实现，查找快 增删慢

- `LinkedList` 链表实现，增删快 查找慢

** 一般用 `ArrayList`，要插队(不是队尾)增删改用 `LinkedList`

Kotlin 中，

- `listOf` 返回值是 `List` 接口，对空列表和单元素有优化

- `mutableListOf` 返回值是 `ArrayList`，无特殊优化

### Map

- `HashMap` 哈希表实现（数组 + 链表 + 红黑树实现），查找快，插入无序 + 迭代无序

- `LinkedHashMap` 实现方式 哈希表 + 链表（保证有序），查找相对 `HashMap` 慢点（链表 内存消耗多点） 插入无序 + 迭代有序

- `TreeMap` 红黑树实现，键不可 `null`，查找相对 `HashMap` 慢点（排序），插入有序 + 迭代有序，所有的 `key` 都必须直接或间接的实现 `Comparable` 接口

只有 `TreeMap` 键不可 `null`，值全可 `null`

** 一般用 `HashMap`，要求迭代有序用 `LinkedHashMap`，要排序用 `TreeMap`

- `Set`

** 只是 `Map` 的键值对隐藏了值，同 `Map`

---

维度 | HashMap	| TreeMap | LinkedHashMap
:-: | :-: | :-: | :-: 
底层实现 | 哈希表 | 红黑树 | 哈希表+链表
插入顺序 | 无序 | 无序（基于键的自然排序或自定义排序） | 保持插入顺序
迭代顺序 | 无序 | 有序（基于键的自然排序或自定义排序） | 保持插入顺序或访问顺序
查找效率 | O(1) | O(log n) | O(1)
键的唯一性 | 允许null键和null值 | 不许null键 可null值 | 允许null键和null值
性能 | 在大多数情况下，具有良好的性能 | 相比HashMap，由于排序逻辑，稍稍慢一些 | 相比HashMap，由于维护链表，稍稍慢一些
空间需求 | 相对较低（无序） | 相对较高（访问有序） | 相对较高（保持插入顺序或访问顺序）

