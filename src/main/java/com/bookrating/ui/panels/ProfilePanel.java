package com.bookrating.ui.panels;

import com.bookrating.ui.components.BasePanel;
import com.bookrating.dao.RatingDAO;
import com.bookrating.model.Rating;
import com.bookrating.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class ProfilePanel extends BasePanel {
    private User currentUser;
    private RatingDAO ratingDAO;
    private JButton logoutButton;

    public ProfilePanel(User user, RatingDAO ratingDAO, ActionListener logoutListener) {
        this.currentUser = user;
        this.ratingDAO = ratingDAO;
        initComponents();

        if (logoutListener != null) {
            logoutButton.addActionListener(logoutListener);
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));

        // 主内容面板
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 用户信息卡片
        JPanel profileCard = createProfileCard();

        // 统计信息面板
        JPanel statsPanel = createStatsPanel();

        // 退出按钮面板
        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(profileCard, BorderLayout.NORTH);
        mainPanel.add(statsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createProfileCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // 用户图标区域 - 移除表情符号
        JPanel iconPanel = new JPanel();
        iconPanel.setBackground(new Color(70, 130, 180));
        iconPanel.setPreferredSize(new Dimension(80, 80));
        iconPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel userIcon = new JLabel("用户", SwingConstants.CENTER);
        userIcon.setFont(new Font("Microsoft YaHei", Font.BOLD, 20));
        userIcon.setForeground(Color.WHITE);
        iconPanel.add(userIcon);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        card.add(iconPanel, gbc);

        gbc.gridheight = 1;
        gbc.gridx = 1;
        gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        usernameLabel.setForeground(Color.BLACK);
        card.add(usernameLabel, gbc);

        gbc.gridx = 2;
        JLabel usernameValue = new JLabel(currentUser.getUsername());
        usernameValue.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
        usernameValue.setForeground(new Color(50, 50, 50));
        card.add(usernameValue, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JLabel emailLabel = new JLabel("邮箱:");
        emailLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        emailLabel.setForeground(Color.BLACK);
        card.add(emailLabel, gbc);

        gbc.gridx = 2;
        JLabel emailValue = new JLabel(currentUser.getEmail());
        emailValue.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        emailValue.setForeground(new Color(70, 70, 70));
        card.add(emailValue, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        JLabel roleLabel = new JLabel("角色:");
        roleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        roleLabel.setForeground(Color.BLACK);
        card.add(roleLabel, gbc);

        gbc.gridx = 2;
        String roleText = "admin".equals(currentUser.getRole()) ? "管理员" : "普通用户";
        JLabel roleValue = new JLabel(roleText);
        roleValue.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        if ("admin".equals(currentUser.getRole())) {
            roleValue.setForeground(new Color(220, 53, 69));
        } else {
            roleValue.setForeground(new Color(40, 167, 69));
        }
        card.add(roleValue, gbc);

        return card;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // 已评分图书数
        List<Rating> userRatings = ratingDAO.getRatingsByUserId(currentUser.getId());
        int ratedBooksCount = userRatings.size();

        JPanel ratedPanel = createStatCard("已评分图书", // 移除表情符号
                String.valueOf(ratedBooksCount),
                new Color(52, 152, 219),
                "本");

        // 平均评分
        double avgUserRating = 0;
        if (ratedBooksCount > 0) {
            double total = 0;
            for (Rating rating : userRatings) {
                total += rating.getRating();
            }
            avgUserRating = total / ratedBooksCount;
        }

        JPanel avgRatingPanel = createStatCard("平均评分", // 移除表情符号
                String.format("%.2f", avgUserRating),
                new Color(46, 204, 113),
                "星");

        // 活跃天数（示例）
        JPanel activePanel = createStatCard("活跃天数", // 移除表情符号
                "1",
                new Color(155, 89, 182),
                "天");

        panel.add(ratedPanel);
        panel.add(avgRatingPanel);
        panel.add(activePanel);

        return panel;
    }

    private JPanel createStatCard(String title, String value, Color color, String unit) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(25, 20, 25, 20)));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        titleLabel.setForeground(new Color(80, 80, 80));

        JLabel valueLabel = new JLabel(value + " " + unit, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 28));
        valueLabel.setForeground(color);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));

        logoutButton = new JButton("退出登录");
        logoutButton.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.BLACK); // 改为黑色文字
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(192, 57, 43), 1),
                BorderFactory.createEmptyBorder(12, 40, 12, 40)));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setPreferredSize(new Dimension(200, 50));

        // 悬停效果
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(231, 76, 60).brighter());
                logoutButton.setForeground(Color.BLACK); // 保持黑色文字
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(231, 76, 60));
                logoutButton.setForeground(Color.BLACK); // 保持黑色文字
            }
        });

        panel.add(logoutButton);
        return panel;
    }
}