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
    private int idEquipe;
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
    
    public void saveinDB() {
        String sql = """
            INSERT INTO joueurs (nom, prenom, score, id_utilisateur, disponibilite)
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
    
    /* ===================== CLASSEMENT ===================== */

    public static ArrayList<Joueur> getClassementByTournoiId(int tournoiId) {
        ArrayList<Joueur> joueurs = new ArrayList<>();

        String sql = """
            SELECT * FROM joueurs
            WHERE id_tournoi = ?
            ORDER BY score DESC
        """;

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
    
    
}
