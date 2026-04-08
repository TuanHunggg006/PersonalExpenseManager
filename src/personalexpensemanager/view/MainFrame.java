package personalexpensemanager.view;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import personalexpensemanager.manager.TransactionManager;

public class MainFrame extends JFrame {
    private TransactionManager manager;
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContent; 
    private final String IMG_PATH = "/img/";

    public MainFrame() {
        manager = new TransactionManager(); 
        setTitle("Quản Lý Chi Tiêu");
        setSize(1000, 850); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Vỏ bọc màu xám căn giữa toàn bộ ứng dụng
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(new Color(230, 230, 230));

        // 2. Khung nội dung chính (CardLayout)
        mainContent = new JPanel(cardLayout);
        mainContent.setPreferredSize(new Dimension(500, 720));
        mainContent.setOpaque(false);

        // THÊM CÁC PANEL VÀO CARDLAYOUT (BẮT BUỘC PHẢI CÓ ĐỂ NÚT BẤM HOẠT ĐỘNG)
        mainContent.add(new AccountPanel(manager), "ACCOUNT");
        mainContent.add(new TransactionPanel(manager, this), "ADD");
        mainContent.add(new ReportPanel(manager), "REPORT");
        
        centerWrapper.add(mainContent);
        add(centerWrapper, BorderLayout.CENTER);

        // 3. THANH MENU ĐIỀU HƯỚNG DƯỚI ĐÁY
        JPanel navWrapper = new JPanel(new GridBagLayout());
        navWrapper.setBackground(new Color(230, 230, 230)); 
        navWrapper.setPreferredSize(new Dimension(1000, 110));

        // Khung xanh bo góc chứa 3 nút
        JPanel navContent = new JPanel(new GridLayout(1, 3)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 120, 215)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40); 
                g2.dispose();
            }
        };
        navContent.setPreferredSize(new Dimension(480, 85));
        navContent.setOpaque(false);

        // Tạo 3 nút với Icon chuẩn Swing (Bấm cực nhạy)
        JButton btnAcc = createNavButton("Tài khoản", "icon_account.png", 32);
        JButton btnAdd = createNavButton("", "icon_add.png", 55);
        JButton btnReport = createNavButton("Khác", "icon_other.png", 32);

        navContent.add(btnAcc); 
        navContent.add(btnAdd); 
        navContent.add(btnReport);

        navWrapper.add(navContent);
        add(navWrapper, BorderLayout.SOUTH);

        // GÁN SỰ KIỆN CHUYỂN TRANG
        btnAcc.addActionListener(e -> cardLayout.show(mainContent, "ACCOUNT"));
        btnAdd.addActionListener(e -> cardLayout.show(mainContent, "ADD"));
        btnReport.addActionListener(e -> cardLayout.show(mainContent, "REPORT"));
        
        cardLayout.show(mainContent, "ACCOUNT");
    }

    private JButton createNavButton(String text, String iconName, int iconSize) {
        JButton btn = new JButton(text);
        try {
            URL url = getClass().getResource(IMG_PATH + iconName);
            if (url != null) {
                Image img = new ImageIcon(url).getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
                btn.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {}

        // Thiết lập hiển thị chuẩn: Icon trên, Chữ dưới
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        
        // Loại bỏ các thành phần mặc định để nhường chỗ cho nền xanh
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return btn;
    }
    
    public void refreshAll() {
        // Thay vì removeAll, hãy tạo lại nội dung và cập nhật vào cardLayout
        mainContent.add(new AccountPanel(manager), "ACCOUNT");
        mainContent.add(new TransactionPanel(manager, this), "ADD");
        mainContent.add(new ReportPanel(manager), "REPORT");
        
        mainContent.revalidate();
        mainContent.repaint();
    }

    public void showCard(String cardName) {
    cardLayout.show(mainContent, cardName);
    mainContent.revalidate(); // Ép vẽ lại sơ đồ
    mainContent.repaint();    // Ép tô màu lại
}
}