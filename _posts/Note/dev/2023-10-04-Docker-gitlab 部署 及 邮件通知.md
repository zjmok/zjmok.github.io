---
layout: post
tags: Docker Gitlab
---

# 使用 docker 部署 gitlab

## 部署 gitlab 服务

拉取镜像，可选 (这里不拉取，创建容器时也会自动拉取)

```
sudo docker pull gitlab/gitlab-ce:latest
```

创建容器并运行

```
sudo docker run --detach \
  --hostname <your_hostname> \
  --publish 443:443 --publish 80:80 --publish 22:22 \
  --name gitlab \
  --restart always \
  --volume </path/to/config>:/etc/gitlab \
  --volume </path/to/logs>:/var/log/gitlab \
  --volume </path/to/data>:/var/opt/gitlab \
  gitlab/gitlab-ce:latest
```

`--hostname` 指定主机名，域名 或 IP，(局域网 可填写 IP 或 DNS 可解析的 域名，自己本机使用可填写 localhost)  
邮件的链接的 hostname 等，填可访问的域名或 ip

挂载目录为: 配置、 日志、 数据

## 配置

### 初始账户

登录 http://<your_hostname>/

初始账户 `root`

初始密码在配置目录的文件 `/etc/gitlab/initial_root_password`, 也可以按后面的步骤重置一个新密码

```
sudo docker exec -it gitlab grep 'Password:' /etc/gitlab/initial_root_password
```

### 配置管理员邮箱

配置 SMTP 相关，打开 gitlab.rb

```
vi /etc/gitlab/gitlab.rb
```

内容

```
gitlab_rails['smtp_enable'] = true
gitlab_rails['smtp_address'] = "smtp.server"
gitlab_rails['smtp_port'] = 465
# 邮箱
gitlab_rails['smtp_user_name'] = "smtp user"
# 授权码
gitlab_rails['smtp_password'] = "smtp password"
# smtp_domain 是你自己的域名或者与你的邮箱帐户关联的域名，hostname
gitlab_rails['smtp_domain'] = "example.com"
gitlab_rails['smtp_authentication'] = "login"
# smtp_enable_starttls_auto 和 smtp_tls 只能二选一，建议直接开启 smtp_tls 为 true
gitlab_rails['smtp_enable_starttls_auto'] = false
gitlab_rails['smtp_tls'] = true
gitlab_rails['smtp_pool'] = false

# 管理员邮箱设置，可能配置不成功，需要通过 root 的设置界面修改，或直接修改数据库
gitlab_rails['gitlab_email_enabled'] = true
gitlab_rails['gitlab_email_from'] = 'example@example.com' # 发件人邮箱
gitlab_rails['gitlab_email_reply_to'] = "example@example.com" # 回复邮箱
gitlab_rails['gitlab_email_display_name'] = "GitLab" # 邮件显示名称
```

需要注意这里的 tls 协议和对应端口号。

邮箱 | TSL 端口 | 非 TSL 端口 | SMTP 地址
--- | --- | --- | ---
163 | 465 或 994 | 25 或 80 | smtp.163.com
QQ | 465 | 25 或 587 | smtp.qq.com
Gmail | 465 | 25 或 587 | smtp.gmail.com
Mail.ru | 465 | 25 或 587 | smtp.mail.ru
Outlook | 465 | 25 或 587 | smtp.live.com
Yahoo | 465 | 25 或 587 | smtp.mail.yahoo.com
iCloud | 465 | 25 或 587 | smtp.mail.me.com

进入容器

```
sudo docker exec -it gitlab bash
```

更新配置

```
gitlab-ctl reconfigure
```

进入 Rails 控制台

```
gitlab-rails console
```

测试发送邮件

```
Notify.test_email('recipient@example.com', 'This is a test email', 'Test').deliver_now
```

到此，发送成功即可

注意 --hostname 没有填写或者填写不对时，需要手动把邮件确认链接的 hostname 改为部署 gitlab 的设备的 IP

### 其它命令

#### 重启服务，在容器执行

