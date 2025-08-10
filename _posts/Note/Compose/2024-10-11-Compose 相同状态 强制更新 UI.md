---
layout: post
tags: Android Compose
---

# Compose 相同状态 强制更新 UI

## 给 State 多包装一层 使用时间戳 保证每次更新状态都是唯一的

State

```
// 包一层带唯一时间戳的类，保证每次 UniqueLoginState 是不同的，强制更新 UI（例如登录失败验证提示，这样就多次返回同一个错误都会提示）
data class UniqueLoginState(
    val state: LoginState,
    val timestamp: Long = System.currentTimeMillis() // 时间戳作为唯一标识符，返回相同 LoginState 时可以强制更新 UI
)

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class Success(val userInfo: UserInfo?) : LoginState()
    data class Error(val message: String?) : LoginState()
}
```

ViewModel

```
    private val _loginState = MutableStateFlow<UniqueLoginState>(UniqueLoginState(LoginState.Idle))
    val loginState: StateFlow<UniqueLoginState> = _loginState

	// 更新
	_loginState.value = UniqueLoginState(LoginState.Success(it))
	
```

UI

```
val loginState by viewModel.loginState.collectAsState()

LaunchedEffect(loginState) {
    if (loginState.state is LoginState.Error) {
        // 登录失败
        val message = (loginState.state as LoginState.Error).message
        toast(message)
    }
}
```

## 直接调用回调方法

在更新状态的同时调用回调方法

ViewModel

```
var listener: ((String) -> Unit)? = null
// 更新 State 同时进行回调
listener.invok("我是 ViewModel")
```

UI

```
val viewModel: LoginViewModel = viewModel()
viewModel.listener = { message ->
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
```
