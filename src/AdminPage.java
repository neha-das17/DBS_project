import javax.swing.*;
import java.sql.*;

public class AdminPage {

    public AdminPage() {

        JFrame f = new JFrame("Admin Panel");

        JLabel title = new JLabel("Admin Controls");
        title.setBounds(120,20,200,30);

        JButton addBtn = new JButton("Add Product");
        addBtn.setBounds(100,70,150,30);

        JButton updateBtn = new JButton("Update Product");
        updateBtn.setBounds(100,110,150,30);

        JButton deleteBtn = new JButton("Delete Product");
        deleteBtn.setBounds(100,150,150,30);

        // 🔹 ADD PRODUCT
        addBtn.addActionListener(e -> {
            JTextField id = new JTextField();
            JTextField name = new JTextField();
            JTextField price = new JTextField();
            JTextField stock = new JTextField();

            Object[] fields = {
                "Product ID:", id,
                "Name:", name,
                "Price:", price,
                "Stock:", stock
            };

            int option = JOptionPane.showConfirmDialog(null, fields, "Add Product", JOptionPane.OK_CANCEL_OPTION);

            if(option == JOptionPane.OK_OPTION){
                try {
                    Connection con = DBConnection.getConnection();

                    CallableStatement cs = con.prepareCall("{call add_product(?,?,?,?,?,?)}");

                    cs.setInt(1, Integer.parseInt(id.getText()));
                    cs.setString(2, name.getText());
                    cs.setString(3, "New Product");
                    cs.setInt(4, Integer.parseInt(price.getText()));
                    cs.setInt(5, Integer.parseInt(stock.getText()));
                    cs.setInt(6, 1); // category

                    cs.execute();

                    JOptionPane.showMessageDialog(f, "Product Added!");

                } catch(Exception ex){
                    JOptionPane.showMessageDialog(f, ex.getMessage());
                }
            }
        });

        // 🔹 UPDATE PRODUCT
        updateBtn.addActionListener(e -> {
            JTextField id = new JTextField();
            JTextField price = new JTextField();
            JTextField stock = new JTextField();

            Object[] fields = {
                "Product ID:", id,
                "New Price:", price,
                "New Stock:", stock
            };

            int option = JOptionPane.showConfirmDialog(null, fields, "Update Product", JOptionPane.OK_CANCEL_OPTION);

            if(option == JOptionPane.OK_OPTION){
                try {
                    Connection con = DBConnection.getConnection();

                    CallableStatement cs = con.prepareCall("{call update_product(?,?,?)}");

                    cs.setInt(1, Integer.parseInt(id.getText()));
                    cs.setInt(2, Integer.parseInt(price.getText()));
                    cs.setInt(3, Integer.parseInt(stock.getText()));

                    cs.execute();

                    JOptionPane.showMessageDialog(f, "Product Updated!");

                } catch(Exception ex){
                    JOptionPane.showMessageDialog(f, ex.getMessage());
                }
            }
        });

        // 🔹 DELETE PRODUCT
        deleteBtn.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Enter Product ID to delete:");

            try {
                Connection con = DBConnection.getConnection();

                CallableStatement cs = con.prepareCall("{call delete_product(?)}");
                cs.setInt(1, Integer.parseInt(id));

                cs.execute();

                JOptionPane.showMessageDialog(f, "Product Deleted!");

            } catch(Exception ex){
                JOptionPane.showMessageDialog(f, ex.getMessage());
            }
        });

        f.add(title);
        f.add(addBtn);
        f.add(updateBtn);
        f.add(deleteBtn);

        f.setSize(350,250);
        f.setLayout(null);
        f.setVisible(true);
    }
}
