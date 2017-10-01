package trail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

/**
 * Generate test data for homework2
 * 
 * @author Shenzhi Zhang
 * @date Sep. 30th, 2017
 */
public class TestdataGenerator {
	public static final int STUDENT_NUMBER = 500;
	public static final int INSTRUCTOR_NUMBER = 20;
	
	public static void main(String[] args) {
		
		Random random = new Random();
		
		// Formalize GPA
		DecimalFormat df = new DecimalFormat("#.00");
		
		// Obtain department set
		String[] Departments = new String[]{"CS", "EE", "CE"};
		
		// Obtain gender set
		String[] genders = new String[]{"Male", "Female"};
		
		// Obtain title set
		String[] titles = new String[]{"Dean", "Director", "Professor", "Assistant Professor"};
		
		// Obtain names set from a txt file
		String[] names = getNames();
		
		// Obtain enrollment state set
		String[] states = new String[]{"waitlisted", "enrolled", "completed"};
		
		// Obtain grade set
		String[] grades = new String[]{"A+", "A", "B", "C", "D", "F"};
		
		// Generate instructors
		List<String> instructorIDs = new ArrayList<>();
		instructorIDs = generateInstructor(random, names, Departments, genders, titles);
		
		// Generate students
		List<String> studentIDs = new ArrayList<>();
		studentIDs = generateStudent(random, names, Departments, df, genders);
		
		// Generate courses (courses are MANUALLY added)
		HashMap<String, Integer> course_Section = new HashMap<>();
		course_Section = generateCourse(random, instructorIDs);
		
		// Generate enrollment table 
		generateEnrollment(random, studentIDs, course_Section, states, grades);
	}
	
	
	
	
	/** Create a name array from txt files with English names */
	public static String[] getNames(){
		BufferedReader br = null;
		String[] nameSet = null;
		try {
			File file = new File("names.txt");
			br = new BufferedReader(new FileReader(file));		// standard procedure to read a file
			String line = "";
			while((line = br.readLine()) != null){
				nameSet = line.split("��");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (br != null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return nameSet;
	}
	
	
	
	/** Generate instructor data */
	public static List<String> generateInstructor(Random random, String[] names, String[] Departments,
			String[] genders, String[] titles){
		List<String> instructorIDs = new ArrayList<>();
		
		for (int i = 0; i < INSTRUCTOR_NUMBER; i++){		// here we create 20 instructor data
			
			// GenerateInstructorName
			int nameIndex1 = random.nextInt(names.length);
			int nameIndex2 = random.nextInt(names.length);
			String firstName = names[nameIndex1];
			String lastName = names[nameIndex2];
			
			// GenerateInstructorID
			int idNum = random.nextInt(5000);
			String instructorID = firstName.charAt(0) + "" + lastName.charAt(0) + idNum;
			instructorIDs.add(instructorID);
			
			// GenerateDepartment
			int departmentIndex = random.nextInt(Departments.length);
			String department = Departments[departmentIndex];
			
			// GenerateGender
			int genderIndex = random.nextInt(2);
			String gender = genders[genderIndex];
			
			//GenerateTitle
			int titleIndex = random.nextInt(titles.length);
			String title = titles[titleIndex];
			
			// Output MySQL queries
			System.out.println("INSERT INTO Faculty (FacultyID, FirstName, LastName,"
					+ "Department, Gender, Title) VALUES("+ "'" + instructorID + "', " + "'" + firstName + 
					"', " + "'" + lastName +
					"', " + "'" + department + "', " +  "'" + gender + "', " + "'" + title + "');");
			System.out.println();
		}
		System.out.println("Instructor data is generated!!!!");
		System.out.println();
		return instructorIDs;
	}
	
	
	
	/** Generate student data */
	public static List<String> generateStudent(Random random, String[] names, String[] Departments,
			DecimalFormat df, String[] genders){
		List<String> studentIDs = new ArrayList<>();
		
		for (int i = 0; i < STUDENT_NUMBER; i++){		// here we create 500 student data
			
			// GenerateStudentName
			int nameIndex1 = random.nextInt(names.length);
			int nameIndex2 = random.nextInt(names.length);
			String firstName = names[nameIndex1];
			String lastName = names[nameIndex2];
			
			// GenerateStudentID
			int idNum = random.nextInt(5000);
			String studentID = firstName.charAt(0) + "" + lastName.charAt(0) + idNum;
			studentIDs.add(studentID);
			
			// GenerateDepartment
			int departmentIndex = random.nextInt(Departments.length);
			String department = Departments[departmentIndex];
			
			// GenerateGPA
			double gpa = random.nextDouble() + 3;
			
			// GenerateGender
			int genderIndex = random.nextInt(2);
			String gender = genders[genderIndex];
			
			// Output MySQL queries
			System.out.println("INSERT INTO Student (StudentID, FirstName, LastName,"
					+ "Department, GPA, Gender) VALUES("+ "'" + studentID + "', " + "'" + firstName + 
					"', " + "'" + lastName +
					"', " + "'" + department + "', " + df.format(gpa) + ", " +  "'" + gender + "');");
			System.out.println();
		}
		System.out.println("Student data is generated!!!!");
		System.out.println();
		return studentIDs;
	}
	
	
	
	/** Generate course data based on instructor data*/
	public static HashMap<String, Integer> generateCourse(Random random, List<String> instructorIDs){
		// here we only manually add 5 courses to database
		HashMap<String, Integer> course_Section = new HashMap<>();
		course_Section.put("COMSW4111", 3);
		course_Section.put("COMSW4701", 1);
		course_Section.put("CSEEW4119", 2);
		course_Section.put("CSORW4231", 1);
		course_Section.put("ENGIE4000", 1);
		String[] time = new String[]{"08:40", "18:00", "09:00", "11:10", "17:20", "15:40", "16:30", "15:00"};
		int timeIndex = 0;
		String[] classroom = new String[]{"Mudd833", "Mat207", "NW501", "IAB417", "Uri120", "Mudd501", "CPS302", "SDW101"};
		int classroomIndex = 0;
		int[] capacity = new int[]{150, 100, 200, 150, 100, 80, 60, 120};
		int capacityIndex = 0;
		
		// Output MySQL queries
		Iterator<Entry<String, Integer>> iterator = course_Section.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, Integer> entry = iterator.next();
			int numSections = entry.getValue();
			for (int p = 1; p <= numSections; p++){
				int instructorIndex = random.nextInt(instructorIDs.size());
				System.out.println("INSERT INTO Course (CourseID, Section, InstructorID,"
						+ "`Time`, Classroom, Capacity) VALUES("+ "'" + entry.getKey() + "', " + "'" + p + 
						"', " + "'" + instructorIDs.get(instructorIndex) +
						"', " + "'" + time[timeIndex++] + "', " + "'" + classroom[classroomIndex++] + "', " + capacity[capacityIndex++] + ");");
				System.out.println();
			}
		}
		System.out.println("Course data is generated!!!!");
		System.out.println();
		return course_Section;
	}
	
	
	
	/** Generate enrollment data based on course data and student data*/
	public static void generateEnrollment(Random random, List<String> studentIDs, 
			Map<String, Integer> course_Section, String[] states, String[] grades){
		
		Iterator<Entry<String, Integer>> mapIte = course_Section.entrySet().iterator();
		List<String> courses = new ArrayList<>();
		while(mapIte.hasNext()){
			courses.add(mapIte.next().getKey());
		}
		
		for (int i = 0; i < studentIDs.size(); i++){
			int numCourses = random.nextInt(courses.size());
			
			if (numCourses == 0)
				continue;
			
			Set<Integer> set = new HashSet<>();
			for (int j = 0; j < numCourses; j++){
				if (j == 0)
					set = new HashSet<>();
				// Assign a course to this student
				int courseIndex = random.nextInt(courses.size());
				if (set.contains(courseIndex))
					continue;
				set.add(courseIndex);
				
				// Choose a section for the assigned course
				int sec = random.nextInt(course_Section.get(courses.get(courseIndex))) + 1;
				
				// 10% waitlisted 70% enrolled 20% completed
				double possibility = random.nextDouble();
				if (possibility < 0.1){
					System.out.println("INSERT INTO Enrollment (StudentID, CourseID, Section,"
							+ "State) VALUES("+ "'" + studentIDs.get(i) + "', " + "'" + courses.get(courseIndex) + 
							"', " + sec + ", " + "'" + states[0] + "');");
					System.out.println();
				}else if (possibility > 0.8){
					System.out.println("INSERT INTO Enrollment (StudentID, CourseID, Section,"
							+ "State, Grade) VALUES("+ "'" + studentIDs.get(i) + "', " + "'" + courses.get(courseIndex) + 
							"', " + sec + ", " + "'" + states[2] + "', " + "'" + grades[random.nextInt(grades.length)] + "');");
					System.out.println();
					
				}else{
					System.out.println("INSERT INTO Enrollment (StudentID, CourseID, Section,"
							+ "State) VALUES("+ "'" + studentIDs.get(i) + "', " + "'" + courses.get(courseIndex) + 
							"', " + sec + ", " + "'" + states[1] + "');");
					System.out.println();
					
				}
			}
		}
		System.out.println();
	}
}
