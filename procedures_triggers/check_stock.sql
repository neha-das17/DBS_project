CREATE OR REPLACE TRIGGER check_stock
BEFORE INSERT ON ORDER_ITEM
FOR EACH ROW
DECLARE
    available NUMBER;
BEGIN
    SELECT stock_quantity INTO available
    FROM PRODUCT
    WHERE product_id = :NEW.product_id;

    IF available < :NEW.quantity THEN
        RAISE_APPLICATION_ERROR(-20001, 'Not enough stock');
    END IF;
END;
/