import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CartPage {

    int userId;

    public CartPage(int uid) {
        this.userId = uid;

        JFrame f = new JFrame("Cart");
        f.setSize(700, 500);
        f.setLayout(new BorderLayout());

        // 🔷 TOP PANEL (Title)
        JPanel topPanel = new JPanel();
        JLabel title = new JLabel("🛒 Your Shopping Cart");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        topPanel.add(title);

        // 🔷 CENTER PANEL (Cart Items)
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(area);

        // 🔷 BOTTOM PANEL (Controls)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(3, 2, 10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JTextField removeField = new JTextField();

        JButton refresh = new JButton("Refresh");
        JButton remove = new JButton("Remove Item");
        JButton checkout = new JButton("Checkout");
        JButton history = new JButton("View Orders");

        bottomPanel.add(new JLabel("CartItemID:"));
        bottomPanel.add(removeField);
        bottomPanel.add(refresh);
        bottomPanel.add(remove);
        bottomPanel.add(checkout);
        bottomPanel.add(history);

        // 🔷 ADD TO FRAME
        f.add(topPanel, BorderLayout.NORTH);
        f.add(scroll, BorderLayout.CENTER);
        f.add(bottomPanel, BorderLayout.SOUTH);

        try {
            Connection con = DBConnection.getConnection();

            Runnable loadCart = () -> {
                try {
                    area.setText("");

                    PreparedStatement ps = con.prepareStatement(
                        "SELECT CI.cart_item_id, P.name, CI.quantity, P.price " +
                        "FROM CART_ITEM CI " +
                        "JOIN CART C ON CI.cart_id = C.cart_id " +
                        "JOIN PRODUCT P ON CI.product_id = P.product_id " +
                        "WHERE C.user_id = ?"
                    );

                    ps.setInt(1, userId);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        area.append(
                            "ID: " + rs.getInt("cart_item_id") +
                            " | " + rs.getString("name") +
                            " | Qty: " + rs.getInt("quantity") +
                            " | Rs." + rs.getInt("price") + "\n"
                        );
                    }

                } catch (Exception ex) {
                    System.out.println(ex);
                }
            };

            loadCart.run();

            refresh.addActionListener(e -> loadCart.run());

            remove.addActionListener(e -> {
                try {
                    int cid = Integer.parseInt(removeField.getText());

                    PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM CART_ITEM WHERE cart_item_id=?"
                    );
                    ps.setInt(1, cid);
                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(f, "Item Removed!");
                    loadCart.run();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(f, "Error: " + ex.getMessage());
                }
            });

        } catch (Exception e) {
            System.out.println(e);
        }

        checkout.addActionListener(e -> {
            new Checkout(userId);
            f.dispose();
        });

        history.addActionListener(e -> {
            new OrderHistory(userId);
        });

        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}