package View.I;

import Model.I.Joueur;
import Model.I.Match;
import Model.I.Ronde;
import com.vaadin.flow.component.Component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "ronde/:rondeId", layout = Layout.class)
public class RondeView extends VerticalLayout
        implements BeforeEnterObserver {

    private Ronde ronde;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {

        String idStr = event.getRouteParameters()
                .get("rondeId")
                .orElse(null);

        if (idStr == null) {
            add(new H1("Ronde introuvable"));
            return;
        }

        ronde = Ronde.getById(Integer.parseInt(idStr));

        if (ronde == null) {
            add(new H1("Ronde introuvable"));
            return;
        }

        buildUI();
    }

    private void buildUI() {
        removeAll();
        setAlignItems(Alignment.CENTER);
        setPadding(true);

        H1 titre = new H1("Ronde " + ronde.getId());
        titre.getStyle().set("color", "var(--lumo-primary-text-color)");

        VerticalLayout matchsLayout = new VerticalLayout();
        matchsLayout.setWidth("80%");

        for (Match match : Match.getByRondeId(ronde.getId())) {
            matchsLayout.add(buildMatchCard(match));
        }

        add(titre, matchsLayout);
    }


    // -------------------- POPUP JOUEURS --------------------

    private void openJoueursDialog(Match match) {

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Match " + match.getId());
        dialog.setWidth("50%");

        List<Joueur> equipe1 = Joueur.getByMatchId(match.getId(), 1);
        List<Joueur> equipe2 = Joueur.getByMatchId(match.getId(), 2);

        Grid<Joueur> grid1 = new Grid<>(Joueur.class, false);
        grid1.addColumn(Joueur::getNom).setHeader("Nom");
        grid1.addColumn(Joueur::getPrenom).setHeader("Prénom");
        grid1.addColumn(Joueur::getScore).setHeader("Score");
        grid1.setItems(equipe1);

        Grid<Joueur> grid2 = new Grid<>(Joueur.class, false);
        grid2.addColumn(Joueur::getNom).setHeader("Nom");
        grid2.addColumn(Joueur::getPrenom).setHeader("Prénom");
        grid2.addColumn(Joueur::getScore).setHeader("Score");
        grid2.setItems(equipe2);

        VerticalLayout layout = new VerticalLayout(
                new H1("Équipe 1"),
                grid1,
                new H1("Équipe 2"),
                grid2
        );

        Button fermer = new Button("Fermer", VaadinIcon.CLOSE.create());
        fermer.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        fermer.addClickListener(e -> dialog.close());

        dialog.add(layout, fermer);
        dialog.open();
    }
    
    private Component buildMatchCard(Match match) {

        // Labels de score qu’on pourra modifier dynamiquement
        Span scoreEquipe1Span = new Span(String.valueOf(match.getScoreEquipe1()));
        Span scoreEquipe2Span = new Span(String.valueOf(match.getScoreEquipe2()));

        Button minus1 = new Button("-", e -> {
            match.updateScore(1, -1);
            scoreEquipe1Span.setText(String.valueOf(match.getScoreEquipe1()));
            // si refresh() recharge tout, tu peux aussi l'appeler ici
            // refresh();
        });

        Button plus1 = new Button("+", e -> {
            match.updateScore(1, 1);
            scoreEquipe1Span.setText(String.valueOf(match.getScoreEquipe1()));
            // si refresh() recharge tout, tu peux aussi l'appeler ici
            // refresh();
        });

        Button minus2 = new Button("-", e -> {
            match.updateScore(2, -1);
            scoreEquipe2Span.setText(String.valueOf(match.getScoreEquipe2()));
            // refresh();
        });

        Button plus2 = new Button("+", e -> {
            match.updateScore(2, 1);
            scoreEquipe2Span.setText(String.valueOf(match.getScoreEquipe2()));
            // refresh();
        });

        Button terminer = new Button("Terminer");
        terminer.addClickListener(e -> {
            match.terminer();
            terminer.setEnabled(false);
            plus1.setEnabled(false);
            minus1.setEnabled(false);
            plus2.setEnabled(false);
            minus2.setEnabled(false);
            // refresh();
        });

        if ("FINI".equals(match.getStatut())) {
            terminer.setEnabled(false);
            plus1.setEnabled(false);
            minus1.setEnabled(false);
            plus2.setEnabled(false);
            minus2.setEnabled(false);
        }

        // Construire un titre centré
        H1 titre = new H1("Match n° " + match.getId());
        titre.getStyle().set("text-align", "center");
        titre.addClickListener(ev -> {
            openJoueursDialog(match);
        });

        // Affichage noms équipes, sport, terrain
        Span sportSpan = new Span("Sport : " + match.getNomSport());
        Span terrainSpan = new Span("Terrain : " + match.getId());

        // Layout pour les scores avec boutons
        HorizontalLayout scoresLayout = new HorizontalLayout(
            minus1, scoreEquipe1Span,
            new Span(" — "),
            scoreEquipe2Span, plus2
        );

        // Ajout des boutons +/-
        HorizontalLayout buttonsEquipe1 = new HorizontalLayout(minus1, plus1);
        HorizontalLayout buttonsEquipe2 = new HorizontalLayout(minus2, plus2);

        // Regroupement layout équipes + boutons + scores
        HorizontalLayout equipesLayout = new HorizontalLayout(
            new VerticalLayout(buttonsEquipe1),
            new VerticalLayout(buttonsEquipe2)
        );

        VerticalLayout card = new VerticalLayout(
            titre,
            sportSpan,
            terrainSpan,
            equipesLayout,
            scoresLayout,
            terminer
        );

        card.getStyle()
            .set("border", "1px solid var(--lumo-contrast-20pct)")
            .set("padding", "1rem")
            .set("border-radius", "10px")
            .set("max-width", "400px")
            .set("margin", "auto");

    return card;
}

    
    private void refresh() {
        removeAll();
        buildUI();
    }
    
}

