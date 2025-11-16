/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is part of CoursBeuvron.

CoursBeuvron is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

CoursBeuvron is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with CoursBeuvron.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insa.toto.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author arbre
 */
/*class Joueur {
   private String nom;
    private String pass;
    private String email;
    private int score;
    private String surnom;
    private String taille;
    private int idjoueur;
    private String tailleCM;
    private String categorie;
    private int id;

    // constructeur par défaut existant


    // constructeur avec paramètres
    public Joueur(String surnom, String categorie, String tailleCM, int id) {
    this.surnom = surnom;
    this.categorie = categorie;
    this.tailleCM = tailleCM;
    this.id = id;
}
public void saveInDB(Connection con) throws SQLException {
    String sql = "INSERT INTO joueur (surnom, categorie, tailleCM) VALUES (?, ?, ?)";

    try (PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setString(1, this.surnom);
        pst.setString(2, this.categorie);
        pst.setString(3, this.tailleCM);
        pst.executeUpdate();
    }
}
    
   public void saveInDB(Connection con) throws SQLException {
    String sql = "INSERT INTO joueur (surnom, categorie, tailleCM) VALUES (?,?,?)";
    try (PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setString(1, this.surnom);
        pst.setString(2, this.categorie);
        pst.setString(3, this.tailleCM);
        pst.executeUpdate();
    }
}
}
    
    
public Joueur(String surnom, String categorie, String tailleCM, int id) {
    this.surnom = surnom;
    this.categorie = categorie;
    this.tailleCM = tailleCM;
    this.id = id;
   }
  
 public Joueur(String surnom, String categorie, int tailleCM) {
        this(0, surnom, categorie, tailleCM);
    }

    // Getters & setters
    public int getId() { return id; }
    public String getSurnom() { return surnom; }
    public String getCategorie() { return categorie; }
    public int getTailleCM() { return tailleCM; }

    public void setId(int id) { this.id = id; }
    public void setSurnom(String surnom) { this.surnom = surnom; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public void setTailleCM(int tailleCM) { this.tailleCM = tailleCM; }

    @Override
    public String toString() {
        return String.format("[%d] %s (%s, %d cm)", id, surnom, categorie, tailleCM);
    }
} */
   


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class Joueur {

    // --- Attributs (selon UML) ---
    private int id;
    private String nom;
    private String prenom;
    private String sexe;
    private Date dateNaissance;
    private int scoreTotal;

    // --- Constructeur par défaut ---
    public Joueur() {}

    // --- Constructeur complet ---
    public Joueur(int id, String nom, String prenom, String sexe, Date dateNaissance, int scoreTotal) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
        this.scoreTotal = scoreTotal;
    }

    // --- Méthode UML ---
    public void majScore(int score) {
        this.scoreTotal += score;
    }

    // --- Méthode pour sauver en base ---
    public void saveInDB(Connection con) throws SQLException {
        String sql = "INSERT INTO joueur (id, nom, prenom, sexe, dateNaissance, scoreTotal) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, this.id);
            pst.setString(2, this.nom);
            pst.setString(3, this.prenom);
            pst.setString(4, this.sexe);

            // gestion du type Date → java.sql.Date
            pst.setDate(5, new java.sql.Date(this.dateNaissance.getTime()));

            pst.setInt(6, this.scoreTotal);

            pst.executeUpdate();
        }
    }

    // --- Getters & Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getSexe() { return sexe; }
    public void setSexe(String sexe) { this.sexe = sexe; }
    public Date getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(Date dateNaissance) { this.dateNaissance = dateNaissance; }
    public int getScoreTotal() { return scoreTotal; }
    public void setScoreTotal(int scoreTotal) { this.scoreTotal = scoreTotal; }
}

   