package View.I;

import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;

import Model.I.User;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;

public class Layout extends AppLayout {

    public Layout() {
        createHeader();
    }

    private void createHeader() {
        // Logo simple (juste un H1 stylé ici)
        H1 logo = new H1("🏆 MatchiCouli 🏆");
        logo.getStyle()
            .set("margin", "0")
            .set("font-size", "24px")
            .set("color", "var(--lumo-primary-color)")
            .set("font-weight", "bold");

        // ----- droite : avatar avec menu -----
        User user = VaadinSession.getCurrent().getAttribute(User.class);
        String initials = user.getNom().substring(0,1) + user.getPrenom().substring(0,1);

        Avatar avatar = new Avatar(initials);
        avatar.setAbbreviation(initials);

        ContextMenu menu = new ContextMenu();
        menu.setTarget(avatar);   // associe le menu à l’avatar
        menu.addItem("Déconnexion", e -> {
            VaadinSession.getCurrent().close();
            getUI().ifPresent(ui -> ui.getPage().setLocation("/login"));
        });

        // ----- layout header -----
        HorizontalLayout header = new HorizontalLayout(logo, avatar);
        header.setWidthFull();
        header.expand(logo);
        header.setPadding(true);
        header.setSpacing(true);
        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        addToNavbar(header);
    }

}
