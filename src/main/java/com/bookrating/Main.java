package com.bookrating;

import com.bookrating.ui.LoginFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // 设置Swing外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 启动登录界面
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            }
        });
    }
}