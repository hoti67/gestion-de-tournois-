package Model.I;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Joueur {

    private static final Connection con = GestionBDD.getConnection();
    private int id;
    private String nom;
    private String prenom;
    private int taille;
    private Date naissance;
    private int sexe;
    private int score;
    private Integer idEquipe;
    private Integer idUtilisateur; // nullable
    private boolean disponible;

    /* ===================== CONSTRUCTEURS ===================== */

    public Joueur(int id, String nom, String prenom, int taille, Date naissance, int sexe, int score, int idEquipe, Integer idUtilisateur, boolean disponible) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.taille = taille;
        this.naissance = naissance;
        this.sexe = sexe;
        this.score = score;
        this.idEquipe = idEquipe;
        this.idUtilisateur = idUtilisateur;
        this.disponible = disponible;
    }

    public Joueur(String nom, String prenom, int taille, Date naissance, int sexe, Integer idUtilisateur) {
        this.nom = nom;
        this.prenom = prenom;
        this.taille = taille;
        this.naissance = naissance;
        this.sexe = sexe;
        this.idUtilisateur = idUtilisateur;
        score = 0;
        idEquipe = null;
        disponible = true;
    }
    
    
    
    public void saveinDB() {
        String sql = """
            INSERT INTO joueurs (nom, prenom, score, id_utilisateur, disponible)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nom);
            ps.setString(2, prenom);
            ps.setInt(3, score);
            ps.setBoolean(5, disponible);

            if (idUtilisateur == null) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, idUtilisateur);
            }

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                this.id = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Joueur> listJoueurs() {
        ArrayList<Joueur> list = new ArrayList<>();
        String sql = "SELECT * FROM joueurs";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(fromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur list Joueurs : " + e.getMessage());
        }
        return list;
    }
    
    /* ===================== SCORES ===================== */

    public void addScore(int delta) {
        this.score += delta;

        String sql = "UPDATE joueurs SET score = ? WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, score);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* ===================== RECUPERATION ===================== */

    public static Joueur getById(int id) {
        String sql = "SELECT * FROM joueurs WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                return fromResultSet(rs);
            }

        } catch (SQLException e) {
            System.out.println("Erreur getDisponibles : " + e);
        }
        return null;
    }

    public static ArrayList<Joueur> getByTournoiId(int tournoiId) {
        ArrayList<Joueur> joueurs = new ArrayList<>();

        String sql = "SELECT * FROM joueurs WHERE id_tournoi = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, tournoiId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                joueurs.add(fromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return joueurs;
    }
    
    public static ArrayList<Joueur> getDisponibles() {
        ArrayList<Joueur> joueurs = new ArrayList<>();

        String sql = "SELECT * FROM joueurs WHERE disponible = true";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                joueurs.add(fromResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erreur getDisponibles : " + e);
        }

        return joueurs;
    }

    /* ===================== MATCH / EQUIPES ===================== */

    public static ArrayList<Joueur> getByMatchId(int matchId, int equipe) {
        ArrayList<Joueur> joueurs = new ArrayList<>();

        String sql = """
            SELECT j.*
            FROM joueurs j
            JOIN match_joueurs mj ON j.id = mj.id_joueur
            WHERE mj.id_match = ? AND mj.equipe = ?
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, matchId);
            ps.setInt(2, equipe);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                joueurs.add(fromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return joueurs;
    }
    
    public static int getScoreTournoi(int idTournoi, int idJoueur) {
        String sql = "SELECT score FROM tournoi_joueurs tj WHERE id_tournoi = ? AND id_joueur = ? ORDER BY tj.score DESC";
        try (PreparedStatement ps = GestionBDD.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idTournoi);
            ps.setInt(2, idJoueur);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("score");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    
    /* ===================== UTILS ===================== */

    private static Joueur fromResultSet(ResultSet rs) throws SQLException {
        return new Joueur(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getInt("taille"),
                rs.getDate("naissance"),
                rs.getInt("sexe"),
                rs.getInt("score"),
                rs.getInt("id_equipe"),
                rs.getInt("id_utilisateur"),
                rs.getBoolean("disponible")
        );
    }

    public int getTaille() {
        return taille;
    }

    public Date getNaissance() {
        return naissance;
    }

    public int getSexe() {
        return sexe;
    }

    public Integer getIdEquipe() {
        return idEquipe;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public int getScore() {
        return score;
    }

    public Integer getIdUtilisateur() {
        return idUtilisateur;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setScore(int score) {
        this.score = score;
        String sql = "UPDATE joueurs SET score = ? WHERE id = ?";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, score);
            st.setInt(2, this.id);

            st.executeUpdate();
        } catch (SQLException err) {
            System.out.println("Erreur dans la mise à jour du score du joueur : " + err);
        }
    }
    
    
}
