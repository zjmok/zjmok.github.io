---
layout: post
tags: Windows 批处理
---

# Windows bat 批处理 选择 的 写法

## set /p

```
set /p value=确认请按数字1然后按回车，否则取消执行:
if "1"=="%value%" (goto tag1) else (goto tag2)
:tag1
echo 111
goto end
:tag2
echo 222
goto end
:end
```

## choice

CHOICE [/C choices] [/N] [/CS] [/T timeout /D choice] [/M text]
- /C    choices       指定要创建的选项列表。默认列表是 "YN"。
- /N                  在提示符中隐藏选项列表。提示前面的消息得到显示，选项依旧处于启用状态。
- /M    text          指定提示之前要显示的消息。如果没有指定，工具只显示提示。

/n 加黑洞 完全隐藏输入内容
choice /c 123 /n >nul

```
choice /c 123 /m 请选择
if %errorlevel%==1 echo 111
if %errorlevel%==2 echo 222
if %errorlevel%==3 echo 333
```
