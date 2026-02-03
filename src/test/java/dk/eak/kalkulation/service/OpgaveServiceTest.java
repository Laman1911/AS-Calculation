package dk.eak.kalkulation.service;

import dk.eak.kalkulation.model.Opgave;
import dk.eak.kalkulation.repository.OpgaveRepository;
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
 * Unit tests for OpgaveService.
 * Tests task management business logic.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("OpgaveService Unit Tests")
class OpgaveServiceTest {

    @Mock
    private OpgaveRepository opgaveRepository;

    private OpgaveService opgaveService;

    @BeforeEach
    void setUp() {
        opgaveService = new OpgaveService(opgaveRepository);
    }

    @Test
    @DisplayName("Should get all tasks for a project")
    void testGetByProjectId_Success() {
        // Arrange
        int projectId = 1;
        Opgave opgave1 = createTestOpgave(1, projectId, "Task 1", "Description 1", 5);
        Opgave opgave2 = createTestOpgave(2, projectId, "Task 2", "Description 2", 10);
        List<Opgave> opgaver = Arrays.asList(opgave1, opgave2);
        when(opgaveRepository.findByProjectId(projectId)).thenReturn(opgaver);

        // Act
        List<Opgave> result = opgaveService.getByProjectId(projectId);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getName());
        assertEquals("Task 2", result.get(1).getName());
        verify(opgaveRepository, times(1)).findByProjectId(projectId);
    }

    @Test
    @DisplayName("Should throw exception when getting tasks with invalid project ID")
    void testGetByProjectId_InvalidId() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            opgaveService.getByProjectId(0);
        });
        assertEquals("Project ID must be valid", exception.getMessage());
        verify(opgaveRepository, never()).findByProjectId(anyInt());
    }

    @Test
    @DisplayName("Should get task by ID")
    void testGetById_Success() {
        // Arrange
        Opgave opgave = createTestOpgave(1, 1, "Task 1", "Description", 5);
        when(opgaveRepository.findById(1)).thenReturn(opgave);

        // Act
        Opgave result = opgaveService.getById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getOpgaveId());
        assertEquals("Task 1", result.getName());
        verify(opgaveRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should throw exception when getting task with invalid ID")
    void testGetById_InvalidId() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            opgaveService.getById(0);
        });
        assertEquals("Opgave ID must be valid", exception.getMessage());
        verify(opgaveRepository, never()).findById(anyInt());
    }

    @Test
    @DisplayName("Should get tasks for a subproject")
    void testGetByDelProjektId_Success() {
        // Arrange
        int delProjektId = 1;
        Opgave opgave = createTestOpgave(1, 1, "Task", "Description", 5);
        opgave.setDelProjektId(delProjektId);
        List<Opgave> opgaver = Arrays.asList(opgave);
        when(opgaveRepository.findByDelProjektId(delProjektId)).thenReturn(opgaver);

        // Act
        List<Opgave> result = opgaveService.getByDelProjektId(delProjektId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(delProjektId, result.get(0).getDelProjektId());
        verify(opgaveRepository, times(1)).findByDelProjektId(delProjektId);
    }

    @Test
    @DisplayName("Should create task successfully")
    void testCreate_Success() {
        // Arrange
        Opgave opgave = createTestOpgave(0, 1, "New Task", "New Description", 5);

        // Act
        opgaveService.create(opgave);

        // Assert
        verify(opgaveRepository, times(1)).create(opgave);
    }

    @Test
    @DisplayName("Should throw exception when creating task with null")
    void testCreate_Null() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            opgaveService.create(null);
        });
        assertEquals("Opgave cannot be null", exception.getMessage());
        verify(opgaveRepository, never()).create(any());
    }

    @Test
    @DisplayName("Should throw exception when creating task with invalid project ID")
    void testCreate_InvalidProjectId() {
        // Arrange
        Opgave opgave = createTestOpgave(0, 0, "Task", "Description", 5);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            opgaveService.create(opgave);
        });
        assertEquals("Project ID must be valid", exception.getMessage());
        verify(opgaveRepository, never()).create(any());
    }

    @Test
    @DisplayName("Should throw exception when creating task with empty name")
    void testCreate_EmptyName() {
        // Arrange
        Opgave opgave = createTestOpgave(0, 1, "", "Description", 5);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            opgaveService.create(opgave);
        });
        assertEquals("Task name cannot be empty", exception.getMessage());
        verify(opgaveRepository, never()).create(any());
    }

    @Test
    @DisplayName("Should throw exception when creating task with negative estimated hours")
    void testCreate_NegativeEstimatedHours() {
        // Arrange
        Opgave opgave = createTestOpgave(0, 1, "Task", "Description", -5);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            opgaveService.create(opgave);
        });
        assertEquals("Estimated hours must be non-negative", exception.getMessage());
        verify(opgaveRepository, never()).create(any());
    }

    @Test
    @DisplayName("Should update task successfully")
    void testUpdate_Success() {
        // Arrange
        Opgave opgave = createTestOpgave(1, 1, "Updated Task", "Updated Description", 8);

        // Act
        opgaveService.update(opgave);

        // Assert
        verify(opgaveRepository, times(1)).update(opgave);
    }

    @Test
    @DisplayName("Should throw exception when updating task with invalid ID")
    void testUpdate_InvalidId() {
        // Arrange
        Opgave opgave = createTestOpgave(0, 1, "Task", "Description", 5);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            opgaveService.update(opgave);
        });
        assertEquals("Opgave ID must be valid", exception.getMessage());
        verify(opgaveRepository, never()).update(any());
    }

    @Test
    @DisplayName("Should delete task successfully")
    void testDelete_Success() {
        // Act
        opgaveService.delete(1);

        // Assert
        verify(opgaveRepository, times(1)).delete(1);
    }

    @Test
    @DisplayName("Should throw exception when deleting task with invalid ID")
    void testDelete_InvalidId() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            opgaveService.delete(0);
        });
        assertEquals("Opgave ID must be valid", exception.getMessage());
        verify(opgaveRepository, never()).delete(anyInt());
    }

    // Helper method to create test Opgave
    private Opgave createTestOpgave(int id, int projectId, String name, String description, int estimatedHours) {
        Opgave opgave = new Opgave();
        opgave.setOpgaveId(id);
        opgave.setProject_id(projectId);
        opgave.setName(name);
        opgave.setDescription(description);
        opgave.setEstimatedHours(estimatedHours);
        opgave.setDeadline(LocalDate.now().plusDays(5));
        return opgave;
    }
}
