---
layout: post
tags: Android
---

# Shared Element 共享元素

在 Android 中，共享元素是一种美观而且流畅的转场动画效果，它可以在 Activity 之间共享和传递视图元素，提供无缝的用户体验。下面是使用共享元素进行转场动画的基本步骤：

1. 准备两个 Activity：Activity A 和 Activity B。
2. 在布局文件中，给需要共享的元素添加一个共享元素的过渡名称（transitionName），例如 `android:transitionName="shared_element"`。
3. 在 Activity A 中，当执行跳转到 Activity B 的操作时，创建一个共享元素的过渡名字对应的 View 对象，并将其传递给 Activity B。这可以通过调用 `ActivityOptionsCompat.makeSceneTransitionAnimation()` 来实现。示例代码如下：
   ```java
   Intent intent = new Intent(ActivityA.this, ActivityB.class);
   ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(ActivityA.this, sharedElementView, "shared_element");
   startActivity(intent, options.toBundle());
   ```
4. 在 Activity B 中，将接收到的共享元素的过渡名字对应的 View 对象设置给相应的视图元素。示例代码如下：
   ```java
   View sharedElementView = findViewById(R.id.shared_element_view);
   sharedElementView.setTransitionName("shared_element");
   ```
5. 在 Activity B 中，为了使转场动画生效，在 `onCreate()` 方法或者 `onEnterAnimationComplete()` 方法中添加以下代码：
   ```java
   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
       Transition transition = TransitionInflater.from(this).inflateTransition(android.R.transition.move);
       getWindow().setSharedElementEnterTransition(transition);
   }
   ```
6. （可选）如果你希望在返回到 Activity A 时也有共享元素的过渡效果，可以在 Activity B 中重复步骤 3 和 4，并在 `onBackPressed()` 方法中执行返回操作。
7. 运行应用程序并观察转场动画的效果。

请注意，共享元素只能在支持共享元素转场的 Android 版本上使用（Android 5.0 及以上版本），并且需要将 AppCompat 库或者 androidx.appcompat 库添加到项目的依赖中。
