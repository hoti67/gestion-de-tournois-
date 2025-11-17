/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.I;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import Controller.I.LayoutController;
import Model.I.User;


public class Layout extends AppLayout {
    
    private DrawerToggle toggle;
    private SideNav sideNav = new SideNav();
    private H3 txt;
    private Avatar avatarName;
    private Button avatarButton;
    private User user;
    private Div logoutText = new Div();
    private Div container = new Div();
    private ContextMenu contextMenu;
    private HorizontalLayout logoutLayout = new HorizontalLayout();
    private Icon logoutIcon = VaadinIcon.SIGN_OUT.create();
    
    public Layout() {
        new LayoutController(this);
    }
    
    // SideNav commune
    public void SN(){
        txt = new H3("MoveINSA");
        txt.addClassName("text-secondary");
        txt.getStyle()
            .set("font-family", "Arial, sans-serif")  // Police
            .set("text-align", "center");
        
        toggle = new DrawerToggle();     
        
        DeconnexionUser();
    }
    
    // --------------------------------------------------------------- Bouton de deconnexion du user utilise
    public void DeconnexionUser() {
        avatarName = new Avatar(user.getNom().toUpperCase()+ " " +user.getPrenom().toUpperCase()); // mettre les initiales de l'etudiant
        avatarButton = new Button(avatarName);
        
        avatarButton.getStyle()
            .set("border-radius", "50%")  
            .set("width", "37px")  
            .set("height", "37px")  
            .set("border", "none")  
            .set("cursor", "pointer")  
            .set("position", "relative")
            .set("left", "85%");

        avatarName.getStyle()
            .set("width", "37px")  
            .set("height", "37px") 
            .set("border-radius", "50%");
        
        container.add(avatarButton);
        
        contextMenu = new ContextMenu(avatarButton);
        contextMenu.setOpenOnClick(true);

        logoutText.setText("Déconnexion");
        logoutLayout.add(logoutIcon, logoutText);
        
        contextMenu.addItem(logoutLayout, event -> {
            Notification.show("Vous êtes déconnecté");
        });
    }
    
    // --------------------------------------------------------------- SideNav pour les etudiants
    public void viewJoueur(){  
        avatarName.setColorIndex(2);

        addToNavbar(toggle, txt, avatarButton);
        
        /*sideNav.addItem(new SideNavItem("Accueil", HomeView.class, VaadinIcon.HOME.create()));  // Nom, classe liée, icône
        sideNav.addItem(new SideNavItem("Mon profil", ProfilView.class, VaadinIcon.USER.create()));
        sideNav.addItem(new SideNavItem("Offres", OffreView.class, VaadinIcon.DIPLOMA.create()));
        sideNav.addItem(new SideNavItem("Partenaires", PartenaireView.class, VaadinIcon.GLOBE.create()));
        sideNav.addItem(new SideNavItem("Mes Candidatures", CandidatureView.class, VaadinIcon.ENVELOPE.create()));
*/
        addToDrawer(sideNav);
    }
    
    
    // --------------------------------------------------------------- SideNav pour les admins
    public void viewAdmin(){
        avatarName.setColorIndex(0);
        addToNavbar(toggle, txt, avatarButton);
        /*
        sideNav.addItem(new SideNavItem("Accueil", HomeView.class, VaadinIcon.HOME.create()));
        sideNav.addItem(new SideNavItem("Mon profil", ProfilView.class, VaadinIcon.USER.create()));
        sideNav.addItem(new SideNavItem("Offres", OffreView.class, VaadinIcon.DIPLOMA.create()));
        sideNav.addItem(new SideNavItem("Partenaires", PartenaireView.class, VaadinIcon.GLOBE.create()));
        sideNav.addItem(new SideNavItem("Utilisateurs", UserListView.class, VaadinIcon.GROUP.create()));
        */
        addToDrawer(sideNav);
    }
    
    
    public DrawerToggle getToggle() {
        return toggle;
    }

    public SideNav getSideNav() {
        return sideNav;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public HorizontalLayout getLogoutLayout() {
        return logoutLayout;
    }    
}
