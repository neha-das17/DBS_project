CREATE OR REPLACE PROCEDURE add_product(
    pid NUMBER,
    pname VARCHAR2,
    pdesc VARCHAR2,
    pprice NUMBER,
    pstock NUMBER,
    cid NUMBER
) IS
BEGIN
    INSERT INTO PRODUCT VALUES(pid, pname, pdesc, pprice, pstock, cid);
END;
/