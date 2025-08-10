---
layout: post
tags: Java Kotlin
---

- List

  - ArrayList 可变数组实现，查找快 增删慢

  - LinkedList 链表实现，增删快 查找慢

** 一般用 ArrayList，要插队(不是队尾)增删改用 LinkedList

- Map

  - HashMap 哈希表实现（数组+链表+红黑树实现），查找快，插入无序 迭代无序

  - LinkedHashMap 哈希表+链表(保证有序)，查找相对Hash慢点(链表 内存消耗多点) 插入无序 迭代有序

  - TreeMap 红黑树，键不可null，查找相对Hash慢点(排序)，插入有序 迭代有序，所有的key都必须直接或间接的实现Comparable接口

只有TreeMap键不可null，值全可null

** 一般用 HashMap，要求迭代有序用 LinkedHashMap，要排序用 TreeMap

- Set

** 只是Map的键值对隐藏了值，同Map

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

