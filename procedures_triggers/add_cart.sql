CREATE OR REPLACE PROCEDURE add_to_cart(
    p_cart_id NUMBER,
    p_pid NUMBER,
    p_qty NUMBER
)
IS
BEGIN
    INSERT INTO cart_item(cart_id, product_id, quantity)
    VALUES (p_cart_id, p_pid, p_qty);
END;
/