package personalexpensemanager.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import personalexpensemanager.manager.AuthManager;
import personalexpensemanager.model.User;

public class LoginFrame extends JFrame {
    private AuthManager authManager = new AuthManager();

    public LoginFrame() {
        setTitle("Đăng Nhập Hệ Thống");
        setSize(450, 650); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- Panel Nền ---
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    ImageIcon bgIcon = new ImageIcon(getClass().getResource("/img/background.png"));
                    g.drawImage(bgIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {}
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());
        this.setContentPane(backgroundPanel);

        // --- Form chứa các thành phần ---
        JPanel loginForm = new JPanel();
        loginForm.setOpaque(false);
        loginForm.setLayout(new BoxLayout(loginForm, BoxLayout.Y_AXIS));

        // 1. LOGO (Cân đối 220x220)
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/img/logo.png"));
            Image img = logoIcon.getImage().getScaledInstance(320, 230, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(img));
            lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
            loginForm.add(lblLogo);
        } catch (Exception e) {}

        loginForm.add(Box.createRigidArea(new Dimension(0, 40)));

        // 2. Ô TÀI KHOẢN
        JTextField txtUser = new JTextField();
        setupField(txtUser, "/img/icon_user.png", "Tài khoản");
        loginForm.add(txtUser);
        
        loginForm.add(Box.createRigidArea(new Dimension(0, 25)));

        // 3. Ô MẬT KHẨU
        JPasswordField txtPass = new JPasswordField();
        setupField(txtPass, "/img/icon_lock.png", "Mật khẩu");
        loginForm.add(txtPass);

        // 4. LINK ĐĂNG KÝ (Đã làm to hơn và chỉnh vị trí)
        JLabel lblReg = new JLabel("Đăng ký tài khoản ");
        // Tăng cỡ chữ lên 17 và in đậm (BOLD)
        lblReg.setFont(new Font("Arial", Font.BOLD, 17)); 
        lblReg.setForeground(new Color(0, 102, 204));
        lblReg.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Panel bao quanh để đẩy chữ sang phải
        JPanel regWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        regWrapper.setOpaque(false);
        // Đồng bộ chiều rộng 380 với ô nhập liệu
        regWrapper.setMaximumSize(new Dimension(380, 35)); 
        regWrapper.add(lblReg);
        loginForm.add(regWrapper);
        
        loginForm.add(Box.createRigidArea(new Dimension(0, 30)));

        // 5. NÚT ĐĂNG NHẬP (Kích thước to chuẩn 380x70)
        JButton btnLogin = new JButton("Đăng nhập");
        Dimension buttonSize = new Dimension(380, 70);
        btnLogin.setMaximumSize(buttonSize);
        btnLogin.setPreferredSize(buttonSize);
        btnLogin.setMinimumSize(buttonSize);
        
        btnLogin.setBackground(Color.WHITE);
        btnLogin.setForeground(new Color(0, 102, 204));
        btnLogin.setFont(new Font("Arial", Font.BOLD, 22));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginForm.add(btnLogin);

        add(loginForm);

        // --- Xử lý sự kiện ---
        btnLogin.addActionListener(e -> {
            User user = authManager.login(txtUser.getText(), new String(txtPass.getPassword()));
            if (user != null) {
                new MainFrame().setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Tài khoản hoặc mật khẩu không chính xác!");
            }
        });

        lblReg.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RegisterFrame(authManager).setVisible(true);
            }
        });
    }

    private void setupField(JTextField field, String iconPath, String hint) {
        field.setMaximumSize(new Dimension(380, 55));
        field.setPreferredSize(new Dimension(380, 55));
        field.setFont(new Font("Arial", Font.PLAIN, 18));
        
        ImageIcon icon = null;
        try {
            Image img = new ImageIcon(getClass().getResource(iconPath)).getImage();
            icon = new ImageIcon(img.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        } catch (Exception e) {}

        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(0, 55, 0, 10)
        ));

        final ImageIcon finalIcon = icon;
        field.setUI(new javax.swing.plaf.basic.BasicTextFieldUI() {
            @Override
            protected void paintBackground(Graphics g) {
                super.paintBackground(g);
                if (finalIcon != null) {
                    g.drawImage(finalIcon.getImage(), 12, 12, null);
                }
            }
        });

        field.setText(hint);
        field.setForeground(Color.GRAY);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(hint)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(hint);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }
}