package dk.eak.kalkulation.service;

import dk.eak.kalkulation.model.DelProjekt;
import dk.eak.kalkulation.repository.DelProjektRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for DelProjektService.
 * Tests subproject management business logic.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DelProjektService Unit Tests")
class DelProjektServiceTest {

    @Mock
    private DelProjektRepository delProjektRepository;

    private DelProjektService delProjektService;

    @BeforeEach
    void setUp() {
        delProjektService = new DelProjektService(delProjektRepository);
    }

    @Test
    @DisplayName("Should get all subprojects for a project")
    void testGetByProjectId_Success() {
        // Arrange
        int projectId = 1;
        DelProjekt dp1 = createTestDelProjekt(1, projectId, "SubProject 1", "Description 1");
        DelProjekt dp2 = createTestDelProjekt(2, projectId, "SubProject 2", "Description 2");
        List<DelProjekt> delProjekter = Arrays.asList(dp1, dp2);
        when(delProjektRepository.findByProjektId(projectId)).thenReturn(delProjekter);

        // Act
        List<DelProjekt> result = delProjektService.getByProjectId(projectId);

        // Assert
        assertEquals(2, result.size());
        assertEquals("SubProject 1", result.get(0).getName());
        assertEquals("SubProject 2", result.get(1).getName());
        verify(delProjektRepository, times(1)).findByProjektId(projectId);
    }

    @Test
    @DisplayName("Should get subproject by ID")
    void testGetById_Success() {
        // Arrange
        DelProjekt delProjekt = createTestDelProjekt(1, 1, "SubProject 1", "Description");
        when(delProjektRepository.findById(1)).thenReturn(delProjekt);

        // Act
        DelProjekt result = delProjektService.getById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getDelProjektId());
        assertEquals("SubProject 1", result.getName());
        verify(delProjektRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should create subproject successfully")
    void testCreate_Success() {
        // Arrange
        DelProjekt delProjekt = createTestDelProjekt(0, 1, "New SubProject", "New Description");

        // Act
        delProjektService.create(delProjekt);

        // Assert
        verify(delProjektRepository, times(1)).create(delProjekt);
    }

    @Test
    @DisplayName("Should throw exception when creating subproject with null")
    void testCreate_Null() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            delProjektService.create(null);
        });
        assertEquals("DelProjekt cannot be null", exception.getMessage());
        verify(delProjektRepository, never()).create(any());
    }

    @Test
    @DisplayName("Should throw exception when creating subproject with invalid project ID")
    void testCreate_InvalidProjectId() {
        // Arrange
        DelProjekt delProjekt = createTestDelProjekt(0, 0, "SubProject", "Description");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            delProjektService.create(delProjekt);
        });
        assertEquals("Project ID must be valid", exception.getMessage());
        verify(delProjektRepository, never()).create(any());
    }

    @Test
    @DisplayName("Should throw exception when creating subproject with empty name")
    void testCreate_EmptyName() {
        // Arrange
        DelProjekt delProjekt = createTestDelProjekt(0, 1, "", "Description");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            delProjektService.create(delProjekt);
        });
        assertEquals("Name cannot be empty", exception.getMessage());
        verify(delProjektRepository, never()).create(any());
    }

    @Test
    @DisplayName("Should throw exception when creating subproject with blank name")
    void testCreate_BlankName() {
        // Arrange
        DelProjekt delProjekt = createTestDelProjekt(0, 1, "   ", "Description");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            delProjektService.create(delProjekt);
        });
        assertEquals("Name cannot be empty", exception.getMessage());
        verify(delProjektRepository, never()).create(any());
    }

    @Test
    @DisplayName("Should update subproject successfully")
    void testUpdate_Success() {
        // Arrange
        DelProjekt delProjekt = createTestDelProjekt(1, 1, "Updated SubProject", "Updated Description");

        // Act
        delProjektService.update(delProjekt);

        // Assert
        verify(delProjektRepository, times(1)).update(delProjekt);
    }

    @Test
    @DisplayName("Should throw exception when updating subproject with invalid ID")
    void testUpdate_InvalidId() {
        // Arrange
        DelProjekt delProjekt = createTestDelProjekt(0, 1, "SubProject", "Description");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            delProjektService.update(delProjekt);
        });
        assertEquals("DelProjekt ID must be valid", exception.getMessage());
        verify(delProjektRepository, never()).update(any());
    }

    @Test
    @DisplayName("Should delete subproject successfully")
    void testDelete_Success() {
        // Act
        delProjektService.delete(1);

        // Assert
        verify(delProjektRepository, times(1)).delete(1);
    }

    @Test
    @DisplayName("Should throw exception when deleting subproject with invalid ID")
    void testDelete_InvalidId() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            delProjektService.delete(0);
        });
        assertEquals("DelProjekt ID must be valid", exception.getMessage());
        verify(delProjektRepository, never()).delete(anyInt());
    }

    // Helper method to create test DelProjekt
    private DelProjekt createTestDelProjekt(int id, int projectId, String name, String description) {
        DelProjekt delProjekt = new DelProjekt();
        delProjekt.setDelProjektId(id);
        delProjekt.setProjectId(projectId);
        delProjekt.setName(name);
        delProjekt.setDescription(description);
        return delProjekt;
    }
}
