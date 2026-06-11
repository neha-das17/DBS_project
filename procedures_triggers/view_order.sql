set serveroutput on;
CREATE OR REPLACE PROCEDURE view_orders(p_uid NUMBER)
IS
BEGIN
    FOR rec IN (
        SELECT * FROM orders
        WHERE user_id = p_uid
    ) LOOP
        DBMS_OUTPUT.PUT_LINE('Order ID: ' || rec.order_id ||
                             ' Total: ' || rec.total_amount);
    END LOOP;
END;
/