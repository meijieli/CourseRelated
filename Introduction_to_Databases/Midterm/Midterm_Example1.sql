/* Mid-term example --- Modify order details table in northwind database */
/* A weak entity uses a LineNo as part of Primary Key */
/* Goal: 		
	Create a new OrderDetails table that use OrderID + LineNo as primary key and 
	OrderID and ProductID as foreign key */
    
/* Step 1:
	Copy data into new table */
DROP TABLE `NewOrderDetails`;
CREATE TABLE NewOrderDetails AS SELECT * FROM `order details`;

/* Step 2:
	Modify the schema of new table to meet our requirement */
ALTER TABLE `northwind`.`NewOrderDetails` 
/* Add a column LineNo, have to set it to auto_increment now, or there will be duplicate primary keys. */
ADD COLUMN `LineNo` INT NOT NULL AUTO_INCREMENT,		
ADD PRIMARY KEY(`LineNo`, `OrderID`),
ADD INDEX `fk_order_id_idx`(`OrderID` ASC),
ADD INDEX `fk_product_id_idx`(`ProductID` ASC);
/*
ADD CONSTRAINT `	fk_order_id`
	FOREIGN KEY (`OrderID`)			
    REFERENCES `northwind`.`Orders`(`OrderID`)	
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    
ADD CONSTRAINT `fk_product_id`
	FOREIGN KEY (`ProductID`)
    REFERENCES `northwind`.`Products`(`ProductID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    */


/* Step 3:
	We will need to take two steps to correct LineNo.
	1) Cancel the auto_increment on LineNo.
    2) Modify(Update) current LineNo.
    */
ALTER TABLE `northwind`.`NewOrderDetails`
CHANGE COLUMN `LineNo` `LineNo` INT(11) NOT NULL; /* Be carful with syntax here, two table names. */

/* This part won't work! Can't query a table and update it at the same time 
UPDATE NewOrderDetails SET LineNo = 
	(SELECT Count(*) AS NewLineNo FROM northwind.NewOrderDetails as A WHERE NewOrderDetails.OrderID = A.OrderID
	AND NewOrderDetails.LineNo <= A.LineNo);
*/

/* Step 4:
    Add trigger to let LineNo increment for each orderID */
DELIMITER $$
CREATE TRIGGER `northwind`.`NewOrderDetails_BEFORE_INSERT` BEFORE INSERT ON `NewOrderDetails` FOR EACH ROW
BEGIN
	DECLARE new_line_no INT;
    
    SELECT MAX(`LineNo`) INTO new_line_no FROM NewOrderDetails WHERE OrderID = New.OrderID;
    
    SET new_line_no = new_line_no + 1;
    
    SET New.LineNo = new_line_no;

END $$
DELIMITER ;

DELIMITER $$	
CREATE TRIGGER `northwind`.`NewOrderDetails_BEFORE_UPDATE` BEFORE UPDATE ON `NewOrderDetails` FOR EACH ROW
BEGIN
	IF NOT New.LineNo = Old.LineNo THEN
		SIGNAL SQLSTATE '04011'
			SET MESSAGE_TEXT = 'Cannot change a line item';
	END IF;
END $$
DELIMITER ;

/* Step 5:
	Create a Procedure to update LineNo when deleting an order detail */
DELIMITER $$
CREATE PROCEDURE `delete_NewOrderDetails` (OldOrderID int, OldLineNo int)
BEGIN 
	DELETE FROM NewOrderDetails WHERE LineNo = OldLindeNo AND OrderID = OldOrderID;
    
    UPDATE NewOrderDetails SET LineNo = LineNo - 1 WHERE LineNo > OldLineNo AND OrderID = OldOrderID;
END $$
DELIMITER ;
