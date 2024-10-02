//Name: Moe Myat Thwe
//Admin no: P2340362
//Class : DIT/FT/2B/23

package jprg_ca_2ndtry;

import java.util.ArrayList;

public class Student {
    private String name;
    private String adminNumber;
    private String className;
    private double gpa;
    private ArrayList<Module> modules;

    public Student(String name, String adminNumber, String className, ArrayList<Module> modules, double gpa) {
        this.name = name;
        this.adminNumber = adminNumber;
        this.className = className;
        this.modules = modules;
        this.gpa = calculateGPA(modules);
    }

    public String getName() { return name; }
    public String getAdminNumber() { return adminNumber; }
    public String getClassName() { return className; }
    public double getGpa() { return gpa; }
    public ArrayList<Module> getModules() { return modules; }

    public void setName(String name) { this.name = name; }
    public void setAdmin(String adminNumber) { this.adminNumber = adminNumber; }
    public void setClassNo(String className) { this.className = className; }
    public void setGpa(double gpa) { this.gpa = gpa; }
    public void setModules(ArrayList<Module> modules) { this.modules = modules; }

    public String getFormattedDetails() {
        return "Name: " + name + "\n" +
               "Admin: " + adminNumber + "\n" +
               "Class No: " + className + "\n" +
               "GPA: " + gpa;
    }
    
    public int getNumOfModules(){
        int numOfModules = 0;
        
        numOfModules = modules.size();
        return numOfModules;
    }
    
    public String getStudentInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Name: ").append(this.name).append("\n");
        info.append("Student ID: ").append(this.adminNumber).append("\n");
        info.append("Class ID: ").append(this.className).append("\n");
        info.append("GPA: ").append(this.gpa).append("\n");
        info.append("Modules:\n");
        for (Module module : modules) {
            info.append(" - ").append(module.getModuleInfo()).append("\n");
        }
        return info.toString();
    }

    public static double calculateGPA(ArrayList<Module> modules) {
        double totalGradePoints = 0.0;
        int totalCreditUnits = 0;

        for (Module module : modules) {
            totalGradePoints += module.getCreditUnit() * module.getGradePoints(module.getStudentMarks());
            totalCreditUnits += module.getCreditUnit();
        }
        return totalCreditUnits == 0 ? 0 : totalGradePoints / totalCreditUnits;
    }
    
    public String toString(){
        return getStudentInfo();
    }
}
