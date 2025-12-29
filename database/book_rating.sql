-- 图书打分系统数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS book_rating_db;
USE book_rating_db;

-- 用户表
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role ENUM('admin', 'user') DEFAULT 'user',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 图书表
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

-- 评分表
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

-- 插入示例数据
-- 用户数据（密码都是123456）
INSERT INTO users (username, password, email, role) VALUES
('admin', 'admin123', 'admin@example.com', 'admin'),
('user1', 'user123', 'user1@example.com', 'user'),
('user2', 'user123', 'user2@example.com', 'user'),
('张三', 'zhangsan123', 'zhangsan@example.com', 'user'),
('李四', 'lisi123', 'lisi@example.com', 'user');

-- 图书数据
INSERT INTO books (title, author, isbn, description, category) VALUES
('Java编程思想', 'Bruce Eckel', '9787111213826', 'Java经典教材，深入浅出讲解Java编程思想', '编程'),
('深入理解计算机系统', 'Randal E. Bryant', '9787111321330', '计算机系统经典教材，CSAPP圣经', '计算机科学'),
('算法导论', 'Thomas H. Cormen', '9787111407010', '算法经典教材，算法学习的圣经', '算法'),
('设计模式', 'Erich Gamma', '9787111167972', '设计模式经典，GOF23种设计模式', '软件工程'),
('Effective Java', 'Joshua Bloch', '9787111556052', 'Java最佳实践指南', '编程'),
('Python编程：从入门到实践', 'Eric Matthes', '9787115428028', 'Python入门经典教材', '编程'),
('数据库系统概念', 'Abraham Silberschatz', '9787111453824', '数据库系统经典教材', '数据库'),
('计算机网络', 'Andrew S. Tanenbaum', '9787115281488', '计算机网络经典教材', '网络'),
('操作系统概念', 'Abraham Silberschatz', '9787111543987', '操作系统经典教材', '操作系统'),
('代码大全', 'Steve McConnell', '9787121022982', '软件开发实践指南', '软件工程');

-- 评分数据
INSERT INTO ratings (user_id, book_id, rating, comment) VALUES
(2, 1, 4.5, '非常经典的Java教材，适合有一定基础的学习者'),
(2, 2, 5.0, '计算机系统必读，内容非常深入'),
(3, 1, 4.0, 'Java入门的好书，但有些内容比较难懂'),
(3, 3, 4.8, '算法学习的圣经，每个程序员都应该读'),
(4, 1, 4.2, '讲解很详细，适合自学'),
(4, 4, 4.5, '设计模式经典，工作中很实用'),
(5, 5, 4.7, 'Java开发必读，很多最佳实践'),
(5, 6, 4.3, 'Python入门好书，实例丰富'),
(2, 7, 4.1, '数据库系统经典，理论性较强'),
(3, 8, 4.6, '计算机网络权威教材'),
(4, 9, 4.4, '操作系统概念讲解清晰'),
(5, 10, 4.9, '代码大全，软件开发百科全书');

-- 更新图书平均评分
UPDATE books SET average_rating = (
    SELECT AVG(rating) FROM ratings WHERE book_id = books.id
) WHERE id IN (SELECT DISTINCT book_id FROM ratings);

-- 显示创建的表结构
SHOW TABLES;

-- 显示各表数据量
SELECT 'users' as table_name, COUNT(*) as record_count FROM users
UNION ALL
SELECT 'books', COUNT(*) FROM books
UNION ALL
SELECT 'ratings', COUNT(*) FROM ratings;

-- 显示图书平均评分
SELECT 
    b.title,
    b.author,
    b.category,
    b.average_rating,
    COUNT(r.id) as rating_count
FROM books b
LEFT JOIN ratings r ON b.id = r.book_id
GROUP BY b.id
ORDER BY b.average_rating DESC;