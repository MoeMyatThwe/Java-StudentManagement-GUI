/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jprg_ca_2ndtry;

//Name: Moe Myat Thwe
//Admin no: P2340362
//Class : DIT/FT/2B/23

/**
 *
 * @author Moe Myat Thwe
 */
public class Main {
    public static void main(String [] args){
        StudentManagementView smView = new StudentManagementView();
        
        StudentManagementController smController = new StudentManagementController(smView);
                
        smView.setController(smController);
        
        smView.setVisible(true);    

    }
}
