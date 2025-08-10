---
layout: post
tags: Git
---

## 为什么Github要把代码合并请求称为pull request而不是push request？

是这样的，这个应该分开来解释。

这个pull指的是权限主体的操作。你提交了代码，但是你没有操作上游repo的权限，你需要上游repo的主人review你的代码，然后把你的代码修改pull到他的repo中去，这是对于pull的解释。

而request则指的是发起主体的操作。也就是说，上游repo的主人虽然有repo的控制权，可以把你的代码更改pull到他自己的repo里，但是他不会主动去pull。而是需要你（发起主体）向上游repo的主人提交申请，也就是request，上游repo的主人才会去响应你的request，也就是执行你所说的review和pull的过程。

所以，pull request的理解方法是：一个通知上游repo所有者拉取代码(pull)的请求(request)。

在英语中，request一般指的是提交一个申请，需要对方对申请给予答复的。而request之前的修饰词，则是答复方的动作，当然，中文中也是一样。比如“入团申请”，你提交申请之后，需要对方允许你入团你才算是团员。所以，入团的动作不是你主动做的，而是由审核的人把你的名字加上去才算“入团”。同理“pull request”中，request是你提交的，而pull则是对方做的事情。
