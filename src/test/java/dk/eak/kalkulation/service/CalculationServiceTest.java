package dk.eak.kalkulation.service;

import dk.eak.kalkulation.model.Opgave;
import dk.eak.kalkulation.model.Projekt;
import dk.eak.kalkulation.repository.OpgaveRepository;
import dk.eak.kalkulation.repository.TimeEntryRepository;
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
 * Unit tests for CalculationService.
 * Tests project calculations and metrics.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CalculationService Unit Tests")
class CalculationServiceTest {

    @Mock
    private OpgaveRepository opgaveRepository;

    @Mock
    private TimeEntryRepository timeEntryRepository;

    private CalculationService calculationService;

    @BeforeEach
    void setUp() {
        calculationService = new CalculationService(opgaveRepository, timeEntryRepository);
    }

    @Test
    @DisplayName("Should calculate total estimated hours")
    void testTotalEstimatedHours_Success() {
        // Arrange
        int projectId = 1;
        Opgave opgave1 = createTestOpgave(1, 5);
        Opgave opgave2 = createTestOpgave(2, 10);
        Opgave opgave3 = createTestOpgave(3, null); // null hours
        List<Opgave> opgaver = Arrays.asList(opgave1, opgave2, opgave3);
        when(opgaveRepository.findByProjectId(projectId)).thenReturn(opgaver);

        // Act
        int result = calculationService.totalEstimatedHours(projectId);

        // Assert
        assertEquals(15, result);
        verify(opgaveRepository, times(1)).findByProjectId(projectId);
    }

