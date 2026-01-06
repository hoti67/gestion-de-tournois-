package Model.I;

import java.sql.*;
import java.util.ArrayList;

public class Tournoi {

    private static final Connection con = GestionBDD.getConnection();

    private int id;
    private String nom;
    private String statut;

    // ---------------- Constructeurs ----------------

    public Tournoi(int id, String nom, String statut) {
        this.id = id;
        this.nom = nom;
        this.statut = statut;
    }

    public Tournoi(String nom) {
        this.nom = nom;
    }

    public Tournoi(String nom, String statut) {
        this.nom = nom;
        this.statut = statut;
    }
    
    

    // ---------------- BDD ----------------

    public void saveInDB() {
        String sql = "INSERT INTO tournois (nom, statut) VALUES (?, ?)";

        try (PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, nom);
            st.setString(2, statut);

            st.executeUpdate();

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getInt(1);
                } else {
                    throw new SQLException("Aucune clé générée pour tournois.");
                }
            }
        }
        catch(SQLException err){
            System.out.println("Erreur dans la save du tournoi : "+err);
        }
    }


    public static Tournoi getByID(int id) {
        String sql = "SELECT * FROM tournois WHERE id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Tournoi(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("statut")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur get tournoi : " + e.getMessage());
        }
        return null;
    }

    public static ArrayList<Tournoi> listTournois() {
        ArrayList<Tournoi> list = new ArrayList<>();
        String sql = "SELECT * FROM tournois";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Tournoi(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("statut")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur list tournois : " + e.getMessage());
        }
        return list;
    }

    public ArrayList<Joueur> getClassement() {
        ArrayList<Joueur> joueurs = new ArrayList<>();
        
        String sql = """
             SELECT j.*, tj.score AS score_tournoi
            FROM joueurs j
            JOIN tournoi_joueurs tj ON j.id = tj.id_joueur
            WHERE tj.id_tournoi = ?
            ORDER BY tj.score DESC
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                joueurs.add(new Joueur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getInt("taille"),
                    rs.getDate("naissance"),
                    rs.getInt("sexe"),
                    rs.getInt("score_tournoi"),
                    rs.getInt("id_equipe"),
                    rs.getInt("id_utilisateur"),
                    rs.getBoolean("disponible")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return joueurs;
    }
    
    public void saveJoueurs(){
        String sql = """
            INSERT INTO tournoi_joueurs (id_tournoi, id_joueur, score)
            SELECT ?, j.id, 0
            FROM joueurs j
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }   

    public static boolean allTournoisFinis(){

        String sql = """
            SELECT *
            FROM tournois
            WHERE statut = 'EN COURS'
            """;

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return false; // true ou false
            }
            return true;
        } catch(SQLException err){
            System.out.println("Erreur dans la recherche des tournois finis : "+err);
            return false;
        }
    }

    
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;

        String sql = "UPDATE tournois SET statut = ? WHERE id = ?";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, statut);
            st.setInt(2, this.id);

            st.executeUpdate();
        } catch (SQLException err) {
            System.out.println("Erreur dans la mise à jour du statut du tournoi : " + err);
        }
    }

    
    
}

