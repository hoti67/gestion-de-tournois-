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
import com.vaadin.flow.router.PageTitle;


@Route("")
@PageTitle("Login")
public class LoginView extends VerticalLayout {
    private VerticalLayout VL;
    private LoginForm loginForm;
    
    public LoginView() {
        initializeComponents();
        new LoginController(this);
    }

//------------------------------------------------------- Initialise les composants
    private void initializeComponents () {        
        
        /* -------------------- LoginForm -------------------- */
        LoginI18n i18n = LoginI18n.createDefault();
        LoginI18n.Form form = i18n.getForm();
        form.setTitle("Connexion au site");
        form.setUsername("Email");
        form.setPassword("Mot de passe");

        loginForm = new LoginForm();
        loginForm.setI18n(i18n);

        // Cadre autour du loginForm
        loginForm.getStyle()
                .set("border", "1px solid var(--lumo-contrast-20pct)")
                .set("border-radius", "8px")
                .set("padding", "1rem");


        /* -------------------- Titre -------------------- */
        H3 title = new H3("Matchicouli");
        title.getStyle()
                    .set("margin", "0");

        Div titleBox = new Div(title);
        titleBox.getStyle()
                .set("padding", "12px")
                .set("border", "1px solid var(--lumo-contrast-30pct)")
                .set("border-radius", "8px")
                .set("background", "var(--lumo-base-color)")
                .set("width", "30%")
                .set("text-align", "center");


        /* -------------------- Carte centrale -------------------- */
        add(titleBox, loginForm);
        this.setAlignItems(Alignment.CENTER);
        this.setSpacing(true);
        this.setPadding(true);

        // Cadre de la carte
        this.getStyle()
                .set("border", "1px solid var(--lumo-contrast-30pct)")
                .set("border-radius", "12px")
                .set("width", "350px");


        /* -------------------- Layout principal -------------------- */
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

    }
    
// -------------------------------------------------------------- Getters
    public VerticalLayout getVL() {
        return VL;
    }

    public LoginForm getLoginForm() {
        return loginForm;
    } 
}
