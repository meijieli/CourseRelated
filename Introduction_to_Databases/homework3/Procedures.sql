-- -----------------------------------------------------
-- Procedure `RegistrationSystem`.`student_BEFORE_INSERT`
-- -----------------------------------------------------
DROP PROCEDURE IF EXISTS `insert_person_student`;
DELIMITER $$
CREATE PROCEDURE `insert_person_student`(UNI VARCHAR(12), Last_Name VARCHAR(45), First_Name VARCHAR(45))
BEGIN
	INSERT INTO person VALUES(UNI, Last_Name, First_Name, 'Student');
END; $$
DELIMITER ;


-- -----------------------------------------------------
-- Procedure `RegistrationSystem`.`student_BEFORE_UPDATE`
-- -----------------------------------------------------
DROP PROCEDURE IF EXISTS `update_person_student`;
DELIMITER $$
CREATE PROCEDURE `update_person_student`(UNI VARCHAR(12), Last_Name VARCHAR(45), First_Name VARCHAR(45))
BEGIN
	UPDATE person SET person.Last_Name = Last_Name, person.First_Name = First_Name
    WHERE person.UNI = UNI;
END; $$
DELIMITER ;


-- -----------------------------------------------------
-- Procedure `RegistrationSystem`.`student_BEFORE_DELETE`
-- -----------------------------------------------------
DROP PROCEDURE IF EXISTS `delete_person_student`;
DELIMITER $$
CREATE PROCEDURE `delete_person_student`(UNI VARCHAR(12))
BEGIN
	DELETE FROM person WHERE person.UNI = UNI;
END; $$
DELIMITER ;


-- -----------------------------------------------------
-- Procedure `RegistrationSystem`.`faculty_BEFORE_INSERT`
-- -----------------------------------------------------
DROP PROCEDURE IF EXISTS `insert_person_faculty`;
DELIMITER $$
CREATE PROCEDURE `insert_person_faculty`(UNI VARCHAR(12), Last_Name VARCHAR(45), First_Name VARCHAR(45))
BEGIN
	INSERT INTO person VALUES(UNI, Last_Name, First_Name, 'Faculty');
END; $$
DELIMITER ;


-- -----------------------------------------------------
-- Procedure `RegistrationSystem`.`faculty_BEFORE_UPDATE`
-- -----------------------------------------------------
DROP PROCEDURE IF EXISTS `update_person_faculty`;
DELIMITER $$
CREATE PROCEDURE `update_person_faculty`(UNI VARCHAR(12), Last_Name VARCHAR(45), First_Name VARCHAR(45))
BEGIN
	UPDATE person SET person.Last_Name = Last_Name, person.First_Name = First_Name
    WHERE person.UNI = UNI;
END; $$
DELIMITER ;


-- -----------------------------------------------------
-- Procedure `RegistrationSystem`.`faculty_BEFORE_DELETE`
-- -----------------------------------------------------
DROP PROCEDURE IF EXISTS `delete_person_faculty`;
DELIMITER $$
CREATE PROCEDURE `delete_person_faculty`(UNI VARCHAR(12))
BEGIN
	DELETE FROM person WHERE person.UNI = UNI;
END;  $$
DELIMITER ;