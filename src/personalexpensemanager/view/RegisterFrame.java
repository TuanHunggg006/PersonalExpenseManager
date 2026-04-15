package personalexpensemanager.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import personalexpensemanager.manager.AuthManager;
import personalexpensemanager.model.User;

public class RegisterFrame extends JFrame {
    public RegisterFrame(AuthManager auth) {
        setTitle("Đăng Ký Thành Viên");
        setSize(400, 550); 
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
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

        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setPreferredSize(new Dimension(320, 420));

      
        JTextField txtFullName = new JTextField();
        styleField(txtFullName);
        
        JTextField txtUser = new JTextField();
        styleField(txtUser);
        
        JPasswordField txtPass = new JPasswordField();
        styleField(txtPass);

        
        container.add(createLabelWrapper("Họ và tên"));
        container.add(txtFullName);
        container.add(Box.createRigidArea(new Dimension(0, 15)));
        
        container.add(createLabelWrapper("Tên tài khoản"));
        container.add(txtUser);
        container.add(Box.createRigidArea(new Dimension(0, 15)));
        
        container.add(createLabelWrapper("Mật khẩu"));
        container.add(txtPass);
        container.add(Box.createRigidArea(new Dimension(0, 30)));

        
        JButton btnOk = new JButton("Đăng ký ngay");
        btnOk.setMaximumSize(new Dimension(320, 50)); 
        btnOk.setPreferredSize(new Dimension(320, 50));
        btnOk.setBackground(Color.WHITE);
        btnOk.setForeground(new Color(0, 102, 204));
        btnOk.setFont(new Font("Arial", Font.BOLD, 18)); 
        btnOk.setFocusPainted(false);
        btnOk.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
        btnOk.setAlignmentX(Component.CENTER_ALIGNMENT);

        container.add(btnOk);
        add(container);

        
        btnOk.addActionListener(e -> {
            String username = txtUser.getText();
            String password = new String(txtPass.getPassword());
            String fullName = txtFullName.getText();
            String userId = "U" + System.currentTimeMillis() % 1000;

            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đủ thông tin!");
                return;
            }

            if (auth.register(new User(userId, username, password, fullName))) {
                JOptionPane.showMessageDialog(this, "Đăng ký thành công!");
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Tài khoản đã tồn tại!");
            }
        });
    }

    private JPanel createLabelWrapper(String text) {
   
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(320, 30)); // Tăng chiều cao từ 20 lên 30

        JLabel label = new JLabel(text);
    
        label.setFont(new Font("Arial", Font.BOLD, 17)); 
        label.setForeground(new Color(40, 40, 40)); // Màu đậm hơn một chút để rõ nét
    
        wrapper.add(label);
        return wrapper;
    }

    private void styleField(JTextField field) {
        field.setMaximumSize(new Dimension(320, 45)); // Chiều cao ô nhập giảm xuống 45
        field.setPreferredSize(new Dimension(320, 45));
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
            BorderFactory.createEmptyBorder(0, 12, 0, 12)
        ));
    }
}