package com.bookrating.ui;

import com.bookrating.dao.UserDAO;
import com.bookrating.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private UserDAO userDAO;
    
    public LoginFrame() {
        userDAO = new UserDAO();
        
        setTitle("图书打分系统 - 登录");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        initComponents();
        layoutComponents();
    }
    
    private void initComponents() {
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        
        loginButton = new JButton("登录");
        registerButton = new JButton("注册");
        
        // 登录按钮事件
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginFrame.this, 
                        "用户名和密码不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // 验证登录
                User user = userDAO.authenticate(username, password);
                if (user != null) {
                    JOptionPane.showMessageDialog(LoginFrame.this, 
                        "登录成功！欢迎 " + user.getUsername(), "成功", JOptionPane.INFORMATION_MESSAGE);
                    openMainFrame(user);
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, 
                        "用户名或密码错误！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // 注册按钮事件
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegisterFrame();
            }
        });
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // 标题面板
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("图书打分系统");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
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
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        // 添加到主窗口
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // 设置按钮样式
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        
        registerButton.setBackground(new Color(46, 139, 87));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
    }
    
    private void openMainFrame(User user) {
        MainFrame mainFrame = new MainFrame(user);
        mainFrame.setVisible(true);
        this.dispose();
    }
    
    private void openRegisterFrame() {
        RegisterFrame registerFrame = new RegisterFrame(this);
        registerFrame.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            }
        });
    }
}