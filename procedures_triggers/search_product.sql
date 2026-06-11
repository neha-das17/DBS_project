set serveroutput on;
CREATE OR REPLACE PROCEDURE search_product (
    p_keyword IN VARCHAR2
)
IS
BEGIN
    FOR rec IN (
        SELECT p.product_id, p.name, p.price, p.stock_quantity, c.category_name
        FROM PRODUCT p
        JOIN CATEGORY c ON p.category_id = c.category_id
        WHERE LOWER(p.name) LIKE LOWER('%' || p_keyword || '%')
           OR LOWER(c.category_name) LIKE LOWER('%' || p_keyword || '%')
    )
    LOOP
        DBMS_OUTPUT.PUT_LINE(
            rec.product_id || ' - ' ||
            rec.name || ' - ' ||
            rec.category_name || ' - Rs.' ||
            rec.price || ' - Stock: ' ||
            rec.stock_quantity
        );
    END LOOP;
END;
/