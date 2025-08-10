---
layout: post
tags: Android
---

Coroutine + Retrofit + ViewModule 基本使用

```
interface UserService {
  @POST("api/front/user/register")
  suspend fun register(@Body params: MutableMap<String, Any>): ApiResult<UserInfo>
}

object ServiceCreator {
  private val retrofit = Retrofit.Builder()
    .xxx
    .build()

  val userApiService: UserService = retrofit.create(UserService::class.java)
}

class UserRepository {
  private val apiService by lazy { ServiceCreator.userApiService }

  suspend fun register() = withContext(Dispatchers.IO) {
    val params = mutableMapOf<String, String>()
    apiService.register(params).resultData()
  }
}

class UserViewModule : ViewModule() {
  private val userRepository by lazy { UserRepository() }
  val userInfo = MutableLiveData<UserInfo>()

  fun register(){
    viewModelScope.launch {
	  val result = userRepository.register()
	  userInfo.postValue(result)
	}
  }
}

data class ApiResult<T>(
    val code: String,
    val message: String,
    private val data: T?
) {
    fun resultData(): T {
        if (code == "0" && data != null) {
            return data
        } else {
            throw ApiException(code, message)
        }
    }
}
```