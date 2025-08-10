---
layout: post
tags: Dev
---

# JetBrains系列 配置文件保存位置

旧版本 设置目录 缓存目录 的默认保持位置

```
#---------------------------------------------------------------------
# Uncomment this option if you want to customize a path to the settings directory.
#---------------------------------------------------------------------
# idea.config.path=${user.home}/.IntelliJIdea版本号/config

#---------------------------------------------------------------------
# Uncomment this option if you want to customize a path to the caches directory.
#---------------------------------------------------------------------
# idea.system.path=${user.home}/.IntelliJIdea版本号/system
```

新版本移动到了 AppData 下 (官方文件里没有更新注释)

```
#---------------------------------------------------------------------
# Uncomment this option if you want to customize a path to the settings directory.
#---------------------------------------------------------------------
# idea.config.path=${user.home}/AppData/Roaming/JetBrains/IntelliJIdea版本号

#---------------------------------------------------------------------
# Uncomment this option if you want to customize a path to the caches directory.
#---------------------------------------------------------------------
# idea.system.path=${user.home}/AppData/Local/JetBrains/IntelliJIdea版本号
```

在IDEA的安装目录 `./bin/idea.properties` 可修改配置路径

可直接设置在IDE安装目录

```
idea.config.path=${idea.home.path}/config/settings
idea.system.path=${idea.home.path}/config/caches
```

或每个IDE集中保存配置文件

IDEA

```
config_home=Z:/.config
idea.config.path=${config_home}/JetBrains/IntelliJIdea2020.3/config
idea.system.path=${config_home}/JetBrains/IntelliJIdea2020.3/system
```

Clion

```
config_home=Z:/.config
idea.config.path=${config_home}/JetBrains/Clion2020.3/config
idea.system.path=${config_home}/JetBrains/Clion2020.3/system
```

PyCharm

```
config_home=Z:/.config
idea.config.path=${config_home}/JetBrains/PyCharm2020.3/config
idea.system.path=${config_home}/JetBrains/PyCharm2020.3/system
```

AndroidStudio

```
config_home=Z:/.config
idea.config.path=${config_home}/Google/AndroidStudio2022.1/config
idea.system.path=${config_home}/Google/AndroidStudio2022.1/system
```

DevEcoStudio

```
config_home=Z:/.config
idea.config.path=${config_home}/Huawei/DevEcoStudio3.0/config
idea.system.path=${config_home}/Huawei/DevEcoStudio3.0/system
```

重启后会提示是否导入旧配置文件到新位置

# Maven

修改配置文件 `./conf/settings.xml` 即可修改maven仓库目录

注意看是自己安装的maven还是idea插件的maven

自己安装的maven配置路径: `Z:\AwesomeProgram\apache-maven-3.6.3\conf\settings.xml`

idea插件maven3配置路径: `Z:\AwesomeProgram\IntelliJ\ideaIU-2020.3.4.win\plugins\maven\lib\maven3\conf\settings.xml`

```
  <!-- localRepository
   | The path to the local repository maven will use to store artifacts.
   |
   | Default: ${user.home}/.m2/repository
  <localRepository>/path/to/local/repo</localRepository>
```

仓库默认目录在User目录下: `C:\Users\Administrator\.m2\repository`

自定义修改为

```
  <localRepository>Z:\.repo\.m2\repository</localRepository>
```

# Gradle

环境变量 设置变量即可, 默认在 User 目录下

```
# GRADLE_USER_HOME=C:\Users\Administrator\.gradle
GRADLE_USER_HOME=Z:\.repo\.gradle
```

# Git SSH 等

```
~/.gitconfig
~/.ssh
```
