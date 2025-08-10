---
layout: post
tags: 适配
---

获取 OAID (匿名设备标识符, Open Anonymous Device Identifier, 最长64位)

官方地址 [`http://www.msa-alliance.cn/`](http://www.msa-alliance.cn/)

*注意有版本限制

- 1.从官方地址 [`http://www.msa-alliance.cn/`](http://www.msa-alliance.cn/)
  下载SDK, 然后解压

  把miit_mdid_x.x.x.aar拷贝到项的libs目录，并设置依赖，其中x.x.x代表版本号。
  
- 2.将supplierconfig.json拷贝到项目assets目录下，并修改里面对应内容，特别是需要设置appid的部分。需要设置appid的部分需要去对应厂商的应用商店里注册自己的app。

- 3.在app级别 `build.gradle` 的 `dependencies` 节点下添加依赖

```
implementation files('libs/miit_mdid_x.x.x.aar')
```

- 4.混淆设置(项目不做混淆可忽略)

```
-keep class com.bun.miitmdid.core.** {*;}
```

- 5.so库的平台过滤和压缩根据自己项目来就好了

- 6.代码调用 这里跟官方的使用方式不太一致(感觉官方的太繁琐了)

  在app的Application类添加参数和初始化及使用方法

```
public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        //...
        //your code
        //...
        //广告联盟SDK
        initMSA();
    }

    public static String sOAID = null;//大厂广告联盟应对谷歌禁止获取imei的对策oaid

    /**
     * 国内大厂广告联盟SDK初始化及调用
     */
    private void initMSA() {
        JLibrary.InitEntry(this);
        //根据官方描述 利用反射方式获取 兼容性更强 MdidSdkHelper.InitSdk
        int nres = MdidSdkHelper.InitSdk(this, true, (isSupported, idSupplier) -> {
            //isSupported 是否支持补充设备标识符获取
//            Logger.t("MSA").i("isSupported = " + isSupported);
            if (!isSupported) {
                sOAID = null;
            } else {
                //匿名设备标识符
                String oaid = idSupplier.getOAID();
                //开发者匿名设备标识符
                String vaid = idSupplier.getVAID();
                //应用匿名设备标识符
                String aaid = idSupplier.getAAID();
                idSupplier.shutDown();
                sOAID = oaid;
//                Logger.t("MSA").i("oaid = " + oaid + "\tvaid = " + vaid + "\taaid = " + aaid);
            }
        });
//        Logger.t("MSA").i("nres = " + nres);
        if (nres == ErrorCode.INIT_ERROR_MANUFACTURER_NOSUPPORT) {
            //1008611 不支持的厂商

        } else if (nres == ErrorCode.INIT_ERROR_DEVICE_NOSUPPORT) {
            //1008612 不支持的设备

        } else if (nres == ErrorCode.INIT_ERROR_LOAD_CONFIGFILE) {
            //1008613 加载配置文件失败

        } else if (nres == ErrorCode.INIT_ERROR_RESULT_DELAY) {
            //1008614 信息将会延迟返回，获取数据可能在异步线程，取决于设备

        } else if (nres == ErrorCode.INIT_HELPER_CALL_ERROR) {
            //1008615 反射调用失败

        }
    }

}
```

搞掂

- 使用的时候直接用 `App.sOAID`

```
String oaid = App.sOAID;
Toast.makeText(mContext, oaid, Toast.LENGTH_SHORT).show();
```
- 打完收工
