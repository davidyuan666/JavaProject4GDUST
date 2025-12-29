package com.bookrating.ui;

import com.bookrating.model.User;
import com.bookrating.dao.BookDAO;
import com.bookrating.dao.RatingDAO;
import com.bookrating.model.Book;
import com.bookrating.model.Rating;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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

        setTitle("📚 图书打分系统 - 欢迎 " + user.getUsername());
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
        loadBooks();
        loadMyRatings();
    }

    private void initComponents() {
        // 设置主面板背景色
        getContentPane().setBackground(new Color(245, 247, 250));

        // 创建选项卡面板
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(245, 247, 250));
        tabbedPane.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // 图书列表面板
        bookListPanel = new JPanel(new BorderLayout(10, 10));
        bookListPanel.setBackground(new Color(245, 247, 250));
        bookListPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initBookListPanel();

        // 评分面板
        ratingPanel = new JPanel(new BorderLayout(10, 10));
        ratingPanel.setBackground(new Color(245, 247, 250));
        ratingPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initRatingPanel();

        // 个人资料面板
        profilePanel = new JPanel(new BorderLayout(10, 10));
        profilePanel.setBackground(new Color(245, 247, 250));
        profilePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initProfilePanel();

        // 添加选项卡
        tabbedPane.addTab("📖 图书列表", bookListPanel);
        tabbedPane.addTab("⭐ 我要评分", ratingPanel);
        tabbedPane.addTab("👤 个人资料", profilePanel);

        // 如果是管理员，添加管理选项卡
        if ("admin".equals(currentUser.getRole())) {
            JPanel adminPanel = new JPanel();
            adminPanel.setBackground(new Color(245, 247, 250));
            adminPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            JLabel adminLabel = new JLabel("⚙️ 管理员功能（待实现）");
            adminLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
            adminLabel.setForeground(new Color(70, 130, 180));
            adminPanel.add(adminLabel);
            tabbedPane.addTab("⚙️ 管理", adminPanel);
        }
    }

    private void initBookListPanel() {
        // 搜索面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchPanel.setBackground(new Color(245, 247, 250));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel searchLabel = new JLabel("🔍 搜索图书:");
        searchLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        searchLabel.setForeground(new Color(60, 60, 60));

        searchField = new JTextField(25);
        searchField.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(300, 38));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        searchButton = createStyledButton("搜索", new Color(70, 130, 180), 14);
        refreshButton = createStyledButton("刷新", new Color(46, 125, 50), 14);

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);

        // 图书表格
        String[] columns = { "ID", "书名", "作者", "ISBN", "分类", "平均评分" };
        bookTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 5)
                    return Double.class;
                return String.class;
            }
        };

        bookTable = new JTable(bookTableModel);
        bookTable.setFont(new Font("Microsoft YaHei", Font.PLAIN, 13));
        bookTable.setRowHeight(35);
        bookTable.setSelectionBackground(new Color(220, 240, 255));
        bookTable.setSelectionForeground(Color.BLACK);
        bookTable.setGridColor(new Color(230, 230, 230));
        bookTable.setShowGrid(true);
        bookTable.setIntercellSpacing(new Dimension(1, 1));

        // 设置表头
        bookTable.getTableHeader().setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        bookTable.getTableHeader().setBackground(new Color(70, 130, 180));
        bookTable.getTableHeader().setForeground(Color.WHITE);
        bookTable.getTableHeader().setReorderingAllowed(false);

        // 设置列宽
        bookTable.getColumnModel().getColumn(0).setPreferredWidth(60); // ID
        bookTable.getColumnModel().getColumn(1).setPreferredWidth(250); // 书名
        bookTable.getColumnModel().getColumn(2).setPreferredWidth(180); // 作者
        bookTable.getColumnModel().getColumn(3).setPreferredWidth(150); // ISBN
        bookTable.getColumnModel().getColumn(4).setPreferredWidth(120); // 分类
        bookTable.getColumnModel().getColumn(5).setPreferredWidth(100); // 平均评分

        // 设置评分列的渲染器
        bookTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (value instanceof Double) {
                    double rating = (Double) value;
                    setText(String.format("%.2f", rating));

                    // 根据评分设置颜色
                    if (rating >= 4.5) {
                        setForeground(new Color(46, 125, 50)); // 绿色
                        setFont(getFont().deriveFont(Font.BOLD));
                    } else if (rating >= 3.5) {
                        setForeground(new Color(255, 152, 0)); // 橙色
                    } else if (rating >= 2.5) {
                        setForeground(new Color(244, 67, 54)); // 红色
                    } else {
                        setForeground(new Color(158, 158, 158)); // 灰色
                    }
                }

                // 设置居中对齐
                setHorizontalAlignment(SwingConstants.CENTER);

                return c;
            }
        });

        // 设置其他列居中对齐
        for (int i = 0; i < bookTable.getColumnCount(); i++) {
            if (i != 1) { // 书名列左对齐，其他居中
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
                bookTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)));
        scrollPane.getViewport().setBackground(Color.WHITE);

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
        // 主面板使用垂直分割
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setResizeWeight(0.5);
        mainSplitPane.setDividerSize(5);
        mainSplitPane.setBorder(BorderFactory.createEmptyBorder());

        // 上半部分：评分表单
        JPanel ratingFormPanel = new JPanel(new GridBagLayout());
        ratingFormPanel.setBackground(Color.WHITE);
        ratingFormPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                        "📝 评分表单",
                        javax.swing.border.TitledBorder.LEFT,
                        javax.swing.border.TitledBorder.TOP,
                        new Font("Microsoft YaHei", Font.BOLD, 16),
                        new Color(70, 130, 180)),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // 选择图书
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel bookLabel = new JLabel("📚 选择图书:");
        bookLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        bookLabel.setForeground(new Color(60, 60, 60));
        ratingFormPanel.add(bookLabel, gbc);

        gbc.gridx = 1;
        bookComboBox = new JComboBox<>();
        bookComboBox.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        bookComboBox.setPreferredSize(new Dimension(350, 40));
        bookComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Book) {
                    Book book = (Book) value;
                    setText(book.getTitle() + " - " + book.getAuthor());
                }
                return c;
            }
        });
        ratingFormPanel.add(bookComboBox, gbc);

        // 评分
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel ratingLabel = new JLabel("⭐ 评分 (1-5):");
        ratingLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        ratingLabel.setForeground(new Color(60, 60, 60));
        ratingFormPanel.add(ratingLabel, gbc);

        gbc.gridx = 1;
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(3.0, 1.0, 5.0, 0.5);
        ratingSpinner = new JSpinner(spinnerModel);
        ratingSpinner.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        ratingSpinner.setPreferredSize(new Dimension(120, 40));
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) ratingSpinner.getEditor();
        editor.getTextField().setHorizontalAlignment(JTextField.CENTER);
        ratingFormPanel.add(ratingSpinner, gbc);

        // 显示评分星星
        gbc.gridx = 2;
        JLabel starLabel = new JLabel("★★★★★");
        starLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        starLabel.setForeground(new Color(255, 193, 7));
        ratingFormPanel.add(starLabel, gbc);

        // 评论
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel commentLabel = new JLabel("💬 评论:");
        commentLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        commentLabel.setForeground(new Color(60, 60, 60));
        ratingFormPanel.add(commentLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        commentArea = new JTextArea(5, 30);
        commentArea.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        JScrollPane commentScroll = new JScrollPane(commentArea);
        commentScroll.setPreferredSize(new Dimension(400, 120));
        ratingFormPanel.add(commentScroll, gbc);

        // 提交按钮
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        submitRatingButton = createStyledButton("⭐ 提交评分", new Color(46, 125, 50), 16);
        submitRatingButton.setPreferredSize(new Dimension(200, 50));
        ratingFormPanel.add(submitRatingButton, gbc);

        // 下半部分：我的评分记录
        JPanel ratingsHistoryPanel = new JPanel(new BorderLayout());
        ratingsHistoryPanel.setBackground(Color.WHITE);
        ratingsHistoryPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                        "📋 我的评分记录",
                        javax.swing.border.TitledBorder.LEFT,
                        javax.swing.border.TitledBorder.TOP,
                        new Font("Microsoft YaHei", Font.BOLD, 16),
                        new Color(70, 130, 180)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        myRatingsArea = new JTextArea();
        myRatingsArea.setFont(new Font("Microsoft YaHei", Font.PLAIN, 13));
        myRatingsArea.setEditable(false);
        myRatingsArea.setLineWrap(true);
        myRatingsArea.setWrapStyleWord(true);
        myRatingsArea.setBackground(new Color(250, 250, 250));
        myRatingsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane ratingsScroll = new JScrollPane(myRatingsArea);
        ratingsScroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        ratingsHistoryPanel.add(ratingsScroll, BorderLayout.CENTER);

        // 按钮事件
        submitRatingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitRating();
            }
        });

        // 设置分割面板
        mainSplitPane.setTopComponent(ratingFormPanel);
        mainSplitPane.setBottomComponent(ratingsHistoryPanel);

        ratingPanel.add(mainSplitPane, BorderLayout.CENTER);
    }

    private void initProfilePanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 247, 250));

        // 用户信息卡片
        JPanel profileCard = new JPanel(new GridBagLayout());
        profileCard.setBackground(Color.WHITE);
        profileCard.setBorder(BorderFactory.createCompoundBorder(
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
        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        userIcon.setForeground(new Color(70, 130, 180));
        profileCard.add(userIcon, gbc);

        gbc.gridheight = 1;
        gbc.gridx = 1;
        gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("👤 用户名:");
        usernameLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
        usernameLabel.setForeground(new Color(60, 60, 60));
        profileCard.add(usernameLabel, gbc);

        gbc.gridx = 2;
        JLabel usernameValue = new JLabel(currentUser.getUsername());
        usernameValue.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        usernameValue.setForeground(new Color(30, 30, 30));
        profileCard.add(usernameValue, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JLabel emailLabel = new JLabel("📧 邮箱:");
        emailLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
        emailLabel.setForeground(new Color(60, 60, 60));
        profileCard.add(emailLabel, gbc);

        gbc.gridx = 2;
        JLabel emailValue = new JLabel(currentUser.getEmail());
        emailValue.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        emailValue.setForeground(new Color(30, 30, 30));
        profileCard.add(emailValue, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        JLabel roleLabel = new JLabel("🎭 角色:");
        roleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
        roleLabel.setForeground(new Color(60, 60, 60));
        profileCard.add(roleLabel, gbc);

        gbc.gridx = 2;
        String roleText = "admin".equals(currentUser.getRole()) ? "管理员 👑" : "普通用户 👤";
        JLabel roleValue = new JLabel(roleText);
        roleValue.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
        roleValue
                .setForeground("admin".equals(currentUser.getRole()) ? new Color(220, 53, 69) : new Color(40, 167, 69));
        profileCard.add(roleValue, gbc);

        // 统计信息面板
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setBackground(new Color(245, 247, 250));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        // 已评分图书数
        List<Rating> userRatings = ratingDAO.getRatingsByUserId(currentUser.getId());
        int ratedBooksCount = userRatings.size();

        JPanel ratedPanel = createStatCard("📊 已评分图书", String.valueOf(ratedBooksCount),
                new Color(70, 130, 180), "本");

        // 平均评分
        double avgUserRating = 0;
        if (ratedBooksCount > 0) {
            double total = 0;
            for (Rating rating : userRatings) {
                total += rating.getRating();
            }
            avgUserRating = total / ratedBooksCount;
        }

        JPanel avgRatingPanel = createStatCard("⭐ 平均评分",
                String.format("%.2f", avgUserRating), new Color(46, 125, 50), "星");

        // 活跃天数（示例）
        JPanel activePanel = createStatCard("📅 活跃天数", "1",
                new Color(255, 152, 0), "天");

        statsPanel.add(ratedPanel);
        statsPanel.add(avgRatingPanel);
        statsPanel.add(activePanel);

        // 退出按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 247, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton logoutButton = createStyledButton("🚪 退出登录", new Color(220, 53, 69), 14);
        logoutButton.setPreferredSize(new Dimension(180, 45));
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        buttonPanel.add(logoutButton);

        mainPanel.add(profileCard, BorderLayout.NORTH);
        mainPanel.add(statsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        profilePanel.add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, String value, Color color, String unit) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        titleLabel.setForeground(new Color(100, 100, 100));

        JLabel valueLabel = new JLabel(value + " " + unit, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
        valueLabel.setForeground(color);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JButton createStyledButton(String text, Color bgColor, int fontSize) {
        JButton button = new JButton(text);
        button.setFont(new Font("Microsoft YaHei", Font.BOLD, fontSize));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 确保文字清晰可见
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);

        // 添加悬停效果
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
                button.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
                button.setForeground(Color.WHITE);
            }
        });

        return button;
    }

    private void layoutComponents() {
        // 设置窗口图标
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/icon.png"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            // 如果找不到图标，使用默认图标
        }

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
                    book.getAverageRating()
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
                    book.getAverageRating()
            };
            bookTableModel.addRow(row);
        }

        if (books.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "未找到包含 \"" + keyword + "\" 的图书",
                    "搜索结果", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateBookComboBox() {
        bookComboBox.removeAllItems();
        List<Book> books = bookDAO.getAllBooks();
        for (Book book : books) {
            bookComboBox.addItem(book);
        }

        if (!books.isEmpty()) {
            bookComboBox.setSelectedIndex(0);
        }
    }

    private void submitRating() {
        Book selectedBook = (Book) bookComboBox.getSelectedItem();
        if (selectedBook == null) {
            JOptionPane.showMessageDialog(this,
                    "请选择一本书！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double rating = (Double) ratingSpinner.getValue();
        String comment = commentArea.getText().trim();

        // 检查是否已经评分过
        if (ratingDAO.hasUserRatedBook(currentUser.getId(), selectedBook.getId())) {
            int option = JOptionPane.showConfirmDialog(this,
                    "您已经对《" + selectedBook.getTitle() + "》评分过了，是否更新评分？",
                    "确认", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (option == JOptionPane.YES_OPTION) {
                // 更新评分 - 先获取用户对该图书的所有评分，然后取第一个
                List<Rating> userRatings = ratingDAO.getRatingsByUserId(currentUser.getId());
                Rating existingRating = null;

                for (Rating r : userRatings) {
                    if (r.getBookId() == selectedBook.getId()) {
                        existingRating = r;
                        break;
                    }
                }

                if (existingRating != null) {
                    existingRating.setRating(rating);
                    existingRating.setComment(comment);

                    if (ratingDAO.updateRating(existingRating)) {
                        bookDAO.updateAverageRating(selectedBook.getId());

                        JOptionPane.showMessageDialog(this,
                                "评分更新成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                        commentArea.setText("");
                        loadMyRatings();
                        loadBooks();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "评分更新失败！", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "未找到原始评分记录！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
            return;
        }

        // 创建评分对象
        Rating newRating = new Rating(currentUser.getId(), selectedBook.getId(), rating, comment);

        if (ratingDAO.addRating(newRating)) {
            // 更新图书平均评分
            bookDAO.updateAverageRating(selectedBook.getId());

            // 显示成功消息
            JOptionPane.showMessageDialog(this,
                    "✅ 评分提交成功！\n\n" +
                            "书名: " + selectedBook.getTitle() + "\n" +
                            "评分: " + rating + " 星\n" +
                            "评论: "
                            + (comment.isEmpty() ? "无" : comment.substring(0, Math.min(50, comment.length())) + "..."),
                    "成功", JOptionPane.INFORMATION_MESSAGE);

            commentArea.setText("");
            loadMyRatings();
            loadBooks(); // 刷新图书列表以更新平均评分
        } else {
            JOptionPane.showMessageDialog(this,
                    "评分提交失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadMyRatings() {
        List<Rating> ratings = ratingDAO.getRatingsByUserId(currentUser.getId());
        StringBuilder sb = new StringBuilder();

        if (ratings.isEmpty()) {
            sb.append("📝 您还没有对任何图书评分。\n");
            sb.append("\n");
            sb.append("快去「我要评分」页面为喜欢的图书打分吧！");
        } else {
            sb.append("📋 您已对 ").append(ratings.size()).append(" 本图书评分：\n\n");

            for (int i = 0; i < ratings.size(); i++) {
                Rating rating = ratings.get(i);
                Book book = bookDAO.getBookById(rating.getBookId());
                if (book != null) {
                    sb.append("📚 第 ").append(i + 1).append(" 本\n");
                    sb.append("  书名: ").append(book.getTitle()).append("\n");
                    sb.append("  作者: ").append(book.getAuthor()).append("\n");

                    // 显示星星评分
                    int fullStars = (int) rating.getRating();
                    int halfStar = (rating.getRating() - fullStars) >= 0.5 ? 1 : 0;
                    int emptyStars = 5 - fullStars - halfStar;

                    sb.append("  评分: ");
                    for (int j = 0; j < fullStars; j++)
                        sb.append("★");
                    for (int j = 0; j < halfStar; j++)
                        sb.append("⭐");
                    for (int j = 0; j < emptyStars; j++)
                        sb.append("☆");
                    sb.append(" (").append(rating.getRating()).append(" 星)\n");

                    if (!rating.getComment().isEmpty()) {
                        sb.append("  评论: ").append(rating.getComment()).append("\n");
                    }
                    sb.append("  时间: ").append(rating.getCreatedAt()).append("\n");

                    if (i < ratings.size() - 1) {
                        sb.append("─".repeat(40)).append("\n");
                    }
                }
            }
        }

        myRatingsArea.setText(sb.toString());
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