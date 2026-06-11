CREATE OR REPLACE PROCEDURE place_order(p_uid IN NUMBER)
IS
    CURSOR cart_cursor IS
        SELECT ci.product_id, ci.quantity
        FROM cart_item ci
        JOIN cart c ON ci.cart_id = c.cart_id
        WHERE c.user_id = p_uid;

    v_pid product.product_id%TYPE;
    v_qty NUMBER;
    v_price product.price%TYPE;
    v_total NUMBER := 0;
    v_oid orders.order_id%TYPE;
BEGIN
    INSERT INTO orders (user_id, order_date, total_amount)
    VALUES (p_uid, SYSDATE, 0)
    RETURNING order_id INTO v_oid;
    FOR rec IN cart_cursor LOOP
        v_pid := rec.product_id;
        v_qty := rec.quantity;
        BEGIN
            SELECT price INTO v_price
            FROM product
            WHERE product_id = v_pid;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                DBMS_OUTPUT.PUT_LINE('Product not found: ' || v_pid);
                CONTINUE;
        END;
        INSERT INTO order_item(order_id, product_id, quantity, price)
        VALUES (v_oid, v_pid, v_qty, v_price);
        UPDATE product
        SET stock_quantity = stock_quantity - v_qty
        WHERE product_id = v_pid;
        v_total := v_total + (v_price * v_qty);
    END LOOP;
    UPDATE orders
    SET total_amount = v_total
    WHERE order_id = v_oid;
    DELETE FROM cart_item
    WHERE cart_id IN (
        SELECT cart_id FROM cart WHERE user_id = p_uid
    );
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
/