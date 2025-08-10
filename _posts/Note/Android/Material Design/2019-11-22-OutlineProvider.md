---
layout: post
tags: Android
---

使用示例:

```
//ImageView iv = ...
ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
	@Override
	public void getOutline(View view, Outline outline) {
		float radius = ConvertUtils.dp2px(10);//10dp
		outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
	}
};
iv.setOutlineProvider(viewOutlineProvider);
iv.setClipToOutline(true);//裁剪
```

---

实现抽象类 
`android.view.ViewOutlineProvider` 的方法 
`android.view.ViewOutlineProvider#getOutline(android.view.View, android.graphics.Outline)`

`android.graphics.Outline`的主要api如下:

```
android.graphics.Outline#set

android.graphics.Outline#setAlpha

android.graphics.Outline#setRect(int, int, int, int)

android.graphics.Outline#setRect(android.graphics.Rect)

android.graphics.Outline#setRoundRect(int, int, int, int, float)

android.graphics.Outline#setRoundRect(android.graphics.Rect, float)

android.graphics.Outline#setOval(int, int, int, int)

android.graphics.Outline#setOval(android.graphics.Rect)

android.graphics.Outline#setConvexPath

```