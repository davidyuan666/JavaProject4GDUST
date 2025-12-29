package com.bookrating.ui;

import com.bookrating.dao.UserDAO;
import com.bookrating.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        setSize(500, 450); // 进一步增加窗口大小
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        usernameField = new JTextField(30); // 进一步增加列数
        passwordField = new JPasswordField(30);

        // 美化输入框
        styleTextField(usernameField);
        styleTextField(passwordField);

        // 创建更美观的按钮
        loginButton = createStyledButton("登录", new Color(70, 130, 180), new Color(100, 160, 210));
        registerButton = createStyledButton("注册", new Color(46, 139, 87), new Color(66, 159, 107));

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
        button.setPreferredSize(new Dimension(150, 50)); // 进一步增加按钮大小
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
        field.setPreferredSize(new Dimension(350, 50)); // 进一步增加输入框大小

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

        // 使用GridBagLayout，这是最灵活的布局管理器
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // 标题
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("图书打分系统");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 36));
        titleLabel.setForeground(new Color(50, 50, 50));
        mainPanel.add(titleLabel, gbc);

        // 副标题
        gbc.gridy = 1;
        JLabel subtitleLabel = new JLabel("Book Rating System");
        subtitleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(150, 150, 150));
        mainPanel.add(subtitleLabel, gbc);

        // 用户名标签
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(20, 10, 5, 10);

        JLabel userLabel = new JLabel("用户名:");
        userLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        userLabel.setForeground(new Color(80, 80, 80));
        mainPanel.add(userLabel, gbc);

        // 用户名输入框
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.weightx = 1.0; // 让输入框可以扩展
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

        // 按钮面板
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 10, 10, 10);
        gbc.weightx = 0;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        mainPanel.add(buttonPanel, gbc);

        // 版本信息
        gbc.gridy = 5;
        gbc.insets = new Insets(20, 10, 0, 10);

        JLabel versionLabel = new JLabel("版本 1.0.0", SwingConstants.CENTER);
        versionLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        versionLabel.setForeground(new Color(180, 180, 180));
        mainPanel.add(versionLabel, gbc);

        // 添加弹性空间，使内容居中
        gbc.gridy = 6;
        gbc.weighty = 1.0;
        mainPanel.add(Box.createGlue(), gbc);

        // 设置窗口布局
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
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
        // 设置Swing外观为系统默认
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            }
        });
    }
}