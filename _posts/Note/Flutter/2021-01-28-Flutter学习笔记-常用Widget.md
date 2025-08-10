---
layout: post
tags: Flutter
---

常用 Widget

---

Meterial  (Android风格)

Cupertino  (iOS风格)

---

```
Scaffold  (脚手架 页面基本框架)

SafeArea  (适配不含状态栏和虚拟按键的安全区域)

Container  (div)

ClipRect  (将 child 剪裁为给定的矩形大小)
ClipRRect  (圆角矩形剪裁)
ClipOval  (圆裁剪)
ClipPath  (路径裁剪)

ConstrainedBox  (constraints: BoxConstraints 最 大/小 宽/高 度)

Padding  (内边距)

SizedBox  (透明间隔)

Divider  (间隔线)

Card  (CardView)

InkWell  (可以给子widget增加点击效果 涟漪效果 水波纹效果)

Center

Row  (可用于水平适应填满 match_parent) (mainAxisSize 属性设置类似 wrap_content match_parent)

Column  (可用于垂直适应填满 match_parent)

Flex  (弹性布局 Row/Column 的父类 指定方向后跟 Row/Column 效果差不多)

Scrollable  (ScrollView)

Row/Column + Flexible/Expanded (flex = LinearLayout + weight)

Wrap  (流布局) Flow

Text

表单 TextField CheckBox Radio Switch CheckboxListTile RadioListTile SwichListTile Slide

OutlineInputBorder  (border:输入框边框)

按钮 RaisedButton FlatButton OutlineButton IconButton ButtonBar

其它可处理点击的 Widget：InkWell (有 ripple 效果) 、GestureDetector (可用于检测各种手势)

Image

ClipOval + Image  (圆图)

ListView

ListTile

Stack  (类似FrameLayout)

Stack + Align  (类似LinearLayout)

Stack + Positioned  (类似RelativeLayout)

AspectRatio  (设置child宽高比 ratio 比例)

PageView (ViewPager)

AppBar

BottomNavigationBar

TabBar + TabBarView + TabController  (联动)

Drawer + DrawerHeader

BoxDecoration

CircleAvatar

时间戳
DateTime.now()
第三方库
[date_format]()
日期选择
showDatePicker
时间选择
showTimePicker
选择器第三方库
[flutter_cupertino_date_picker]()
```

