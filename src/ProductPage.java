import javax.swing.*;
import java.sql.*;

public class ProductPage {

    int userId;

    public ProductPage(int uid) {
        this.userId = uid;

        JFrame f = new JFrame("Products");

        JTextArea area = new JTextArea();
        area.setEditable(false);

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBounds(20, 20, 440, 150);

        JTextField searchField = new JTextField();
        searchField.setBounds(20, 190, 150, 30);

        JButton searchBtn = new JButton("Search");
        searchBtn.setBounds(180, 190, 100, 30);

        JButton history = new JButton("View Orders");
        history.setBounds(300, 190, 150, 30);

        JLabel l1 = new JLabel("Product ID:");
        l1.setBounds(20, 240, 100, 30);

        JTextField productField = new JTextField();
        productField.setBounds(140, 240, 120, 30);

        JLabel l2 = new JLabel("Quantity:");
        l2.setBounds(20, 280, 100, 30);

        JTextField qtyField = new JTextField();
        qtyField.setBounds(140, 280, 120, 30);

        JButton addBtn = new JButton("Add to Cart");
        addBtn.setBounds(50, 340, 150, 30);

        JButton cartBtn = new JButton("Go to Cart");
        cartBtn.setBounds(230, 340, 150, 30);

        history.addActionListener(e -> new OrderHistory(userId));

        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();

            searchBtn.addActionListener(e -> {
                try {
                    String keyword = searchField.getText();

                    PreparedStatement ps = con.prepareStatement(
                        "SELECT p.product_id, p.name, p.price, p.stock_quantity, c.category_name " +
                        "FROM PRODUCT p JOIN CATEGORY c ON p.category_id = c.category_id " +
                        "WHERE LOWER(p.name) LIKE LOWER(?) OR LOWER(c.category_name) LIKE LOWER(?)"
                    );

                    ps.setString(1, "%" + keyword + "%");
                    ps.setString(2, "%" + keyword + "%");

                    ResultSet rs = ps.executeQuery();
                    area.setText("");

                    boolean found = false;

                    while(rs.next()){
                        found = true;
                        area.append(
                            "ID: " + rs.getInt("product_id") +
                            " | " + rs.getString("name") +
                            " | " + rs.getString("category_name") +
                            " | Rs." + rs.getInt("price") +
                            " | Stock: " + rs.getInt("stock_quantity") + "\n"
                        );
                    }

                    if(!found){
                        area.setText("No products found.");
                    }

                } catch(Exception ex){
                    JOptionPane.showMessageDialog(f, ex.getMessage());
                }
            });

            ResultSet rs = st.executeQuery(
                "SELECT p.product_id, p.name, p.price, p.stock_quantity, c.category_name " +
                "FROM PRODUCT p JOIN CATEGORY c ON p.category_id = c.category_id"
            );

            while(rs.next()) {
                area.append(
                    "ID: " + rs.getInt("product_id") +
                    " | " + rs.getString("name") +
                    " | " + rs.getString("category_name") +
                    " | Rs." + rs.getInt("price") +
                    " | Stock: " + rs.getInt("stock_quantity") + "\n"
                );
            }

            addBtn.addActionListener(e -> {
                try {
                    int productId = Integer.parseInt(productField.getText());
                    int quantity = Integer.parseInt(qtyField.getText());

                    PreparedStatement check = con.prepareStatement(
                        "SELECT stock_quantity FROM PRODUCT WHERE product_id=?"
                    );

                    check.setInt(1, productId);
                    ResultSet rsCheck = check.executeQuery();

                    if(rsCheck.next()){
                        int stock = rsCheck.getInt("stock_quantity");

                        if(quantity > stock){
                            JOptionPane.showMessageDialog(f,"Not enough stock!");
                            return;
                        }
                    } else {
                        JOptionPane.showMessageDialog(f,"Invalid Product ID!");
                        return;
                    }

                    PreparedStatement ps1 = con.prepareStatement(
                        "SELECT cart_id FROM CART WHERE user_id=?"
                    );
                    ps1.setInt(1, userId);
                    ResultSet rs1 = ps1.executeQuery();

                    int cartId = 0;
                    if(rs1.next()) {
                        cartId = rs1.getInt("cart_id");
                    } else {
                        cartId = (int)(Math.random()*1000);

                        PreparedStatement psNew = con.prepareStatement(
                            "INSERT INTO CART VALUES (?,?)"
                        );
                        psNew.setInt(1, cartId);
                        psNew.setInt(2, userId);
                        psNew.executeUpdate();
                    }

                    PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO CART_ITEM VALUES (?,?,?,?)"
                    );

                    ps.setInt(1, (int)(Math.random()*1000));
                    ps.setInt(2, cartId);
                    ps.setInt(3, productId);
                    ps.setInt(4, quantity);

                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(f, "Added to Cart!");

                } catch(Exception ex) {
                    JOptionPane.showMessageDialog(f, ex.getMessage());
                }
            });

        } catch(Exception e) {
            System.out.println(e);
        }

        cartBtn.addActionListener(e -> {
            new CartPage(userId);
            f.dispose();
        });

        f.add(scroll);
        f.add(searchField);
        f.add(searchBtn);
        f.add(history);
        f.add(l1); 
        f.add(productField);
        f.add(l2); 
        f.add(qtyField);
        f.add(addBtn);
        f.add(cartBtn);

        f.setSize(500, 450);
        f.setLayout(null);
        f.setVisible(true);
    }
}