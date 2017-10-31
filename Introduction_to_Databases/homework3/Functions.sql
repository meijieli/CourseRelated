USE `RegistrationSystem`;

-- -----------------------------------------------------
-- Function `RegistrationSystem`.`get_semester`
-- Return the semester number according to the month
-- -----------------------------------------------------
DROP FUNCTION IF EXISTS `get_semester`;
DELIMITER $$
CREATE FUNCTION `get_semester` () RETURNS INT(1)
BEGIN
	DECLARE current_month CHAR(15);
    DECLARE result INT(1);
    
    SELECT DATE_FORMAT(NOW(), '%M') INTO current_month;
    
    IF current_month = 'September' OR current_month = 'October' OR current_month = 'November' OR current_month = 'December' THEN
		SET result = 1;
	ELSEIF current_month = 'January' OR current_month = 'February' OR current_month = 'March' OR current_month = 'April' THEN
		SET result = 2;
	ELSEIF current_month = 'May' OR current_month = 'June' THEN
		SET result = 3;
	ELSE 
		SET result = 4;
	END IF;
    
RETURN result;
END; $$
DELIMITER ;


-- -----------------------------------------------------
-- Function `RegistrationSystem`.`generate_uni`
-- Return a legal uni that is generated from the person's first and last name
-- -----------------------------------------------------
DROP FUNCTION IF EXISTS `generate_uni`;
DELIMITER $$
CREATE FUNCTION `generate_uni`(last_name VARCHAR(45), first_name VARCHAR(45)) RETURNS varchar(12) 
BEGIN 
	DECLARE c1 CHAR(1);
    DECLARE c2 CHAR(1);
    DECLARE prefix CHAR(3);
    DECLARE uni_count INT;
    DECLARE newUni VARCHAR(12);
    
    SET c1 = UPPER(SUBSTR(last_name, 1, 1));
    SET c2 = UPPER(SUBSTR(first_name, 1, 1));
    SET prefix = CONCAT(c1, c2, '%');
    
    SELECT COUNT(UNI) FROM Person WHERE UNI LIKE prefix INTO uni_count;
    SET newUni = CONCAT(SUBSTR(prefix, 1, 2), uni_count + 1);
    
RETURN newUni;
END; $$
DELIMITER ;


-- -----------------------------------------------------
-- Function `RegistrationSystem`.`check_faculty`
-- Return a value indicates whether there is an error when insert a faculty to course_participant
-- -----------------------------------------------------
DROP FUNCTION IF EXISTS `check_faculty`;
DELIMITER $$
CREATE FUNCTION `check_faculty`(Current_key VARCHAR(45), Current_UNI VARCHAR(12)) RETURNS INT 
BEGIN 

		DECLARE result INT;
        
		/* Check whether the class has already had a faculty */
		IF EXISTS (SELECT * FROM course_participant WHERE Current_key = course_participant.Section_Key AND
			course_participant.Type = 'Faculty') THEN
            SET result = 1;


		/* Check whether the assigned faculty has already taught three classes IN THIS SEMESTER*/
		ELSEIF  
        (SELECT COUNT(*) FROM 
			course_participant JOIN sections ON course_participant.Section_key = sections.Section_key
            WHERE UNI = Current_UNI AND year = 
					(SELECT year FROM sections
					WHERE sections.Section_key = Current_key)            
            AND semester = 
					(SELECT semester FROM sections
					WHERE sections.Section_key = Current_key)) = 3 THEN
                    SET result = 2;
		END IF;
    
RETURN result;
END; $$
DELIMITER ;


-- -----------------------------------------------------
-- Function `RegistrationSystem`.`check_student`
-- Return a value indicates whether there is an error when insert a student to course_participant
-- -----------------------------------------------------
DROP FUNCTION IF EXISTS `check_student`;
DELIMITER $$
CREATE FUNCTION `check_student`(Current_key VARCHAR(45), Current_UNI VARCHAR(12)) RETURNS TINYINT 
BEGIN 
		
        /* Check whether there is a prerequisite course that is not in the courses that this student has taken */
		SET @result = (SELECT EXISTS 
			(SELECT A.prereq_id FROM
				((SELECT prereq_id FROM course_prereqs WHERE course_id = 
					(SELECT DISTINCT(course_id) FROM sections JOIN course_participant ON sections.Section_Key = course_participant.Section_key
					WHERE sections.Section_key = Current_key)) AS A)
			WHERE A.prereq_id NOT IN
				(SELECT CourseID FROM course_taken WHERE course_taken.StudentUNI = Current_UNI)));

		IF @result = 1 THEN
			RETURN FALSE;
		ELSE
			RETURN TRUE;
		END IF;
        
END; $$
DELIMITER ;