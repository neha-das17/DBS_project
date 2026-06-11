import javax.swing.*;
import java.sql.*;

public class Checkout {

    int userId;

    public Checkout(int uid) {
        this.userId = uid;

        JFrame f = new JFrame("Checkout");

        JLabel label = new JLabel("Click to Place Order");
        label.setBounds(50,40,200,30);

        JButton place = new JButton("Place Order");
        place.setBounds(50,100,150,30);

        JButton back = new JButton("Back to Cart");
        back.setBounds(50,150,150,30);

        // 🔹 Place Order Button
        place.addActionListener(e -> {
            try {
                Connection con = DBConnection.getConnection();

                CallableStatement cs =
                    con.prepareCall("{call place_order(?)}");

                cs.setInt(1, userId); // dynamic user
                cs.execute();

                JOptionPane.showMessageDialog(f, "Order Placed Successfully!");

                con.close();

            } catch(Exception ex) {
                JOptionPane.showMessageDialog(f, "Error: " + ex.getMessage());
            }
        });

        // 🔹 Back button
        back.addActionListener(e -> {
            new CartPage(userId);
            f.dispose();
        });

        f.add(label);
        f.add(place);
        f.add(back);

        f.setSize(300,250);
        f.setLayout(null);
        f.setVisible(true);
    }
}