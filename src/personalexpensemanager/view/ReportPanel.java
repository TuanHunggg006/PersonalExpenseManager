package personalexpensemanager.view;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import personalexpensemanager.manager.TransactionManager;
import personalexpensemanager.model.Transaction;

public class ReportPanel extends JPanel {
    private TransactionManager manager;
    private final String IMG_PATH = "/img/";
    private DefaultListModel<String> suggestModel;
    private JList<String> suggestList;
    private JScrollPane suggestScroll;

    public ReportPanel(TransactionManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout());

        JPanel mainContent = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 162, 232, 100), 0, getHeight(), Color.WHITE);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainContent.setLayout(new BorderLayout());

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.setOpaque(false);
        northPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        JLabel title = new JLabel("Báo cáo tài chính", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSearchTitle = new JLabel("Tìm kiếm giao dịch");
        lblSearchTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblSearchTitle.setForeground(new Color(50, 50, 50));
        lblSearchTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSearchTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));

        JPanel searchBox = new JPanel(new BorderLayout(10, 0));
        searchBox.setOpaque(false);
        searchBox.setMaximumSize(new Dimension(500, 45));
        searchBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 120, 215), 2, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JTextField txtSearch = new JTextField();
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 18));
        txtSearch.setBorder(null);
        txtSearch.setOpaque(false);
        JLabel lblSearchIcon = new JLabel("🔍");
        searchBox.add(lblSearchIcon, BorderLayout.WEST);
        searchBox.add(txtSearch, BorderLayout.CENTER);

        suggestModel = new DefaultListModel<>();
        suggestList = new JList<>(suggestModel);
        suggestList.setFont(new Font("Arial", Font.PLAIN, 16));
        suggestScroll = new JScrollPane(suggestList);
        suggestScroll.setMaximumSize(new Dimension(500, 120));
        suggestScroll.setVisible(false);

        northPanel.add(title);
        northPanel.add(lblSearchTitle);
        northPanel.add(searchBox);
        northPanel.add(suggestScroll);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 20, 25));

        Object[] topExpenseData = findTopCategoryData("Expense");
        Object[] topIncomeData = findTopCategoryData("Income");

        centerPanel.add(createGradientCard("Phân tích chi tiêu", "Chi nhiều nhất: " + topExpenseData[0], (ImageIcon) topExpenseData[1], new Color(231, 76, 60), new Color(255, 235, 235)));
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(createGradientCard("Phân tích thu nhập", "Thu nhiều nhất: " + topIncomeData[0], (ImageIcon) topIncomeData[1], new Color(46, 204, 113), new Color(235, 255, 240)));

        centerPanel.add(Box.createVerticalGlue());

        JPanel logoutCard = new JPanel(new BorderLayout(20, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(240, 240, 240), getWidth(), getHeight(), Color.WHITE);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
                g2.dispose();
            }
        };
        logoutCard.setOpaque(false);
        logoutCard.setMaximumSize(new Dimension(500, 70));
        logoutCard.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        logoutCard.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblLogoutIcon = new JLabel();
        try {
            URL url = getClass().getResource(IMG_PATH + "icon_lock.png");
            if (url != null) lblLogoutIcon.setIcon(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
        } catch (Exception e) {}

        JLabel lblLogoutText = new JLabel("Đăng xuất tài khoản");
        lblLogoutText.setFont(new Font("Arial", Font.BOLD, 20));
        lblLogoutText.setForeground(new Color(100, 100, 100));

        logoutCard.add(lblLogoutIcon, BorderLayout.WEST);
        logoutCard.add(lblLogoutText, BorderLayout.CENTER);

        logoutCard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int opt = JOptionPane.showConfirmDialog(null, "Bạn có muốn đăng xuất không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (opt == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        centerPanel.add(logoutCard);

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateSuggest(); }
            public void removeUpdate(DocumentEvent e) { updateSuggest(); }
            public void changedUpdate(DocumentEvent e) { updateSuggest(); }

            private void updateSuggest() {
                String text = txtSearch.getText().trim().toLowerCase();
                suggestModel.removeAllElements();
                if (text.isEmpty()) {
                    suggestScroll.setVisible(false);
                } else {
                    boolean found = false;
                    for (Transaction t : manager.getAllTransactions()) {
                        if (t.getTitle().toLowerCase().contains(text)) {
                            suggestModel.addElement(t.getDate() + " - " + t.getTitle() + ": " + String.format("%,.0f đ", t.getAmount()));
                            found = true;
                        }
                    }
                    suggestScroll.setVisible(found);
                }
                revalidate();
            }
        });

        mainContent.add(northPanel, BorderLayout.NORTH);
        mainContent.add(centerPanel, BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);
    }

    private JPanel createGradientCard(String title, String desc, ImageIcon icon, Color primaryColor, Color startColor) {
        JPanel card = new JPanel(new BorderLayout(20, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, startColor, getWidth(), getHeight(), Color.WHITE);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setColor(primaryColor);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 30, 30);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(500, 150));
        card.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setOpaque(false);
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(primaryColor);
        JLabel lblDesc = new JLabel("<html>" + desc + "</html>");
        lblDesc.setFont(new Font("Arial", Font.BOLD, 18));
        lblDesc.setForeground(new Color(60, 60, 60));
        textPanel.add(lblTitle);
        textPanel.add(lblDesc);

        if (icon != null) card.add(new JLabel(icon), BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    private Object[] findTopCategoryData(String type) {
        Map<String, Double> map = new HashMap<>();
        for (Transaction t : manager.getAllTransactions()) {
            if (t.getType().equalsIgnoreCase(type)) {
                map.put(t.getCategory(), map.getOrDefault(t.getCategory(), 0.0) + t.getAmount());
            }
        }
        String topCat = "Chưa có dữ liệu";
        double max = 0;
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                topCat = entry.getKey();
            }
        }
        String displayStr = max > 0 ? String.format("%s (%,.0f đ)", topCat, max) : topCat;
        ImageIcon icon = (max > 0) ? getIconForCategory(topCat) : null;
        return new Object[]{displayStr, icon};
    }

    private ImageIcon getIconForCategory(String category) {
        String fileName = "";
        switch (category) {
            case "Ăn uống": fileName = "icon_anuong.png"; break;
            case "Dịch vụ sinh hoạt": fileName = "icon_dichvu.png"; break;
            case "Đi lại": fileName = "icon_dilai.png"; break;
            case "Con cái": fileName = "icon_concai.png"; break;
            case "Nhà cửa": fileName = "icon_nhacua.png"; break;
            case "Sức khỏe": fileName = "icon_suckhoe.png"; break;
            case "Hưởng thụ": fileName = "icon_huongthu.png"; break;
            case "Hiếu hỉ": fileName = "icon_hieuhi.png"; break;
            case "Lương": fileName = "icon_luong.png"; break;
            case "Thưởng": fileName = "icon_thuong.png"; break;
            case "Tiền lãi": fileName = "icon_tienlai.png"; break;
            default: fileName = "icon_khac.png"; break;
        }
        try {
            URL url = getClass().getResource(IMG_PATH + fileName);
            if (url != null) {
                Image img = new ImageIcon(url).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {}
        return null;
    }
}