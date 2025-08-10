---
layout: post
tags: Android
---

使用 Markor 时一直感觉缺了点什么。这么优秀的一个 Markdown 编辑器居然没有行号！把源码 clone 下来，开干！

---

先上成果图

<table><tr>
<td><img src="/img/markor-todo.jpg"/></td>
<td><img src="/img/markor-quicknote.jpg"/></td>
</tr></table>

---

编辑界面其实就是一个自定义的 EditText , 可以考虑把行号绘制上去，这样的话复杂的只是计算

折腾了大半天，也算有个初步成果

看代码里的注释，应该思路会比较清晰的

上代码，上注释
```
    public HighlightingEditor(Context context, AttributeSet attrs) {
        super(context, attrs);

        // a lot of code here
        // ...

        if (showLineNumber) {
            // 初始化时先重新计算填充边距
            oldPaddingLeft = getPaddingLeft();
            resetPadding();
        }
    }

    // 重新计算填充边距
    private void resetPadding() {
        setPadding(lineNumberWidth + oldPaddingLeft, getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }

    // 行号开关 后续可以在 App 的设置页面提供开关
    boolean showLineNumber = true;

    int lineStart;
    int lineEnd;
    String lineText; // 用于记录 EditText 每行的内容

    int lineNumber; // 记录行号
    boolean needLineNumber; // 记录是否需要绘制行号

    int oldPaddingLeft; // 重新设置填充边距前的填充边距
    int lineNumberWidth; // 重新设置填充边距时行号的宽度
    ArrayList<Integer> lineNumberYList; // 记录每个行号的 Y 坐标
    int lineBottomY; // line bottom Y, the height of each row, real line height

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showLineNumber) {
            lineStart = 0;
            lineNumber = 0;
            needLineNumber = true;
            lineNumberWidth = 0;
            if (lineNumberYList == null) {
                lineNumberYList = new ArrayList<>();
            }
            lineNumberYList.clear();
            lineBottomY = getPaddingTop();

            // todo 优化
            // line number: 1 2 3 4 ...
            for (int lineIndex = 0; lineIndex < getLineCount(); lineIndex++) {
                // 真正的行高，读取的一行文本在UI上显示因超长而换行时的多行算在一起，因为只要显示一个行号: LineBottom - LineTop
                lineBottomY += getLayout().getLineBottom(lineIndex) - getLayout().getLineTop(lineIndex);

                lineEnd = getLayout().getLineEnd(lineIndex);
                lineText = getText().toString().substring(lineStart, lineEnd);
                lineStart = lineEnd;

                lineNumber++;

                // text bottom of text: height - descent
                lineNumberYList.add(lineBottomY - getLayout().getLineDescent(lineIndex));
                // horizontal: paddingLeft/4 + lineNumberMinWidth + paddingLeft/4 + | + paddingLeft/2 + inputText
                // y: top of current line == bottom of previous line
                canvas.drawText(needLineNumber ? String.valueOf((lineNumber)) : "", oldPaddingLeft / 4.0f, lineNumberYList.get(lineIndex), getPaint());

                if (lineNumberWidth < getPaint().measureText(String.valueOf(lineNumber))) {
                    lineNumberWidth = (int) (getPaint().measureText(String.valueOf(lineNumber)) + 0.5f);
                }

                if (lineText.endsWith("\n")) {
                    needLineNumber = true;
                } else {
                    lineNumber--;
                    needLineNumber = false;
                }
            }

            // 行号区域宽度计算好了，再重新计算填充边距
            resetPadding();
            // vertical line: |
            canvas.drawLine(lineNumberWidth + oldPaddingLeft / 2.0f, getPaddingTop(), lineNumberWidth + oldPaddingLeft / 2.0f, lineBottomY, getPaint());
        }
    }

```

算是初步完成了功能，还可以优化，自用是够了

修改后的源码 [https://github.com/cymok/markor.git](https://github.com/cymok/markor.git)
