import javax.swing.*;
import java.sql.*;

public class Login {

    public Login() {
        JFrame f = new JFrame("Login");

        JLabel l1 = new JLabel("Email:");
        l1.setBounds(30,50,80,30);

        JTextField email = new JTextField();
        email.setBounds(100,50,150,30);

        JLabel l2 = new JLabel("Password:");
        l2.setBounds(30,100,80,30);

        JPasswordField pass = new JPasswordField();
        pass.setBounds(100,100,150,30);

        JButton login = new JButton("User Login");
        login.setBounds(50,160,120,30);

        JButton adminLogin = new JButton("Admin Login");
        adminLogin.setBounds(180,160,140,30);

        // 🔹 USER LOGIN
        login.addActionListener(e -> {
            try {
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(
                    "SELECT user_id FROM USERS WHERE email=? AND password=? AND role='customer'"
                );

                ps.setString(1, email.getText());
                ps.setString(2, new String(pass.getPassword()));

                ResultSet rs = ps.executeQuery();

                if(rs.next()) {
                    int userId = rs.getInt("user_id");

                    JOptionPane.showMessageDialog(f, "User Login Successful!");

                    new ProductPage(userId);
                    f.dispose();

                } else {
                    JOptionPane.showMessageDialog(f, "Invalid User Credentials");
                }

                con.close();

            } catch(Exception ex) {
                JOptionPane.showMessageDialog(f, ex.getMessage());
            }
        });

        // 🔹 ADMIN LOGIN
        adminLogin.addActionListener(e -> {
            try {
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(
                    "SELECT user_id FROM USERS WHERE email=? AND password=? AND role='admin'"
                );

                ps.setString(1, email.getText());
                ps.setString(2, new String(pass.getPassword()));

                ResultSet rs = ps.executeQuery();

                if(rs.next()) {

                    JOptionPane.showMessageDialog(f, "Admin Login Successful!");

                    new AdminPage();
                    f.dispose();

                } else {
                    JOptionPane.showMessageDialog(f, "Invalid Admin Credentials");
                }

                con.close();

            } catch(Exception ex) {
                JOptionPane.showMessageDialog(f, ex.getMessage());
            }
        });

        f.add(l1); f.add(email);
        f.add(l2); f.add(pass);
        f.add(login);
        f.add(adminLogin);

        f.setSize(400,300);
        f.setLayout(null);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        new Login();
    }
}