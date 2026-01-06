package Model.I;

import java.sql.*;
import java.util.ArrayList;

public class Sport {

    private static final Connection con = GestionBDD.getConnection();

    private int id;
    private String nom;
    private int nbJoueurs;

    public Sport(int id, String nom, int nbJoueurs) {
        this.id = id;
        this.nom = nom;
        this.nbJoueurs = nbJoueurs;
    }

    public static ArrayList<Sport> listSports() {
        ArrayList<Sport> sports = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement(
                "SELECT * FROM sports")) {

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                sports.add(new Sport(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("nb_joueurs")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur listSports : " + e.getMessage());
        }
        return sports;
    }

    public static Sport getById(int id) {
        try (PreparedStatement st = con.prepareStatement(
                "SELECT * FROM sports WHERE id = ?")) {

            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return new Sport(
                        id,
                        rs.getString("nom"),
                        rs.getInt("nb_joueurs")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur getSportById : " + e.getMessage());
        }
        return null;
    }

    public static ArrayList<Sport> getByTournoiId(int idTournoi) {
        ArrayList<Sport> sports = new ArrayList<>();

        String sql = "SELECT id_sport FROM terrains WHERE id_tournoi = ?";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, idTournoi);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                sports.add(Sport.getById(rs.getInt("id_sport")));
            }
        } catch (SQLException e) {
            System.out.println("Erreur getByTournoiId Sports : " + e.getMessage());
        }
        return sports;
    }
    
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public int getNbJoueurs() {
        return nbJoueurs;
    }
    
}
