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

import fr.insa.beuvron.utils.database.ConnectionSimpleSGBD;
import static fr.insa.toto.model.GestionBdD.GestionSchema.creeSchema;
import static fr.insa.toto.model.GestionBdD.GestionSchema.deleteSchema;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;

/**
 *
 * @author arbre
 */
public class GestionBdD {

    static void razBdd(Connection con) throws SQLException {
         System.out.println("Réinitialisation de la base de données...");
    try {
        deleteSchema(con);   // supprime toutes les tables si elles existent
        creeSchema(con);     // recrée toutes les tables
        System.out.println("Base de données réinitialisée avec succès !");
    } catch (SQLException ex) {
        System.err.println("Erreur pendant la réinitialisation : " + ex.getMessage());
        throw ex;
    }// Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
 
public class GestionSchema {

    /**
     *
     * @param con
     * @throws SQLException
     */
    public static void creeSchema(Connection con)
            throws SQLException {
        try {
            con.setAutoCommit(false);
            try (Statement st = con.createStatement()) {
                // creation des tables
                st.executeUpdate("drop table if exists utilisateur");
                st.executeUpdate("create table utilisateur ( "
                        + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ","       
                        + " surnom varchar(30) not null unique,"
                        + " pass varchar(20),"
                        + " role integer not null "
                        + ") "
                );
                st.executeUpdate("drop table if exists joueur");
                 st.executeUpdate("create table joueur ( "
                        + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ","
                        + " nom VARCHAR(50) NOT NULL,"
                        + " prenom VARCHAR(50) NOT NULL,"
                        + " sexe VARCHAR(10),"
                        + " dateNaissance DATE,"
                        + " scoreTotal INT DEFAULT 0"
                        + ") "
                );
                st.executeUpdate("drop table if exists equipe");
                st.executeUpdate("drop table if exists matchs");
                st.executeUpdate("create table matchs ( "
                        + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ","
                        + " ronde varchar(255) not null "         
                        + ") ENGINE=InnoDB;"
                );
                
                st.executeUpdate("create table equipe ( "
                        + "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
                        + " numero int not null ,"
                        + " score int, "
                        + " idmatchs integer not null,"
                        + "CONSTRAINT fk_match FOREIGN KEY (idmatchs) REFERENCES matchs(id)" 
                        + ") ENGINE=InnoDB;"
                );
                st.executeUpdate("drop table if exists composition");
                 st.executeUpdate("create table composition ( "
                        + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ","
                        + " idequipe integer not null, "
                        + " idjoueur integer not null"
                        + " )"
                );
                st.executeUpdate("drop table if exists loisir");
                st.executeUpdate("create table loisir ( "
                        + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ","
                        + " nom varchar(20) not null unique,"
                        + " description text"
                        + ") "
                );
                st.executeUpdate("drop table if exists pratique");
                st.executeUpdate("create table pratique ( "
                        + " idutilisateur integer not null,"
                        + " idloisir integer not null,"
                        + " niveau integer not null "
                        + ") "
                );
                con.commit();
                st.executeUpdate("drop table if exists apprecie");
                st.executeUpdate("create table apprecie ( "
                        + " u1 integer not null,"
                        + " u2 integer not null"
                        + ") "
                );

                st.executeUpdate("alter table apprecie\n"
                        + "  add constraint fk_apprecie_u1\n"
                        + "  foreign key (u1) references utilisateur(id)"
                );
                st.executeUpdate("alter table apprecie\n"
                        + "  add constraint fk_apprecie_u2\n"
                        + "  foreign key (u2) references utilisateur(id)"
                );
                st.executeUpdate("alter table pratique\n"
                        + "  add constraint fk_pratique_idutilisateur\n"
                        + "  foreign key (idutilisateur) references utilisateur(id)"
                );

                st.executeUpdate("alter table pratique\n"
                        + "  add constraint fk_pratique_idloisir\n"
                        + "  foreign key (idloisir) references loisir(id)"
                );

                con.commit();
            }
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }

    /**
     *
     * @param con
     * @throws SQLException
     */
    public static void deleteSchema(Connection con) throws SQLException {
        try (Statement st = con.createStatement()) {
            try {
                st.executeUpdate(
                        "alter table utilisateur "
                        + "drop constraint fk_utilisateur_u1");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate(
                        "alter table joueur "
                        + "drop constraint fk_utilisateur_u1");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate(
                        "alter table matchs "
                        + "drop constraint fk_utilisateur_u1");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate(
                        "alter table equipe "
                        + "drop constraint fk_utilisateur_u1");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate(
                        "alter table composition "
                        + "drop constraint fk_utilisateur_u1");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate(
                        "alter table utilisateur "
                        + "drop constraint fk_utilisateur_u2");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate(
                        "alter table pratique "
                        + "drop constraint fk_pratique_idutilisateur");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate(
                        "alter table pratique "
                        + "drop constraint fk_pratique_idloisir");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table apprecie");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table pratique");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table loisir");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table utilisateur");
            } catch (SQLException ex) {
            }
        }
    }

    /**
     *
     * @param con
     * @throws SQLException
     */
    public static void razBdd(Connection con) throws SQLException {
        System.out.println("Réinitialisation de la base de données...");
    try {
        deleteSchema(con);   // méthode pour supprimer les tables (DROP)
        creeSchema(con);     // méthode pour recréer les tables
        System.out.println("Base de données réinitialisée avec succès !");
    } catch (SQLException ex) {
        System.err.println("Erreur pendant la réinitialisation : " + ex.getMessage());
        throw ex;
    }
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            razBdd(con);
        } catch (SQLException ex) {
            throw new Error(ex);
        }
    }

}  
}