    @Test
    @DisplayName("Should throw exception when calculating estimated hours with invalid project ID")
    void testTotalEstimatedHours_InvalidId() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculationService.totalEstimatedHours(0);
        });
        assertEquals("Project ID must be valid", exception.getMessage());
        verify(opgaveRepository, never()).findByProjectId(anyInt());
    }

    @Test
    @DisplayName("Should calculate total registered hours")
    void testTotalRegisteredHours_Success() {
        // Arrange
        int projectId = 1;
        when(timeEntryRepository.sumHoursByProjektId(projectId)).thenReturn(20);

        // Act
        int result = calculationService.totalRegisteredHours(projectId);

        // Assert
        assertEquals(20, result);
        verify(timeEntryRepository, times(1)).sumHoursByProjektId(projectId);
    }

    @Test
    @DisplayName("Should throw exception when calculating registered hours with invalid project ID")
    void testTotalRegisteredHours_InvalidId() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculationService.totalRegisteredHours(0);
        });
        assertEquals("Project ID must be valid", exception.getMessage());
        verify(timeEntryRepository, never()).sumHoursByProjektId(anyInt());
    }

    @Test
    @DisplayName("Should calculate remaining estimated hours")
    void testRemainingEstimatedHours_Success() {
        // Arrange
        int projectId = 1;
        Opgave opgave = createTestOpgave(1, 30);
        when(opgaveRepository.findByProjectId(projectId)).thenReturn(Arrays.asList(opgave));
        when(timeEntryRepository.sumHoursByProjektId(projectId)).thenReturn(10);

        // Act
        int result = calculationService.remainingEstimatedHours(projectId);

        // Assert
        assertEquals(20, result);
    }

    @Test
    @DisplayName("Should return zero when registered hours exceed estimated")
    void testRemainingEstimatedHours_Exceeded() {
        // Arrange
        int projectId = 1;
        Opgave opgave = createTestOpgave(1, 30);
        when(opgaveRepository.findByProjectId(projectId)).thenReturn(Arrays.asList(opgave));
        when(timeEntryRepository.sumHoursByProjektId(projectId)).thenReturn(40);

        // Act
        int result = calculationService.remainingEstimatedHours(projectId);

        // Assert
        assertEquals(0, result);
    }

    @Test
    @DisplayName("Should calculate working days between dates")
    void testWorkingDaysBetween_Success() {
        // Arrange - Feb 2-6, 2026 (Mon-Fri)
        LocalDate start = LocalDate.of(2026, 2, 2);
        LocalDate end = LocalDate.of(2026, 2, 6);

        // Act
        int result = calculationService.workingDaysBetween(start, end);

        // Assert
        assertEquals(5, result); // Monday to Friday
    }

    @Test
    @DisplayName("Should exclude weekends from working days calculation")
    void testWorkingDaysBetween_WithWeekends() {
        // Arrange - Feb 3-9, 2026 (Mon-Sun)
        LocalDate start = LocalDate.of(2026, 2, 3);
        LocalDate end = LocalDate.of(2026, 2, 9);

        // Act
        int result = calculationService.workingDaysBetween(start, end);

        // Assert
        assertEquals(5, result); // Excludes Saturday and Sunday
    }

    @Test
    @DisplayName("Should return zero when start date is null")
    void testWorkingDaysBetween_NullStart() {
        // Act
        int result = calculationService.workingDaysBetween(null, LocalDate.now());

        // Assert
        assertEquals(0, result);
    }

    @Test
    @DisplayName("Should return zero when end date is null")
    void testWorkingDaysBetween_NullEnd() {
        // Act
        int result = calculationService.workingDaysBetween(LocalDate.now(), null);

        // Assert
        assertEquals(0, result);
    }

    @Test
    @DisplayName("Should return zero when end date is before start date")
    void testWorkingDaysBetween_InvalidRange() {
        // Arrange
        LocalDate start = LocalDate.of(2026, 2, 10);
        LocalDate end = LocalDate.of(2026, 2, 5);

        // Act
        int result = calculationService.workingDaysBetween(start, end);

        // Assert
        assertEquals(0, result);
    }

    @Test
    @DisplayName("Should calculate required hours per workday")
    void testRequiredHoursPerWorkday_Success() {
        // Arrange
        Projekt projekt = new Projekt();
        projekt.setProjectId(1);
        projekt.setStartDate(LocalDate.of(2026, 2, 2)); // Monday
        projekt.setEndDate(LocalDate.of(2026, 2, 6));   // Friday (5 working days)

        Opgave opgave = createTestOpgave(1, 50);
        when(opgaveRepository.findByProjectId(1)).thenReturn(Arrays.asList(opgave));
        when(timeEntryRepository.sumHoursByProjektId(1)).thenReturn(0);

        // Act
        double result = calculationService.requiredHoursPerWorkday(projekt);

        // Assert
        assertEquals(10.0, result); // 50 hours / 5 working days
    }

    @Test
    @DisplayName("Should return zero when project is null")
    void testRequiredHoursPerWorkday_NullProject() {
        // Act
        double result = calculationService.requiredHoursPerWorkday(null);

        // Assert
        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("Should return zero when start date is null")
    void testRequiredHoursPerWorkday_NullStartDate() {
        // Arrange
        Projekt projekt = new Projekt();
        projekt.setProjectId(1);
        projekt.setStartDate(null);
        projekt.setEndDate(LocalDate.now());

        // Act
        double result = calculationService.requiredHoursPerWorkday(projekt);

        // Assert
        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("Should calculate progress percentage")
    void testGetProgressPercentage_Success() {
        // Arrange
        int projectId = 1;
        Opgave opgave = createTestOpgave(1, 100);
        when(opgaveRepository.findByProjectId(projectId)).thenReturn(Arrays.asList(opgave));
        when(timeEntryRepository.sumHoursByProjektId(projectId)).thenReturn(50);

        // Act
        double result = calculationService.getProgressPercentage(projectId);

        // Assert
        assertEquals(50.0, result);
    }

    @Test
    @DisplayName("Should return 0% when no estimated hours")
    void testGetProgressPercentage_NoEstimatedHours() {
        // Arrange
        int projectId = 1;
        when(opgaveRepository.findByProjectId(projectId)).thenReturn(Arrays.asList());

        // Act
        double result = calculationService.getProgressPercentage(projectId);

        // Assert
        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("Should cap progress percentage at 100%")
    void testGetProgressPercentage_Over100Percent() {
        // Arrange
        int projectId = 1;
        Opgave opgave = createTestOpgave(1, 50);
        when(opgaveRepository.findByProjectId(projectId)).thenReturn(Arrays.asList(opgave));
        when(timeEntryRepository.sumHoursByProjektId(projectId)).thenReturn(100);

        // Act
        double result = calculationService.getProgressPercentage(projectId);

        // Assert
        assertEquals(100.0, result);
    }

    @Test
    @DisplayName("Should throw exception when calculating progress with invalid project ID")
    void testGetProgressPercentage_InvalidId() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculationService.getProgressPercentage(0);
        });
        assertEquals("Project ID must be valid", exception.getMessage());
        verify(opgaveRepository, never()).findByProjectId(anyInt());
    }

    // Helper method to create test Opgave
    private Opgave createTestOpgave(int id, Integer estimatedHours) {
        Opgave opgave = new Opgave();
        opgave.setOpgaveId(id);
        opgave.setProject_id(1);
        opgave.setName("Task " + id);
        opgave.setEstimatedHours(estimatedHours);
        return opgave;
    }
}
