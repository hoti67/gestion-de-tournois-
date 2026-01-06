package Model.I;

import java.sql.*;
import java.util.ArrayList;

public class Terrain {

    private static final Connection con = GestionBDD.getConnection();

    private int id;
    private int idSport;
    private int idTournoi;
    private boolean disponible = true;

    public Terrain(int idSport, int idTournoi, boolean disponible) {
        this.idSport = idSport;
        this.idTournoi = idTournoi;
        this.disponible = disponible;
    }

    public void saveInDB() {
        try (PreparedStatement st = con.prepareStatement("""
            INSERT INTO terrains (id_sport, id_tournoi, disponibilite)
            VALUES (?, ?, ?)
        """)) {
            st.setInt(1, idSport);
            st.setInt(2, idTournoi);
            st.setBoolean(3, disponible);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur save terrain : " + e.getMessage());
        }
    }

    public static ArrayList<Terrain> getDisponibles(int idTournoi, int idSport) {
        ArrayList<Terrain> terrains = new ArrayList<>();

        try (PreparedStatement st = con.prepareStatement("""
            SELECT * FROM terrains
            WHERE id_tournoi = ? AND id_sport = ? AND disponibilite = true
        """)) {

            st.setInt(1, idTournoi);
            st.setInt(2, idSport);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Terrain t = new Terrain(
                        rs.getInt("id_sport"),
                        rs.getInt("id_tournoi"),
                        true
                );
                t.id = rs.getInt("id");
                terrains.add(t);
            }
        } catch (SQLException e) {
            System.out.println("Erreur terrains dispo : " + e.getMessage());
        }
        return terrains;
    }

    public int getId() {
        return id;
    }

    public int getIdSport() {
        return idSport;
    }

    public int getIdTournoi() {
        return idTournoi;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
    
}
