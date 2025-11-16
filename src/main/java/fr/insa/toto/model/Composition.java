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
class Composition {
    private int idEquipe;
    private int idJoueur;

    public Composition() {
        // constructeur vide
    }

    public Composition(int idEquipe, int idJoueur) {
        this.idEquipe = idEquipe;
        this.idJoueur = idJoueur;
    }
public void saveInDB(Connection con) {
    try (PreparedStatement pst = con.prepareStatement(
            "INSERT INTO composition (idEquipe, idJoueur) VALUES (?, ?)")) {
        pst.setInt(1, this.idEquipe);
        pst.setInt(2, this.idJoueur);
        pst.executeUpdate();
    } catch (SQLException ex) {
        throw new RuntimeException("Erreur lors de l'insertion de la composition dans la base", ex);
    }
}
}
