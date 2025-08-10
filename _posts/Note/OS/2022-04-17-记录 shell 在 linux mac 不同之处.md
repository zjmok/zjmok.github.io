---
layout: post
tags: Linux Mac
---

记录 shell 在 linux mac 不同之处

### `expr`

- `expr` 在 Linux 中 乘号 `*` 前边必须加反斜杠 `\` 才能实现乘法运算，Mac不需要转义

- `expr` 在 MAC 中 shell 的 expr 语法是：$((表达式))，此处表达式中的 `*` 不需要转义符号 `\` 

```
# Linux
`expr $a \* $b`
# Mac
$((expr $a * $b))
```

### `find`

- `find` 在 Linux 中 默认路径为当前路径

- `find` 在 Mac 中 没有默认路径，即必须有路径参数

```
# Mac Linux
find . -name "*.txt"
# Linux
find -name "*.txt"
```

