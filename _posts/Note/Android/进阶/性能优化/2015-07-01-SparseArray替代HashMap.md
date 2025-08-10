---
layout: post
tags: Android
---

**SparseArray 和 HashMap 的对比：**

SparseArray比HashMap更省内存，在某些条件下性能更好，  
1. 主要是因为它避免了对key的自动装箱（int转为Integer类型），它内部则是通过两个数组来进行数据存储的，一个存储key，另外一个存储value，  
2. 为了优化性能，它内部对数据还采取了压缩的方式来表示稀疏数组的数据，从而节约内存空间，  
从源码中可以看到key和value分别是用数组表示，SparseArray只能存储key为int类型的数据，
3. 同时，SparseArray在存储和读取数据时候，使用的是二分查找法。

**SparseArray应用场景：**

虽说SparseArray性能比较好，但是由于其添加、查找、删除数据都需要先进行一次二分查找，所以在数据量大的情况下性能并不明显，将降低至少50%。  
满足下面两个条件我们可以使用SparseArray代替HashMap：

- 数据量不大，最好在千级以内

- key必须为int类型，这中情况下的HashMap可以用SparseArray代替