```
gitlab-ctl restart
```

#### Gitlab Rails 控制台

进入 Rails 控制台

```
gitlab-rails console
```

控制台相关命令

#### 查看smpt配置

```
// 检查邮件的协议，进入 `gitlab-rails console` 执行
ActionMailer::Base.delivery_method

// 查看smpt配置，进入 `gitlab-rails console` 执行
ActionMailer::Base.smtp_settings
```

#### 修改用户密码

```
// 获取 id=1 的用户
user = User.where(id: 1).first

// 设置新密码
user.password = '新密码'

// 保存
user.save
```

#### 修改管理员邮箱

- 在 root 页面上添加一个新邮箱后再删除 root 用户的 admin@example.com

管理员账户的邮箱地址直接存储在 PostgreSQL 数据库中，这个值会覆盖 gitlab.rb 的部分设置

- 直接修改数据库修改邮箱

控制台执行

```
# 查找管理员账户（通常是root）
admin = User.find_by(username: 'root')

# 更新邮箱并跳过验证（生产环境谨慎使用）
admin.update!(email: 'admin@your-real-domain.com', skip_reconfirmation: true)

# 同时更新通知邮箱
admin.notification_email = 'admin@your-real-domain.com'
admin.save!

# 验证修改
admin.reload
puts "当前管理员邮箱: #{admin.email}"
puts "当前通知邮箱: #{admin.notification_email}"
exit
```

---
---

## docker-compose

```
version: '3'

services:
  gitlab:
    image: gitlab/gitlab-ce:latest
    container_name: gitlab
    restart: always
    hostname: 'gitlab.example.com' # 邮件的链接的 hostname 等，填可访问的域名或 ip
    environment:
      GITLAB_OMNIBUS_CONFIG: |
        external_url 'http://gitlab.example.com'  # 替换为你的URL
        # 其他GitLab配置可以在这里添加
        # gitlab_rails['gitlab_shell_ssh_port'] = 2222
        # nginx['listen_port'] = 80
        # nginx['listen_https'] = false  # 如果你在前面使用反向代理
        # SMTP配置示例
        gitlab_rails['smtp_enable'] = true
        gitlab_rails['smtp_address'] = "smtp.example.com"
        gitlab_rails['smtp_port'] = 587
        gitlab_rails['smtp_user_name'] = "user@example.com"
        gitlab_rails['smtp_password'] = "password"
        gitlab_rails['smtp_domain'] = "example.com"
        gitlab_rails['smtp_authentication'] = "login"
        gitlab_rails['smtp_enable_starttls_auto'] = false
        gitlab_rails['smtp_tls'] = true
        gitlab_rails['smtp_pool'] = true
        # 管理员邮箱设置，需要通过 root 的设置界面修改，或修改数据库
        gitlab_rails['gitlab_email_enabled'] = true
        gitlab_rails['gitlab_email_from'] = 'gitlab@yourdomain.com'  # 发件人邮箱
        gitlab_rails['gitlab_email_reply_to'] = 'noreply@yourdomain.com'  # 回复邮箱
        gitlab_rails['gitlab_email_display_name'] = 'GitLab'  # 邮件显示名称
    ports:
      - "80:80"
      - "443:443"  # 如果需要HTTPS
      - "22:22"    # Git SSH端口
    volumes:
      - gitlab_config:/etc/gitlab
      - gitlab_logs:/var/log/gitlab
      - gitlab_data:/var/opt/gitlab
    networks:
      - gitlab_network

volumes:
  gitlab_config: # 配置文件
  gitlab_logs: # 日志文件
  gitlab_data: # 存储仓库数据、上传的附件、数据库数据等

networks:
  gitlab_network:
    driver: bridge
```

可选额外挂载

```
volumes:
  - ./gitlab.rb:/etc/gitlab/gitlab.rb # 挂载自定义配置文件
  - ./ssh:/etc/ssh # 自定义SSH配置
  - ./certs:/etc/gitlab/ssl # SSL证书目录
```
