---
layout: post
tags: Java SpringBoot
---

# 路上的坑

## `@Transactional` 不生效

把 `@Transactional` 放在了测试类的测试方法上，掉下坑里很久很久 才发现 无论如何都不会执行成功，放到 `Service` 上就正常使用了

后来搜索到有关内容: 

- [不要在 Spring Boot 集成测试中使用 @Transactional](https://segmentfault.com/a/1190000015635145)

在测试运行时，测试类中 @Transactional 注解，会导致测试中 Entity 数据的操作都是在内存中完成，
最终并不会进行 commit 操作，也就是不会将 Entity 数据进行持久化操作，从而导致测试的行为和真实应用的行为不一致。

- [在junit 测试时，加入@Transactional注解](https://blog.csdn.net/zhangyingchengqi/article/details/115766965)

在junit中的@Transactional与在业务层中的@Transactional是不同的，在这里，不管是否有异常都会回滚.
