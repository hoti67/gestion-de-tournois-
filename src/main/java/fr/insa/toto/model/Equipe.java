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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author arbre
 */
public class Equipe {
    /*private int id;
    private int numero;
    private int score;
    private int idmatchs;
    private int idMatch;
    private int idMatchs;

    // constructeur sans arguments
    public Equipe(int par, int par1, int id1) {
    }

    // constructeur avec tous les champs
    public Equipe(int id, int numero, int score, int idmatchs) {
        this.id = id;
        this.numero = numero;
        this.score = score;
        this.idmatchs = idmatchs;
    }
   public void saveInDB(Connection con) {
    String sql = "INSERT INTO equipe (numero, score, idmatchs) VALUES (?, ?, ?)";
    try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        ps.setInt(1, this.numero);
        ps.setInt(2, this.score);
        ps.setInt(3, this.idmatchs);

        ps.executeUpdate();

        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                this.id = rs.getInt(1);
            }
        }

    } catch (SQLException ex) {
        throw new RuntimeException("Erreur lors de l'insertion de l'équipe dans la base", ex);
    }
} 

    // getters et setters...
}
*/
    
 private int numero;
    private int score;
    private int idmatchs;

    public Equipe(int numero, int score, int idmatchs) {
        this.numero = numero;
        this.score = score;
        this.idmatchs = idmatchs;  // doit correspondre à un id existant dans Matchs
    }

    public void saveInDB(Connection con) {
        try {
            PreparedStatement pst = con.prepareStatement(
                "INSERT INTO equipe (numero, score, idmatchs) VALUES (?, ?, ?)"
            );
            pst.setInt(1, this.numero);
            pst.setInt(2, this.score);
            pst.setInt(3, this.idmatchs);
            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion de l'équipe dans la base", e);
        }
    }
}