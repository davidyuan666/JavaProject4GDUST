package com.bookrating.ui.components;

import javax.swing.*;
import java.awt.*;

public abstract class BasePanel extends JPanel {
    protected static final Color BACKGROUND_COLOR = new Color(245, 247, 250);

    // 使用浅色背景，黑色文字
    protected static final Color PRIMARY_COLOR = new Color(240, 248, 255); // 浅蓝色背景
    protected static final Color SUCCESS_COLOR = new Color(240, 255, 240); // 浅绿色背景
    protected static final Color DANGER_COLOR = new Color(255, 240, 240); // 浅红色背景
    protected static final Color WARNING_COLOR = new Color(255, 248, 220); // 浅黄色背景

    // 表头颜色 - 使用浅色背景，黑色文字
    protected static final Color TABLE_HEADER_BG = new Color(240, 240, 240); // 浅灰色背景
    protected static final Color TABLE_HEADER_FG = Color.BLACK; // 黑色文字

    // 按钮文字颜色 - 使用黑色
    protected static final Color BUTTON_TEXT_COLOR = Color.BLACK;

    protected static final Font TITLE_FONT = new Font("Microsoft YaHei", Font.BOLD, 16);
    protected static final Font LABEL_FONT = new Font("Microsoft YaHei", Font.BOLD, 14);
    protected static final Font TEXT_FONT = new Font("Microsoft YaHei", Font.PLAIN, 14);
    protected static final Font SMALL_FONT = new Font("Microsoft YaHei", Font.PLAIN, 13);
    protected static final Font TABLE_HEADER_FONT = new Font("Microsoft YaHei", Font.BOLD, 14);

    public BasePanel() {
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }

    protected JButton createStyledButton(String text, Color bgColor, int fontSize) {
        JButton button = new JButton(text);
        button.setFont(new Font("Microsoft YaHei", Font.BOLD, fontSize));
        button.setBackground(bgColor);
        button.setForeground(BUTTON_TEXT_COLOR); // 黑色文字
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1), // 灰色边框
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);

        // 强制设置文字颜色为黑色
        button.setForeground(Color.BLACK);

        // 添加悬停效果 - 稍微变深
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(darkenColor(bgColor, 0.9f));
                button.setForeground(Color.BLACK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
                button.setForeground(Color.BLACK);
            }
        });

        return button;
    }

    // 颜色变暗方法
    private Color darkenColor(Color color, float factor) {
        int r = (int) (color.getRed() * factor);
        int g = (int) (color.getGreen() * factor);
        int b = (int) (color.getBlue() * factor);
        return new Color(r, g, b);
    }
}