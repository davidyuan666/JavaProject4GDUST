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
        setLayout(new BorderLayout(10, 10));

        // 用户信息卡片
        JPanel profileCard = createProfileCard();

        // 统计信息面板
        JPanel statsPanel = createStatsPanel();

        // 退出按钮面板
        JPanel buttonPanel = createButtonPanel();

        add(profileCard, BorderLayout.NORTH);
        add(statsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createProfileCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(40, 40, 40, 40)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // 用户图标
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        JLabel userIcon = new JLabel("用户");
        userIcon.setFont(new Font("Microsoft YaHei", Font.BOLD, 48));
        userIcon.setForeground(PRIMARY_COLOR);
        card.add(userIcon, gbc);

        gbc.gridheight = 1;
        gbc.gridx = 1;
        gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setFont(LABEL_FONT);
        usernameLabel.setForeground(new Color(60, 60, 60));
        card.add(usernameLabel, gbc);

        gbc.gridx = 2;
        JLabel usernameValue = new JLabel(currentUser.getUsername());
        usernameValue.setFont(TEXT_FONT);
        usernameValue.setForeground(new Color(30, 30, 30));
        card.add(usernameValue, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JLabel emailLabel = new JLabel("邮箱:");
        emailLabel.setFont(LABEL_FONT);
        emailLabel.setForeground(new Color(60, 60, 60));
        card.add(emailLabel, gbc);

        gbc.gridx = 2;
        JLabel emailValue = new JLabel(currentUser.getEmail());
        emailValue.setFont(TEXT_FONT);
        emailValue.setForeground(new Color(30, 30, 30));
        card.add(emailValue, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        JLabel roleLabel = new JLabel("角色:");
        roleLabel.setFont(LABEL_FONT);
        roleLabel.setForeground(new Color(60, 60, 60));
        card.add(roleLabel, gbc);

        gbc.gridx = 2;
        String roleText = "admin".equals(currentUser.getRole()) ? "管理员" : "普通用户";
        JLabel roleValue = new JLabel(roleText);
        roleValue.setFont(TEXT_FONT);
        roleValue.setForeground("admin".equals(currentUser.getRole()) ? DANGER_COLOR : SUCCESS_COLOR);
        card.add(roleValue, gbc);

        return card;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 15, 0));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        // 已评分图书数
        List<Rating> userRatings = ratingDAO.getRatingsByUserId(currentUser.getId());
        int ratedBooksCount = userRatings.size();

        JPanel ratedPanel = createStatCard("已评分图书", String.valueOf(ratedBooksCount), PRIMARY_COLOR, "本");

        // 平均评分
        double avgUserRating = 0;
        if (ratedBooksCount > 0) {
            double total = 0;
            for (Rating rating : userRatings) {
                total += rating.getRating();
            }
            avgUserRating = total / ratedBooksCount;
        }

        JPanel avgRatingPanel = createStatCard("平均评分", String.format("%.2f", avgUserRating), SUCCESS_COLOR, "星");

        // 活跃天数（示例）
        JPanel activePanel = createStatCard("活跃天数", "1", WARNING_COLOR, "天");

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
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(LABEL_FONT);
        titleLabel.setForeground(new Color(100, 100, 100));

        JLabel valueLabel = new JLabel(value + " " + unit, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
        valueLabel.setForeground(color);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        logoutButton = createStyledButton("退出登录", DANGER_COLOR, 14);
        logoutButton.setPreferredSize(new Dimension(180, 45));

        panel.add(logoutButton);
        return panel;
    }
}