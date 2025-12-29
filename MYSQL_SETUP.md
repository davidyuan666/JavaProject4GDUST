# MySQL 安装与配置指南

## 1. 下载MySQL

### Windows 用户
1. 访问 MySQL 官网：https://dev.mysql.com/downloads/installer/
2. 下载 MySQL Installer for Windows
3. 运行安装程序，选择 "Developer Default" 安装类型

### macOS 用户
```bash
# 使用 Homebrew 安装
brew install mysql

# 启动 MySQL 服务
brew services start mysql
```

### Linux 用户 (Ubuntu/Debian)
```bash
# 更新包列表
sudo apt update

# 安装 MySQL
sudo apt install mysql-server

# 启动 MySQL 服务
sudo systemctl start mysql
sudo systemctl enable mysql
```

## 2. 初始配置

### Windows/macOS
安装完成后，MySQL会提示设置root密码。请记住这个密码。

### Linux
```bash
# 运行安全安装脚本
sudo mysql_secure_installation

# 按照提示操作：
# 1. 设置root密码
# 2. 移除匿名用户
# 3. 禁止root远程登录
# 4. 移除测试数据库
# 5. 重新加载权限表
```

## 3. 验证安装

```bash
# 登录MySQL
mysql -u root -p

# 输入密码后，应该看到MySQL提示符
mysql>

# 查看版本
SELECT VERSION();

# 退出
EXIT;
```

## 4. 创建项目数据库

```bash
# 登录MySQL
mysql -u root -p

# 执行SQL脚本
source database/book_rating.sql

# 或者逐条执行：
CREATE DATABASE IF NOT EXISTS book_rating_db;
USE book_rating_db;

-- 然后复制 database/book_rating.sql 中的内容执行
```

## 5. 测试数据库连接

```bash
# 查看数据库
SHOW DATABASES;

# 查看表
USE book_rating_db;
SHOW TABLES;

# 查看用户数据
SELECT * FROM users LIMIT 5;

# 查看图书数据
SELECT * FROM books LIMIT 5;

# 查看评分数据
SELECT * FROM ratings LIMIT 5;
```

## 6. 常见问题

### Q1: 忘记root密码怎么办？

#### Windows
1. 停止MySQL服务
2. 创建文本文件 `reset_password.txt`，内容：
```sql
ALTER USER 'root'@'localhost' IDENTIFIED BY 'new_password';
```
3. 以安全模式启动MySQL：
```cmd
mysqld --init-file=C:\path\to\reset_password.txt --console
```
4. 重启正常服务

#### Linux/macOS
```bash
# 停止MySQL服务
sudo systemctl stop mysql  # 或 sudo service mysql stop

# 以安全模式启动
sudo mysqld_safe --skip-grant-tables &

# 登录MySQL（无需密码）
mysql -u root

# 修改密码
USE mysql;
UPDATE user SET authentication_string=PASSWORD('new_password') WHERE User='root';
FLUSH PRIVILEGES;
EXIT;

# 重启MySQL服务
sudo systemctl restart mysql  # 或 sudo service mysql restart
```

### Q2: 连接被拒绝怎么办？

检查以下事项：
1. MySQL服务是否运行
   ```bash
   # Windows
   net start mysql
   
   # Linux/macOS
   sudo systemctl status mysql
   ```

2. 防火墙是否阻止连接
   ```bash
   # Windows: 检查Windows Defender防火墙
   # Linux: sudo ufw allow mysql
   ```

3. 用户权限是否正确
   ```sql
   -- 在MySQL中执行
   GRANT ALL PRIVILEGES ON book_rating_db.* TO 'root'@'localhost';
   FLUSH PRIVILEGES;
   ```

### Q3: 如何修改数据库连接配置？

编辑 `src/main/java/com/bookrating/dao/DatabaseConnection.java`：

```java
// 修改以下配置
private static final String URL = "jdbc:mysql://localhost:3306/book_rating_db";
private static final String USERNAME = "root";
private static final String PASSWORD = "your_password_here";

// 如果MySQL运行在不同端口（默认3306）
// private static final String URL = "jdbc:mysql://localhost:3307/book_rating_db";

// 如果MySQL运行在远程服务器
// private static final String URL = "jdbc:mysql://your_server_ip:3306/book_rating_db";
```

## 7. MySQL 常用命令

```sql
-- 查看所有数据库
SHOW DATABASES;

-- 使用数据库
USE database_name;

-- 查看当前数据库所有表
SHOW TABLES;

-- 查看表结构
DESCRIBE table_name;

-- 创建新用户
CREATE USER 'username'@'localhost' IDENTIFIED BY 'password';

-- 授予权限
GRANT ALL PRIVILEGES ON database_name.* TO 'username'@'localhost';

-- 刷新权限
FLUSH PRIVILEGES;

-- 备份数据库
mysqldump -u root -p book_rating_db > backup.sql

-- 恢复数据库
mysql -u root -p book_rating_db < backup.sql
```

## 8. 图形化管理工具

推荐使用以下工具管理MySQL：

1. **MySQL Workbench** (官方工具)
   - 下载：https://dev.mysql.com/downloads/workbench/
   - 功能：数据库设计、SQL开发、数据管理

2. **phpMyAdmin** (Web界面)
   - 适合喜欢Web界面的用户
   - 需要PHP环境支持

3. **DBeaver** (跨平台)
   - 支持多种数据库
   - 免费开源
   - 下载：https://dbeaver.io/

## 9. 性能优化建议

对于学习项目，默认配置即可。如果数据量较大，可以考虑：

```sql
-- 为常用查询字段创建索引
CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_books_author ON books(author);
CREATE INDEX idx_ratings_book_id ON ratings(book_id);
CREATE INDEX idx_ratings_user_id ON ratings(user_id);

-- 优化表结构
OPTIMIZE TABLE books;
OPTIMIZE TABLE ratings;
OPTIMIZE TABLE users;
```

## 10. 安全建议

1. **不要使用默认密码**
2. **定期备份数据**
3. **限制远程访问**
4. **定期更新MySQL版本**
5. **使用强密码策略**

---

**注意**：本指南适用于学习和开发环境。生产环境需要更严格的安全配置。