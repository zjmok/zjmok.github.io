---
layout: post
tags: Android Kotlin
---

上一篇讲了 by 的使用, 这一篇分析一个用 by 实现的功能的例子

### 案例分析

在实际应用中, Fragment 的 ViewBinding 对象 为了避免内存泄漏 需要在视图销毁时进行置空

ViewBindingPropertyDelegate 库 可以直接通过委托处理, 不需要手动在 `onDestroyView` 中执行置空操作

```
val binding: FragmentSubscribeBinding by viewBinding(CreateMethod.INFLATE)
```

追溯源码
```
    CreateMethod.BIND -> viewBinding({
        ViewBindingCache.getBind(viewBindingClass).bind(requireView())
    }, onViewDestroyed)
    CreateMethod.INFLATE -> when (this) {
        is DialogFragment -> dialogFragmentViewBinding(
            onViewDestroyed,
            {
                ViewBindingCache.getInflateWithLayoutInflater(viewBindingClass)
                    .inflate(layoutInflater, null, false)
            },
            viewNeedsInitialization = false
        )
        else -> fragmentViewBinding(
            onViewDestroyed,
            {
                ViewBindingCache.getInflateWithLayoutInflater(viewBindingClass)
                    .inflate(layoutInflater, null, false)
            },
            viewNeedsInitialization = false
        )
    }
```

这是在 Fragment 里执行的, 可以看到, 调用了 `fragmentViewBinding(xxx)`

继续找
```
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun <F : Fragment, T : ViewBinding> fragmentViewBinding(
    onViewDestroyed: (T) -> Unit,
    viewBinder: (F) -> T,
    viewNeedsInitialization: Boolean = true
): ViewBindingProperty<F, T> {
    return FragmentViewBindingProperty(viewNeedsInitialization, viewBinder, onViewDestroyed)
}
```

return FragmentViewBindingProperty

打开 FragmentViewBindingProperty
```
    @MainThread
    override fun getValue(thisRef: F, property: KProperty<*>): T {
        val viewBinding = super.getValue(thisRef, property)
        registerFragmentLifecycleCallbacksIfNeeded(thisRef)
        return viewBinding
    }
```

再查看 super.getValue(xxx)

```
        val lifecycle = getLifecycleOwner(thisRef).lifecycle
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            this.viewBinding = null
            Log.w(TAG, ERROR_ACCESS_AFTER_DESTROY)
            // We can access ViewBinding after Fragment.onDestroyView() was called,
            // but it isn't saved to prevent memory leaks
            return viewBinder(thisRef)
        } else {
            val viewBinding = viewBinder(thisRef)
            lifecycle.addObserver(ClearOnDestroyLifecycleObserver(this))
            this.viewBinding = viewBinding
            return viewBinding
        }
```

当lifecycle 的状态不是 DESTROYED 时, 执行了 lifecycle.addObserver(ClearOnDestroyLifecycleObserver(this))

先看 ClearOnDestroyLifecycleObserver

```
        @MainThread
        override fun onDestroy(owner: LifecycleOwner) {
            property.postClear()
        }
```

postClear

```
    @MainThread
    @CallSuper
    public override fun clear() {
        checkMainThread()
        val viewBinding = viewBinding
        this.viewBinding = null
        if (viewBinding != null) {
            onViewDestroyed(viewBinding)
        }
    }

    @RestrictTo(LIBRARY_GROUP)
    protected fun postClear() {
        if (!mainHandler.post { clear() }) {
            clear()
        }
    }
```

`this.viewBinding = null` 置空了

最后, 再确认一下前面 lifecycle 的 owner 是谁, 找到 getLifecycleOwner, 是个抽象函数,  
分别有4种实现, 对应 Activity DialogFragment Fragment ViewGroup, 那看到是找 Fragment 的

```
    override fun getLifecycleOwner(thisRef: F): LifecycleOwner {
        try {
            return thisRef.viewLifecycleOwner
        } catch (ignored: IllegalStateException) {
            error("Fragment doesn't have a view associated with it or the view has been destroyed")
        }
    }
```

没问题, 是 `viewLifecycleOwner`, thisRef 是 Fragment

原理就是在被代理类中利用 fragment 的 viewLifecycleOwner 添加 lifecycle 的观察者, 在视图销毁时将 viewBinding 置空

其实这个库还用到了缓存, 这里只是简单分析
