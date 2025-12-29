# 图书打分系统项目指南

## 项目概述

这是一个基于Java Swing和MySQL的图书打分系统，具有以下功能：

1. **用户管理**：注册、登录、个人资料查看
2. **图书管理**：查看图书列表、搜索图书
3. **评分系统**：对图书进行1-5星评分和评论
4. **数据统计**：显示图书平均评分、用户评分记录

## 项目结构

```
JavaProject4GDUST/
├── src/main/java/com/bookrating/
│   ├── Main.java                    # 程序入口
│   ├── model/                       # 数据模型
│   │   ├── User.java               # 用户模型
│   │   ├── Book.java               # 图书模型
│   │   └── Rating.java             # 评分模型
│   ├── dao/                        # 数据访问层
│   │   ├── DatabaseConnection.java # 数据库连接
│   │   ├── UserDAO.java            # 用户数据访问
│   │   ├── BookDAO.java            # 图书数据访问
│   │   └── RatingDAO.java          # 评分数据访问
│   ├── ui/                         # 用户界面
│   │   ├── LoginFrame.java         # 登录界面
│   │   ├── RegisterFrame.java      # 注册界面
│   │   └── MainFrame.java          # 主界面
│   └── util/                       # 工具类（待扩展）
├── lib/                            # 第三方库
├── database/                       # 数据库脚本
│   └── book_rating.sql             # 数据库初始化脚本
├── .vscode/                        # VSCode配置
│   ├── launch.json                 # 启动配置
│   └── settings.json               # 项目设置
├── README.md                       # 项目说明
└── PROJECT_GUIDE.md                # 项目指南（本文件）
```

## 环境要求

### 1. Java开发环境
- JDK 17或更高版本
- VSCode + Java Extension Pack

### 2. 数据库环境
- MySQL 5.7或更高版本
- MySQL Connector/J 8.0驱动

### 3. 项目依赖
- MySQL驱动：mysql-connector-java-8.0.33.jar

## 安装步骤

### 步骤1：安装MySQL数据库
1. 下载并安装MySQL Community Server
2. 启动MySQL服务
3. 使用root用户登录MySQL

### 步骤2：初始化数据库
```bash
# 登录MySQL
mysql -u root -p

# 执行初始化脚本
source database/book_rating.sql
```

### 步骤3：下载MySQL驱动
1. 访问 https://dev.mysql.com/downloads/connector/j/
2. 下载 Platform Independent 版本的ZIP文件
3. 解压并将 `mysql-connector-java-8.0.33.jar` 放入 `lib/` 目录

### 步骤4：配置数据库连接
如果需要修改数据库连接参数，编辑 `src/main/java/com/bookrating/dao/DatabaseConnection.java`：

```java
private static final String URL = "jdbc:mysql://localhost:3306/book_rating_db";
private static final String USERNAME = "root";
private static final String PASSWORD = "your_password";
```

## 运行项目

### 方法1：使用VSCode运行
1. 打开VSCode
2. 打开项目文件夹 `JavaProject4GDUST`
3. 打开 `src/main/java/com/bookrating/Main.java`
4. 点击右上角的 ▶️ **Run** 按钮
5. 或按 `F5` 启动调试

### 方法2：使用终端编译运行
```bash
# 编译所有Java文件
javac -cp "lib/*" -d bin src/main/java/com/bookrating/**/*.java

# 运行程序
java -cp "bin;lib/*" com.bookrating.Main
```

## 使用说明

### 1. 登录系统
- 默认管理员账户：`admin` / `admin123`
- 默认用户账户：`user1` / `user123`
- 可以注册新用户

### 2. 主要功能

#### 图书列表
- 查看所有图书信息
- 搜索图书（按书名、作者、分类）
- 显示图书平均评分

#### 我要评分
- 选择图书进行评分（1-5星）
- 添加评论
- 查看自己的评分记录

#### 个人资料
- 查看个人信息
- 退出登录

#### 管理员功能（待扩展）
- 图书管理（增删改查）
- 用户管理
- 数据统计

## 数据库说明

### 表结构

#### users 表（用户表）
```sql
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role ENUM('admin', 'user') DEFAULT 'user',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### books 表（图书表）
```sql
CREATE TABLE books (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100) NOT NULL,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    description TEXT,
    category VARCHAR(50),
    average_rating DECIMAL(3,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### ratings 表（评分表）
```sql
CREATE TABLE ratings (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    rating DECIMAL(2,1) NOT NULL CHECK (rating >= 0 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_book (user_id, book_id)
);
```

## 扩展功能建议

### 1. 功能扩展
- 图书管理功能（增删改查）
- 用户个人中心（修改密码、个人信息）
- 图书推荐算法
- 数据导出功能（Excel、PDF）
- 图表展示评分分布

### 2. 技术改进
- 使用连接池管理数据库连接
- 添加日志记录
- 实现密码加密存储
- 添加输入验证和异常处理
- 支持多语言界面

### 3. 界面美化
- 使用更现代的UI组件
- 添加动画效果
- 响应式布局
- 主题切换

## 常见问题

### Q1: 数据库连接失败
A: 检查以下事项：
1. MySQL服务是否启动
2. 数据库连接参数是否正确
3. MySQL驱动是否在lib目录中
4. 数据库用户权限是否足够

### Q2: 程序无法启动
A: 检查以下事项：
1. JDK版本是否为17或更高
2. 类路径是否正确
3. 是否有编译错误

### Q3: 界面显示异常
A: 尝试以下方法：
1. 重启VSCode
2. 清理并重新编译项目
3. 检查Java环境配置

## 技术支持

如有问题，请检查：
1. 环境配置是否正确
2. 数据库是否正常启动
3. 项目结构是否符合要求

---

**项目作者**：广科Java实训项目组  
**版本**：1.0.0  
**最后更新**：2025年12月29日