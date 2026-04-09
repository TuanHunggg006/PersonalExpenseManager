package personalexpensemanager.view;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import personalexpensemanager.manager.TransactionManager;
import personalexpensemanager.model.Transaction;

public class ReportPanel extends JPanel {
    private TransactionManager manager;
    private final String IMG_PATH = "/img/";

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
        mainContent.setPreferredSize(new Dimension(500, 720));

        
        JLabel title = new JLabel("Báo cáo tài chính", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setPreferredSize(new Dimension(500, 100));
        mainContent.add(title, BorderLayout.NORTH);

        
        JPanel cardContainer = new JPanel();
        cardContainer.setLayout(new BoxLayout(cardContainer, BoxLayout.Y_AXIS));
        cardContainer.setOpaque(false);
        cardContainer.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        
        Object[] topExpenseData = findTopCategoryData("Expense");
        Object[] topIncomeData = findTopCategoryData("Income");

        
        cardContainer.add(createGradientCard(
            "Phân tích chi tiêu", 
            "Chi nhiều nhất: " + topExpenseData[0], 
            (ImageIcon) topExpenseData[1],
            new Color(231, 76, 60), 
            new Color(255, 235, 235)
        ));

        cardContainer.add(Box.createRigidArea(new Dimension(0, 30)));

       
        cardContainer.add(createGradientCard(
            "Phân tích thu nhập", 
            "Thu nhiều nhất: " + topIncomeData[0], 
            (ImageIcon) topIncomeData[1],
            new Color(46, 204, 113), 
            new Color(235, 255, 240)
        ));

        mainContent.add(cardContainer, BorderLayout.CENTER);
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
        card.setMaximumSize(new Dimension(500, 180));
        card.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
       
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setOpaque(false);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(primaryColor);
        
        JLabel lblDesc = new JLabel("<html>" + desc + "</html>");
        lblDesc.setFont(new Font("Arial", Font.BOLD, 20));
        lblDesc.setForeground(new Color(60, 60, 60));
        
        textPanel.add(lblTitle);
        textPanel.add(lblDesc);

       
        if (icon != null) {
            JLabel lblIcon = new JLabel(icon);
            card.add(lblIcon, BorderLayout.WEST);
        }
        
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
                Image img = new ImageIcon(url).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {}
        return null;
    }
}