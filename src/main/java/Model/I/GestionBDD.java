/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.I;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class GestionBDD {    // Table User a corrigé

    private static final String URL = "jdbc:mysql://92.222.25.165:3306/m3_iammari01";
    private static final String USER = "m3_iammari01";
    private static final String PASSWORD = "87e871e9";

    // Instance Singleton de Connection
    private static Connection connection;

    // Méthode pour récupérer la connexion existante ou la créer si elle n'existe pas
    public static Connection getConnection(){
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connexion à la BDD effectue ! ");
            }
        }
        catch (SQLException err){
            System.out.println("Probleme lors de la connexion a la BDD");
        }
        return connection;
    }

    // Méthode pour fermer la connexion
    public static void closeConnection() throws SQLException {
        try {
            if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        }
        catch (SQLException err){
            System.out.println("Probleme lors de la fermeture de la BDD");
        }
        
    }
    
    public static void createDB() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            String tournoiTable = """
                                CREATE TABLE tournois (
                                    id int AUTO_INCREMENT PRIMARY KEY,
                                    nom VARCHAR(100)
                                )
                                """;
            stmt.executeUpdate(tournoiTable);
            
            String rondeTable = """
                                CREATE TABLE rondes (
                                    id int AUTO_INCREMENT PRIMARY KEY,
                                    id_tournoi INT,
                                    CONSTRAINT fk_tournoi
                                        FOREIGN KEY (id_tournoi)
                                        REFERENCES tournois(id)
                                )
                                """;
            stmt.executeUpdate(rondeTable);
            
            String equipeTable = """
                                CREATE TABLE equipes (
                                 id int AUTO_INCREMENT PRIMARY KEY
                                )
                                 """;
            stmt.executeUpdate(equipeTable);
            
            String matchTable = """
                                CREATE TABLE matchs (
                                    id int AUTO_INCREMENT PRIMARY KEY,
                                    id_equipe1 INT,  
                                    id_equipe2 INT,
                                    statut VARCHAR(10),
                                    id_ronde INT,
                                    CONSTRAINT fk_equipe1
                                        FOREIGN KEY (id_equipe1)
                                        REFERENCES equipes(id),
                                    CONSTRAINT fk_equipe2
                                        FOREIGN KEY (id_equipe2)
                                        REFERENCES equipes(id),
                                    CONSTRAINT fk_ronde
                                        FOREIGN KEY (id_ronde)
                                        REFERENCES rondes(id)
                                )
                                """;
            stmt.executeUpdate(matchTable);
            
            String joueurTable = """
                                 CREATE TABLE joueurs(
                                    id int AUTO_INCREMENT PRIMARY KEY,
                                    nom VARCHAR(50),
                                    prenom VARCHAR(50),
                                    sexe INT,
                                    naissance DATE,
                                    score FLOAT,
                                    id_equipe INT,
                                    CONSTRAINT fk_equipe
                                       FOREIGN KEY (id_equipe)
                                       REFERENCES equipes(id)
                                       ON DELETE SET NULL
                                 )
                                 """;
            stmt.executeUpdate(joueurTable);
            
            String userTable = """
                               CREATE TABLE utilisateurs(
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    nom VARCHAR(50),
                                    prenom VARCHAR(50),
                                    mail VARCHAR(100),
                                    mdp VARCHAR(50),
                                    role INT                                    
                               )
                               """;
            
            String sportTable = """
                                CREATE TABLE sports(
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    nom VARCHAR(50)
                                )
                                """;
            
            String terrainTable ="""
                                 CREATE TABLE terrains(
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    id_sport VARCHAR,
                                    disponibilite BOOLEAN,
                                    CONSTRAINT fk_equipe
                                        FOREIGN KEY (id_sport)
                                        REFERENCES sports(id)
                                 )
                                 """;
        }
        catch (SQLException err){
            System.out.println("Probleme lors de la fermeture de la BDD");
        }
    }
}
