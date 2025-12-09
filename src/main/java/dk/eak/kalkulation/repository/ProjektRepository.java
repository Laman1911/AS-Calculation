package dk.eak.kalkulation.repository;

import dk.eak.kalkulation.model.Projekt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProjektRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Projekt> rowMapper = new RowMapper<Projekt>() {
        @Override
        public Projekt mapRow(ResultSet rs, int rowNum) throws SQLException {
            Projekt projekt = new Projekt();
            projekt.setId(rs.getInt("id"));
            projekt.setNavn(rs.getString("navn"));
            projekt.setBeskrivelse(rs.getString("beskrivelse"));
            projekt.setBudget(rs.getDouble("budget"));
            projekt.setEstimeredeTimer(rs.getDouble("estimerede_timer"));
            projekt.setTimeRate(rs.getDouble("time_rate"));

            if (rs.getDate("start_dato") != null) {
                projekt.setStartDato(rs.getDate("start_dato").toLocalDate());
            }
            if (rs.getDate("slut_dato") != null) {
                projekt.setSlutDato(rs.getDate("slut_dato").toLocalDate());
            }

            projekt.setStatus(rs.getString("status"));
            return projekt;
        }
    };

    public List<Projekt> getAll() {
        String sql = "SELECT * FROM projekter";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Projekt getById(int id) {
        String sql = "SELECT * FROM projekter WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void create(Projekt p) {
        String sql = "INSERT INTO projekter (navn, beskrivelse, budget, estimerede_timer, time_rate, start_dato, slut_dato, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            p.getNavn(),
            p.getBeskrivelse(),
            p.getBudget(),
            p.getEstimeredeTimer(),
            p.getTimeRate(),
            p.getStartDato(),
            p.getSlutDato(),
            p.getStatus()
        );
    }

    public void update(Projekt p) {
        String sql = "UPDATE projekter SET navn = ?, beskrivelse = ?, budget = ?, estimerede_timer = ?, " +
                     "time_rate = ?, start_dato = ?, slut_dato = ?, status = ? WHERE id = ?";
        jdbcTemplate.update(sql,
            p.getNavn(),
            p.getBeskrivelse(),
            p.getBudget(),
            p.getEstimeredeTimer(),
            p.getTimeRate(),
            p.getStartDato(),
            p.getSlutDato(),
            p.getStatus(),
            p.getId()
        );
    }

    public void delete(int id) {
        String sql = "DELETE FROM projekter WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
