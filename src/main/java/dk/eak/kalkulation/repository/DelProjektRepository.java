package dk.eak.kalkulation.repository;

import dk.eak.kalkulation.model.DelProjekt;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DelProjektRepository {
    private final JdbcTemplate jdbc;

    public DelProjektRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public List<DelProjekt> findByProjektId(int projektId) {
        String sql = "SELECT * FROM del_project WHERE projekt_id = ? ORDER BY del_project_id";
        return jdbc.query(sql, (rs, rn) -> {
            DelProjekt d = new DelProjekt();
            d.setDelProjektId(rs.getInt("del_project_id"));
            d.setProjektId(rs.getInt("projekt_id"));
            d.setName(rs.getString("name"));
            d.setDescription(rs.getString("description"));
            return d;
        }, projektId);
    }

    public void create(DelProjekt d) {
        String sql = "INSERT INTO del_project (projekt_id, name, description) VALUES (?,?,?)";
        jdbc.update(sql, d.getProjektId(), d.getName(), d.getDescription());
    }

    public void delete(int delProjektId) {
        jdbc.update("DELETE FROM del_project WHERE del_project_id = ?", delProjektId);
    }
}
