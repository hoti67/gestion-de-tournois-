package Model.I;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Match {

    private static final Connection con = GestionBDD.getConnection();

    private int id;
    private int idEquipe1;
    private int idEquipe2;
    private int scoreEquipe1;
    private int scoreEquipe2;
    private String statut;
    private int idSport;
    private int idTerrain;
    private int idRonde;

    public Match(int idEquipe1, int idEquipe2, int score1, int score2,
                 String statut, int idSport, int idTerrain, int idRonde) {
        this.idEquipe1 = idEquipe1;
        this.idEquipe2 = idEquipe2;
        this.scoreEquipe1 = score1;
        this.scoreEquipe2 = score2;
        this.statut = statut;
        this.idSport = idSport;
        this.idTerrain = idTerrain;
        this.idRonde = idRonde;
    }

    /* ===================== BDD ===================== */

    public static ArrayList<Match> getByRondeId(int idRonde) {
        ArrayList<Match> list = new ArrayList<>();
        String sql = "SELECT * FROM matchs WHERE id_ronde = ?";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, idRonde);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Match m = new Match(
                        rs.getInt("id_equipe1"),
                        rs.getInt("id_equipe2"),
                        rs.getInt("score_equipe1"),
                        rs.getInt("score_equipe2"),
                        rs.getString("statut"),
                        rs.getInt("id_sport"),
                        rs.getInt("id_terrain"),
                        rs.getInt("id_ronde")
                );
                m.id = rs.getInt("id");
                list.add(m);
            }
        } catch (SQLException e) {
            System.out.println("Erreur getByRondeId : " + e.getMessage());
        }
        return list;
    }

    /* ===================== SCORES ===================== */

    public void updateScore(int equipe, int delta) {

        if (equipe == 1) {
            scoreEquipe1 = Math.max(scoreEquipe1 + delta, 0);
        } else {
            scoreEquipe2 = Math.max(scoreEquipe2 + delta, 0);
        }

        String sql = equipe == 1
            ? "UPDATE matchs SET score_equipe1 = ? WHERE id = ?"
            : "UPDATE matchs SET score_equipe2 = ? WHERE id = ?";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, equipe == 1 ? scoreEquipe1 : scoreEquipe2);
            st.setInt(2, id);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur updateScore : " + e.getMessage());
        }
    }

    public void terminer() {

        try {
            con.setAutoCommit(false);

            try (PreparedStatement st = con.prepareStatement(
                    "UPDATE matchs SET statut = 'FINI' WHERE id = ?")) {
                st.setInt(1, id);
                st.executeUpdate();
            }

            try (PreparedStatement st = con.prepareStatement(
                    "UPDATE terrains SET disponibilite = true WHERE id = ?")) {
                st.setInt(1, idTerrain);
                st.executeUpdate();
            }

            updateScoreJoueurs();

            statut = "FINI";
            con.commit();

        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) {}
            System.out.println("Erreur terminer match : " + e.getMessage());
        }
    }

    private void updateScoreJoueurs() throws SQLException {

        int gain1 = 0, gain2 = 0;

        if (scoreEquipe1 > scoreEquipe2) gain1 = 3;
        else if (scoreEquipe2 > scoreEquipe1) gain2 = 3;
        else gain1 = gain2 = 1;

        updateEquipeScore(1, gain1);
        updateEquipeScore(2, gain2);
    }

    private void updateEquipeScore(int equipe, int gain) throws SQLException {
        try (PreparedStatement st = con.prepareStatement("""
            UPDATE joueurs j
            JOIN match_joueurs mj ON j.id = mj.id_joueur
            SET j.score = j.score + ?
            WHERE mj.id_match = ? AND mj.equipe = ?
        """)) {
            st.setInt(1, gain);
            st.setInt(2, id);
            st.setInt(3, equipe);
            st.executeUpdate();
        }
    }

    /* ===================== UTILS ===================== */

    public void assignJoueur(Joueur joueur, int idEquipe) {
        try (PreparedStatement st = con.prepareStatement(
                "INSERT INTO match_joueurs (id_match, id_joueur, equipe) VALUES (?, ?, ?)")) {

            st.setInt(1, id);
            st.setInt(2, joueur.getId());
            st.setInt(3, idEquipe);
            st.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erreur assignJoueur : " + e.getMessage());
        }
    }
    
    public void saveInDB() {
        try (PreparedStatement st = con.prepareStatement("""
            INSERT INTO matchs (id_equipe1, id_equipe2, score_equipe1,
                                score_equipe2, statut, id_sport,
                                id_terrain, id_ronde)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """, Statement.RETURN_GENERATED_KEYS)) {

            st.setInt(1, idEquipe1);
            st.setInt(2, idEquipe2);
            st.setInt(3, scoreEquipe1);
            st.setInt(4, scoreEquipe2);
            st.setString(5, statut);
            st.setInt(6, idSport);
            st.setInt(7, idTerrain);
            st.setInt(8, idRonde);
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) id = rs.getInt(1);

        } catch (SQLException e) {
            System.out.println("Erreur save Match : " + e.getMessage());
        }
    }

    public String getNomSport() {
        try (PreparedStatement st = con.prepareStatement(
                "SELECT nom FROM sports WHERE id = ?")) {
            st.setInt(1, idSport);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return rs.getString("nom");
        } catch (SQLException e) {}
        return "Inconnu";
    }

    public String getNomTerrain() {
        try (PreparedStatement st = con.prepareStatement(
                "SELECT nom FROM terrains WHERE id = ?")) {
            st.setInt(1, idTerrain);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return rs.getString("nom");
        } catch (SQLException e) {}
        return "Terrain";
    }

    public boolean isTermine(){
        if (statut.equals("FINI")){
            return true;
        }
        else return false;
    }
    
    public int getId() {
        return id;
    }

    public int getIdEquipe1() {
        return idEquipe1;
    }

    public int getIdEquipe2() {
        return idEquipe2;
    }

    public int getScoreEquipe1() {
        return scoreEquipe1;
    }

    public int getScoreEquipe2() {
        return scoreEquipe2;
    }

    public String getStatut() {
        return statut;
    }

    public int getIdSport() {
        return idSport;
    }

    public int getIdTerrain() {
        return idTerrain;
    }

    public int getIdRonde() {
        return idRonde;
    }
    
    
}
