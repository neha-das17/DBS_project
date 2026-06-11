CREATE OR REPLACE PROCEDURE update_product(
    pid NUMBER,
    pprice NUMBER,
    pstock NUMBER
) IS
BEGIN
    UPDATE PRODUCT
    SET price = pprice,
        stock_quantity = pstock
    WHERE product_id = pid;
END;
/