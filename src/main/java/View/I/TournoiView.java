package View.I;

import Model.I.*;
import Model.I.User;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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

        H1 titre = new H1("Liste des tournois et des joueurs");
        titre.getStyle().set("color", "var(--lumo-primary-text-color)");

        Grid<Tournoi> grid = new Grid<>(Tournoi.class, false);
        //grid.setWidth("30%");
        grid.addColumn(Tournoi::getId).setHeader("ID").setAutoWidth(true);
        grid.addColumn(Tournoi::getNom).setHeader("Nom").setFlexGrow(1);
        grid.setItems(Tournoi.listTournois());

        grid.addItemClickListener(e ->
            getUI().ifPresent(ui ->
                ui.navigate("tournoi/" + e.getItem().getId())
            )
        );
        
        Grid<Joueur> grid2 = new Grid<>(Joueur.class, false);
        //grid2.setWidth("70%");
        grid2.addColumn(Joueur::getId).setHeader("ID").setAutoWidth(true);
        grid2.addColumn(Joueur::getNom).setHeader("Nom").setFlexGrow(1);
        grid2.addColumn(Joueur::getPrenom).setHeader("Prenom").setFlexGrow(1);
        grid2.addColumn(Joueur::getTaille).setHeader("Taille").setFlexGrow(1);
        grid2.addColumn(Joueur::getNaissance).setHeader("Naissance").setFlexGrow(1);
        grid2.setItems(Joueur.listJoueurs());
        
        HorizontalLayout grids = new HorizontalLayout(grid, grid2);
        grids.setWidthFull();              // 
        grids.setAlignItems(Alignment.STRETCH);
        grids.setFlexGrow(1, grid);        // 1/3
        grids.setFlexGrow(2, grid2);       // 2/3 (joueurs plus grand)

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
            
            Button btnCreate = new Button("Créer joueur + utilisateur");
            btnCreate.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            btnCreate.addClickListener(e -> openCreateDialog());
            HorizontalLayout HL = new HorizontalLayout(creerTournoi, btnCreate);
            add(HL);
            }

        add(grids);
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
            if(!Tournoi.allTournoisFinis()){
                Notification.show("Le tournoi précédent n'est pas terminé");
                return;
            }
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
            Tournoi tournoi = new Tournoi(nomField.getValue(), "EN COURS");
            tournoi.saveInDB();
            tournoi.saveJoueurs();

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

    private void openCreateDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Création d’un joueur et utilisateur");

        // Champs
        TextField nom = new TextField("Nom");
        TextField prenom = new TextField("Prénom");

        IntegerField taille = new IntegerField("Taille (cm)");
        taille.setMin(50);
        taille.setMax(250);
        taille.setStepButtonsVisible(true);

        TextField sexe = new TextField("Sexe (1 : Homme / 2 : Femme)");

        DatePicker naissance = new DatePicker("Date de naissance");
        naissance.setMax(LocalDate.now());

        EmailField mail = new EmailField("Email");

        PasswordField mdp = new PasswordField("Mot de passe");
        PasswordField mdpConfirm = new PasswordField("Confirmation");

        // Layout
        FormLayout form = new FormLayout(
                nom, prenom,
                taille, sexe,
                naissance, mail,
                mdp, mdpConfirm
        );

        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 2)
        );

        form.setColspan(mail, 2);

        dialog.add(form);

        // Boutons
        Button cancel = new Button("Annuler", e -> dialog.close());
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button create = new Button("Créer");
        create.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        create.addClickListener(e -> {

            // 🔎 VALIDATIONS MANUELLES
            if (nom.isEmpty() || prenom.isEmpty()) {
                Notification.show("Nom et prénom obligatoires", 3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            if (taille.isEmpty() || taille.getValue() < 50 || taille.getValue() > 250) {
                Notification.show("Taille invalide", 3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            if (sexe.isEmpty()) {
                Notification.show("Sexe obligatoire", 3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            if (naissance.isEmpty()) {
                Notification.show("Date de naissance obligatoire", 3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            if (mail.isEmpty() || !mail.getValue().contains("@")) {
                Notification.show("Email invalide", 3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            if (mdp.isEmpty() || mdp.getValue().length() < 8) {
                Notification.show("Mdp min 8 caractères", 3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            if (!mdp.getValue().equals(mdpConfirm.getValue())) {
                Notification.show("Mdp ne correspondent pas", 3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            try {
                User u = new User(nom.getValue(), prenom.getValue(), mail.getValue(), mdp.getValue(), 1);  // 1 pour joueurs
                u.saveInDB();
                Joueur j = new Joueur(nom.getValue(), prenom.getValue(), taille.getValue(), Date.from(naissance.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), Integer.parseInt(sexe.getValue()), User.idUser(mail.getValue()));
                j.saveinDB();
                
                Notification.show("Création réussie", 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                dialog.close();

            } catch (SQLException ex) {
                Notification.show("Erreur SQL !", 3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        dialog.getFooter().add(cancel, create);

        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);
        dialog.open();
    }

}
