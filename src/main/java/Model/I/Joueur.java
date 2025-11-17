/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.I;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.time.LocalDate;


public class Joueur {
    
    private static final Connection con = GestionBDD.getConnection();
    
    private int id;
    private String nom;
    private String prenom;
    boolean sexe;
    private LocalDate naissance;
    private float score;
    private int id_equipe;

    public Joueur(int id, String nom, String prenom, boolean sexe, LocalDate naissance, float score, int id_equipe) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.naissance = naissance;
        this.score = score;
        this.id_equipe = id_equipe;
    }
    
    // ---------------------------------------------------------------------------- Save in DataBase. Id is AUTOINCREMENT  
    public void saveInDB() throws SQLException {
        String sql = "INSERT INTO joueurs (nom, prenom, sexe, date_naissance, score, id_equipe) VALUES (?, ?, ?, ?, ?, ?)";
        try(PreparedStatement pst = con.prepareStatement(sql)){
            pst.setString(1, nom);
            pst.setString(2, prenom);
            pst.setBoolean(3, sexe);
            pst.setDate(4, java.sql.Date.valueOf(naissance));
            pst.setFloat(5, score);
            pst.setInt(6, id_equipe);  // Chercher l'id de l'equipe dans la classe equipe
            pst.executeUpdate();
            System.out.println("Partenaire saved");
        }
    }
    
    // ----------------------------------------------------------------------------- Recherche idjoueur avec nom, prenom
    public static int idJoueur (String nom, String prenom){
        String sql = "SELECT id FROM joueurs WHERE nom = ? && prenom = ?";
        try (PreparedStatement st = con.prepareStatement(sql)){
            st.setString(1, nom);
            st.setString(2, prenom);
            
            try (ResultSet res = st.executeQuery()){
                if (res.next()){
                    return res.getInt("id");
                } else return -1;
            }
        }
        catch(SQLException e){
            System.out.println("Erreur lors de la recherche de l'id joueur: "+e);
            return -1;
        }
    }
    
    // ------------------------------------------------------------------------------- Listes partenaires
    public static ArrayList<Joueur> listPart(){
        ArrayList<Joueur> joueurs = new ArrayList<>();
        String sql = "SELECT * FROM joueurs";
        try (Statement st = con.createStatement()){
            
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()){
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                boolean sexe = rs.getBoolean("sexe");
                LocalDate naissance = rs.getDate(nom).toLocalDate();
                float score = rs.getFloat("score");
                int id_equipe = 0; // A corriger avec la classe equipe
                Joueur joueur = new Joueur(id, nom, prenom, sexe, naissance, score, id_equipe);
                joueurs.add(joueur);
            }
        }
        catch(SQLException err){
            System.out.println("Probleme lors de la recherche de la liste des Partenaires : "+err);
            err.printStackTrace();
        }
        return joueurs;
    }
    
    // --------------------------------------------------------------------------------- Supprimer ligne de la bdd
    public static void deleteRow(String nom, String prenom) throws SQLException { 
        String sql = """
                     DELETE FROM joueurs
                     WHERE id = ?;
                     """;
        try (PreparedStatement pst = con.prepareStatement(sql)){
            pst.setInt(1, idJoueur(nom, prenom));
            pst.executeUpdate();
            System.out.println("Partenaire supprime");
        }
        
    }
}
