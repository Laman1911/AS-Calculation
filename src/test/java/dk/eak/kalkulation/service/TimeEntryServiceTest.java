package dk.eak.kalkulation.service;

import dk.eak.kalkulation.model.TimeEntry;
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
 * Unit tests for TimeEntryService.
 * Tests time tracking business logic.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TimeEntryService Unit Tests")
class TimeEntryServiceTest {

    @Mock
    private TimeEntryRepository timeEntryRepository;

    private TimeEntryService timeEntryService;

    @BeforeEach
    void setUp() {
        timeEntryService = new TimeEntryService(timeEntryRepository);
    }

    @Test
    @DisplayName("Should get all time entries for a task")
    void testGetByOpgaveId_Success() {
        // Arrange
        int opgaveId = 1;
        TimeEntry te1 = createTestTimeEntry(1, opgaveId, LocalDate.now().minusDays(2), 5);
        TimeEntry te2 = createTestTimeEntry(2, opgaveId, LocalDate.now().minusDays(1), 6);
        List<TimeEntry> timeEntries = Arrays.asList(te1, te2);
        when(timeEntryRepository.findByOpgaveId(opgaveId)).thenReturn(timeEntries);

        // Act
        List<TimeEntry> result = timeEntryService.getByOpgaveId(opgaveId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(5, result.get(0).getHours());
        assertEquals(6, result.get(1).getHours());
        verify(timeEntryRepository, times(1)).findByOpgaveId(opgaveId);
    }

    @Test
    @DisplayName("Should throw exception when getting time entries with invalid opgave ID")
    void testGetByOpgaveId_InvalidId() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            timeEntryService.getByOpgaveId(0);
        });
        assertEquals("Opgave ID must be valid", exception.getMessage());
        verify(timeEntryRepository, never()).findByOpgaveId(anyInt());
    }

    @Test
    @DisplayName("Should get time entry by ID")
    void testGetById_Success() {
        // Arrange
        TimeEntry timeEntry = createTestTimeEntry(1, 1, LocalDate.now().minusDays(1), 5);
        when(timeEntryRepository.findById(1)).thenReturn(timeEntry);

        // Act
        TimeEntry result = timeEntryService.getById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTimeEntryId());
        assertEquals(5, result.getHours());
        verify(timeEntryRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should throw exception when getting time entry with invalid ID")
    void testGetById_InvalidId() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            timeEntryService.getById(0);
        });
        assertEquals("Time Entry ID must be valid", exception.getMessage());
        verify(timeEntryRepository, never()).findById(anyInt());
    }

    @Test
    @DisplayName("Should get total hours by project")
    void testGetTotalHoursByProject_Success() {
        // Arrange
        when(timeEntryRepository.sumHoursByProjektId(1)).thenReturn(25);

        // Act
        int result = timeEntryService.getTotalHoursByProject(1);

        // Assert
        assertEquals(25, result);
        verify(timeEntryRepository, times(1)).sumHoursByProjektId(1);
    }

    @Test
    @DisplayName("Should throw exception when getting total hours with invalid project ID")
    void testGetTotalHoursByProject_InvalidId() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            timeEntryService.getTotalHoursByProject(0);
        });
        assertEquals("Projekt ID must be valid", exception.getMessage());
        verify(timeEntryRepository, never()).sumHoursByProjektId(anyInt());
    }

    @Test
    @DisplayName("Should create time entry successfully")
    void testCreate_Success() {
        // Arrange
        TimeEntry timeEntry = createTestTimeEntry(0, 1, LocalDate.now().minusDays(1), 5);

        // Act
        timeEntryService.create(timeEntry);

        // Assert
        verify(timeEntryRepository, times(1)).create(timeEntry);
    }

    @Test
    @DisplayName("Should throw exception when creating time entry with null")
    void testCreate_Null() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            timeEntryService.create(null);
        });
        assertEquals("TimeEntry cannot be null", exception.getMessage());
        verify(timeEntryRepository, never()).create(any());
    }

    @Test
    @DisplayName("Should throw exception when creating time entry with invalid opgave ID")
    void testCreate_InvalidOpgaveId() {
        // Arrange
        TimeEntry timeEntry = createTestTimeEntry(0, 0, LocalDate.now().minusDays(1), 5);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            timeEntryService.create(timeEntry);
        });
        assertEquals("Opgave ID must be valid", exception.getMessage());
        verify(timeEntryRepository, never()).create(any());
    }

    @Test
    @DisplayName("Should throw exception when creating time entry with null work date")
    void testCreate_NullWorkDate() {
        // Arrange
        TimeEntry timeEntry = createTestTimeEntry(0, 1, null, 5);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            timeEntryService.create(timeEntry);
        });
        assertEquals("Work date cannot be null", exception.getMessage());
        verify(timeEntryRepository, never()).create(any());
    }

    @Test
    @DisplayName("Should throw exception when creating time entry with future work date")
    void testCreate_FutureWorkDate() {
        // Arrange
        TimeEntry timeEntry = createTestTimeEntry(0, 1, LocalDate.now().plusDays(1), 5);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            timeEntryService.create(timeEntry);
        });
        assertEquals("Work date cannot be in the future", exception.getMessage());
        verify(timeEntryRepository, never()).create(any());
    }

    @Test
    @DisplayName("Should throw exception when creating time entry with zero hours")
    void testCreate_ZeroHours() {
        // Arrange
        TimeEntry timeEntry = createTestTimeEntry(0, 1, LocalDate.now().minusDays(1), 0);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            timeEntryService.create(timeEntry);
        });
        assertEquals("Hours must be greater than 0", exception.getMessage());
        verify(timeEntryRepository, never()).create(any());
    }

    @Test
    @DisplayName("Should throw exception when creating time entry with negative hours")
    void testCreate_NegativeHours() {
        // Arrange
        TimeEntry timeEntry = createTestTimeEntry(0, 1, LocalDate.now().minusDays(1), -5);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            timeEntryService.create(timeEntry);
        });
        assertEquals("Hours must be greater than 0", exception.getMessage());
        verify(timeEntryRepository, never()).create(any());
    }

    @Test
    @DisplayName("Should throw exception when creating time entry with hours exceeding 24")
    void testCreate_ExcessiveHours() {
        // Arrange
        TimeEntry timeEntry = createTestTimeEntry(0, 1, LocalDate.now().minusDays(1), 25);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            timeEntryService.create(timeEntry);
        });
        assertEquals("Hours cannot exceed 24 per day", exception.getMessage());
        verify(timeEntryRepository, never()).create(any());
    }

    @Test
    @DisplayName("Should update time entry successfully")
    void testUpdate_Success() {
        // Arrange
        TimeEntry timeEntry = createTestTimeEntry(1, 1, LocalDate.now().minusDays(1), 7);

        // Act
        timeEntryService.update(timeEntry);

        // Assert
        verify(timeEntryRepository, times(1)).update(timeEntry);
    }

    @Test
    @DisplayName("Should throw exception when updating time entry with invalid ID")
    void testUpdate_InvalidId() {
        // Arrange
        TimeEntry timeEntry = createTestTimeEntry(0, 1, LocalDate.now().minusDays(1), 5);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            timeEntryService.update(timeEntry);
        });
        assertEquals("Time Entry ID must be valid", exception.getMessage());
        verify(timeEntryRepository, never()).update(any());
    }

    @Test
    @DisplayName("Should delete time entry successfully")
    void testDelete_Success() {
        // Act
        timeEntryService.delete(1);

        // Assert
        verify(timeEntryRepository, times(1)).delete(1);
    }

    @Test
    @DisplayName("Should throw exception when deleting time entry with invalid ID")
    void testDelete_InvalidId() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            timeEntryService.delete(0);
        });
        assertEquals("Time Entry ID must be valid", exception.getMessage());
        verify(timeEntryRepository, never()).delete(anyInt());
    }

    // Helper method to create test TimeEntry
    private TimeEntry createTestTimeEntry(int id, int opgaveId, LocalDate workDate, int hours) {
        TimeEntry timeEntry = new TimeEntry();
        timeEntry.setTimeEntryId(id);
        timeEntry.setOpgaveId(opgaveId);
        timeEntry.setWorkDate(workDate);
        timeEntry.setHours(hours);
        return timeEntry;
    }
}
