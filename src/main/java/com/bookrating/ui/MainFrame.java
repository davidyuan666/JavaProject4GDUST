package com.bookrating.ui;

import com.bookrating.ui.panels.BookListPanel;
import com.bookrating.ui.panels.RatingPanel;
import com.bookrating.ui.panels.ProfilePanel;
import com.bookrating.model.User;
import com.bookrating.dao.BookDAO;
import com.bookrating.dao.RatingDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private User currentUser;
    private BookDAO bookDAO;
    private RatingDAO ratingDAO;
    private JTabbedPane tabbedPane;

    public MainFrame(User user) {
        this.currentUser = user;
        this.bookDAO = new BookDAO();
        this.ratingDAO = new RatingDAO();

        setTitle("图书打分系统 - 欢迎 " + user.getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // 设置系统外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        // 设置主面板背景色
        getContentPane().setBackground(new Color(245, 247, 250));

        // 创建选项卡面板
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(245, 247, 250));
        tabbedPane.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // 创建各个面板
        BookListPanel bookListPanel = new BookListPanel(bookDAO);
        RatingPanel ratingPanel = new RatingPanel(currentUser, bookDAO, ratingDAO);
        ProfilePanel profilePanel = new ProfilePanel(currentUser, ratingDAO, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        // 添加选项卡
        tabbedPane.addTab("图书列表", bookListPanel);
        tabbedPane.addTab("我要评分", ratingPanel);
        tabbedPane.addTab("个人资料", profilePanel);

        // 如果是管理员，添加管理选项卡
        if ("admin".equals(currentUser.getRole())) {
            JPanel adminPanel = new JPanel();
            adminPanel.setBackground(new Color(245, 247, 250));
            adminPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            JLabel adminLabel = new JLabel("管理员功能（待实现）");
            adminLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
            adminLabel.setForeground(new Color(70, 130, 180));
            adminPanel.add(adminLabel);
            tabbedPane.addTab("管理", adminPanel);
        }
    }

    private void layoutComponents() {
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void logout() {
        int option = JOptionPane.showConfirmDialog(this,
                "确定要退出登录吗？", "确认退出",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            this.dispose();
        }
    }
}