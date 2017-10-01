/* As a student, I want to see which courses a professor ‘DK4226’ has taught. */
SELECT * FROM Course WHERE InstructorID = 'DK4226';

/* As a professor ‘CL1027’, I want to see which courses students in my class have taken. */
SELECT DISTINCT CourseID FROM Enrollment WHERE studentID IN
(SELECT StudentID FROM Enrollment 
	WHERE Enrollment.CourseID = 
		(SELECT CourseID FROM Course WHERE InstructorID = 'CL1027')
	AND State = 'Completed');

/* As an administrator, I want to know what percentage of students take course ‘COMSW4111’. */
SELECT COUNT(*) / (SELECT COUNT(*) FROM (SELECT COUNT(*)  FROM student GROUP BY StudentID) as b)
FROM (SELECT COUNT(*) FROM Enrollment WHERE CourseID = 'COMSW4111' AND State = 'Enrolled' GROUP BY StudentID) as a;

/* As a professor, I want to know which student who has taken my class is most competent to be a TA (has the highest grade). */
SELECT StudentID, Grade FROM Enrollment
WHERE CourseID = 'COMSW4111' AND State = 'Completed'
ORDER BY Grade;

/* As a student, I want to see whether the number of students enrolled has reached the course capacity. */
SELECT -1 * COUNT(*) + (SELECT Capacity FROM Course
	WHERE CourseID = 'COMSW4111' AND Section = 1) AS RemainingPlace
FROM (SELECT COUNT(*) FROM Enrollment
WHERE CourseID = 'COMSW4111' AND Section = 1 AND State = 'Enrolled'
GROUP BY StudentID) AS A;

/* As an advisor, I want to see a student’s grades in all courses. */
SELECT StudentID, CourseID, Grade
FROM Enrollment 
WHERE StudentID = 'LJ323' AND State = 'Completed'
ORDER BY Grade;

/* As a professor, I might want to see the information of all the students I have ever taught. */
SELECT Student.StudentID, FirstName, LastName, Department FROM Student JOIN
	(SELECT StudentID FROM Enrollment JOIN 
		(SELECT * FROM Course WHERE Course.InstructorID = 'MC3117') AS A
	 ON Enrollment.CourseID = A.CourseID) AS B
ON Student.StudentID = B.StudentID;

/* As an administrator, I want to see the average waitlist length of a course’s all sections. */
SELECT FLOOR(COUNT(*) / 3) FROM 
	(SELECT COUNT(*) FROM Enrollment 
		WHERE CourseID = 'COMSW4111' AND 
		(Section = 1 OR Section = 2 OR Section = 3) AND State = 'waitlisted' 
		GROUP BY StudentID) AS A;
        
/* As an administrator, I want to see which students have enrolled into Scott Joan’s class. */
SELECT Student.StudentID, FirstName, LastName FROM Enrollment JOIN Student
ON Student.StudentID = Enrollment.StudentID WHERE CourseID = 
	(SELECT CourseID FROM Course JOIN Faculty
	ON Course.InstructorID = Faculty.FacultyID
	WHERE Faculty.FirstName = 'Scott' AND Faculty.LastName = 'Joan');
    
/* As an administrator, I want to how many students are enrolled in courses at different times. */
SELECT Course.`Time`, COUNT(*) AS NumberofTimes FROM Enrollment JOIN Course ON
Course.CourseID = Enrollment.CourseID AND Course.Section = Enrollment.Section
GROUP BY `Time`;
