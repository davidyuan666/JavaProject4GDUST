package com.bookrating.ui;

import com.bookrating.dao.UserDAO;
import com.bookrating.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JButton registerButton;
    private JButton cancelButton;
    private UserDAO userDAO;
    private LoginFrame loginFrame;
    
    public RegisterFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        userDAO = new UserDAO();
        
        setTitle("图书打分系统 - 注册");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);
        
        initComponents();
        layoutComponents();
    }
    
    private void initComponents() {
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        emailField = new JTextField(20);
        
        registerButton = new JButton("注册");
        cancelButton = new JButton("取消");
        
        // 注册按钮事件
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                String email = emailField.getText();
                
                // 验证输入
                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, 
                        "所有字段都必须填写！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, 
                        "两次输入的密码不一致！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!email.contains("@")) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, 
                        "请输入有效的邮箱地址！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // 检查用户名是否已存在
                if (userDAO.usernameExists(username)) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, 
                        "用户名已存在！请选择其他用户名。", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // 创建新用户
                User newUser = new User(username, password, email, "user");
                if (userDAO.addUser(newUser)) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, 
                        "注册成功！请使用新账户登录。", "成功", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(RegisterFrame.this, 
                        "注册失败！请稍后重试。", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // 取消按钮事件
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // 标题面板
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("用户注册");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        
        // 表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("用户名:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("密码:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("确认密码:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(confirmPasswordField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("邮箱:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        
        // 添加到主窗口
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // 设置按钮样式
        registerButton.setBackground(new Color(46, 139, 87));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        
        cancelButton.setBackground(new Color(220, 53, 69));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
    }
}