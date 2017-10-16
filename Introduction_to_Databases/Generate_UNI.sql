DELIMITER $$
CREATE FUNCTION `generate_uni` (last_name VARCHAR(32), first_name VARCHAR(32)) RETURNS VARCHAR(8)
BEGIN 
	DECLARE c1 CHAR(2);
    DECLARE c2 CHAR(2);
    DECLARE prefix CHAR(5);
    DECLARE uni_count INT;
    DECLARE newUni VARCHAR(8);
    
    SET c1 = UPPER(SUBSTR(last_name, 1, 2));
    SET c2 = UPPER(SUBSTR(first_name, 1, 2));
    SET prefix = CONCAT(c1, c2, "%");
    
    SELECT COUNT(uni) FROM People WHERE UNI LIKE prefix INTO uni_count;
    Set newUni = CONCAT(prefix, uni_count + 1);
    
RETURN newUni;
END $$
DELIMITER ;
