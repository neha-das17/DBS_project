import javax.swing.*;
import java.sql.*;

public class OrderHistory {

    int userId;

    public OrderHistory(int uid){
        this.userId = uid;

        JFrame f = new JFrame("Order History");

        JTextArea area = new JTextArea();
        area.setBounds(20,20,350,250);

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
            "SELECT * FROM ORDERS WHERE user_id=?");

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                area.append(
                    "OrderID: " + rs.getInt("order_id") +
                    " | Date: " + rs.getDate("order_date") +
                    " | Total: Rs." + rs.getInt("total_amount") + "\n"
                );
            }

        } catch(Exception e){
            System.out.println(e);
        }

        f.add(area);
        f.setSize(400,350);
        f.setLayout(null);
        f.setVisible(true);
    }
}
