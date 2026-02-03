package dk.eak.kalkulation.repository;

import dk.eak.kalkulation.model.Projekt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for ProjektRepository.
 * Tests database operations with H2 in-memory database.
 */
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("ProjektRepository Integration Tests")
class ProjektRepositoryIT {

    @Autowired
    private ProjektRepository projektRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Should retrieve all projects from database")
    void testGetAll_Success() {
        // Act
        List<Projekt> projects = projektRepository.getAll();

        // Assert
        assertNotNull(projects);
        assertFalse(projects.isEmpty());
        assertTrue(projects.stream().anyMatch(p -> p.getName().equals("Test Project 1")));
    }

    @Test
    @DisplayName("Should retrieve project by ID")
    void testGetById_Success() {
        // Act
        Projekt projekt = projektRepository.getById(1);

        // Assert
        assertNotNull(projekt);
        assertEquals(1, projekt.getProjectId());
        assertEquals("Test Project 1", projekt.getName());
    }

    @Test
    @DisplayName("Should create new project in database")
    void testCreate_Success() {
        // Arrange
        int initialCount = projektRepository.getAll().size();
        Projekt newProjekt = new Projekt();
        newProjekt.setName("Brand New Project");
        newProjekt.setDescription("Newly created");
        newProjekt.setStartDate(LocalDate.of(2026, 5, 1));
        newProjekt.setEndDate(LocalDate.of(2026, 5, 31));

        // Act
        projektRepository.create(newProjekt);

        // Assert
        List<Projekt> allProjects = projektRepository.getAll();
        assertEquals(initialCount + 1, allProjects.size());
        assertTrue(allProjects.stream().anyMatch(p -> p.getName().equals("Brand New Project")));
    }

    @Test
    @DisplayName("Should update existing project")
    void testUpdate_Success() {
        // Arrange
        Projekt projekt = projektRepository.getById(1);
        String originalName = projekt.getName();
        projekt.setName("Updated Project Name");

        // Act
        projektRepository.update(projekt);

        // Assert
        Projekt updated = projektRepository.getById(1);
        assertEquals("Updated Project Name", updated.getName());
        assertNotEquals(originalName, updated.getName());
    }

    @Test
    @DisplayName("Should delete project from database")
    void testDelete_Success() {
        // Arrange
        int initialCount = projektRepository.getAll().size();

        // Act
        projektRepository.delete(1);

        // Assert
        List<Projekt> projects = projektRepository.getAll();
        assertEquals(initialCount - 1, projects.size());
        assertFalse(projects.stream().anyMatch(p -> p.getProjectId() == 1));
    }

    @Test
    @DisplayName("Should handle project without dates")
    void testCreate_WithoutDates() {
        // Arrange
        Projekt newProjekt = new Projekt();
        newProjekt.setName("Project Without Dates");
        newProjekt.setDescription("No dates assigned");

        // Act
        projektRepository.create(newProjekt);

        // Assert
        List<Projekt> allProjects = projektRepository.getAll();
        Projekt created = allProjects.stream()
            .filter(p -> p.getName().equals("Project Without Dates"))
            .findFirst()
            .orElse(null);
        assertNotNull(created);
        assertNull(created.getStartDate());
        assertNull(created.getEndDate());
    }
}

