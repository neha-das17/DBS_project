CREATE OR REPLACE PROCEDURE remove_from_cart(
    p_cart_id NUMBER,
    p_pid NUMBER
)
IS
BEGIN
    DELETE FROM cart_item
    WHERE cart_id = p_cart_id AND product_id = p_pid;
END;
/