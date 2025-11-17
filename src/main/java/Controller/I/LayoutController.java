/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.I;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import Model.I.User;
import View.I.*;
import java.sql.SQLException;


public class LayoutController {
    private Layout LE;
    
    public LayoutController(Layout LE){
        
        this.LE = LE;
        
        whichLayout();
        logout();
    }
    
    // Gère sur quelle vue est renvoyé l'utilisateur après sa connexion
    private void whichLayout(){
        User currentUser = VaadinSession.getCurrent().getAttribute(User.class);     // Set l'attribut à la connexion dans le loginController
        if (currentUser != null) {
            int currentRole = currentUser.getRole();
            String currentMail = currentUser.getEmail();

            try {
                User user = User.searchByMail(currentMail);
                this.LE.setUser(user);
                this.LE.SN();
            } catch (SQLException err){
                System.out.println("Erreur lors de la recherche de l'user sous le mail : "+ err);
            }

            switch (currentRole){

                case 1:  // Joueur
                    this.LE.viewJoueur();
                    break;

                case 0:  // Admin
                    this.LE.viewAdmin();
                    break;
            }
        }
    }
    
    // Deconnexion de l'utilisateur
    public void logout() {        
        this.LE.getLogoutLayout().addClickListener(event -> {
            UI.getCurrent().navigate(LoginView.class);
        });    
    }
}
