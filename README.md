# 校园二手交易平台（XYETP）

面向校园场景的二手交易网站，基于 Spring MVC + MyBatis + MySQL，打包为 WAR 部署到 Tomcat。支持用户注册登录、发布/浏览商品、下单确认、地址管理以及后台管理。

## 功能概览
- 前台
  - 用户注册/登录、找回密码、头像上传
  - 商品发布（含多图上传）、分类浏览、搜索、详情
  - 下单与订单确认、收货地址管理
- 后台
  - 管理员登录
  - 商品上/下架、删除
  - 分类管理（含默认“其他”）
  - 订单、举报查看（可按需简化）
- UI
  - 首页 4 列商品卡片布局，蓝色清爽风格
  - 轮播已移除，直接突出“最新上架”

## 技术栈
- Java 8+，Spring MVC，MyBatis
- MySQL 5.7+/8.0
- 前端：JSP + jQuery + Layui
- 构建：Maven，打包 WAR 部署 Tomcat 9（8088）

## 环境要求
- JDK 1.8+
- Maven 3.5+
- MySQL 5.7+（本地 root 无密码可直接用，若有密码需改配置）
- Tomcat 9（示例使用 Homebrew 安装，端口 8088）

## 快速开始（本地 Mac，Homebrew Tomcat@9）
1) 编译打包  
   ```bash
   mvn -DskipTests package
   ```
2) 停 Tomcat  
   ```bash
   /opt/homebrew/opt/tomcat@9/bin/catalina stop
   ```
3) 部署 WAR  
   ```bash
   cp target/kd-second-hand-workshop.war /opt/homebrew/opt/tomcat@9/libexec/webapps/ROOT.war
   ```
4) 清理旧解压目录（防旧文件干扰）  
   ```bash
   rm -rf /opt/homebrew/opt/tomcat@9/libexec/webapps/ROOT
   ```
5) 启动 Tomcat  
   ```bash
   /opt/homebrew/opt/tomcat@9/bin/catalina start
   ```
6) 访问首页  
   http://localhost:8088/

## 数据库配置
- 配置文件：`src/main/resources/jdbc.properties`
- 默认连接：`jdbc:mysql://localhost:3306/market`，用户 `root`，无密码  
  如有密码，请修改 `username/password` 并重启 Tomcat。
- 初始化 SQL：`src/main/resources/market.sql`

## 图片与静态资源
- 图片存储：Tomcat 运行时目录 `/opt/homebrew/opt/tomcat@9/libexec/webapps/ROOT/images/web`
- 访问路径：`http://localhost:8088/assets/web/<文件名>`（前后端统一）
- 发布时上传的图片自动写入上述目录；手动放样例图可直接复制到该目录，数据库 `image.img_url` 只存文件名。

## 默认分类
- 发布页会自动初始化分类：数码电子、书籍教材、日用百货、运动出行、其他。
- 若分类下拉为空，可在 `catelog` 表手动插入 `name='其他', status=1`。

## 常见问题
- 端口占用/停不掉：`lsof -nP -i tcp:8088` 查 PID，`kill <PID>` 后重启 Tomcat。
- 图片 404：硬刷新；确认文件在 `webapps/ROOT/images/web`；数据库只存文件名；访问用 `/assets/web/文件名`。
- 样式没更新：Ctrl/Cmd+Shift+R 清缓存；或重新部署 WAR 并删掉旧的 ROOT 目录。
- MySQL 连接失败：检查 `jdbc.properties` 中的 URL/用户名/密码，改后重启。

## 部署到 GitHub（最简步骤）
```bash
git init
git add .
git commit -m "init: xyetp"
git branch -M main
git remote add origin https://github.com/<yourname>/<repo>.git
git push -u origin main
```
若用 HTTPS，请使用 GitHub Personal Access Token 作为密码。
