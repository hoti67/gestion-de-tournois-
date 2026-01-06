package View.I;

import Model.I.*;
import Model.I.User;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import java.util.HashMap;
import java.util.Map;

@Route(value = "tournois", layout = Layout.class)
public class TournoiView extends VerticalLayout {

    public TournoiView() {

        User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
        boolean isAdmin = currentUser != null && currentUser.getRole() == 0;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setPadding(true);

        H1 titre = new H1("🏆 Liste des tournois");
        titre.getStyle().set("color", "var(--lumo-primary-text-color)");

        Grid<Tournoi> grid = new Grid<>(Tournoi.class, false);
        grid.setWidth("70%");
        grid.addColumn(Tournoi::getId).setHeader("ID").setAutoWidth(true);
        grid.addColumn(Tournoi::getNom).setHeader("Nom").setFlexGrow(1);
        grid.setItems(Tournoi.listTournois());

        grid.addItemClickListener(e ->
            getUI().ifPresent(ui ->
                ui.navigate("tournoi/" + e.getItem().getId())
            )
        );

        add(titre);

        if (isAdmin) {
            Button creerTournoi = new Button(
                    "Créer un tournoi",
                    VaadinIcon.PLUS_CIRCLE.create()
            );
            creerTournoi.addThemeVariants(
                    ButtonVariant.LUMO_PRIMARY,
                    ButtonVariant.LUMO_SUCCESS
            );

            creerTournoi.addClickListener(e ->
                openCreateTournoiDialog(grid)
            );

            add(creerTournoi);
        }

        add(grid);
    }
    
    private void openCreateTournoiDialog(Grid<Tournoi> grid) {

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Créer un tournoi");

        dialog.setWidth("600px");

        // ---------- Nom tournoi ----------
        TextField nomField = new TextField("Nom du tournoi");
        nomField.setWidthFull();

        // ---------- Sports ----------
        VerticalLayout sportsLayout = new VerticalLayout();
        sportsLayout.setPadding(false);
        sportsLayout.setSpacing(false);

        H3 sportsTitle = new H3("Sports et nombre de terrains");

        // On garde une map Sport -> NumberField
        Map<Sport, NumberField> terrainsParSport = new HashMap<>();

        for (Sport sport : Sport.listSports()) {

            Checkbox checkSport = new Checkbox(sport.getNom());

            NumberField nbTerrains = new NumberField("Terrains");
            nbTerrains.setValue(1.0);
            nbTerrains.setMin(1);
            nbTerrains.setStep(1);
            nbTerrains.setEnabled(false);
            nbTerrains.setWidth("120px");

            checkSport.addValueChangeListener(e ->
                nbTerrains.setEnabled(e.getValue())
            );
            
            terrainsParSport.put(sport, nbTerrains);
            Span info = new Span(
                sport.getNbJoueurs() + " joueurs / équipe"
            );
            info.getStyle().set("font-size", "var(--lumo-font-size-s)");

            HorizontalLayout ligne = new HorizontalLayout(checkSport, nbTerrains, info);
            ligne.setAlignItems(Alignment.END);

            sportsLayout.add(ligne);
        }

        // ---------- Boutons ----------
        Button creer = new Button("Créer", VaadinIcon.CHECK.create());
        creer.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        Button annuler = new Button("Annuler", VaadinIcon.CLOSE.create());
        annuler.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        creer.addClickListener(e -> {

            if (nomField.isEmpty()) {
                Notification.show("Nom du tournoi obligatoire");
                return;
            }

            // Vérifier qu'au moins un sport est sélectionné
            boolean auMoinsUnSport = terrainsParSport.entrySet().stream()
                .anyMatch(entry -> entry.getValue().isEnabled());

            if (!auMoinsUnSport) {
                Notification.show("Sélectionnez au moins un sport");
                return;
            }

            // ---------- Création tournoi ----------
            Tournoi tournoi = new Tournoi(nomField.getValue());
            tournoi.saveInDB();

            // récupérer l'id (méthode simple mais ok pour ton projet)
            Tournoi t = Tournoi.listTournois()
                    .get(Tournoi.listTournois().size() - 1);

            // ---------- Création terrains ----------
            for (Map.Entry<Sport, NumberField> entry : terrainsParSport.entrySet()) {

                Sport sport = entry.getKey();
                NumberField field = entry.getValue();

                if (field.isEnabled()) {
                    int nbTerrains = field.getValue().intValue();

                    for (int i = 0; i < nbTerrains; i++) {
                        Terrain terrain = new Terrain(
                            sport.getId(),
                            t.getId(),
                            true // dispo
                        );
                        terrain.saveInDB();
                    }
                }
            }

            grid.setItems(Tournoi.listTournois());
            dialog.close();
        });

        annuler.addClickListener(e -> dialog.close());

        HorizontalLayout actions = new HorizontalLayout(annuler, creer);
        actions.setJustifyContentMode(JustifyContentMode.END);

        dialog.add(
            nomField,
            sportsTitle,
            sportsLayout,
            actions
        );

        dialog.open();
    }


}
