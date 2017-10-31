-- -----------------------------------------------------
-- Trigger `RegistrationSystem`.`student_BEFORE_INSERT`
-- -----------------------------------------------------
DROP TRIGGER IF EXISTS `student_BEFORE_INSERT`;
DELIMITER $$
CREATE TRIGGER `RegistrationSystem`.`student_BEFORE_INSERT`
	BEFORE INSERT ON `student` FOR EACH ROW
BEGIN
	
    /* Discard the UNI given by the user, generate a new UNI */
    DECLARE generated_UNI VARCHAR(12);
    SET generated_UNI = generate_uni(NEW.Last_Name, NEW.First_Name);
    
    /* Check the validity of enrollment year */
    IF NEW.year > (SELECT DATE_FORMAT(NOW(), '%Y')) THEN
		SIGNAL SQLSTATE '45001'
			SET MESSAGE_TEXT = 'Invalid enrollment year';
	ELSE
		SET NEW.UNI = generated_UNI;
        /* Call procedure to insert in person table */
		CALL insert_person_student(generated_UNI, NEW.Last_Name, New.First_Name);
	END IF;
END; $$
DELIMITER ;


-- -----------------------------------------------------
-- Trigger `RegistrationSystem`.`student_BEFORE_UPDATE`
-- -----------------------------------------------------
DROP TRIGGER IF EXISTS `student_BEFORE_UPDATE`;
DELIMITER $$
CREATE TRIGGER `RegistrationSystem`.`student_BEFORE_UPDATE`
	BEFORE UPDATE ON `student` FOR EACH ROW
BEGIN
	
    /* Check whether the primary key is updated */
    IF NOT NEW.UNI = OLD.UNI THEN
		SIGNAL SQLSTATE '45001'
			SET MESSAGE_TEXT = 'Cannot change UNI';
	ELSEIF NEW.year > (SELECT DATE_FORMAT(NOW(), '%Y')) THEN
		SIGNAL SQLSTATE '45001'
			SET MESSAGE_TEXT = 'Invalid enrollment year';
	ELSE
        /* Call procedure to update on person table */
		CALL update_person_student(OLD.UNI, NEW.Last_Name, New.First_Name);
	END IF;
END; $$
DELIMITER ;
    

-- -----------------------------------------------------
-- Trigger `RegistrationSystem`.`student_BEFORE_DELETE`
-- -----------------------------------------------------
DROP TRIGGER IF EXISTS `student_BEFORE_DELETE`;
DELIMITER $$
CREATE TRIGGER `RegistrationSystem`.`student_BEFORE_DELETE`
	BEFORE UPDATE ON `student` FOR EACH ROW
BEGIN
	
    /* Check whether the student exists */
    IF NOT EXISTS (SELECT * FROM student WHERE student.UNI = NEW.UNI) THEN
		SIGNAL SQLSTATE '45001'
			SET MESSAGE_TEXT = 'Invalid UNI';
	ELSE
        /* Call procedure to delete on person table */
		CALL delete_person_student(OLD.UNI);
	END IF;
END; $$
DELIMITER ;

    
-- -----------------------------------------------------
-- Trigger `RegistrationSystem`.`faculty_BEFORE_INSERT`
-- -----------------------------------------------------
DROP TRIGGER IF EXISTS `faculty_BEFORE_INSERT`;
DELIMITER $$
CREATE TRIGGER `RegistrationSystem`.`faculty_BEFORE_INSERT`
	BEFORE INSERT ON `faculty` FOR EACH ROW
BEGIN
	
	/* Discard the UNI given by the user, generate a new UNI */
    DECLARE generated_UNI VARCHAR(12);
    SET generated_UNI = generate_uni(NEW.Last_Name, NEW.First_Name);

    /* Check the validity of pay grade */    
    IF NEW.pay_grade NOT IN ('1','2','3','7') THEN
		SIGNAL SQLSTATE '45001'
			SET MESSAGE_TEXT = 'Invalid pay grade';
	ELSE
		SET NEW.UNI = generated_UNI;
        /* Call procedure to insert in person table */
		CALL insert_person_faculty(generated_UNI, NEW.Last_Name, New.First_Name);
	END IF;
END; $$
DELIMITER ;


