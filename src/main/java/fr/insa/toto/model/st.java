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
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author arbre
 */
class st {

    private Connection connexion;
    private Statement statement;

    // Constructeur : connexion à la base
    public st(String url, String user, String password) throws SQLException {
        this.connexion = DriverManager.getConnection(url, user, password);
        this.statement = connexion.createStatement();
    }

    // Exécution d'une requête de type UPDATE / INSERT / DELETE
    public int executeUpdate(String sql) throws SQLException {
        return this.statement.executeUpdate(sql);
    }

    // Exécution d'une requête SELECT
    public ResultSet executeQuery(String sql) throws SQLException {
        return this.statement.executeQuery(sql);
    }

    // Méthode pour fermer la connexion proprement
    public void close() {
        try {
            if (statement != null) statement.close();
            if (connexion != null) connexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
