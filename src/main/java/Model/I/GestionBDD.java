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
    
    public static void dropDB() {
    try (Statement stmt = connection.createStatement()) {

        stmt.execute("SET FOREIGN_KEY_CHECKS = 0");

        stmt.execute("DROP TABLE IF EXISTS match_joueurs");
        stmt.execute("DROP TABLE IF EXISTS matchs");
        stmt.execute("DROP TABLE IF EXISTS joueurs");
        stmt.execute("DROP TABLE IF EXISTS equipes");
        stmt.execute("DROP TABLE IF EXISTS terrains");
        stmt.execute("DROP TABLE IF EXISTS rondes");
        stmt.execute("DROP TABLE IF EXISTS sports");
        stmt.execute("DROP TABLE IF EXISTS tournois");
        stmt.execute("DROP TABLE IF EXISTS utilisateurs");
        stmt.execute("DROP TABLE IF EXISTS tournoi_joueurs");

        stmt.execute("SET FOREIGN_KEY_CHECKS = 1");

        System.out.println("BDD supprimée avec succès");

    } catch (SQLException e) {
        System.out.println("Erreur lors du drop BDD : " + e.getMessage());
    }
}


    
    public static void createDB(){
        try (Statement stmt = connection.createStatement()) {
            String tournoiTable = """
                                CREATE TABLE tournois (
                                    id int AUTO_INCREMENT PRIMARY KEY,
                                    nom VARCHAR(100),
                                    statut VARCHAR(20)
                                )
                                """;
            stmt.executeUpdate(tournoiTable);
            
            String sportTable = """
                                CREATE TABLE sports(
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    nom VARCHAR(50),
                                    nb_joueurs INT NOT NULL
                                )
                                """;
            stmt.executeUpdate(sportTable);
            
            String terrainTable ="""
                                 CREATE TABLE terrains(
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    id_sport INT,
                                    id_tournoi INT,
                                    disponibilite BOOLEAN,
                                    CONSTRAINT fk_sport
                                        FOREIGN KEY (id_sport)
                                        REFERENCES sports(id),
                                    CONSTRAINT fk_tournoi_terrain
                                        FOREIGN KEY (id_tournoi)
                                        REFERENCES tournois(id)
                                 )
                                 """;
            stmt.executeUpdate(terrainTable);
            
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
                                    id INT PRIMARY KEY AUTO_INCREMENT,
                                    nom VARCHAR(100) NOT NULL,
                                    id_tournoi INT NOT NULL,
                                    FOREIGN KEY (id_tournoi) REFERENCES tournois(id)
                                );
                                 """;
            stmt.executeUpdate(equipeTable);
            
            String matchTable = """
                                CREATE TABLE matchs (
                                    id INT AUTO_INCREMENT PRIMARY KEY,

                                    id_equipe1 INT NOT NULL,
                                    score_equipe1 INT DEFAULT 0,

                                    id_equipe2 INT NOT NULL,
                                    score_equipe2 INT DEFAULT 0,

                                    statut VARCHAR(20),

                                    id_sport INT NOT NULL,
                                    id_terrain INT NOT NULL,
                                    id_ronde INT NOT NULL,

                                    CONSTRAINT fk_match_equipe1
                                        FOREIGN KEY (id_equipe1)
                                        REFERENCES equipes(id),

                                    CONSTRAINT fk_match_equipe2
                                        FOREIGN KEY (id_equipe2)
                                        REFERENCES equipes(id),

                                    CONSTRAINT fk_match_sport
                                        FOREIGN KEY (id_sport)
                                        REFERENCES sports(id),

                                    CONSTRAINT fk_match_terrain
                                        FOREIGN KEY (id_terrain)
                                        REFERENCES terrains(id),

                                    CONSTRAINT fk_match_ronde
                                        FOREIGN KEY (id_ronde)
                                        REFERENCES rondes(id)
                                        ON DELETE CASCADE
                                )
                                """;
            stmt.executeUpdate(matchTable);
            
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
            stmt.executeUpdate(userTable);
            
            String joueurTable = """
                                 CREATE TABLE joueurs(
                                    id int AUTO_INCREMENT PRIMARY KEY,
                                    nom VARCHAR(50),
                                    prenom VARCHAR(50),
                                    taille INT,
                                    sexe INT,
                                    naissance DATE,
                                    score FLOAT,
                                    id_equipe INT,
                                    id_utilisateur INT,
                                    disponible BOOLEAN default true,
                                    CONSTRAINT fk_equipe_joueurs
                                       FOREIGN KEY (id_equipe)
                                       REFERENCES equipes(id)
                                       ON DELETE SET NULL,
                                    CONSTRAINT fk_joueur_utilisateur
                                        FOREIGN KEY (id_utilisateur)
                                        REFERENCES utilisateurs(id)
                                 )
                                 """;
            stmt.executeUpdate(joueurTable);
            
            String matchJoueursTable = """
                                    CREATE TABLE match_joueurs (
                                        id_match INT NOT NULL,
                                        id_joueur INT NOT NULL,
                                        equipe INT, -- 1 ou 2 pour l'équipe dans ce match
                                        PRIMARY KEY (id_match, id_joueur),
                                        FOREIGN KEY (id_match) REFERENCES matchs(id) ON DELETE CASCADE,
                                        FOREIGN KEY (id_joueur) REFERENCES joueurs(id) ON DELETE CASCADE
                                    );
                                    """;
            stmt.executeUpdate(matchJoueursTable);
            
            String tournoiJoueursTable = """
                                    CREATE TABLE tournoi_joueurs (
                                        id_tournoi INT,
                                        id_joueur INT,
                                        score INT,
                                        CONSTRAINT fk_tj
                                            FOREIGN KEY (id_tournoi)
                                            REFERENCES tournois(id)
                                            ON DELETE CASCADE,
                                        CONSTRAINT fk_joueurs_tournoi
                                            FOREIGN KEY (id_joueur)
                                            REFERENCES joueurs(id)
                                            ON DELETE CASCADE
                                    );
                                    """;
            stmt.executeUpdate(tournoiJoueursTable);
        }
        catch (SQLException err){
            System.out.println("Probleme lors de la creation de la BDD ! "+err);
        }
    }
    
    public static void insertDB() {
        try (Statement stmt = connection.createStatement()) {
            
            // Sports
            stmt.executeUpdate("INSERT INTO sports (nom, nb_joueurs) VALUES\n" +
                                "('Football', 11),\n" +
                                "('Futsal', 5),\n" +
                                "('Basketball', 5),\n" +
                                "('Volleyball', 6),\n" +
                                "('Handball', 7),\n" +
                                "('Rugby', 15),\n" +
                                "('Hockey', 6),\n" +
                                "('Tennis', 1),\n" +
                                "('Tennis de table', 1),\n" +
                                "('Badminton', 2),\n" +
                                "('Boxe', 1),\n" +
                                "('Judo', 1);");

            // Users
            stmt.executeUpdate("""
                INSERT INTO utilisateurs (nom, prenom, mail, mdp, role)
                VALUES
                ('Dupont', 'Alex', 'alex.dupont@example.com', '1234', 1),
                ('Martin', 'Lucas', 'lucas.martin@example.com', '1234', 1),
                ('Durand', 'Emma', 'emma.durand@example.com', '1234', 1),
                ('Petit', 'Lina', 'lina.petit@example.com', '1234', 1),
                ('Moreau', 'Tom', 'tom.moreau@example.com', '1234', 1),
                ('Roux', 'Hugo', 'hugo.roux@example.com', '1234', 1),
                ('Fournier', 'Jade', 'jade.fournier@example.com', '1234', 1),
                ('Girard', 'Manon', 'manon.girard@example.com', '1234', 1),
                ('Andre', 'Leo', 'leo.andre@example.com', '1234', 1),
                ('Mercier', 'Paul', 'paul.mercier@example.com', '1234', 1),
                ('Blanc', 'Sarah', 'sarah.blanc@example.com', '1234', 1),
                ('Garnier', 'Clara', 'clara.garnier@example.com', '1234', 1),
                ('Chevalier', 'Noah', 'noah.chevalier@example.com', '1234', 1),
                ('Lambert', 'Ethan', 'ethan.lambert@example.com', '1234', 1),
                ('Francois', 'Liam', 'liam.francois@example.com', '1234', 1),
                ('Henry', 'Nathan', 'nathan.henry@example.com', '1234', 1),
                ('Ammari', 'Inayat', 'a@i.fr', '1', 0);
            """);
            
            // Joueurs
            stmt.executeUpdate("""
                INSERT INTO joueurs (nom, prenom, taille, sexe, naissance, score, id_equipe, id_utilisateur, disponible)
                VALUES
                ('Dupont', 'Alex', 180, 1, '2000-01-01', 0, NULL, 1, true),
                ('Martin', 'Lucas', 175, 1, '2001-02-02', 0, NULL, 2, true),
                ('Durand', 'Emma', 165, 2, '2002-03-03', 0, NULL, 3, true),
                ('Petit', 'Lina', 170, 2, '2001-04-04', 0, NULL, 4, true),
                ('Moreau', 'Tom', 182, 1, '1999-05-05', 0, NULL, 5, true),
                ('Roux', 'Hugo', 178, 1, '2000-06-06', 0, NULL, 6, true),
                ('Fournier', 'Jade', 168, 2, '2002-07-07', 0, NULL, 7, true),
                ('Girard', 'Manon', 172, 2, '2001-08-08', 0, NULL, 8, true),
                ('Andre', 'Leo', 185, 1, '1998-09-09', 0, NULL, 9, true),
                ('Mercier', 'Paul', 176, 1, '2000-10-10', 0, NULL, 10, true),
                ('Blanc', 'Sarah', 169, 2, '2001-11-11', 0, NULL, 11, true),
                ('Garnier', 'Clara', 171, 2, '2002-12-12', 0, NULL, 12, true),
                ('Chevalier', 'Noah', 183, 1, '1999-03-15', 0, NULL, 13, true),
                ('Lambert', 'Ethan', 179, 1, '2000-04-18', 0, NULL, 14, true),
                ('Francois', 'Liam', 181, 1, '1998-06-20', 0, NULL, 15, true),
                ('Henry', 'Nathan', 177, 1, '2001-07-22', 0, NULL, 16, true)
            """);
            
            System.out.println("BDD peuplée avec succès !");

        } catch (SQLException e) {
            System.out.println("Erreur insertDB : " + e.getMessage());
    }
}

}
