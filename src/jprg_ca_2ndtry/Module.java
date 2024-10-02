//Name: Moe Myat Thwe
//Admin no: P2340362
//Class : DIT/FT/2B/23

package jprg_ca_2ndtry;

public class Module {
    private String moduleCode;
    private String moduleName;
    private int creditUnit;
    private double studentMarks;
    
    
    public Module(String moduleCode, String moduleName, 
            int creditUnit, double StudentMarks){
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.creditUnit = creditUnit;
        this.studentMarks = StudentMarks;
    }
    
       
    //---------------------------------------------------------------
    // getter and setter
    //---------------------------------------------------------------

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getCreditUnit() {
        return creditUnit;
    }

    public void setCreditUnit(int creditUnit) {
        this.creditUnit = creditUnit;
    }

    public double getStudentMarks() {
        return studentMarks;
    }

    public void setStudentMarks(double studentMarks) {
        this.studentMarks = studentMarks;
    }
    
    //---------------------------------------------------------------
    // getter and setter end
    //---------------------------------------------------------------
    
    public String getModuleInfo() {
        return moduleCode + " - " + moduleName + " - " + creditUnit + " credits - " + studentMarks + " marks";
    }
    
    //---------------------------------------------------------------------------
    // getGrade() method to return the grades A to F according to the studentMarks
    //---------------------------------------------------------------------------
    public String getGrade() {
        if (studentMarks <= 100 && studentMarks >= 80) return "A";
         if (studentMarks < 80 && studentMarks >= 70) return "B";
        if (studentMarks < 70 && studentMarks >= 60) return "C";
        if (studentMarks < 60 && studentMarks >= 50) return "D";
        if (studentMarks >= 0 && studentMarks < 50) return "F";
        return null;
    }
    
    // convert to gradePoints according to the studentMarks
    public double getGradePoints(double studentMarks){
        if(studentMarks >= 80 && studentMarks <=100){
            return 4;
        }
        else if(studentMarks >= 70 && studentMarks < 80){
            return 3;
        }
        else if(studentMarks >= 60 && studentMarks < 70){
            return 2;
        }
        else if(studentMarks >= 50 && studentMarks < 60){
            return 1;
        }
        else if(studentMarks >= 0 && studentMarks < 50){
            return 0;
        }     
        else{
            return -1;
        }
    }
}

