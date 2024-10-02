//Name: Moe Myat Thwe
//Admin no: P2340362
//Class : DIT/FT/2B/23

package jprg_ca_2ndtry;

import javax.swing.*;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import javax.swing.DefaultListModel;
import java.util.HashMap;
import java.util.Map;
import java.util.AbstractMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.Dimension;
import java.awt.BorderLayout;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import java.awt.Color;
import java.util.stream.Collectors;


public class StudentController {
    private UserView view;
    private ArrayList<Student> students;
    private int currentStudentIndex;
    private int currentModuleIndex;

    public StudentController(UserView view) {
        this.view = view;
        loadStudents();  
        this.currentStudentIndex = 0;
        this.currentModuleIndex = 0;
        initView();
        initController();
    }
    

    private void initView() {
        view.updateTotalStudents(students.size());
        view.populateClassComboBox(students);
        displayStudentData();
        view.getJTextAreaShowingModulePerformanceRank().setText(calculateModulePerformanceRank());
    }

    private void initController() {
        view.getStudentInfoNEXTButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                currentStudentIndex = (currentStudentIndex + 1) % students.size();
                currentModuleIndex = 0;
                displayStudentData();
            }
        });

        view.getStudentInfoPREVButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                currentStudentIndex = (currentStudentIndex - 1 + students.size()) % students.size();
                currentModuleIndex = 0;
                displayStudentData();
            }
        });
        
        view.getSearchStudentByOrderNumberButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                int studentIndex;
                try {
                    studentIndex = Integer.parseInt(view.getStudentOrderField().getText()) - 1;
                    if (studentIndex < 0 || studentIndex >= students.size()) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid student number between 1 and " + students.size() + ".");
                    } else {
                        currentStudentIndex = studentIndex;
                        currentModuleIndex = 0;
                        displayStudentData();
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number.");
                }
            }
        });
        
        view.getSearchModuleByOrderNumberButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                int moduleIndex;
                Student student = students.get(currentStudentIndex);
                try {
                    moduleIndex = Integer.parseInt(view.getStudentModuleOrderField().getText()) - 1;
                    if (moduleIndex < 0 || moduleIndex >= student.getModules().size()) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid module number between 1 and " + student.getModules().size() + ".");
                    } else {
                        currentModuleIndex = moduleIndex;
                        displayModuleData(student);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number.");
                }
            }
        });

        view.getStudentModuleNEXTButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Student student = students.get(currentStudentIndex);
                currentModuleIndex = (currentModuleIndex + 1) % student.getModules().size();
                displayModuleData(student);
            }
        });

        view.getStudentModulePREVButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Student student = students.get(currentStudentIndex);
                currentModuleIndex = (currentModuleIndex - 1 + student.getModules().size()) % student.getModules().size();
                displayModuleData(student);
            }
        });

        view.getButtonForExit().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JOptionPane.showMessageDialog(null, "Thank you!");
                System.exit(0);
            }
        });

        view.getButtonForSearch().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                displaySearchResults();
            }
        });
        
        view.getSearchButtonForOutstandingStudent().addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            searchTopStudentsInClass();
           }
        });
        
        view.getToggleButtonSearch().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                filterStudentsByModules();
            }
        });
        
        view.getDisplayChartButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayModulePerformanceChart();
            }
        });
    


    }
    
    private void loadStudents() {
        StudentData studentData = new StudentData();
        try {
            students = studentData.loadStudents(); 
        } catch (IOException e) {
            e.printStackTrace();
            students = new ArrayList<>();
        }
    }
    
    private void displayStudentData() {
    Student student = students.get(currentStudentIndex);
    view.getNameField().setText(student.getName());
    view.getAdminField().setText(student.getAdminNumber());
    view.getClassNoField().setText(student.getClassName());
    view.getGpaField().setText(String.format("%.2f", calculateGPA(student.getModules())));
    view.updateStudentPanelTitle(currentStudentIndex, students.size());
    view.getStudentOrderField().setText(String.valueOf(currentStudentIndex + 1)); 
    view.updateTotalModules(currentModuleIndex, student.getModules().size());

    displayModuleData(student);
}

    private void displayModuleData(Student student) {
        if (student.getModules().isEmpty()) {
            view.getModCodeField().setText("");
            view.getModNameField().setText("");
            view.getMarksField().setText("");
            view.getCreditField().setText("");
            view.getGradeField().setText("");
            view.updateModulePanelTitle(0, 0);
            return;
        }

    Module module = student.getModules().get(currentModuleIndex);
    view.getModCodeField().setText(module.getModuleCode());
    view.getModNameField().setText(module.getModuleName());
    view.getMarksField().setText(String.valueOf(module.getStudentMarks()));
    view.getCreditField().setText(String.valueOf(module.getCreditUnit()));
    view.getGradeField().setText(String.valueOf(module.getGradePoints(module.getStudentMarks())));
    view.updateModulePanelTitle(currentModuleIndex, student.getModules().size());
    view.getStudentModuleOrderField().setText(String.valueOf(currentModuleIndex + 1));
    }

    private double calculateGPA(ArrayList<Module> modules) {
        double totalGradePoints = 0.0;
        int totalCreditUnits = 0;
        
        for (Module module : modules) {
            totalGradePoints += module.getCreditUnit() * module.getGradePoints(module.getStudentMarks());
            totalCreditUnits += module.getCreditUnit();
        }
        return totalCreditUnits == 0 ? 0 : totalGradePoints / totalCreditUnits;
    }

    private void displaySearchResults() {
    String searchText = view.getTextFieldForSearch().getText().trim();
    boolean searchByName = view.getRadioButtonForSearchByName().isSelected();

   if (searchText.isEmpty()) {
            SoundPlayer.playSound("error.wav");  // Play the error sound
            JOptionPane.showMessageDialog(null, "Please enter a search term.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (searchByName) {
            if (!isValidName(searchText)) {
                  SoundPlayer.playSound("error.wav");  // Play the error sound
                JOptionPane.showMessageDialog(null, "Please enter a valid name (e.g., David Chan).", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            searchByName(searchText.toLowerCase());
        } else {
            if (!isValidClass(searchText)) {
                  SoundPlayer.playSound("error.wav");  // Play the error sound
                JOptionPane.showMessageDialog(null, "Please enter a valid class (e.g., DIT/FT/2A/02).", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            searchByClass(searchText);
        }
    }

private boolean isValidName(String name) {
    return name.split("\\s+").length == 2;
}

private boolean isValidClass(String classNo) {
    // Simple regex for class number validation. Adjust as needed.
    return classNo.matches("[A-Z]{3}/FT/\\d[A-Z]/\\d{2}");
}


    private void searchByName(String name) {
    ArrayList<Student> matchedStudents = new ArrayList<>();
    

    for (Student student : students) {
        String studentName = student.getName().trim().toLowerCase();
        System.out.println("Checking student: " + student.getAdminNumber() + " with name: " + studentName);  // Debug statement

        if (studentName.equals(name)) {
            matchedStudents.add(student);
        }
    }

    if (!matchedStudents.isEmpty()) {
        currentStudentIndex = students.indexOf(matchedStudents.get(0));
        displayStudentData();
        view.getPanelForSearchResult().setText(getMultipleStudentsDetails(matchedStudents));
    } else {
        view.getPanelForSearchResult().setText("No such student found.");
        clearStudentAndModuleData();
    }
}

private String getMultipleStudentsDetails(ArrayList<Student> students) {
    StringBuilder sb = new StringBuilder();
    sb.append("Found ").append(students.size()).append(" students:\n");
    sb.append("---------------------------------------\n");
    for (Student student : students) {
        sb.append("Name: ").append(student.getName()).append("\n");
        sb.append("Admin: ").append(student.getAdminNumber()).append("\n");
        sb.append("Class No: ").append(student.getClassName()).append("\n");
        sb.append("GPA: ").append(student.getGpa()).append("\n");
        sb.append("-------------------------------------------\n");
    }
    return sb.toString();
}



    private void searchByClass(String classNo) {
        ArrayList<Student> classStudents = new ArrayList<>();
        for (Student student : students) {
            if (student.getClassName().equalsIgnoreCase(classNo)) {
                classStudents.add(student);
            }
        }

        if (!classStudents.isEmpty()) {
            currentStudentIndex = students.indexOf(classStudents.get(0));
            displayStudentData();
            view.getPanelForSearchResult().setText("Class results:\n" + getClassSummary(classStudents));
        } else {
            view.getPanelForSearchResult().setText("Class cannot be found.");
            clearStudentAndModuleData();
        }
    }

     private String getClassSummary(ArrayList<Student> classStudents) {
        StringBuilder sb = new StringBuilder();
        sb.append("Class results:\n");
        sb.append("--------------------------------------------------\n");
        for (Student student : classStudents) {
            sb.append("Name: ").append(student.getName()).append("\n");
            sb.append("Admin: ").append(student.getAdminNumber()).append("\n");
            sb.append("Class No: ").append(student.getClassName()).append("\n");
            sb.append("GPA: ").append(student.getGpa()).append("\n");
            sb.append("--------------------------------------------------\n");
        }
        return sb.toString();
    }

    private void clearStudentAndModuleData() {
        view.getNameField().setText("");
        view.getAdminField().setText("");
        view.getClassNoField().setText("");
        view.getGpaField().setText("");
        view.getModCodeField().setText("");
        view.getModNameField().setText("");
        view.getMarksField().setText("");
        view.getCreditField().setText("");
        view.getGradeField().setText("");
    }

    //---------------Search For Outstanding Students--------------------------

    private void searchTopStudentsInClass() {
        String selectedClass = (String) view.getJComboBoxForChoosingClass().getSelectedItem();
        if (selectedClass == null || selectedClass.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select a class.");
            return;
        }

        ArrayList<Student> topStudents = new ArrayList<>();
        for (Student student : students) {
            if (student.getClassName().equals(selectedClass) && calculateGPA(student.getModules()) > 3.5) {
                topStudents.add(student);
            }
        }

        updateTopStudentsTable(topStudents);
    }

    private void updateTopStudentsTable(ArrayList<Student> topStudents) {
        DefaultTableModel tableModel = (DefaultTableModel) view.getTableForOutstandingSturentsResult().getModel();
        tableModel.setRowCount(0);

        for (Student student : topStudents) {
            Object[] rowData = new Object[]{
                student.getName(),
                student.getAdminNumber(),
                calculateGPA(student.getModules())
            };
            tableModel.addRow(rowData);
        }
    }
    public void filterStudentsByModules() {
    List<String> selectedModules = new ArrayList<>();
    if (view.getCheckBox1Module().isSelected()) selectedModules.add("1 Module");
    if (view.getCheckBox2Modules().isSelected()) selectedModules.add("2 Modules");
    if (view.getCheckBox3Modules().isSelected()) selectedModules.add("3 Modules");
    if (view.getCheckBox4Modules().isSelected()) selectedModules.add("4 Modules");
    if (view.getCheckBox5Modules().isSelected()) selectedModules.add("5 Modules");
    if (view.getCheckBoxMoreThan5Modules().isSelected()) selectedModules.add("More than 5 Modules");

    // Set the progress bar to determinate mode
    view.getJProgressBarForFilteringModule().setIndeterminate(false);
    view.getJProgressBarForFilteringModule().setValue(0);
    view.getJProgressBarForFilteringModule().setMaximum(100);

    SwingWorker<List<String>, Void> worker = new SwingWorker<>() {
        @Override
        protected List<String> doInBackground() throws Exception {
            List<String> filteredStudents = new ArrayList<>();
            int totalSteps = 100; // Number of steps for the progress bar
            int sleepTime = 20; // 2 secs

            for (int i = 0; i < totalSteps; i++) {
                Thread.sleep(sleepTime);
                setProgress(i + 1);
            }

            for (Student student : students) {
                int moduleCount = student.getModules().size();
                if (selectedModules.contains(moduleCount + " Modules") ||
                        (moduleCount > 5 && selectedModules.contains("More than 5 Modules"))) {
                    StringBuilder studentInfo = new StringBuilder(student.getName() + " - " + student.getAdminNumber() + " - " + student.getClassName() + " - " + moduleCount + " Modules");
                    for (Module module : student.getModules()) {
                        studentInfo.append(" - ").append(module.getModuleName());
                    }
                    filteredStudents.add(studentInfo.toString());
                }
            }
            return filteredStudents;
        }

        @Override
        protected void done() {
            try {
                List<String> result = get();
                DefaultListModel<String> listModel = new DefaultListModel<>();
                for (String studentInfo : result) {
                    listModel.addElement(studentInfo);
                }
                view.getJListForResultFilteringNumOfModules().setModel(listModel);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                view.getJProgressBarForFilteringModule().setValue(0); // Reset the progress bar
            }
        }
    };

    worker.addPropertyChangeListener(evt -> {
        if ("progress".equals(evt.getPropertyName())) {
            int progress = (Integer) evt.getNewValue();
            view.getJProgressBarForFilteringModule().setValue(progress);
        }
    });

    worker.execute();
}
    
    public void displayModulePerformanceChart() {
    Map<String, Double> moduleMarks = new HashMap<>();
    Map<String, Integer> moduleCounts = new HashMap<>();

    for (Student student : students) {
        for (Module module : student.getModules()) {
            moduleMarks.put(module.getModuleName(),
                    moduleMarks.getOrDefault(module.getModuleName(), 0.0) + module.getStudentMarks());
            moduleCounts.put(module.getModuleName(),
                    moduleCounts.getOrDefault(module.getModuleName(), 0) + 1);
        }
    }

    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    for (String moduleName : moduleMarks.keySet()) {
        double averageMark = moduleMarks.get(moduleName) / moduleCounts.get(moduleName);
        double percentage = (averageMark / 100) * 100; // Assuming 100 is the maximum mark
        dataset.addValue(percentage, "Performance (%)", moduleName);
    }

    JFreeChart barChart = ChartFactory.createBarChart(
            "Module Performance",
            "Module",
            "Average Performance (%)",
            dataset,
            PlotOrientation.VERTICAL,
            false, true, false);

    // Customize the bar colors
    CategoryPlot plot = barChart.getCategoryPlot();
    BarRenderer renderer = (BarRenderer) plot.getRenderer();
    renderer.setSeriesPaint(0, new Color(248, 229, 246)); 

    ChartPanel chartPanel = new ChartPanel(barChart);
    chartPanel.setPreferredSize(new Dimension(800, 600));

    // Remove all existing components from the chart panel container
    view.getChartPanelContainer().removeAll();
    
    // Add the new chart panel
    view.getChartPanelContainer().setLayout(new BorderLayout());
    view.getChartPanelContainer().add(chartPanel, BorderLayout.CENTER);
    view.getChartPanelContainer().validate();
    view.getChartPanelContainer().repaint();
}
    private String calculateModulePerformanceRank() {
        Map<String, Double> moduleMarks = new HashMap<>();
        Map<String, Integer> moduleCounts = new HashMap<>();

        for (Student student : students) {
            for (Module module : student.getModules()) {
                moduleMarks.put(module.getModuleName(),
                        moduleMarks.getOrDefault(module.getModuleName(), 0.0) + module.getStudentMarks());
                moduleCounts.put(module.getModuleName(),
                        moduleCounts.getOrDefault(module.getModuleName(), 0) + 1);
            }
        }

        List<Map.Entry<String, Double>> modulePerformance = moduleMarks.entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue() / moduleCounts.get(entry.getKey())))
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .collect(Collectors.toList());

        StringBuilder rankInfo = new StringBuilder("Module Performance Rank \n\n\n\n");
        for (int i = 0; i < modulePerformance.size(); i++) {
            Map.Entry<String, Double> entry = modulePerformance.get(i);
            double percentage = (entry.getValue() / 100) * 100; // Assuming 100 is the maximum mark
            rankInfo.append((i + 1) + ". " + entry.getKey() + ": " + String.format("%.2f", percentage) + "%\n");
        }

        return rankInfo.toString();
    }
    
    

}


