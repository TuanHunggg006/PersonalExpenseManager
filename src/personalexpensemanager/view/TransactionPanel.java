package personalexpensemanager.view;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import personalexpensemanager.manager.TransactionManager;
import personalexpensemanager.model.Transaction;

public class TransactionPanel extends JPanel {
    private String currentType = "Expense"; 
    private String selectedCategory = "Ăn uống"; 
    private String selectedWallet = "Ví";
    private JPanel grid;
    private ButtonGroup catGroup;
    private JTextField txtAmount;
    private JFormattedTextField txtDate;
    private DecimalFormat df = new DecimalFormat("#,###");
    private TransactionManager manager;
    private MainFrame parent;
    private final String ICON_PATH = "/img/";

    public TransactionPanel(TransactionManager manager, MainFrame parent) {
        this.manager = manager;
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(500, 720));

        JPanel northBox = new JPanel();
        northBox.setLayout(new BoxLayout(northBox, BoxLayout.Y_AXIS));
        northBox.setBackground(Color.WHITE);

        
        JLabel lblHeader = new JLabel("Chọn hạng mục", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 26));
        lblHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblHeader.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

       
        JPanel tabWrapper = new JPanel(new GridLayout(1, 2)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(240, 240, 240));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        tabWrapper.setOpaque(false);
        tabWrapper.setMaximumSize(new Dimension(450, 60));

        JButton btnChi = createTabButton("CHI TIỀN", true);
        JButton btnThu = createTabButton("NHẬN TIỀN", false);
        tabWrapper.add(btnChi); tabWrapper.add(btnThu);

        
        JPanel amountButtonBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, 25, 25);
                g2.setColor(new Color(0, 162, 232));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(2, 2, getWidth()-4, getHeight()-4, 25, 25);
                g2.dispose();
            }
        };
        amountButtonBox.setLayout(new BoxLayout(amountButtonBox, BoxLayout.Y_AXIS));
        amountButtonBox.setOpaque(false);
        amountButtonBox.setMaximumSize(new Dimension(450, 120));
        amountButtonBox.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        JLabel lblHint = new JLabel("Số tiền", SwingConstants.CENTER);
        lblHint.setFont(new Font("Arial", Font.BOLD, 20));
        lblHint.setForeground(Color.GRAY);
        lblHint.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtAmount = new JTextField("0 đ");
        txtAmount.setFont(new Font("Arial", Font.BOLD, 50));
        txtAmount.setHorizontalAlignment(JTextField.CENTER);
        txtAmount.setBorder(null);
        txtAmount.setOpaque(false);
        txtAmount.setMaximumSize(new Dimension(400, 70));

        amountButtonBox.add(lblHint);
        amountButtonBox.add(txtAmount);

        
        JPanel detailSelectionPanel = new JPanel(new GridLayout(2, 1, 0, 15));
        detailSelectionPanel.setBackground(Color.WHITE);
        detailSelectionPanel.setMaximumSize(new Dimension(450, 140));
        detailSelectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JComboBox<String> cbWallet = new JComboBox<>(new String[]{"Ví", "Ngân hàng"});
        cbWallet.setFont(new Font("Arial", Font.PLAIN, 18));
        cbWallet.addActionListener(e -> selectedWallet = cbWallet.getSelectedItem().toString());
        
        try {
            MaskFormatter mf = new MaskFormatter("##/##/####");
            mf.setPlaceholderCharacter('_');
            txtDate = new JFormattedTextField(mf);
            txtDate.setFont(new Font("Arial", Font.PLAIN, 18));
            txtDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        } catch (Exception e) { txtDate = new JFormattedTextField(); }

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); row1.setOpaque(false);
        row1.add(new JLabel("Thanh toán: ") {{ setFont(new Font("Arial", Font.BOLD, 20)); }}); row1.add(cbWallet);
        
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); row2.setOpaque(false);
        row2.add(new JLabel("Ngày: ") {{ setFont(new Font("Arial", Font.BOLD, 20)); }}); row2.add(txtDate);

        detailSelectionPanel.add(row1); detailSelectionPanel.add(row2);

        northBox.add(lblHeader);
        northBox.add(tabWrapper);
        northBox.add(Box.createRigidArea(new Dimension(0, 20)));
        northBox.add(amountButtonBox);
        northBox.add(detailSelectionPanel);

       
        grid = new JPanel(new GridLayout(0, 1, 0, 8));
        grid.setBackground(Color.WHITE);
        grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        updateCategoryGrid();
        
        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        
        JPanel southBox = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southBox.setBackground(Color.WHITE);
        JButton btnSave = new JButton("LƯU GIAO DỊCH") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 162, 232));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnSave.setPreferredSize(new Dimension(380, 75)); // Thu hẹp chiều ngang
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Arial", Font.BOLD, 24));
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.setContentAreaFilled(false);
        btnSave.setBorderPainted(false);
        btnSave.setFocusPainted(false);
        southBox.add(btnSave);

        btnChi.addActionListener(e -> { updateTabStyle(btnChi, btnThu, true); });
        btnThu.addActionListener(e -> { updateTabStyle(btnThu, btnChi, false); });
        btnSave.addActionListener(e -> saveTransaction());

        txtAmount.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                String in = txtAmount.getText().replaceAll("[^0-9]", "");
                if(!in.isEmpty()) txtAmount.setText(df.format(Long.parseLong(in)) + " đ");
            }
        });

        add(northBox, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(southBox, BorderLayout.SOUTH);
    }

    private void updateCategoryGrid() {
        grid.removeAll();
        catGroup = new ButtonGroup();
        String[] cats = currentType.equals("Expense") ? 
            new String[]{"Ăn uống", "Dịch vụ", "Đi lại", "Con cái", "Nhà cửa", "Sức khỏe", "Du lịch", "Học phí", "Mua sắm", "Khác"} :
            new String[]{"Lương", "Thưởng", "Tiền lãi", "Quà tặng", "Khác"};
        
        for (String c : cats) {
            JToggleButton b = new JToggleButton(c) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    // Hiệu ứng Gradient xanh trắng cho hạng mục
                    GradientPaint gp = new GradientPaint(0, 0, new Color(240, 248, 255), getWidth(), getHeight(), Color.WHITE);
                    g2.setPaint(gp);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2.setColor(new Color(200, 220, 240));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };
            b.setFont(new Font("Arial", Font.BOLD, 22));
            b.setForeground(new Color(0, 102, 204)); // Chữ màu xanh
            b.setHorizontalAlignment(SwingConstants.LEFT);
            b.setPreferredSize(new Dimension(440, 80));
            b.setContentAreaFilled(false);
            b.setBorderPainted(false);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            b.setIcon(getIconForCategory(c));
            b.setIconTextGap(25);
            
            b.addActionListener(e -> selectedCategory = c);
            catGroup.add(b); grid.add(b);
        }
        grid.revalidate(); grid.repaint();
    }

    private ImageIcon getIconForCategory(String category) {
        String fileName = "";
        switch (category) {
            case "Ăn uống": fileName = "icon_anuong.png"; break;
            case "Dịch vụ": fileName = "icon_dichvu.png"; break;
            case "Đi lại": fileName = "icon_dilai.png"; break;
            case "Con cái": fileName = "icon_concai.png"; break;
            case "Nhà cửa": fileName = "icon_nhacua.png"; break;
            case "Sức khỏe": fileName = "icon_suckhoe.png"; break;
            case "Du lịch": fileName = "icon_dulich.png"; break;
            case "Học phí": fileName = "icon_hocphi.png"; break;
            case "Mua sắm": fileName = "icon_muasam.png"; break;
            case "Lương": fileName = "icon_luong.png"; break;
            case "Thưởng": fileName = "icon_thuong.png"; break;
            case "Tiền lãi": fileName = "icon_tienlai.png"; break;
            case "Quà tặng": fileName = "icon_quatang.png"; break;
            default: fileName = "icon_khac.png"; break;
        }
        try {
            URL url = getClass().getResource(ICON_PATH + fileName);
            if (url != null) {
                Image img = new ImageIcon(url).getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {}
        return null;
    }

    private JButton createTabButton(String text, boolean active) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        if (active) {
            btn.setBackground(new Color(0, 162, 232));
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(new Color(240, 240, 240));
            btn.setForeground(Color.GRAY);
        }
        return btn;
    }

    private void updateTabStyle(JButton active, JButton inactive, boolean isChi) {
        active.setBackground(new Color(0, 162, 232));
        active.setForeground(Color.WHITE);
        inactive.setBackground(new Color(240, 240, 240));
        inactive.setForeground(Color.GRAY);
        currentType = isChi ? "Expense" : "Income";
        updateCategoryGrid();
    }

    private void saveTransaction() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(txtDate.getText());
            String clean = txtAmount.getText().replaceAll("[^0-9]", "");
            if (clean.isEmpty() || Double.parseDouble(clean) <= 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền!");
                return;
            }
            manager.addTransaction(new Transaction("T"+System.currentTimeMillis(), selectedCategory, Double.parseDouble(clean), currentType, selectedCategory, txtDate.getText(), "", selectedWallet));
            parent.refreshAll();
            JOptionPane.showMessageDialog(this, "Đã lưu: " + selectedCategory);
            txtAmount.setText("0 đ");
        } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Ngày tháng không hợp lệ!"); }
    }
}