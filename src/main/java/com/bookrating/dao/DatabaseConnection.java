package com.bookrating.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/book_rating_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456"; // 修改为您的MySQL密码

    static {
        try {
            // 对于MySQL 8.0+ 使用这个驱动类
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL驱动加载成功");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL驱动加载失败: " + e.getMessage());
            e.printStackTrace();

            // 尝试加载旧版本驱动
            try {
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("MySQL旧版本驱动加载成功");
            } catch (ClassNotFoundException e2) {
                System.err.println("MySQL旧版本驱动也加载失败");
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        System.out.println("尝试连接数据库...");
        System.out.println("URL: " + URL);
        System.out.println("用户名: " + USERNAME);

        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("数据库连接成功");
            return conn;
        } catch (SQLException e) {
            System.err.println("数据库连接失败: " + e.getMessage());
            throw e;
        }
    }
}