package Model.I;

import java.sql.*;

public class Equipe {

    private static final Connection con = GestionBDD.getConnection();

    private Integer id;
    private String nom;
    private int idTournoi;

    public Equipe(String nom, int idTournoi) {
        this.nom = nom;
        this.idTournoi = idTournoi;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public int getIdTournoi() {
        return idTournoi;
    }

    public void saveInDB() {
        try (PreparedStatement st = con.prepareStatement(
                "INSERT INTO equipes (nom, id_tournoi) VALUES (?, ?)", 
                Statement.RETURN_GENERATED_KEYS)) {

            st.setString(1, nom);
            st.setInt(2, idTournoi);
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Erreur saveInDB Equipe : " + e.getMessage());
        }
    }
}
