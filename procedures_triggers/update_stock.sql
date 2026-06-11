CREATE OR REPLACE TRIGGER update_stock
AFTER INSERT ON order_item
FOR EACH ROW
BEGIN
    UPDATE product
    SET stock_quantity = stock_quantity - :NEW.quantity
    WHERE product_id = :NEW.product_id;
END;
/