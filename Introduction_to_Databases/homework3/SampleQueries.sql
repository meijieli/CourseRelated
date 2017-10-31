-- Provide some sample queries according to the requirements of homework 3 

/*
1.	For each of Student and Faculty,
		/* 1.	Implement a view that supports query of all attributes, including those in the corresponding Person tuple. */
		SELECT * FROM facultyview;
		SELECT * FROM studentview;

		/* 2.	Implement a procedure for create, update and delete of an entity by UNI. 
        The procedures should affect both the derived entity, e.g. student, and the base entity (Person). */
        INSERT INTO student VALUES('SZ2695', 'Shenzhi', 'Zhang', 2017, 3.99);
        SELECT * FROM person WHERE First_Name = 'Zhang' AND Last_Name = 'Shenzhi';
/*
2.	Populate the following tables with sample data, 
		/* 1.	Student (at least 10 tuples) */
        SELECT * FROM student;
        
		/* 2.	Faculty (at least 5 tuples) */
        SELECT * FROM faculty;
        
        /* 3.	Course (at least 5 tuples) */
        SELECT * FROM courses;
        
		/* 4.	Section, with at least 2 sections per course. */
        SELECT * FROM sections;
        
        /* 5.	Prereqs: Every course in the course table must have at lest one prereq. */
        SELECT * FROM course_prereqs;
        
        /* 6.	Course_participants */
        SELECT * FROM course_participant;

/*
3.	Enrolling a student for a section is an INSERT on the Course_Participant table. Assigning a faculty member to a section is also an INSERT on the Course_Participant table. Enforce the constrains below using triggers. (NOTE: You WILL have to add columns to some of the tables. Figuring out the required columns is part of the assignment). You do not need to implement triggers for any operations other than INSERT. 
		/* 1.	A student may ONLY enroll in a section if the student has completed the course prereqs.*/ 
        INSERT INTO course_participant VALUES('AB1', '20143COMS49563', 'Student');
        -- Response: Error Code: 1644. Prerequistes not fulfilled
        
		/* 2.	Sections have enrollment limits. A student enrollment should fail if the course is at then enrollment limit.*/
        UPDATE sections SET sections.LimitNum = 0 WHERE sections.Section_Key = '20172COMS96642';  -- Let the problem occur
        INSERT INTO course_participant VALUES('SZ1', '20172COMS96642', 'Student');
        -- Response: Error Code: 1644. The class has reached its capacity

        /* 3.	A faculty member may only teach 3 sections a semester. */
        UPDATE course_participant SET course_participant.UNI = 'MP4' WHERE course_participant.Section_Key = '20172COMS96643' AND Type = 'Faculty';
		DELETE FROM course_participant WHERE course_participant.Section_Key = '20172CENG86751' AND Type = 'Faculty';
        INSERT INTO course_participant VALUES('MP4', '20172CENG86751', 'Faculty');
        -- Response: Error Code: 1644. This faculty has already taught three classes

/*
4.	Create the following views: */
        /* 1.	A view that contains a tuple for each student of the form (UNI, completed course, year and semester completed). */
        SELECT * FROM course_taken WHERE StudentUNI = 'AS4';
        
        /* 2.	A view that contains a tuple for each faculty member of the form (uni, course_id, semester, year) representing each course a faculty member is teaching or has taught. */
		SELECT * FROM course_taught WHERE FacultyUNI = 'MP4';