package com.bookrating.ui;

import com.bookrating.model.User;
import com.bookrating.dao.BookDAO;
import com.bookrating.dao.RatingDAO;
import com.bookrating.model.Book;
import com.bookrating.model.Rating;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainFrame extends JFrame {
    private User currentUser;
    private BookDAO bookDAO;
    private RatingDAO ratingDAO;
    
    private JTabbedPane tabbedPane;
    private JPanel bookListPanel;
    private JPanel ratingPanel;
    private JPanel profilePanel;
    
    private JTable bookTable;
    private DefaultTableModel bookTableModel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton refreshButton;
    
    private JComboBox<Book> bookComboBox;
    private JSpinner ratingSpinner;
    private JTextArea commentArea;
    private JButton submitRatingButton;
    private JTextArea myRatingsArea;
    
    public MainFrame(User user) {
        this.currentUser = user;
        this.bookDAO = new BookDAO();
        this.ratingDAO = new RatingDAO();
        
        setTitle("图书打分系统 - 欢迎 " + user.getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        initComponents();
        layoutComponents();
        loadBooks();
        loadMyRatings();
    }
    
    private void initComponents() {
        // 创建选项卡面板
        tabbedPane = new JTabbedPane();
        
        // 图书列表面板
        bookListPanel = new JPanel(new BorderLayout());
        initBookListPanel();
        
        // 评分面板
        ratingPanel = new JPanel(new BorderLayout());
        initRatingPanel();
        
        // 个人资料面板
        profilePanel = new JPanel(new BorderLayout());
        initProfilePanel();
        
        // 添加选项卡
        tabbedPane.addTab("图书列表", bookListPanel);
        tabbedPane.addTab("我要评分", ratingPanel);
        tabbedPane.addTab("个人资料", profilePanel);
        
        // 如果是管理员，添加管理选项卡
        if ("admin".equals(currentUser.getRole())) {
            JPanel adminPanel = new JPanel();
            adminPanel.add(new JLabel("管理员功能（待实现）"));
            tabbedPane.addTab("管理", adminPanel);
        }
    }
    
    private void initBookListPanel() {
        // 搜索面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        searchButton = new JButton("搜索");
        refreshButton = new JButton("刷新");
        
        searchPanel.add(new JLabel("搜索:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);
        
        // 图书表格
        String[] columns = {"ID", "书名", "作者", "ISBN", "分类", "平均评分"};
        bookTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable = new JTable(bookTableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        
        // 按钮事件
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBooks();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBooks();
            }
        });
        
        // 添加到面板
        bookListPanel.add(searchPanel, BorderLayout.NORTH);
        bookListPanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void initRatingPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 选择图书
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("选择图书:"), gbc);
        
        gbc.gridx = 1;
        bookComboBox = new JComboBox<>();
        mainPanel.add(bookComboBox, gbc);
        
        // 评分
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("评分 (1-5):"), gbc);
        
        gbc.gridx = 1;
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(3.0, 1.0, 5.0, 0.5);
        ratingSpinner = new JSpinner(spinnerModel);
        mainPanel.add(ratingSpinner, gbc);
        
        // 评论
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("评论:"), gbc);
        
        gbc.gridx = 1;
        commentArea = new JTextArea(5, 30);
        commentArea.setLineWrap(true);
        JScrollPane commentScroll = new JScrollPane(commentArea);
        mainPanel.add(commentScroll, gbc);
        
        // 提交按钮
        gbc.gridx = 1; gbc.gridy = 3;
        submitRatingButton = new JButton("提交评分");
        submitRatingButton.setBackground(new Color(70, 130, 180));
        submitRatingButton.setForeground(Color.WHITE);
        submitRatingButton.setFocusPainted(false);
        mainPanel.add(submitRatingButton, gbc);
        
        // 我的评分记录
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        mainPanel.add(new JLabel("我的评分记录:"), gbc);
        
        gbc.gridy = 5;
        myRatingsArea = new JTextArea(10, 40);
        myRatingsArea.setEditable(false);
        JScrollPane ratingsScroll = new JScrollPane(myRatingsArea);
        mainPanel.add(ratingsScroll, gbc);
        
        // 按钮事件
        submitRatingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitRating();
            }
        });
        
        ratingPanel.add(mainPanel, BorderLayout.CENTER);
    }
    
    private void initProfilePanel() {
        JPanel profileInfoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        profileInfoPanel.add(new JLabel("用户名:"), gbc);
        
        gbc.gridx = 1;
        profileInfoPanel.add(new JLabel(currentUser.getUsername()), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        profileInfoPanel.add(new JLabel("邮箱:"), gbc);
        
        gbc.gridx = 1;
        profileInfoPanel.add(new JLabel(currentUser.getEmail()), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        profileInfoPanel.add(new JLabel("角色:"), gbc);
        
        gbc.gridx = 1;
        String roleText = "admin".equals(currentUser.getRole()) ? "管理员" : "普通用户";
        profileInfoPanel.add(new JLabel(roleText), gbc);
        
        // 退出按钮
        JButton logoutButton = new JButton("退出登录");
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(logoutButton);
        
        profilePanel.add(profileInfoPanel, BorderLayout.CENTER);
        profilePanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void layoutComponents() {
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private void loadBooks() {
        // 清空表格
        bookTableModel.setRowCount(0);
        
        // 加载图书数据
        List<Book> books = bookDAO.getAllBooks();
        for (Book book : books) {
            Object[] row = {
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getCategory(),
                String.format("%.2f", book.getAverageRating())
            };
            bookTableModel.addRow(row);
        }
        
        // 更新下拉框
        updateBookComboBox();
    }
    
    private void searchBooks() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadBooks();
            return;
        }
        
        // 清空表格
        bookTableModel.setRowCount(0);
        
        // 搜索图书
        List<Book> books = bookDAO.searchBooks(keyword);
        for (Book book : books) {
            Object[] row = {
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getCategory(),
                String.format("%.2f", book.getAverageRating())
            };
            bookTableModel.addRow(row);
        }
    }
    
    private void updateBookComboBox() {
        bookComboBox.removeAllItems();
        List<Book> books = bookDAO.getAllBooks();
        for (Book book : books) {
            bookComboBox.addItem(book);
        }
    }
    
    private void submitRating() {
        Book selectedBook = (Book) bookComboBox.getSelectedItem();
        if (selectedBook == null) {
            JOptionPane.showMessageDialog(this, "请选择一本书！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double rating = (Double) ratingSpinner.getValue();
        String comment = commentArea.getText().trim();
        
        // 检查是否已经评分过
        if (ratingDAO.hasUserRatedBook(currentUser.getId(), selectedBook.getId())) {
            int option = JOptionPane.showConfirmDialog(this, 
                "您已经对这本书评分过了，是否更新评分？", "确认", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                // TODO: 更新评分
                JOptionPane.showMessageDialog(this, "更新评分功能待实现", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
            return;
        }
        
        // 创建评分对象
        Rating newRating = new Rating(currentUser.getId(), selectedBook.getId(), rating, comment);
        
        if (ratingDAO.addRating(newRating)) {
            // 更新图书平均评分
            bookDAO.updateAverageRating(selectedBook.getId());
            
            JOptionPane.showMessageDialog(this, "评分提交成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            commentArea.setText("");
            loadMyRatings();
            loadBooks(); // 刷新图书列表以更新平均评分
        } else {
            JOptionPane.showMessageDialog(this, "评分提交失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadMyRatings() {
        List<Rating> ratings = ratingDAO.getRatingsByUserId(currentUser.getId());
        StringBuilder sb = new StringBuilder();
        
        for (Rating rating : ratings) {
            Book book = bookDAO.getBookById(rating.getBookId());
            if (book != null) {
                sb.append("书名: ").append(book.getTitle()).append("\n");
                sb.append("评分: ").append(rating.getRating()).append(" 星\n");
                sb.append("评论: ").append(rating.getComment()).append("\n");
                sb.append("时间: ").append(rating.getCreatedAt()).append("\n");
                sb.append("-".repeat(40)).append("\n");
            }
        }
        
        if (sb.length() == 0) {
            sb.append("您还没有对任何图书评分。");
        }
        
        myRatingsArea.setText(sb.toString());
    }
    
    private void logout() {
        int option = JOptionPane.showConfirmDialog(this, 
            "确定要退出登录吗？", "确认", JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            this.dispose();
        }
    }
}