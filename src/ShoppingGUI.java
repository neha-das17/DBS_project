import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class ShoppingGUI {

    public static void main(String[] args) {

        JFrame f = new JFrame("Shopping System");

        JLabel l1 = new JLabel("Enter User ID:");
        l1.setBounds(50, 50, 150, 30);

        JTextField t1 = new JTextField();
        t1.setBounds(200, 50, 150, 30);

        JButton b1 = new JButton("Place Order");
        b1.setBounds(120, 120, 150, 40);

        f.add(l1);
        f.add(t1);
        f.add(b1);

        f.setSize(400, 250);
        f.setLayout(null);
        f.setVisible(true);

        // Button click event
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int uid = Integer.parseInt(t1.getText());

                    // Load driver
                    Class.forName("oracle.jdbc.driver.OracleDriver");

                    // Connect to DB
                    Connection con = DriverManager.getConnection(
                        "jdbc:oracle:thin:@localhost:1521/XEPDB1",
                        "shopdb",
                        "ANDBSlab"
                    );

                    // Call procedure
                    CallableStatement cs = con.prepareCall("{call place_order(?)}");
                    cs.setInt(1, uid);
                    cs.execute();

                    JOptionPane.showMessageDialog(f, "Order Placed Successfully!");

                    con.close();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(f, "Error: " + ex.getMessage());
                }
            }
        });
    }
}