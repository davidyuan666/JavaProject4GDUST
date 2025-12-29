package com.bookrating.ui.panels;

import com.bookrating.ui.components.BasePanel;
import com.bookrating.dao.BookDAO;
import com.bookrating.dao.RatingDAO;
import com.bookrating.model.Book;
import com.bookrating.model.Rating;
import com.bookrating.model.User;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RatingPanel extends BasePanel {
    private User currentUser;
    private BookDAO bookDAO;
    private RatingDAO ratingDAO;

    private JComboBox<Book> bookComboBox;
    private JSpinner ratingSpinner;
    private JTextArea commentArea;
    private JTextArea myRatingsArea;

    public RatingPanel(User user, BookDAO bookDAO, RatingDAO ratingDAO) {
        this.currentUser = user;
        this.bookDAO = bookDAO;
        this.ratingDAO = ratingDAO;
        initComponents();
    }

    private void initComponents() {
        // 使用垂直分割面板
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setResizeWeight(0.5);
        mainSplitPane.setDividerSize(5);
        mainSplitPane.setBorder(BorderFactory.createEmptyBorder());

        // 上半部分：评分表单
        JPanel ratingFormPanel = createRatingFormPanel();

        // 下半部分：我的评分记录
        JPanel ratingsHistoryPanel = createRatingsHistoryPanel();

        mainSplitPane.setTopComponent(ratingFormPanel);
        mainSplitPane.setBottomComponent(ratingsHistoryPanel);

        setLayout(new BorderLayout());
        add(mainSplitPane, BorderLayout.CENTER);

        // 加载数据
        updateBookComboBox();
        loadMyRatings();
    }

    private JPanel createRatingFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                        "评分表单",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        TITLE_FONT,
                        PRIMARY_COLOR),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // 选择图书
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel bookLabel = new JLabel("选择图书:");
        bookLabel.setFont(LABEL_FONT);
        bookLabel.setForeground(new Color(60, 60, 60));
        panel.add(bookLabel, gbc);

        gbc.gridx = 1;
        bookComboBox = new JComboBox<>();
        bookComboBox.setFont(TEXT_FONT);
        bookComboBox.setPreferredSize(new Dimension(350, 40));
        panel.add(bookComboBox, gbc);

        // 评分
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel ratingLabel = new JLabel("评分 (1-5):");
        ratingLabel.setFont(LABEL_FONT);
        ratingLabel.setForeground(new Color(60, 60, 60));
        panel.add(ratingLabel, gbc);

        gbc.gridx = 1;
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(3.0, 1.0, 5.0, 0.5);
        ratingSpinner = new JSpinner(spinnerModel);
        ratingSpinner.setFont(TEXT_FONT);
        ratingSpinner.setPreferredSize(new Dimension(120, 40));
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) ratingSpinner.getEditor();
        editor.getTextField().setHorizontalAlignment(JTextField.CENTER);
        panel.add(ratingSpinner, gbc);

        // 评论
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel commentLabel = new JLabel("评论:");
        commentLabel.setFont(LABEL_FONT);
        commentLabel.setForeground(new Color(60, 60, 60));
        panel.add(commentLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        commentArea = new JTextArea(5, 30);
        commentArea.setFont(TEXT_FONT);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        JScrollPane commentScroll = new JScrollPane(commentArea);
        commentScroll.setPreferredSize(new Dimension(400, 120));
        panel.add(commentScroll, gbc);

        // 提交按钮
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton submitButton = createStyledButton("提交评分", SUCCESS_COLOR, 16);
        submitButton.setPreferredSize(new Dimension(200, 50));
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitRating();
            }
        });
        panel.add(submitButton, gbc);

        return panel;
    }

    private JPanel createRatingsHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                        "我的评分记录",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        TITLE_FONT,
                        PRIMARY_COLOR),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        myRatingsArea = new JTextArea();
        myRatingsArea.setFont(SMALL_FONT);
        myRatingsArea.setEditable(false);
        myRatingsArea.setLineWrap(true);
        myRatingsArea.setWrapStyleWord(true);
        myRatingsArea.setBackground(new Color(250, 250, 250));
        myRatingsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(myRatingsArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
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
            JOptionPane.showMessageDialog(this, "请选择一本书！", "错误", JOptionPane.ERROR_MESSAGE);
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
                // 更新评分
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

        // 创建新评分
        Rating newRating = new Rating(currentUser.getId(), selectedBook.getId(), rating, comment);

        if (ratingDAO.addRating(newRating)) {
            bookDAO.updateAverageRating(selectedBook.getId());

            JOptionPane.showMessageDialog(this,
                    "评分提交成功！\n\n" +
                            "书名: " + selectedBook.getTitle() + "\n" +
                            "评分: " + rating + " 星\n" +
                            "评论: "
                            + (comment.isEmpty() ? "无" : comment.substring(0, Math.min(50, comment.length())) + "..."),
                    "成功", JOptionPane.INFORMATION_MESSAGE);

            commentArea.setText("");
            loadMyRatings();
        } else {
            JOptionPane.showMessageDialog(this,
                    "评分提交失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadMyRatings() {
        List<Rating> ratings = ratingDAO.getRatingsByUserId(currentUser.getId());
        StringBuilder sb = new StringBuilder();

        if (ratings.isEmpty()) {
            sb.append("您还没有对任何图书评分。\n\n");
            sb.append("快去为喜欢的图书打分吧！");
        } else {
            sb.append("您已对 ").append(ratings.size()).append(" 本图书评分：\n\n");

            for (int i = 0; i < ratings.size(); i++) {
                Rating rating = ratings.get(i);
                Book book = bookDAO.getBookById(rating.getBookId());
                if (book != null) {
                    sb.append("第 ").append(i + 1).append(" 本\n");
                    sb.append("  书名: ").append(book.getTitle()).append("\n");
                    sb.append("  作者: ").append(book.getAuthor()).append("\n");
                    sb.append("  评分: ").append(rating.getRating()).append(" 星\n");

                    if (!rating.getComment().isEmpty()) {
                        sb.append("  评论: ").append(rating.getComment()).append("\n");
                    }
                    sb.append("  时间: ").append(rating.getCreatedAt()).append("\n");

                    if (i < ratings.size() - 1) {
                        sb.append("----------------------------------------\n");
                    }
                }
            }
        }

        myRatingsArea.setText(sb.toString());
    }
}