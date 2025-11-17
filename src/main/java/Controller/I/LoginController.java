/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.I;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinSession;
import Model.I.User;
import View.I.LoginView;
import View.I.HomeView;
import java.sql.SQLException;


public class LoginController {
    private LoginView loginview;
    
    
    public LoginController(LoginView loginview){
        this.loginview = loginview;
        
        // Gere la connection de l'utilisateur
        this.loginview.getLoginForm().addLoginListener(event -> {
            String username = event.getUsername();
            String password = event.getPassword();

                try {
                    User currentUser = User.login(username, password);
                    if (currentUser != null){
                        VaadinSession.getCurrent().setAttribute(User.class, currentUser);
                        UI.getCurrent().navigate(HomeView.class);
                    } else {
                        this.loginview.getLoginForm().setEnabled(true); // Réactiver le bouton
                        Notification.show("L'adresse mail ou/et le mot de passe est incorrect");
                    }
                }
                catch (SQLException err){
                    System.out.println("login pas dans la bdd, "+err);
                }
            
        });
        
        /* à Utiliser si élaboration du mdp oublié
        this.loginview.getLoginForm().addForgotPasswordListener(evt->{
            UI.getCurrent().navigate(ForgotPasswordView.class);
        });*/
    }
}
