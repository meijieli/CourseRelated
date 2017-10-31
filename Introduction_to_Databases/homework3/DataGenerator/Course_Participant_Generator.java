package homework_1a;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 * Generate test data for course_participant table in homework3
 * 
 * @author Shenzhi Zhang
 * @date Oct. 29th, 2017
 */
public class Course_Participant_Generator {
	public static void main(String[] args) {
		String[] sections = getSections();	
		String[] faculties = getFaculties();
		String[] students = getStudents();
		assignFaculties(sections, faculties);
		assignStudents(sections, students);
	}
	
	/** Create a name array from txt files with section keysec */
	public static String[] getSections(){
		BufferedReader br = null;
		String[] sectionSet = null;
		try {
			File file = new File("sections.txt");
			br = new BufferedReader(new FileReader(file));		// standard procedure to read a file
			String line = "";
			while((line = br.readLine()) != null){
				sectionSet = line.split(",");
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
		return sectionSet;
	}
	
	public static String[] getFaculties(){
		BufferedReader br = null;
		String[] facultySet = null;
		try {
			File file = new File("faculties.txt");
			br = new BufferedReader(new FileReader(file));		// standard procedure to read a file
			String line = "";
			while((line = br.readLine()) != null){
				facultySet = line.split(",");
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
		return facultySet;
	}	
	
	public static String[] getStudents(){
		BufferedReader br = null;
		String[] studentSet = null;
		try {
			File file = new File("students.txt");
			br = new BufferedReader(new FileReader(file));		// standard procedure to read a file
			String line = "";
			while((line = br.readLine()) != null){
				studentSet = line.split(",");
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
		return studentSet;
	}	
	
	public static void assignFaculties(String[] sections, String[] faculty){
		Random random = new Random();
		for (int i = 0; i < sections.length; i++){
			String curSection = sections[i];
			int facultyIndex = random.nextInt(faculty.length);
			
			// Output MySQL queries
			System.out.println("INSERT INTO course_participant VALUES("+ "'" + faculty[facultyIndex] + 
					"', " + "'" + curSection + "'," + "'Faculty'" +  ");");
			System.out.println();
		}
		System.out.println("==================");
		System.out.println();
	}
	
	public static void assignStudents(String[] sections, String[] students){
		Random random = new Random();
		for (int i = 0; i < students.length; i++){
			String curStudent = students[i];
			int sectionsIndex = random.nextInt(sections.length);
			
			// Output MySQL queries
			System.out.println("INSERT INTO course_participant VALUES("+ "'" + curStudent + 
					"', " + "'" + sections[sectionsIndex] + "'," + "'Student'" +  ");");
			System.out.println();
		}
		System.out.println("==================");
		System.out.println();
	}
}
