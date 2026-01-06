package View.I;

import Model.I.Joueur;
import Model.I.Ronde;
import Model.I.Tournoi;
import Model.I.User;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "tournoi/:tournoiId", layout = Layout.class)
public class TournoiDetailView extends VerticalLayout
        implements BeforeEnterObserver {

    private Tournoi tournoi;
    private boolean isAdmin;
    private Button btn;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {

        User user = VaadinSession.getCurrent().getAttribute(User.class);
        isAdmin = user != null && user.getRole() == 0;

        String idStr = event.getRouteParameters()
                .get("tournoiId")
                .orElse(null);

        if (idStr == null) {
            add(new H1("Tournoi introuvable"));
            return;
        }

        tournoi = Tournoi.getByID(Integer.parseInt(idStr));

        if (tournoi == null) {
            add(new H1("Tournoi introuvable"));
            return;
        }

        buildUI();
    }

    private void buildUI() {
        removeAll();
        setAlignItems(Alignment.CENTER);

        H1 titre = new H1(tournoi.getNom() + " (" + tournoi.getStatut().toLowerCase() + ")");
        titre.getStyle().set("color", "var(--lumo-primary-text-color)");

        // ----- Bouton retour -----
        Button retour = new Button("Retour", VaadinIcon.ARROW_LEFT.create());
        retour.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        retour.addClickListener(e ->
            getUI().ifPresent(ui ->
                ui.navigate("/tournois")
            )
        );
        
        // ----- Bouton fin -----
        Button fin = new Button("Fin du tournoi", VaadinIcon.ARROW_LEFT.create());
        fin.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        fin.addClickListener(e -> {
            Notification.show("Fin de tournoi !", 3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
            tournoi.setStatut("FINI");
            titre.setText(tournoi.getNom() + " (" + tournoi.getStatut().toLowerCase() + ")");
            btn.setEnabled(false);
            for (Joueur j : Joueur.listJoueurs()){
                System.out.println(j.getNom()+" a son score à 0");
                j.setScore(0);
            }
            getUI().ifPresent(ui -> ui.navigate("/tournois"));
        });
        
        Div left = new Div(retour);
        Div center = new Div(titre);
        Div right = new Div(fin); // vide

        // styles pour le centrage
        left.getStyle().set("flex", "1");
        center.getStyle().set("flex", "1").set("text-align", "center");
        right.getStyle().set("flex", "1");

        HorizontalLayout header = new HorizontalLayout(left, center, right);
        header.setWidthFull();
        header.setAlignItems(Alignment.CENTER);
        
        
        Tabs tabs = new Tabs(
                new Tab(VaadinIcon.INFO_CIRCLE.create(), new Paragraph(" Infos")),
                new Tab(VaadinIcon.REFRESH.create(), new Paragraph(" Rondes")),
                new Tab(VaadinIcon.TROPHY.create(), new Paragraph(" Classement"))
        );
        tabs.setWidth("70%");

        Div content = new Div();
        content.setWidth("70%");
        content.add(buildInfos());

        tabs.addSelectedChangeListener(e -> {
            content.removeAll();
            switch (tabs.getSelectedIndex()) {
                case 0 -> content.add(buildInfos());
                case 1 -> content.add(buildRondes());
                case 2 -> content.add(buildClassement());
            }
        });

        add(header, tabs, content);
    }

    private Component buildInfos() {
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(Alignment.CENTER);

        layout.add(
                new Paragraph("ID : " + tournoi.getId()),
                new Paragraph("Nom : " + tournoi.getNom())
        );

        if (isAdmin) {
            btn = new Button(
                    "Lancer une ronde",
                    VaadinIcon.PLAY.create()
            );
            if (tournoi.getStatut().equals(("FINI"))) btn.setEnabled(false);
            else btn.setEnabled(true);
            btn.addThemeVariants(
                    com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY
            );

            btn.addClickListener(e -> {
                Ronde.creerNouvelleRonde(tournoi.getId());
                Notification.show("Nouvelle ronde créée");
            });

            layout.add(btn);
        }

        return layout;
    }

    private Component buildRondes() {
        Grid<Ronde> grid = new Grid<>(Ronde.class, false);
        grid.setWidth("100%");
        grid.addColumn(Ronde::getId).setHeader("Ronde");

        grid.setItems(Ronde.getByTournoiId(tournoi.getId()));

        grid.addItemClickListener(e ->
            getUI().ifPresent(ui ->
                ui.navigate("ronde/" + e.getItem().getId())
            )
        );

        return grid;
    }

    private Component buildClassement() {
        Grid<Joueur> grid = new Grid<>(Joueur.class, false);
        grid.setWidth("100%");
        grid.addColumn(Joueur::getNom).setHeader("Nom");
        grid.addColumn(Joueur::getPrenom).setHeader("Prénom");
        grid.addColumn(j -> Joueur.getScoreTournoi(tournoi.getId(), j.getId()))
            .setHeader("Score")
            .setAutoWidth(true);

        grid.setItems(tournoi.getClassement());
        System.out.println("Classement affiché !");
        return grid;
    }
}