-- -----------------------------------------------------
-- Trigger `RegistrationSystem`.`faculty_BEFORE_UPDATE`
-- -----------------------------------------------------
DROP TRIGGER IF EXISTS `faculty_BEFORE_UPDATE`;
DELIMITER $$
CREATE TRIGGER `RegistrationSystem`.`faculty_BEFORE_UPDATE`
	BEFORE UPDATE ON `faculty` FOR EACH ROW
BEGIN
	
    /* Check whether the primary key is updated */
    IF NOT NEW.UNI = OLD.UNI THEN
		SIGNAL SQLSTATE '45001'
			SET MESSAGE_TEXT = 'Cannot change UNI';
	ELSEIF NEW.pay_grade NOT IN ('1','2','3','7') THEN
		SIGNAL SQLSTATE '45001'
			SET MESSAGE_TEXT = 'Invalid pay grade';
	ELSE
        /* Call procedure to update on person table */
		CALL update_person_faculty(OLD.UNI, NEW.Last_Name, New.First_Name);
	END IF;
END; $$
DELIMITER ;


-- -----------------------------------------------------
-- Trigger `RegistrationSystem`.`faculty_BEFORE_DELETE`
-- -----------------------------------------------------
DROP TRIGGER IF EXISTS `faculty_BEFORE_DELETE`;
DELIMITER $$
CREATE TRIGGER `RegistrationSystem`.`faculty_BEFORE_DELETE`
	BEFORE UPDATE ON `faculty` FOR EACH ROW
BEGIN
	
    /* Check whether the faculty exists */
    IF NOT EXISTS (SELECT * FROM faculty WHERE faculty.UNI = NEW.UNI) THEN
		SIGNAL SQLSTATE '45001'
			SET MESSAGE_TEXT = 'Invalid UNI';
	ELSE
        /* Call procedure to delete on person table */
		CALL delete_person_faculty(OLD.UNI);
	END IF;
END; $$
DELIMITER ;


-- -----------------------------------------------------
-- Trigger `RegistrationSystem`.`person_BEFORE_INSERT`
-- -----------------------------------------------------
DROP TRIGGER IF EXISTS `person_BEFORE_INSERT`;
DELIMITER $$
CREATE TRIGGER `RegistrationSystem`.`person_BEFORE_INSERT`
	BEFORE INSERT ON `person` FOR EACH ROW
BEGIN
	
    DECLARE generated_UNI VARCHAR(12);
    SET generated_UNI = generate_uni(NEW.Last_Name, NEW.First_Name);
    /* Ignore the security issue of inserting to Person here */
END; $$
DELIMITER ;


-- -----------------------------------------------------
-- Trigger `RegistrationSystem`.`course_participant_BEFORE_INSERT`
-- -----------------------------------------------------
DROP TRIGGER IF EXISTS `course_participant_BEFORE_INSERT`;
DELIMITER $$
CREATE TRIGGER `RegistrationSystem`.`course_participant_BEFORE_INSERT`
	BEFORE INSERT ON `course_participant` FOR EACH ROW
BEGIN
	
    DECLARE current_num INT;
    
    /* Check whether the class is at capacity */
    SELECT COUNT(UNI) FROM course_participant 
		WHERE course_participant.Section_Key = NEW.Section_Key AND course_participant.Type = 'Student' INTO current_num;
	IF current_num > (SELECT sections.LimitNum FROM sections WHERE sections.Section_Key = NEW.Section_Key) THEN
		SIGNAL SQLSTATE '45001'
			SET MESSAGE_TEXT = 'The class has reached its capacity';
	END IF;
	
    /* If the inserted record is a faculty */
    IF NEW.Type = 'Faculty' THEN
		IF check_faculty(NEW.Section_Key, NEW.UNI) = 1 THEN
				SIGNAL SQLSTATE '45001'
				SET MESSAGE_TEXT = 'The class has already had a faculty';
       ELSEIF check_faculty(NEW.Section_Key, NEW.UNI) = 2 THEN
				SIGNAL SQLSTATE '45001'
				SET MESSAGE_TEXT = 'This faculty has already taught three classes';
		END IF;
	END IF;
    
    /* Check whether the student has finished the prerequisites for this course */
    IF NEW.Type = 'Student' Then
		IF NOT check_student(NEW.Section_Key, NEW.UNI) THEN
			SIGNAL SQLSTATE '45001'
			SET MESSAGE_TEXT = 'Prerequistes not fulfilled';
		END IF;
	END IF;
            
END; $$
DELIMITER ;