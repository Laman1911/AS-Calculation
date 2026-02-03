package dk.eak.kalkulation.repository;

import dk.eak.kalkulation.model.DelProjekt;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DelProjektRepository {

    private final JdbcTemplate jdbc;

    public DelProjektRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // 🔹 CREATE
    public void create(DelProjekt dp) {
        String sql = """
            INSERT INTO delprojekt (project_id, name, description)
            VALUES (?, ?, ?)
        """;

        jdbc.update(sql,
                dp.getProjectId(),
                dp.getName(),
                dp.getDescription()
        );
    }

    // 🔹 FIND BY ID
    public DelProjekt findById(int delProjektId) {
        String sql = "SELECT * FROM delprojekt WHERE delprojekt_id = ?";
        return jdbc.queryForObject(sql, (rs, rowNum) -> mapDelProjekt(rs), delProjektId);
    }

    // 🔹 FIND BY PROJECT ID
    public List<DelProjekt> findByProjektId(int projectId) {
        String sql = """
            SELECT delprojekt_id, project_id, name, description
            FROM delprojekt
            WHERE project_id = ?
        """;

        return jdbc.query(sql, (rs, rowNum) -> mapDelProjekt(rs), projectId);
    }

    // 🔹 UPDATE
    public void update(DelProjekt dp) {
        String sql = "UPDATE delprojekt SET project_id = ?, name = ?, description = ? WHERE delprojekt_id = ?";
        jdbc.update(sql,
                dp.getProjectId(),
                dp.getName(),
                dp.getDescription(),
                dp.getDelProjektId()
        );
    }

    // 🔹 DELETE
    public void delete(int id) {
        String sql = "DELETE FROM delprojekt WHERE delprojekt_id = ?";
        jdbc.update(sql, id);
    }

    // Helper method to map ResultSet to DelProjekt
    private DelProjekt mapDelProjekt(java.sql.ResultSet rs) throws java.sql.SQLException {
        DelProjekt dp = new DelProjekt();
        dp.setDelProjektId(rs.getInt("delprojekt_id"));
        dp.setProjectId(rs.getInt("project_id"));
        dp.setName(rs.getString("name"));
        dp.setDescription(rs.getString("description"));
        return dp;
    }
}
