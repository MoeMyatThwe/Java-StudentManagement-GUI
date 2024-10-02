/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jprg_ca_2ndtry;

/**
 *
 * @author Moe Myat Thwe
 */
import java.awt.event.ActionEvent;

public class StudentManagementController {
    private StudentManagementView smView;
    
    public StudentManagementController(StudentManagementView smView){
        this.smView = smView;
    }
    
//    public void start(){
//        
//    }
//    
//    public void handleAdminButtonClicked(ActionEvent e){
//        new AdminView().setVisible(true);
//    }
//    
    public void handleUserIDButtonAction(ActionEvent e){
        UserView userView = new UserView(smView);
        userView.setVisible(true);
        smView.setVisible(false);
    }
    
    public void handleAdminIDButtonAction(ActionEvent e){
        AdminView admView = new AdminView();
        admView.setVisible(true);
        smView.setVisible(false);
    }
}
