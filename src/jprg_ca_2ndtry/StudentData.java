/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//Name: Moe Myat Thwe
//Admin no: P2340362
//Class : DIT/FT/2B/23

package jprg_ca_2ndtry;

import java.io.*;
import java.util.*;
/**
 *
 * @author Moe Myat Thwe
 */
public class StudentData {
    
    private final String studentFile = "students.txt";
    
    // =====================================================================
    // Writer
    // =====================================================================
    
    public ArrayList<Student> loadStudents() throws IOException{
        
        ArrayList<Student> students = new ArrayList<>();
        
        File file = new File(studentFile);
        
        if(!file.exists()){
            return students;
        }
 
    ////----------------------------------modified code #1------------------------------------
    
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 5) {
                    // Skip the line if it doesn't have the minimum expected number of parts
                    continue;
                }
                String className = parts[0];
                String adminNumber = parts[1];
                String name = parts[2];
                int numOfModules;
                try {
                    numOfModules = Integer.parseInt(parts[3]);
                } catch (NumberFormatException e) {
                    // Skip the line if numOfModules is not a valid integer
                    continue;
                }
                ArrayList<Module> modules = new ArrayList<>();
                for (int i = 4; i < parts.length; i += 4) {
                    if (i + 3 >= parts.length) {
                        // Skip the module if there are not enough parts for a full module entry
                        continue;
                    }
                    String moduleCode = parts[i];
                    String moduleName = parts[i + 1];
                    int creditUnit;
                    double studentMarks;
                    try {
                        creditUnit = Integer.parseInt(parts[i + 2]);
                        studentMarks = Double.parseDouble(parts[i + 3]);
                    } catch (NumberFormatException e) {
                        // Skip this module if creditUnit or studentMarks are not valid numbers
                        continue;
                    }
                    modules.add(new Module(moduleCode, moduleName, creditUnit, studentMarks));
                }
                double gpa = Student.calculateGPA(modules);
                students.add(new Student(name, adminNumber, className, modules, gpa));
            }
        }
        return students;
    }
    ////----------------------------------------------------------------------------------
    public void saveStudents(ArrayList<Student> students) throws IOException{
        try(PrintWriter pw = new PrintWriter(new FileWriter(studentFile))){
            pw.println(students.size());
            for(Student student : students){
                pw.println(studentToFileString(student));
            }
        }
    }
    
    private String studentToFileString(Student student){
        StringBuilder sb = new StringBuilder();
        sb.append(student.getClassName()).append(";")
                .append(student.getAdminNumber()).append(";")
                .append(student.getName()).append(";")
                .append(student.getNumOfModules()).append(";");
                for(Module module: student.getModules()){
                    sb.append(module.getModuleCode()).append(";")
                    .append(module.getModuleName()).append(";")
                    .append(module.getCreditUnit()).append(";")
                    .append(module.getStudentMarks()).append(";");   
                }
        return sb.toString();
    }
    

    
    public static void main(String [] args){
        try{
            StudentData loader = new StudentData();
            ArrayList<Student> students = loader.loadStudents();
            for(Student student : students){
                System.out.println(student);
            }
            
//                ArrayList<Module> modules = new ArrayList<>();
//                modules.add(new Module("ST0501", "Java", 4, 85));
//                modules.add(new Module("ST0502", "FOP", 6, 75));
//                Student newStudent = new Student("Rubens Tan", "p1234567", "DIT/FT/2B/23",
//                         modules, Student.calculateGPA(modules));
//                students.add(newStudent);                
//                loader.saveStudents(students);
           
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
