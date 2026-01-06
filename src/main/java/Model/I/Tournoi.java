package Model.I;

import java.sql.*;
import java.util.ArrayList;

public class Tournoi {

    private static final Connection con = GestionBDD.getConnection();

    private int id;
    private String nom;

    // ---------------- Constructeurs ----------------

    public Tournoi(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public Tournoi(String nom) {
        this.nom = nom;
    }

    // ---------------- BDD ----------------

    public void saveInDB() {
        String sql = "INSERT INTO tournois (nom) VALUES (?)";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, nom);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur save tournoi : " + e.getMessage());
        }
    }

    public static Tournoi getByID(int id) {
        String sql = "SELECT * FROM tournois WHERE id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Tournoi(
                        rs.getInt("id"),
                        rs.getString("nom")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur get tournoi : " + e.getMessage());
        }
        return null;
    }

    public static ArrayList<Tournoi> listTournois() {
        ArrayList<Tournoi> list = new ArrayList<>();
        String sql = "SELECT * FROM tournois";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Tournoi(
                        rs.getInt("id"),
                        rs.getString("nom")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur list tournois : " + e.getMessage());
        }
        return list;
    }

    // ---------------- Getters ----------------

    public int getId() { return id; }
    public String getNom() { return nom; }
}

