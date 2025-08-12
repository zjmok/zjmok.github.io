---
layout: post
tags: Others
---

# moffee，一个 markdown 生成 ppt 的工具

moffee <https://github.com/BMPixel/moffee>

### 安装运行

moffee 是用 Python 编写的

```shell
pipx install moffee
# or `pip install moffee`
```

在实时 Web 服务器中预览幻灯片或导出为 HTML

```shell
moffee live example.md # launch a server
# or
moffee make example.md -o output_html/ # export to HTML
```

### 语法

基本基于 markdown 格式

特色语法：

- `##` 二级标题会自动换页

- Create columns with `<->`，左右分割

```markdown
Left column content
<->
Right column content
```

- Stack content with `===`，上下分割

```markdown
Top section
===
Bottom section
```

- 上下左右分割混合使用

```markdown
Top row
===
Left column
<->
Right column
===
Bottom row
```

- Manually create slides with `---`，手动换页

```markdown
Content for slide 1

---

Content for slide 2
```

### END
