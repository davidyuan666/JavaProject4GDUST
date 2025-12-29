package com.bookrating.ui;

import com.bookrating.dao.UserDAO;
import com.bookrating.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        setSize(500, 500); // 增加窗口大小以容纳更多字段
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        usernameField = new JTextField(30);
        passwordField = new JPasswordField(30);
        confirmPasswordField = new JPasswordField(30);
        emailField = new JTextField(30);

        // 美化输入框
        styleTextField(usernameField);
        styleTextField(passwordField);
        styleTextField(confirmPasswordField);
        styleTextField(emailField);

        // 创建与登录界面一致的按钮
        registerButton = createStyledButton("注册", new Color(46, 139, 87), new Color(66, 159, 107));
        cancelButton = createStyledButton("取消", new Color(220, 53, 69), new Color(240, 73, 89));

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

    private JButton createStyledButton(String text, Color normalColor, Color hoverColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(hoverColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(hoverColor);
                } else {
                    g2.setColor(normalColor);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setForeground(Color.WHITE);
        button.setFont(new Font("微软雅黑", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(150, 50));
        button.setMinimumSize(new Dimension(150, 50));

        // 添加鼠标悬停效果
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return button;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)));
        field.setBackground(new Color(250, 250, 250));
        field.setPreferredSize(new Dimension(350, 50));

        // 添加焦点效果
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                        BorderFactory.createEmptyBorder(11, 14, 11, 14)));
                field.setBackground(Color.WHITE);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                        BorderFactory.createEmptyBorder(12, 15, 12, 15)));
                field.setBackground(new Color(250, 250, 250));
            }
        });
    }

    private void layoutComponents() {
        // 设置窗口背景色
        getContentPane().setBackground(new Color(245, 245, 245));

        // 使用GridBagLayout保持与登录界面一致
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // 标题
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("用户注册");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 32));
        titleLabel.setForeground(new Color(50, 50, 50));
        mainPanel.add(titleLabel, gbc);

        // 副标题
        gbc.gridy = 1;
        JLabel subtitleLabel = new JLabel("Create New Account");
        subtitleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(150, 150, 150));
        mainPanel.add(subtitleLabel, gbc);

        // 用户名标签
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(15, 10, 5, 10);

        JLabel userLabel = new JLabel("用户名:");
        userLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        userLabel.setForeground(new Color(80, 80, 80));
        mainPanel.add(userLabel, gbc);

        // 用户名输入框
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.weightx = 1.0;
        mainPanel.add(usernameField, gbc);

        // 密码标签
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.weightx = 0;

        JLabel passLabel = new JLabel("密码:");
        passLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        passLabel.setForeground(new Color(80, 80, 80));
        mainPanel.add(passLabel, gbc);

        // 密码输入框
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.weightx = 1.0;
        mainPanel.add(passwordField, gbc);

        // 确认密码标签
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.weightx = 0;

        JLabel confirmPassLabel = new JLabel("确认密码:");
        confirmPassLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        confirmPassLabel.setForeground(new Color(80, 80, 80));
        mainPanel.add(confirmPassLabel, gbc);

        // 确认密码输入框
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.weightx = 1.0;
        mainPanel.add(confirmPasswordField, gbc);

        // 邮箱标签
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.weightx = 0;

        JLabel emailLabel = new JLabel("邮箱:");
        emailLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        emailLabel.setForeground(new Color(80, 80, 80));
        mainPanel.add(emailLabel, gbc);

        // 邮箱输入框
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.weightx = 1.0;
        mainPanel.add(emailField, gbc);

        // 按钮面板
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 10, 10, 10);
        gbc.weightx = 0;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, gbc);

        // 提示信息
        gbc.gridy = 7;
        gbc.insets = new Insets(20, 10, 0, 10);

        JLabel hintLabel = new JLabel("已有账户？返回登录界面", SwingConstants.CENTER);
        hintLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        hintLabel.setForeground(new Color(150, 150, 150));
        mainPanel.add(hintLabel, gbc);

        // 添加弹性空间
        gbc.gridy = 8;
        gbc.weighty = 1.0;
        mainPanel.add(Box.createGlue(), gbc);

        // 设置窗口布局
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
    }
}