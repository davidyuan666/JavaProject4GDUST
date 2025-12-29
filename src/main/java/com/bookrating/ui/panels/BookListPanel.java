package com.bookrating.ui.panels;

import com.bookrating.ui.components.BasePanel;
import com.bookrating.dao.BookDAO;
import com.bookrating.model.Book;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BookListPanel extends BasePanel {
    private BookDAO bookDAO;
    private JTable bookTable;
    private DefaultTableModel bookTableModel;
    private JTextField searchField;

    public BookListPanel(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
        setLayout(new BorderLayout(10, 10));
        initComponents();
    }

    private void initComponents() {
        // 搜索面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchPanel.setBackground(BACKGROUND_COLOR);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel searchLabel = new JLabel("搜索图书:");
        searchLabel.setFont(LABEL_FONT);
        searchLabel.setForeground(new Color(60, 60, 60));

        searchField = new JTextField(25);
        searchField.setFont(TEXT_FONT);
        searchField.setPreferredSize(new Dimension(300, 38));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        JButton searchButton = createStyledButton("搜索", PRIMARY_COLOR, 14);
        JButton refreshButton = createStyledButton("刷新", SUCCESS_COLOR, 14);

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
        bookTable.setFont(SMALL_FONT);
        bookTable.setRowHeight(35);
        bookTable.setSelectionBackground(new Color(220, 240, 255));
        bookTable.setSelectionForeground(Color.BLACK);
        bookTable.setGridColor(new Color(230, 230, 230));
        bookTable.setShowGrid(true);

        // 设置交替行颜色
        bookTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // 交替行背景色
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(248, 248, 248));
                    }
                }

                return c;
            }
        });

        // 设置表头 - 使用更清晰的样式
        bookTable.getTableHeader().setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        bookTable.getTableHeader().setBackground(TABLE_HEADER_BG);
        bookTable.getTableHeader().setForeground(TABLE_HEADER_FG);
        bookTable.getTableHeader().setReorderingAllowed(false);
        bookTable.getTableHeader().setPreferredSize(new Dimension(0, 40)); // 增加表头高度

        // 设置列宽
        bookTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        bookTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        bookTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        bookTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        bookTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        bookTable.getColumnModel().getColumn(5).setPreferredWidth(100);

        // 设置评分列的渲染器
        bookTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (value instanceof Double) {
                    double rating = (Double) value;
                    setText(String.format("%.2f", rating));

                    // 根据评分设置颜色 - 使用更亮的颜色
                    if (rating >= 4.5) {
                        setForeground(new Color(39, 174, 96)); // 亮绿色
                        setFont(getFont().deriveFont(Font.BOLD));
                    } else if (rating >= 3.5) {
                        setForeground(new Color(243, 156, 18)); // 亮橙色
                    } else if (rating >= 2.5) {
                        setForeground(new Color(192, 57, 43)); // 亮红色
                    } else {
                        setForeground(new Color(127, 140, 141)); // 灰色
                    }
                }

                setHorizontalAlignment(SwingConstants.CENTER);

                // 设置背景色
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(248, 248, 248));
                    }
                }

                return c;
            }
        });

        // 设置其他列居中对齐
        for (int i = 0; i < bookTable.getColumnCount(); i++) {
            if (i != 1) {
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value,
                            boolean isSelected, boolean hasFocus, int row, int column) {
                        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                                column);

                        // 设置背景色
                        if (!isSelected) {
                            if (row % 2 == 0) {
                                c.setBackground(Color.WHITE);
                            } else {
                                c.setBackground(new Color(248, 248, 248));
                            }
                        }

                        setHorizontalAlignment(SwingConstants.CENTER);
                        return c;
                    }
                };
                bookTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)));
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
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // 加载数据
        loadBooks();
    }

    public void loadBooks() {
        bookTableModel.setRowCount(0);
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
    }

    private void searchBooks() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadBooks();
            return;
        }

        bookTableModel.setRowCount(0);
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
}