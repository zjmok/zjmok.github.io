# Jekyll TeXt Theme 修改版

> 原版主题，请看这里 <https://github.com/kitian616/jekyll-TeXt-theme>

---

## 修改内容

- 页码跳转：`_includes/paginator.html`，新增跳转功能

- 页脚 Copyright：`_layouts/footer.html`、`_data/locale.yml`，年份修改为 `xxxx - xxxx`

- 适配 `layout: post`：新增 `post` 布局 `_layouts/post.html`，继承自 article 布局

- 导航：`_data/navigation.yml`，增加 "首页"

- 列表数量：`_config.yml` 的 `paginate`，修改为每页显示 10 篇文章

- 上下篇超链接文本位置合理对调：`_includes/article-section-navigator.html`

- [ ] 文章添加上拉到顶按钮：

---

## 定制内容

- txt 输出原内容：`_config.yml` 的 `defaults.values.layout`，修改默认布局

- 修改网站名称的字体：`_includes/header.html`

- 修改代码块和代码语句字体大小：`_sass/custom.scss`

- 网站图标：`favicon.ico`，更多参考 <https://kitian616.github.io/jekyll-TeXt-theme/docs/zh/logo-and-favicon>

- 导航：`_data/navigation.yml`，增加 "收藏"（对应文件 `favorites.md`、`favorites.html`）

- About 页面：`about.md`

---

# Jekyll

Jekyll 源码  
<https://github.com/jekyll/jekyll>

Jekyll 官方网站  
<https://jekyllrb.com>

Jekyll 中文网站  
<https://www.jekyll.com.cn>

---

## Jekyll 目录结构

文件 / 目录 | 描述
--- | ---
_config.yml | 存储的是 配置 信息。其中许多 参数是可以在命令行中指定的，但是 在此文件中进行设置会更容易些，并且你不必记住它们。
_drafts | 草稿（Drafts）是未发布的文章（posts）。这些文章的文件名中没有包含 日期： title.MARKUP。了解如何 使用草稿功能。
_includes | These are the partials that can be mixed and matched by your layouts and posts to facilitate reuse. The liquid tag {% include file.ext %} can be used to include the partial in _includes/file.ext.
_layouts | 这里存放的是These are the templates that wrap posts. Layouts are chosen on a post-by-post basis in the front matter, which is described in the next section. The liquid tag {{ content }} is used to inject content into the web page.
_posts | Your dynamic content, so to speak. The naming convention of these files is important, and must follow the format: YEAR-MONTH-DAY-title.MARKUP. The permalinks can be customized for each post, but the date and markup language are determined solely by the file name.
_data | 格式化的站点数据应当放在此目录下。Jekyll 将自动加载此目录下的所有数据文件（支持 .yml、 .yaml、.json、.csv 或 .tsv 格式和扩展名）， 然后就可以通过 `site.data` 来访问了。假定在 此目录下有一个文件名为 members.yml 的文件，那么你可以通过 site.data.members 变量来访问该文件中的内容。
_sass | 这些事可以导入（import）到 main.scss 文件中的 sass 代码片段， main.scss 文件最终经过处理之后形成一个独立的样式文件 main.css 并被用于整个站点。
_site | 在 Jekyll 转换完所有的文件之后，将在此目录下放置生成的站点（默认情况下）。 最好将此目录添加到 .gitignore 文件中。
.jekyll-metadata | This helps Jekyll keep track of which files have not been modified since the site was last built, and which files will need to be regenerated on the next build. This file will not be included in the generated site. It’s probably a good idea to add this to your .gitignore file.
index.html or index.md and other HTML, Markdown files | Provided that the file has a front matter section, it will be transformed by Jekyll. The same will happen for any .html, .markdown, .md, or .textile file in your site’s root directory or directories not listed above.
其它文件/目录 | 除了上面列出的目录和文件外，其它所有目录和文件（例如 css 和 images 目录， favicon.ico 等文件）都将一一复制到 生成的站点中。已经有大量 站点 在使用 Jekyll 了，你可以参考这些站点以了解如何运用这些文件和目录。

---
