-- -----------------------------------------------------
-- View `RegistrationSystem`.`StudentView`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `RegistrationSystem`.`StudentView`;
USE `RegistrationSystem`;
CREATE  OR REPLACE VIEW `StudentView` AS
SELECT
	person.UNI,
    person.Last_Name,
    person.First_Name,
    person.Type,
    Student.Year,
    Student.GPA
    FROM person JOIN student
    ON person.UNI = student.UNI;


-- -----------------------------------------------------
-- View `RegistrationSystem`.`FacultyView`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `RegistrationSystem`.`FacultyView`;
USE `RegistrationSystem`;
CREATE  OR REPLACE VIEW `FacultyView` AS
SELECT
	person.UNI,
    person.Last_Name,
    person.First_Name,
    person.Type,
	faculty.Pay_grade
    FROM person JOIN faculty
    ON person.UNI = faculty.UNI;


-- -----------------------------------------------------
-- View `RegistrationSystem`.`Course_Taught`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `RegistrationSystem`.`Course_Taught`;
USE `RegistrationSystem`;
CREATE  OR REPLACE VIEW `Course_Taught` AS

/* Get UNI, Name, course info from person, course, sections and course_participant tables */
SELECT B.UNI AS FacultyUNI, Person.First_Name AS FirstName, Person.Last_Name AS LastName, B.course_id AS CourseID, B.semester AS Semester, B.year AS Year, B.title AS courseTitle FROM
person JOIN 
	/* Get course title from course, sections, course_participant tables */
	((SELECT A.UNI AS UNI, A.course_id AS course_id, A.semester AS semester, A.year AS year, courses.Title AS title
	FROM courses JOIN 
		/* Get course_id and course info from sections and course_participant */
		((SELECT UNI, course_id, semester, year
		FROM sections JOIN course_participant
		ON sections.Section_key = course_participant.Section_key
        WHERE course_participant.Type = 'Faculty') AS A)
        ON courses.course_id = A.course_id) AS B)
	ON person.UNI = B.UNI;
    
    
-- -----------------------------------------------------
-- View `RegistrationSystem`.`Course_Taken`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `RegistrationSystem`.`Course_Taken`;
USE `RegistrationSystem`;
CREATE  OR REPLACE VIEW `Course_Taken` AS

/* Get UNI, Name, course info from person, course, sections and course_participant tables */
SELECT B.UNI AS StudentUNI, Person.First_Name AS FirstName, Person.Last_Name AS LastName, B.course_id AS CourseID, B.semester AS Semester, B.year AS Year, B.title AS courseTitle FROM
person JOIN 
	/* Get course title from course, sections, course_participant tables */
	((SELECT A.UNI AS UNI, A.course_id AS course_id, A.semester AS semester, A.year AS year, courses.Title AS title
	FROM courses JOIN 
		/* Get course_id and course info from sections and course_participant */
		((SELECT UNI, course_id, semester, year
		FROM sections JOIN course_participant
		ON sections.Section_key = course_participant.Section_key
        WHERE course_participant.Type = 'Student' AND (year < DATE_FORMAT(NOW(), '%Y')) OR (semester < get_semester())) AS A)
        ON courses.course_id = A.course_id) AS B)
	ON person.UNI = B.UNI;