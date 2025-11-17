/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.I;

import Model.I.GestionBDD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class User {
    
    private static final Connection con = GestionBDD.getConnection();
    private String nom;
    private String prenom;
    private String email;
    private String mdp;
    private int role;    // 0 pour admin et 1 pour joueurs
    private String resetCode;
    private LocalDateTime resetExpiration;

    public User(String nom, String prenom, String email, String mdp, int role) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.role = role;
    }
    
    public User(String nom, String prenom, String email, String mdp, int role, String code, LocalDateTime expiration) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.role = role;
        this.resetCode = code;
        this.resetExpiration = expiration;
    }
    
    
    // ---------------------------------------------------------------------------- Save in DataBase. Id is AUTOINCREMENT
    public void saveInDB() {
        String sql = "INSERT INTO utilisateurs(nom, prenom, mail, mdp, role) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(sql)){
            pst.setString(1, nom);
            pst.setString(2, prenom);
            pst.setString(3, email);
            pst.setString(4, mdp);
            pst.setInt(5, role);
            pst.executeUpdate();
            System.out.println("Utilisateur intégré à la base de données !");
        }
        catch (SQLException e){
            System.out.println("Probleme lors de la sauvegarde du login : "+e);
            e.printStackTrace();
        }
    }
    
// ---------------------------------------------------------------------------- Recherche idUser
    public static int idUser (String email) throws SQLException {
        String sql = "SELECT id FROM utilisateurs WHERE mail = ?";
        try (PreparedStatement st = con.prepareStatement(sql)){
            st.setString(1, email);
            
            try (ResultSet res = st.executeQuery()){
                if (res.next()){
                    return res.getInt("id");
                } else return -1;
            }
        }
    }
    
// ---------------------------------------------------------------------------- Recherche Nom
    public static String NomUser (int id) throws SQLException {
        String sql = "SELECT nom FROM utilisateurs WHERE id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)){
            st.setInt(1, id);
            
            try (ResultSet res = st.executeQuery()){
                if (res.next()){
                    return res.getString("nom");
                } else return "";
            }
        }
    }
    
// ---------------------------------------------------------------------------- Recherche Prenom
    public static String PrenomUser (int id) throws SQLException {
        String sql = "SELECT prenom FROM utilisateurs WHERE id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)){
            st.setInt(1, id);
            
            try (ResultSet res = st.executeQuery()){
                if (res.next()){
                    return res.getString("prenom");
                } else return "";
            }
        }
    }
    
// ---------------------------------------------------------------------------- Recherche MailUser
    public static String EmailUser (int id) throws SQLException {
        String sql = "SELECT mail FROM utilisateurs WHERE id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)){
            st.setInt(1, id);
            
            try (ResultSet res = st.executeQuery()){
                if (res.next()){
                    return res.getString("mail");
                } else return "";
            }
        }
    }
    
// ---------------------------------------------------------------------------- Recherche roleUser
    public static int RoleUser (int id) throws SQLException {
        String sql = "SELECT role FROM utilisateurs WHERE id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)){
            st.setInt(1, id);
            
            try (ResultSet res = st.executeQuery()){
                if (res.next()){
                    return res.getInt("role");
                } else return -1;
            }
        }
    }
    
// ---------------------------------------------------------------------------- Delete in DataBase
    public void deleteInDB() throws SQLException {
        String sql = "DELETE FROM utilisateurs WHERE nom = ? AND prenom = ? AND mail = ? AND mdp = ? AND role = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, nom);
            pst.setString(2, prenom);
            pst.setString(3, email);
            pst.setString(4, mdp);
            pst.setDouble(5, role);
            pst.executeUpdate();
        }
    }
    
 // ---------------------------------------------------------------------------- Modify in DataBase.
    public static void modifyInDB(String nom, String prenom, String mail, double role, int id) throws SQLException {
        String sql = "UPDATE utilisateurs SET nom = ?, prenom = ?, mail = ?, irole = ? WHERE id = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, nom);
            pst.setString(2, prenom);
            pst.setString(3, mail);
            pst.setDouble(4, role);
            pst.setDouble(5, id);
            pst.executeUpdate();
        }
    }
    
// --------------------------------------------------------------------------------------- Login in DB 
    public static User login(String mail, String mdp) throws SQLException {
        String sql = "SELECT * FROM utilisateurs WHERE mail = ? AND mdp = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)){
            pst.setString(1, mail);
            pst.setString(2, mdp);
            
            try (ResultSet res = pst.executeQuery()){
                if (res.next()){
                    String nom = res.getString("nom");
                    String prenom = res.getString("prenom");
                    int role = res.getInt("role");
                    User user = new User(nom, prenom, mail, mdp, role);
                    return user;
                } else return null;
            }
        }
    }

    
// ------------------------------------------------------------------------------------------- Trouve l'utilisateur par son Email
    public static User searchByMail(String mail) throws SQLException{
        String sql = "SELECT * FROM utilisateurs WHERE mail = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)){
            pst.setString(1, mail);
            
            try(ResultSet res = pst.executeQuery()){
                if (res.next()){
                    String nom = res.getString("nom");
                    String prenom = res.getString("prenom");
                    String mdp = res.getString("mdp");
                    int role = res.getInt("role");
                    User user = new User(nom, prenom, mail, mdp, role);
                    return user;
                } else return null;
            }
        }
    }
    // ------------------------------------------------------------------------------------------- GETTER
    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }
    
    public String getEmail() {
        return email;
    }

    public String getMdp() {
        return mdp;
    }

    public int getRole() {
        return role;
    }

    public String getResetCode() {
        return resetCode;
    }

    public LocalDateTime getResetExpiration() {
        return resetExpiration;
    }
    
}
