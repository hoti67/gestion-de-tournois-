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

 public class Matchs {
    /*private int id;
    private String ronde;

    public Matchs(String ronde) {
        this.id = id;
        this.ronde = ronde;
    }

    public int getId() {
        return id;
    }

    public String getRonde() {
        return ronde;
    }

    public void saveInDB(Connection con) throws SQLException {
    PreparedStatement pst = con.prepareStatement(
        "INSERT INTO matchs (ronde) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
    pst.setString(1, this.ronde);
    pst.executeUpdate();
    ResultSet rs = pst.getGeneratedKeys();
    if (rs.next()) {
        this.id = rs.getInt(1); // <- ça remplit l'id
    }
}

    
}*/
     
private int id;          // id généré par MySQL
    private String ronde;

    public Matchs(String ronde) {
        this.ronde = ronde;
    }

    public int getId() {
        return id;
    }

    public void saveInDB(Connection con) {
        try {
            PreparedStatement pst = con.prepareStatement(
                "INSERT INTO matchs (ronde) VALUES (?)",
                PreparedStatement.RETURN_GENERATED_KEYS
            );
            pst.setString(1, this.ronde);
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                this.id = rs.getInt(1);  // récupération de l'id auto-incrémenté
            }
            rs.close();
            pst.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion du match dans la base", e);
        }
    }
}
