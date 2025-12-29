package com.bookrating.ui.components;

import javax.swing.*;
import java.awt.*;

public abstract class BasePanel extends JPanel {
    protected static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    protected static final Color PRIMARY_COLOR = new Color(52, 152, 219); // 更亮的蓝色
    protected static final Color SUCCESS_COLOR = new Color(46, 204, 113); // 更亮的绿色
    protected static final Color DANGER_COLOR = new Color(231, 76, 60); // 更亮的红色
    protected static final Color WARNING_COLOR = new Color(241, 196, 15); // 更亮的黄色

    // 表头颜色 - 使用更浅的颜色
    protected static final Color TABLE_HEADER_BG = new Color(70, 130, 180);
    protected static final Color TABLE_HEADER_FG = Color.WHITE;

    protected static final Font TITLE_FONT = new Font("Microsoft YaHei", Font.BOLD, 16);
    protected static final Font LABEL_FONT = new Font("Microsoft YaHei", Font.BOLD, 14);
    protected static final Font TEXT_FONT = new Font("Microsoft YaHei", Font.PLAIN, 14);
    protected static final Font SMALL_FONT = new Font("Microsoft YaHei", Font.PLAIN, 13);

    public BasePanel() {
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }

    protected JButton createStyledButton(String text, Color bgColor, int fontSize) {
        JButton button = new JButton(text);
        button.setFont(new Font("Microsoft YaHei", Font.BOLD, fontSize));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);

        // 确保文字清晰
        button.setForeground(Color.WHITE);

        // 添加悬停效果 - 使用更亮的颜色
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(brightenColor(bgColor, 1.2f));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    // 颜色变亮方法
    private Color brightenColor(Color color, float factor) {
        int r = Math.min(255, (int) (color.getRed() * factor));
        int g = Math.min(255, (int) (color.getGreen() * factor));
        int b = Math.min(255, (int) (color.getBlue() * factor));
        return new Color(r, g, b);
    }
}