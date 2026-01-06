package Model.I;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class Ronde {

    private static final Connection con = GestionBDD.getConnection();

    private int id;
    private int idTournoi;

    public Ronde(int id, int idTournoi) {
        this.id = id;
        this.idTournoi = idTournoi;
    }

    /* ===================== GETTERS ===================== */

    public int getId() {
        return id;
    }

    public int getIdTournoi() {
        return idTournoi;
    }

    /* ===================== BDD ===================== */

    public static Ronde getById(int id) {
        try (PreparedStatement st = con.prepareStatement(
                "SELECT * FROM rondes WHERE id = ?")) {

            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return new Ronde(
                        rs.getInt("id"),
                        rs.getInt("id_tournoi")
                );
            }

        } catch (SQLException e) {
            System.out.println("Erreur getById Ronde : " + e.getMessage());
        }
        return null;
    }

    public static ArrayList<Ronde> getByTournoiId(int idTournoi) {

        ArrayList<Ronde> rondes = new ArrayList<>();

        try (PreparedStatement st = con.prepareStatement("""
            SELECT * FROM rondes
            WHERE id_tournoi = ?
            ORDER BY id ASC
        """)) {

            st.setInt(1, idTournoi);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                rondes.add(new Ronde(
                        rs.getInt("id"),
                        rs.getInt("id_tournoi")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Erreur getByTournoiId Ronde : " + e.getMessage());
        }
        return rondes;
    }

    /* ===================== LOGIQUE METIER ===================== */

    public static boolean rondeEnCours(int idTournoi) {

        try (PreparedStatement st = con.prepareStatement("""
            SELECT COUNT(*) AS nb
            FROM matchs m
            JOIN rondes r ON m.id_ronde = r.id
            WHERE r.id_tournoi = ? AND m.statut != 'FINI'
        """)) {

            st.setInt(1, idTournoi);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt("nb") > 0;
            }

        } catch (SQLException e) {
            System.out.println("Erreur rondeEnCours : " + e.getMessage());
        }

        return false;
    }

    /**
     * Crée une nouvelle ronde :
     * - seulement si toutes les précédentes sont terminées
     * - crée les matchs automatiquement
     */
    public static void creerNouvelleRonde(int idTournoi) {

        if (rondeEnCours(idTournoi)) {
            System.out.println("Impossible : une ronde est encore en cours");
            return;
        }

        try {
            con.setAutoCommit(false);

            // 1️⃣ créer la ronde
            int idRonde;
            try (PreparedStatement st = con.prepareStatement(
                    "INSERT INTO rondes (id_tournoi) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS)) {

                st.setInt(1, idTournoi);
                st.executeUpdate();

                ResultSet rs = st.getGeneratedKeys();
                rs.next();
                idRonde = rs.getInt(1);
            }

            // 2️⃣ pour chaque sport du tournoi
            for (Sport sport : Sport.getByTournoiId(idTournoi)) {

                // terrains disponibles
                ArrayList<Terrain> terrains = Terrain.getDisponibles(
                        idTournoi, sport.getId()
                );

                if (terrains.isEmpty()) continue;

                // joueurs dispo
                int nbJoueursEquipe = sport.getNbJoueurs();
                ArrayList<Joueur> joueurs = Joueur.getDisponibles();

                if (joueurs.size() < nbJoueursEquipe * 2) continue;

                // créer équipes
                Equipe e1 = new Equipe("Equipe A", idTournoi);
                Equipe e2 = new Equipe("Equipe B", idTournoi);
                e1.saveInDB();
                e2.saveInDB();

                Terrain terrain = terrains.get(0);
                terrain.setDisponible(false);

                // créer match
                Match match = new Match(
                        e1.getId(),
                        e2.getId(),
                        0,
                        0,
                        "EN_COURS",
                        sport.getId(),
                        terrain.getId(),
                        idRonde
                );
                match.saveInDB();

                // Mélanger les joueurs de façon aléatoire
                Collections.shuffle(joueurs);

                // Assigner joueurs
                for (int i = 0; i < nbJoueursEquipe; i++) {
                    match.assignJoueur(joueurs.get(i), 1);
                    match.assignJoueur(joueurs.get(i + nbJoueursEquipe), 2);
                }   
            }

            con.commit();

        } catch (SQLException e) {
            try { con.rollback(); } 
            catch (SQLException ex) {
                System.out.println("Erreur creer Nouvelle ronde : "+ ex.getMessage());
            }
            System.out.println("Erreur creerNouvelleRonde : " + e.getMessage());
        }
    }
    
    public static boolean toutesTerminees(int idTournoi) {

        String sql = """
            SELECT COUNT(*) AS restants
            FROM matchs m
            JOIN rondes r ON m.id_ronde = r.id
            WHERE r.id_tournoi = ?
              AND m.termine = false
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTournoi);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("restants") == 0;
            }
        } catch (SQLException e) {
            System.out.println("Erreur toutesTerminees : " + e);
        }
        return false;
    }
}
