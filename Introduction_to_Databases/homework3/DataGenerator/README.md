# README

This data generator contains two java programs for generating test data of homework3.

1.PersonGenerator.java
---
This program will read in a file 'names.txt' which contains some common English names and generate random combinations of them. Two final and static values are set to indicate the number of students and faculties that is going to be created. 
The program will output sql statements that can be directly executed.

2.Course_Participate_Generator.java
---
This program will read in two files 'faculties.txt' and 'students.txt', and parse the files into separated names.
For each course, the program randomly assigns a faculty to it and for each student the program randomly assigns an initial course to the student. When the course_participant_BEFORE_INSERT trigger is activated, the data generated here might violate the prerequisite requirement for registering for a course and cause the program to terminate.
