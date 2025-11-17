/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.I;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import Controller.I.LoginController;


@Route(value = "")
@RouteAlias("login")
public class LoginView extends VerticalLayout {
    private VerticalLayout VL;
    private LoginForm loginForm;
    
    public LoginView() {
        initializeComponents();
        new LoginController(this);
    }

//------------------------------------------------------- Initialise les composants
    private void initializeComponents () {
        /*// Ajouter une image locale avec le nom "1"
        Image image = new Image("/2.png","r");
        image.setWidth("54%");  // L'image prendra 50% de la largeur du layout
        image.setHeight("100%"); // L'image prendra 100% de la hauteur du layout
        Div imageContainer = new Div();
        // Ajouter l'image au Div
         imageContainer.add(image);

        // Positionner le Div avec l'image à la moitié gauche de la page et en haut
        imageContainer.getStyle().set("position", "absolute")
                      .set("top", "0")
                      .set("left", "0")
                      .set("width", "100%")
                      .set("height", "100%")
                      .set("overflow", "hidden");

        // Ajouter le Div à la mise en page
            add(imageContainer);
        
        */
        //------------------------------------------------------- Elements du Login
        LoginI18n i18n = LoginI18n.createDefault();
        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("Connexion au site");
        i18nForm.setUsername("Email");
        i18nForm.setPassword("Mot de passe");
        loginForm = new LoginForm();
        loginForm.setI18n(i18n);
        loginForm.getElement().getStyle().set("margin-left", "auto");
        loginForm.getElement().getStyle().set("margin-right", "auto");
        
        
        //------------------------------------------------------- Elements du layout
        VL = new VerticalLayout(loginForm);
        VL.setSizeFull(); // Prend tout l'espace disponible
        //VL.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        VL.setAlignItems(Alignment.STRETCH); // Étire les composants horizontalement
        VL.getStyle()
         .set("width", "25%")  // Largeur à 50% du parent
         .set("height", "60%") // Hauteur à 75% du parentgetStyle()
         .set("margin-left", "auto")
         .set("margin-right", "10%");
        VL.setJustifyContentMode(JustifyContentMode.CENTER); // Centrage vertical
        VL.setClassName("loginLayout");
        
        
        // -------------------------------------------------------------------- Layout Principal
        this.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        this.setSizeFull(); // Le layout principal occupe toute la taille de la fenêtre
        this.setJustifyContentMode(JustifyContentMode.CENTER); // Centrage vertical
        this.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        // Ajouter le formulaire et le bouton au layout
        add(VL);
    }

    
// -------------------------------------------------------------- Getters
    public VerticalLayout getVL() {
        return VL;
    }

    public LoginForm getLoginForm() {
        return loginForm;
    } 
}
