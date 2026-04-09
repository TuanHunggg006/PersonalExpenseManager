package personalexpensemanager.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import personalexpensemanager.manager.TransactionManager;
import personalexpensemanager.model.Transaction;

public class AccountPanel extends JPanel {
    private CardLayout innerLayout = new CardLayout();
    private JPanel cards;
    private TransactionManager manager;
    private final String IMG_PATH = "/img/";

    public AccountPanel(TransactionManager manager) {
        this.manager = manager;
        this.setLayout(new BorderLayout());
        
        setPreferredSize(new Dimension(500, 720));
        setMinimumSize(new Dimension(500, 720));
        setMaximumSize(new Dimension(500, 720));

        cards = new JPanel(innerLayout);
        cards.setOpaque(false);
        renderOverviewPage();
        add(cards, BorderLayout.CENTER);
    }

    private void renderOverviewPage() {
        JPanel content = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 162, 232), 0, 500, new Color(245, 245, 245));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(30, 25, 30, 25));

        JLabel lblTitle = new JLabel("Tài khoản của tôi");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 34)); 
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(lblTitle);
        content.add(Box.createRigidArea(new Dimension(0, 25)));

        JPanel glassCard = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 100));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
                g2.dispose();
            }
        };
        glassCard.setOpaque(false);
        glassCard.setMaximumSize(new Dimension(500, 160));
        glassCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        glassCard.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel lblTotalTitle = new JLabel("Tổng tài sản");
        lblTotalTitle.setForeground(Color.WHITE);
        lblTotalTitle.setFont(new Font("Arial", Font.BOLD, 22)); 
        
        JLabel lblTotalValue = new JLabel(String.format("%,.0f đ", manager.getBalance()));
        lblTotalValue.setForeground(Color.WHITE);
        lblTotalValue.setFont(new Font("Arial", Font.BOLD, 48)); 

        glassCard.add(lblTotalTitle, BorderLayout.NORTH);
        glassCard.add(lblTotalValue, BorderLayout.CENTER);
        content.add(glassCard);
        content.add(Box.createRigidArea(new Dimension(0, 45)));

        JLabel lblGroupTitle = new JLabel("Tài khoản chi tiêu");
        lblGroupTitle.setFont(new Font("Arial", Font.BOLD, 26)); 
        lblGroupTitle.setForeground(new Color(50, 50, 50));
        lblGroupTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(lblGroupTitle);
        content.add(Box.createRigidArea(new Dimension(0, 20)));

        content.add(createIndividualWalletButton("Ví", manager.getBalanceByWallet("Ví")));
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        content.add(createIndividualWalletButton("Ngân hàng", manager.getBalanceByWallet("Ngân hàng")));

        cards.add(content, "OVERVIEW");
    }

    private JPanel createIndividualWalletButton(String name, double amount) {
        JPanel btn = new JPanel(new BorderLayout(25, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        btn.setOpaque(false);
        btn.setMaximumSize(new Dimension(500, 115)); 
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblIcon = new JLabel();
        try {
            URL url = getClass().getResource(IMG_PATH + "icon_money.png");
            if (url != null) lblIcon.setIcon(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(55, 40, Image.SCALE_SMOOTH)));
        } catch (Exception e) {}

        JPanel info = new JPanel(new GridLayout(2, 1, 0, 5));
        info.setOpaque(false);
        JLabel lblN = new JLabel(name);
        lblN.setFont(new Font("Arial", Font.BOLD, 24)); 
        JLabel lblM = new JLabel(String.format("%,.0f đ", amount));
        lblM.setForeground(Color.GRAY);
        lblM.setFont(new Font("Arial", Font.PLAIN, 20)); 
        info.add(lblN); info.add(lblM);

        btn.add(lblIcon, BorderLayout.WEST);
        btn.add(info, BorderLayout.CENTER);
        btn.add(new JLabel(">") {{ setForeground(Color.LIGHT_GRAY); setFont(new Font("Arial", Font.BOLD, 26)); }}, BorderLayout.EAST);
        
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                cards.add(createDetailsPage(name), "DETAILS");
                innerLayout.show(cards, "DETAILS");
                revalidate(); repaint();
            }
        });
        return btn;
    }

    private JPanel createDetailsPage(String walletName) {
        JPanel detailPage = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 162, 232), 0, 500, new Color(245, 245, 245));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        detailPage.setLayout(new BorderLayout());
        detailPage.setPreferredSize(new Dimension(500, 720));

        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(500, 80));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        JButton btnBack = new JButton("< Quay lại") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 81, 145));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        btnBack.setFont(new Font("Arial", Font.BOLD, 13)); 
        btnBack.setForeground(Color.WHITE);
        btnBack.setPreferredSize(new Dimension(100, 38)); 
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.addActionListener(e -> innerLayout.show(cards, "OVERVIEW"));
        
        JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 20));
        btnWrapper.setOpaque(false);
        btnWrapper.add(btnBack);

        JLabel title = new JLabel(walletName, SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 34)); 
        
        header.add(btnWrapper, BorderLayout.WEST); 
        header.add(title, BorderLayout.CENTER);
        header.add(Box.createHorizontalStrut(115), BorderLayout.EAST);

        double walletIn = manager.getIncomeByWallet(walletName);
        double walletOut = manager.getExpenseByWallet(walletName);
        double currentBal = walletIn - walletOut;

        JPanel summaryArea = new JPanel();
        summaryArea.setLayout(new BoxLayout(summaryArea, BoxLayout.Y_AXIS));
        summaryArea.setOpaque(false);
        summaryArea.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JPanel row1 = new JPanel(new GridLayout(1, 2, 20, 0));
        row1.setOpaque(false);
        row1.add(createStatCard("Tổng thu", walletIn, new Color(39, 174, 96)));
        row1.add(createStatCard("Tổng chi", walletOut, new Color(231, 76, 60)));

        JPanel balCard = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        balCard.setOpaque(false);
        balCard.setMaximumSize(new Dimension(460, 100)); 
        balCard.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
        JLabel lbBalL = new JLabel("Số dư hiện tại");
        lbBalL.setFont(new Font("Arial", Font.BOLD, 24)); 
        JLabel lbBalV = new JLabel(String.format("%,.0f đ", currentBal));
        lbBalV.setFont(new Font("Arial", Font.BOLD, 30)); 
        balCard.add(lbBalL, BorderLayout.WEST);
        balCard.add(lbBalV, BorderLayout.EAST);

        summaryArea.add(row1);
        summaryArea.add(Box.createRigidArea(new Dimension(0, 20)));
        summaryArea.add(balCard);

        JPanel listItems = new JPanel();
        listItems.setLayout(new BoxLayout(listItems, BoxLayout.Y_AXIS));
        listItems.setOpaque(false);

        ArrayList<Transaction> sorted = new ArrayList<>(manager.getAllTransactions());
        sorted.sort((t1, t2) -> {
            try { return new SimpleDateFormat("dd/MM/yyyy").parse(t2.getDate()).compareTo(new SimpleDateFormat("dd/MM/yyyy").parse(t1.getDate())); }
            catch (Exception ex) { return 0; }
        });

        Map<String, ArrayList<Transaction>> grouped = new LinkedHashMap<>();
        for (Transaction t : sorted) {
            if (walletName.equalsIgnoreCase(t.getWalletName())) {
                grouped.computeIfAbsent(t.getDate(), k -> new ArrayList<>()).add(t);
            }
        }

        for (String date : grouped.keySet()) {
            JPanel dateHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 12));
            dateHeader.setBackground(new Color(230, 230, 230));
            dateHeader.setMaximumSize(new Dimension(500, 50));
            JLabel lblDate = new JLabel(date);
            lblDate.setFont(new Font("Arial", Font.BOLD, 20)); 
            dateHeader.add(lblDate);
            listItems.add(dateHeader);

            for (Transaction t : grouped.get(date)) {
                JPanel item = new JPanel(new BorderLayout(15, 0)); // Thêm khoảng cách ngang cho icon
                item.setBackground(Color.WHITE);
                item.setMaximumSize(new Dimension(500, 85)); 
                item.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));
                
                JLabel lblIcon = new JLabel();
                lblIcon.setIcon(getIconForCategory(t.getCategory()));
                lblIcon.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
                
                String color = t.getType().equalsIgnoreCase("Income") ? "#27ae60" : "#e74c3c";
                
                JLabel cat = new JLabel(t.getCategory());
                cat.setFont(new Font("Arial", Font.BOLD, 22)); 
                
                JLabel amt = new JLabel("<html><b style='color:"+color+"; font-size:20px;'>" + String.format("%,.0f đ", t.getAmount()) + "</b> &nbsp;&nbsp;</html>");
                
                item.add(lblIcon, BorderLayout.WEST); 
                item.add(cat, BorderLayout.CENTER);  
                item.add(amt, BorderLayout.EAST);
                listItems.add(item);
            }
        }

        JScrollPane scroll = new JScrollPane(listItems);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel mainContentBox = new JPanel(new BorderLayout());
        mainContentBox.setOpaque(false);
        mainContentBox.add(summaryArea, BorderLayout.NORTH);
        mainContentBox.add(scroll, BorderLayout.CENTER);

        detailPage.add(header, BorderLayout.NORTH);
        detailPage.add(mainContentBox, BorderLayout.CENTER);

        return detailPage;
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
                Image img = new ImageIcon(url).getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {}
        return null;
    }

    private JPanel createStatCard(String title, double value, Color color) {
        JPanel card = new JPanel(new GridLayout(2, 1, 0, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(210, 130)); 
        card.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        
        JLabel t = new JLabel(title, SwingConstants.CENTER);
        t.setFont(new Font("Arial", Font.BOLD, 20)); 
        
        JLabel v = new JLabel(String.format("%,.0f đ", value), SwingConstants.CENTER);
        v.setFont(new Font("Arial", Font.BOLD, 26)); 
        v.setForeground(color);
        
        card.add(t); card.add(v);
        return card;
    }
}