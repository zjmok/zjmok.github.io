---
layout: post
tags: Android
---

TextView auto size 字体大小自动适应

---

XML

```
<TextView
    app:autoSizeTextType="uniform"
    app:autoSizeMinTextSize="16sp"
    app:autoSizeMaxTextSize="100sp"
    app:autoSizeStepGranularity="1sp" />
```


---

Programmatically

```
TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(textView, 16, 100, 1, TypedValue.COMPLEX_UNIT_SP)
```
TextViewCompat类还有多个方法  
unit值可用DP或SP

---
