package dk.eak.kalkulation.service;

import dk.eak.kalkulation.model.Projekt;
import dk.eak.kalkulation.repository.ProjektRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ProjektService.
 * Tests project management business logic.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProjektService Unit Tests")
class ProjektServiceTest {

    @Mock
    private ProjektRepository projektRepository;

    private ProjektService projektService;

    @BeforeEach
    void setUp() {
        projektService = new ProjektService(projektRepository);
    }

    @Test
    @DisplayName("Should get all projects successfully")
    void testGetAll_Success() {
        // Arrange
        Projekt p1 = createTestProjekt(1, "Project 1", "Description 1");
        Projekt p2 = createTestProjekt(2, "Project 2", "Description 2");
        List<Projekt> projects = Arrays.asList(p1, p2);
        when(projektRepository.getAll()).thenReturn(projects);

        // Act
        List<Projekt> result = projektService.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Project 1", result.get(0).getName());
        assertEquals("Project 2", result.get(1).getName());
        verify(projektRepository, times(1)).getAll();
    }

    @Test
    @DisplayName("Should get project by ID successfully")
    void testGetById_Success() {
        // Arrange
        Projekt projekt = createTestProjekt(1, "Test Project", "Test Description");
        when(projektRepository.getById(1)).thenReturn(projekt);

        // Act
        Projekt result = projektService.getById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getProjectId());
        assertEquals("Test Project", result.getName());
        verify(projektRepository, times(1)).getById(1);
    }

    @Test
    @DisplayName("Should throw exception when getting project with invalid ID")
    void testGetById_InvalidId() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projektService.getById(0);
        });
        assertEquals("Project ID must be valid", exception.getMessage());
        verify(projektRepository, never()).getById(anyInt());
    }

    @Test
    @DisplayName("Should create project successfully")
    void testCreate_Success() {
        // Arrange
        Projekt projekt = createTestProjekt(0, "New Project", "New Description");

        // Act
        projektService.create(projekt);

        // Assert
        verify(projektRepository, times(1)).create(projekt);
    }

    @Test
    @DisplayName("Should throw exception when creating project with null")
    void testCreate_Null() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projektService.create(null);
        });
        assertEquals("Projekt cannot be null", exception.getMessage());
        verify(projektRepository, never()).create(any());
    }

    @Test
    @DisplayName("Should throw exception when creating project with empty name")
    void testCreate_EmptyName() {
        // Arrange
        Projekt projekt = createTestProjekt(0, "", "Description");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projektService.create(projekt);
        });
        assertEquals("Project name cannot be empty", exception.getMessage());
        verify(projektRepository, never()).create(any());
    }

    @Test
    @DisplayName("Should throw exception when creating project with blank name")
    void testCreate_BlankName() {
        // Arrange
        Projekt projekt = createTestProjekt(0, "   ", "Description");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projektService.create(projekt);
        });
        assertEquals("Project name cannot be empty", exception.getMessage());
        verify(projektRepository, never()).create(any());
    }

    @Test
    @DisplayName("Should throw exception when end date is before start date")
    void testCreate_InvalidDateRange() {
        // Arrange
        Projekt projekt = new Projekt();
        projekt.setName("Project");
        projekt.setStartDate(LocalDate.of(2026, 2, 10));
        projekt.setEndDate(LocalDate.of(2026, 2, 5));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projektService.create(projekt);
        });
        assertEquals("End date cannot be before start date", exception.getMessage());
        verify(projektRepository, never()).create(any());
    }

    @Test
    @DisplayName("Should update project successfully")
    void testUpdate_Success() {
        // Arrange
        Projekt projekt = createTestProjekt(1, "Updated Project", "Updated Description");

        // Act
        projektService.update(projekt);

        // Assert
        verify(projektRepository, times(1)).update(projekt);
    }

    @Test
    @DisplayName("Should throw exception when updating project with invalid ID")
    void testUpdate_InvalidId() {
        // Arrange
        Projekt projekt = createTestProjekt(0, "Project", "Description");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projektService.update(projekt);
        });
        assertEquals("Project ID must be valid", exception.getMessage());
        verify(projektRepository, never()).update(any());
    }

    @Test
    @DisplayName("Should delete project successfully")
    void testDelete_Success() {
        // Act
        projektService.delete(1);

        // Assert
        verify(projektRepository, times(1)).delete(1);
    }

    @Test
    @DisplayName("Should throw exception when deleting project with invalid ID")
    void testDelete_InvalidId() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projektService.delete(0);
        });
        assertEquals("Project ID must be valid", exception.getMessage());
        verify(projektRepository, never()).delete(anyInt());
    }

    // Helper method to create test Projekt
    private Projekt createTestProjekt(int id, String name, String description) {
        Projekt projekt = new Projekt();
        projekt.setProjectId(id);
        projekt.setName(name);
        projekt.setDescription(description);
        projekt.setStartDate(LocalDate.now());
        projekt.setEndDate(LocalDate.now().plusDays(10));
        return projekt;
    }
}
