package homework_1a;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * Generate test data for student and faculty table in homework3
 * 
 * @author Shenzhi Zhang
 * @date Oct. 29th, 2017
 */
public class PersonGenerator {
	public static final int STUDENT_NUMBER = 500;
	public static final int INSTRUCTOR_NUMBER = 20;
	
	public static void main(String[] args) {
		
		Random random = new Random();
		
		// Formalize GPA
		DecimalFormat df = new DecimalFormat("#.00");
		
		// Pay Grade
		int[] pay = new int[]{1, 2, 3, 7};
		
		// Obtain names set from a txt file
		String[] names = getNames();
		
		// Generate instructors
		generateInstructor(random, names, pay);
		
		// Generate students
		generateStudent(random, names, df);
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
	public static void generateInstructor(Random random, String[] names, int[] pay){
		
		for (int i = 0; i < INSTRUCTOR_NUMBER; i++){		// here we create 20 instructor data
			
			// GenerateInstructorName
			int nameIndex1 = random.nextInt(names.length);
			int nameIndex2 = random.nextInt(names.length);
			String firstName = names[nameIndex1];
			String lastName = names[nameIndex2];
			
			//GeneratePayGrade
			int payIndex = random.nextInt(pay.length);
			int payGrade = pay[payIndex];
			
			// Output MySQL queries
			System.out.println("INSERT INTO Faculty (Last_Name, First_Name,"
					+ " Pay_grade) VALUES(" + "'" + lastName +
					"', " + "'" + firstName +
					"', " + "'" + payGrade + "');");
			System.out.println();
		}
		System.out.println("Instructor data is generated!!!!");
		System.out.println();
	}
	
	
	
	/** Generate student data */
	public static void generateStudent(Random random, String[] names, DecimalFormat df){
		
		for (int i = 0; i < STUDENT_NUMBER; i++){		// here we create 500 student data
			
			// GenerateStudentName
			int nameIndex1 = random.nextInt(names.length);
			int nameIndex2 = random.nextInt(names.length);
			String firstName = names[nameIndex1];
			String lastName = names[nameIndex2];
			
			// GenerateGPA
			double gpa = random.nextDouble() + 3;
			
			// GenerateYear
			int y = 2013 + random.nextInt(5);
			String year = y + ""; 
			
			// Output MySQL queries
			System.out.println("INSERT INTO Student (Last_Name, First_Name,"
					+ "Year, GPA) VALUES("+ "'" + lastName + 
					"', " + "'" + firstName +
					"', " + year + "," + df.format(gpa) + ");");
			System.out.println();
		}
		System.out.println("Student data is generated!!!!");
		System.out.println();
	}
}
