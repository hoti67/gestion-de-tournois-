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
import fr.insa.toto.model.GestionBdD.GestionSchema;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 *
 * @author arbre
 */
public class BdDtest {
    
    public static void createBdDTestV2(Connection con) throws SQLException {
        List<Utilisateur> users = List.of(
                new Utilisateur("toto", "p1", 1),
                new Utilisateur("titi", "p2", 2),
                new Utilisateur("tutu", "p3", 3)
        );
        for (var u : users) {
           u.saveInDB(con);  
        }
        List<Loisir> loisirs = List.of(
                new Loisir("tennis", "c'est fatiguant"),
                new Loisir("sieste", "c'est reposant"),
                new Loisir("lecture", "trop intello")
        );
        for (var lo : loisirs) {
          lo.saveInDB(con);
        }
        /*List<Joueur> joueurs = List.of(
                new Joueur("toto", "S", "180",1),
                new Joueur("titi", "J", "160",2),
                new Joueur("tutu", null, null,3),
                new Joueur("toti", null, "170",4), 
                new Joueur("tuti", "J", "190",5)
        );
        for (var joueur : joueurs) {
             joueur.saveInDB(con);
        }
        
         /*List<Matchs> matchs = List.of(
                new Matchs ("Ronde 1",1),
               new Matchs ("Ronde 1",2)        
        );*/
        List<Matchs> matchs = new ArrayList<>();
        Matchs match1 = new Matchs("Ronde 1");
        match1.saveInDB(con);  // match1.id est maintenant rempli
        matchs.add(match1);

        Matchs match2 = new Matchs("Ronde 1");
        match2.saveInDB(con);  // match2.id est maintenant rempli
        matchs.add(match2);/*
        /*for (var match : matchs) {
             match.saveInDB(con);
        }
        /*List<Equipe> equipes = List.of(
                new Equipe(1, 10,1),
                new Equipe(2, 15, 1),
                new Equipe(1, 12,2),
                new Equipe(2, 5,2)
        );*/
        List<Equipe> equipes = new ArrayList<>();
        equipes.add(new Equipe(1, 10, match1.getId()));
        equipes.add(new Equipe(2, 15, match1.getId()));
        equipes.add(new Equipe(1, 12, match2.getId()));
        equipes.add(new Equipe(2, 5, match2.getId()));
        for (var equipe : equipes) {
           equipe.saveInDB(con);  
        }
         List<Composition> compositions = List.of(
                new Composition (1, 1),
                new Composition (1, 2),
                new Composition (2, 3),
                new Composition (2, 4),
                new Composition (3, 5),
                new Composition (4, 4),
                new Composition (4, 2)
                
        );
        for (var composition : compositions) {
           composition.saveInDB(con);  
        }
        Object sql = null;
        System.out.println("Executing SQL: " + sql);
        int[][] apprecient = new int[][]{
            {0, 1},
            {1, 1},
            {1, 2},
            {2, 1},};
        try (PreparedStatement pst = con.prepareStatement(
                "insert into apprecie (u1,u2) values (?,?)")) {
            for (int[] a : apprecient) {
                pst.setInt(1, users.get(a[0]).getId());
                pst.setInt(2, users.get(a[1]).getId());
                pst.executeUpdate();
            }
        }
        int[][] pratiques = new int[][]{
            {0, 1, 1},
            {1, 0, 2},
            {1, 2, -2},
            {2, 1, -1},};
        try (PreparedStatement pst = con.prepareStatement(
                "insert into pratique (idutilisateur,idloisir,niveau) values (?,?,?)")) {
            for (int[] p : pratiques) {
                pst.setInt(1, users.get(p[0]).getId());
                pst.setInt(2, loisirs.get(p[1]).getId());
                pst.setInt(3, p[2]);
                pst.executeUpdate();
            }
        }
    }
    
/*
    public static void main(String[] args) {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            GestionBdD.razBdd(con);
            createBdDTestV2(con);
        } catch (SQLException ex) {
            throw new Error(ex);
        }
    }
 */
}
        
        
        

