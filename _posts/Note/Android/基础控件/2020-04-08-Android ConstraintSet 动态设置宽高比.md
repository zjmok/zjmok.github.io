---
layout: post
tags: Android
---


```
//获取宽高比
Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.home_banner);
int width = bitmap.getWidth();
int height = bitmap.getHeight();
String ratio = width + ":" + height;
bitmap.recycle();

//设置比例
ConstraintSet set = new ConstraintSet();//All children of ConstraintLayout must have ids to use ConstraintSet
ViewParent viewParent = ivHomeBanner.getParent();
if (viewParent instanceof ConstraintLayout) {
	ConstraintLayout parent = (ConstraintLayout) viewParent;
	set.clone(parent);
	set.setDimensionRatio(ivHomeBanner.getId(), ratio);
	set.applyTo(parent);
}
```
