package dk.eak.kalkulation.repository;

import dk.eak.kalkulation.model.TimeEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for TimeEntryRepository.
 * Tests database operations for time entries with H2 in-memory database.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("TimeEntryRepository Integration Tests")
class TimeEntryRepositoryIT {

    @Autowired
    private TimeEntryRepository timeEntryRepository;

    @Test
    @DisplayName("Should retrieve all time entries for a task")
    void testFindByOpgaveId_Success() {
        // Act
        List<TimeEntry> timeEntries = timeEntryRepository.findByOpgaveId(1);

        // Assert
        assertNotNull(timeEntries);
        assertEquals(2, timeEntries.size());
        assertTrue(timeEntries.stream().allMatch(te -> te.getOpgaveId() == 1));
    }

    @Test
    @DisplayName("Should retrieve time entry by ID")
    void testFindById_Success() {
        // Act
        TimeEntry timeEntry = timeEntryRepository.findById(1);

        // Assert
        assertNotNull(timeEntry);
        assertEquals(1, timeEntry.getTimeEntryId());
        assertEquals(1, timeEntry.getOpgaveId());
        assertEquals(LocalDate.of(2026, 2, 3), timeEntry.getWorkDate());
        assertEquals(3, timeEntry.getHours());
    }

    @Test
    @DisplayName("Should sum hours by project ID")
    void testSumHoursByProjektId_Success() {
        // Act
        int totalHours = timeEntryRepository.sumHoursByProjektId(1);

        // Assert
        assertEquals(20, totalHours); // 3+2+5+8+2 = 20 hours for project 1
    }

    @Test
    @DisplayName("Should return zero when summing hours for project with no time entries")
    void testSumHoursByProjektId_NoEntries() {
        // Act
        int totalHours = timeEntryRepository.sumHoursByProjektId(999);

        // Assert
        assertEquals(0, totalHours);
    }

    @Test
    @DisplayName("Should create new time entry in database")
    void testCreate_Success() {
        // Arrange
        TimeEntry newTimeEntry = new TimeEntry();
        newTimeEntry.setOpgaveId(1);
        newTimeEntry.setWorkDate(LocalDate.of(2026, 2, 6));
        newTimeEntry.setHours(4);

        // Act
        timeEntryRepository.create(newTimeEntry);

        // Assert
        List<TimeEntry> allTimeEntries = timeEntryRepository.findByOpgaveId(1);
        assertTrue(allTimeEntries.stream().anyMatch(te -> 
            te.getWorkDate().equals(LocalDate.of(2026, 2, 6)) && te.getHours() == 4
        ));
    }

    @Test
    @DisplayName("Should create multiple time entries for same task")
    void testCreate_MultipleEntriesForSameTask() {
        // Arrange
        int opgaveId = 2;
        int initialCount = timeEntryRepository.findByOpgaveId(opgaveId).size();

        TimeEntry te1 = new TimeEntry();
        te1.setOpgaveId(opgaveId);
        te1.setWorkDate(LocalDate.of(2026, 2, 6));
        te1.setHours(3);

        TimeEntry te2 = new TimeEntry();
        te2.setOpgaveId(opgaveId);
        te2.setWorkDate(LocalDate.of(2026, 2, 7));
        te2.setHours(4);

        // Act
        timeEntryRepository.create(te1);
        timeEntryRepository.create(te2);

        // Assert
        List<TimeEntry> allTimeEntries = timeEntryRepository.findByOpgaveId(opgaveId);
        assertEquals(initialCount + 2, allTimeEntries.size());
    }

    @Test
    @DisplayName("Should update existing time entry")
    void testUpdate_Success() {
        // Arrange
        TimeEntry timeEntry = timeEntryRepository.findById(1);
        timeEntry.setHours(6); // Changed from 3 to 6
        timeEntry.setWorkDate(LocalDate.of(2026, 2, 4));

        // Act
        timeEntryRepository.update(timeEntry);

        // Assert
        TimeEntry updated = timeEntryRepository.findById(1);
        assertEquals(6, updated.getHours());
        assertEquals(LocalDate.of(2026, 2, 4), updated.getWorkDate());
    }

    @Test
    @DisplayName("Should delete time entry from database")
    void testDelete_Success() {
        // Arrange
        int opgaveId = 1;
        int initialCount = timeEntryRepository.findByOpgaveId(opgaveId).size();

        // Act
        timeEntryRepository.delete(1);

        // Assert
        List<TimeEntry> remainingTimeEntries = timeEntryRepository.findByOpgaveId(opgaveId);
        assertEquals(initialCount - 1, remainingTimeEntries.size());
        assertFalse(remainingTimeEntries.stream().anyMatch(te -> te.getTimeEntryId() == 1));
    }

    @Test
    @DisplayName("Should maintain hour calculations after creating new entry")
    void testSumHours_AfterCreate() {
        // Arrange
        int projektId = 1;
        int initialSum = timeEntryRepository.sumHoursByProjektId(projektId);

        TimeEntry newTimeEntry = new TimeEntry();
        newTimeEntry.setOpgaveId(4); // Task in project 1
        newTimeEntry.setWorkDate(LocalDate.of(2026, 2, 7));
        newTimeEntry.setHours(5);

        // Act
        timeEntryRepository.create(newTimeEntry);

        // Assert
        int newSum = timeEntryRepository.sumHoursByProjektId(projektId);
        assertEquals(initialSum + 5, newSum);
    }

    @Test
    @DisplayName("Should retrieve time entries ordered by date then ID")
    void testFindByOpgaveId_Ordering() {
        // Act
        List<TimeEntry> timeEntries = timeEntryRepository.findByOpgaveId(1);

        // Assert
        for (int i = 0; i < timeEntries.size() - 1; i++) {
            LocalDate currentDate = timeEntries.get(i).getWorkDate();
            LocalDate nextDate = timeEntries.get(i + 1).getWorkDate();
            int comparison = currentDate.compareTo(nextDate);
            if (comparison == 0) {
                // If dates are equal, check ID ordering
                assertTrue(timeEntries.get(i).getTimeEntryId() <= timeEntries.get(i + 1).getTimeEntryId());
            } else {
                assertTrue(comparison <= 0);
            }
        }
    }

    @Test
    @DisplayName("Should handle maximum hour values")
    void testCreate_WithMaximumHours() {
        // Arrange
        TimeEntry newTimeEntry = new TimeEntry();
        newTimeEntry.setOpgaveId(3);
        newTimeEntry.setWorkDate(LocalDate.of(2026, 2, 8));
        newTimeEntry.setHours(24); // Maximum hours in a day

        // Act
        timeEntryRepository.create(newTimeEntry);

        // Assert
        List<TimeEntry> allTimeEntries = timeEntryRepository.findByOpgaveId(3);
        TimeEntry created = allTimeEntries.stream()
            .filter(te -> te.getWorkDate().equals(LocalDate.of(2026, 2, 8)))
            .findFirst()
            .orElse(null);
        assertNotNull(created);
        assertEquals(24, created.getHours());
    }

    @Test
    @DisplayName("Should handle minimum hour value")
    void testCreate_WithMinimumHours() {
        // Arrange
        TimeEntry newTimeEntry = new TimeEntry();
        newTimeEntry.setOpgaveId(2);
        newTimeEntry.setWorkDate(LocalDate.of(2026, 2, 9));
        newTimeEntry.setHours(1); // Minimum hours

        // Act
        timeEntryRepository.create(newTimeEntry);

        // Assert
        List<TimeEntry> allTimeEntries = timeEntryRepository.findByOpgaveId(2);
        TimeEntry created = allTimeEntries.stream()
            .filter(te -> te.getWorkDate().equals(LocalDate.of(2026, 2, 9)))
            .findFirst()
            .orElse(null);
        assertNotNull(created);
        assertEquals(1, created.getHours());
    }

    @Test
    @DisplayName("Should calculate correct sum across multiple tasks")
    void testSumHoursByProjektId_MultipleTasksAndDates() {
        // Act - Project 1 has tasks 1-4
        // Task 1: 3 + 2 = 5 hours
        // Task 2: 5 hours
        // Task 3: 8 hours
        // Task 4: 2 hours
        // Total: 20 hours
        int totalHours = timeEntryRepository.sumHoursByProjektId(1);

        // Assert
        assertEquals(20, totalHours);
    }

    @Test
    @DisplayName("Should maintain data integrity across CRUD operations")
    void testDataIntegrity_CRUDOperations() {
        // Arrange
        int opgaveId = 5;
        List<TimeEntry> beforeCreate = timeEntryRepository.findByOpgaveId(opgaveId);

        // Create
        TimeEntry newTimeEntry = new TimeEntry();
        newTimeEntry.setOpgaveId(opgaveId);
        newTimeEntry.setWorkDate(LocalDate.of(2026, 3, 4));
        newTimeEntry.setHours(7);
        timeEntryRepository.create(newTimeEntry);

        // Find the created entry
        List<TimeEntry> afterCreate = timeEntryRepository.findByOpgaveId(opgaveId);
        TimeEntry created = afterCreate.stream()
            .filter(te -> te.getWorkDate().equals(LocalDate.of(2026, 3, 4)))
            .findFirst()
            .orElse(null);
        assertNotNull(created);

        // Update
        int createdId = created.getTimeEntryId();
        created.setHours(8);
        timeEntryRepository.update(created);

        // Assert updated
        TimeEntry updated = timeEntryRepository.findById(createdId);
        assertEquals(8, updated.getHours());

        // Delete
        timeEntryRepository.delete(createdId);

        // Assert deleted
        List<TimeEntry> afterDelete = timeEntryRepository.findByOpgaveId(opgaveId);
        assertEquals(beforeCreate.size(), afterDelete.size());
    }

    @Test
    @DisplayName("Should update task reference in time entry")
    void testUpdate_TaskReference() {
        // Arrange
        TimeEntry timeEntry = timeEntryRepository.findById(1);
        int originalOpgaveId = timeEntry.getOpgaveId();
        timeEntry.setOpgaveId(2); // Change task reference

        // Act
        timeEntryRepository.update(timeEntry);

        // Assert
        TimeEntry updated = timeEntryRepository.findById(1);
        assertEquals(2, updated.getOpgaveId());
        
        // Verify it's no longer in the original task's list
        List<TimeEntry> originalTaskEntries = timeEntryRepository.findByOpgaveId(originalOpgaveId);
        assertFalse(originalTaskEntries.stream().anyMatch(te -> te.getTimeEntryId() == 1));
    }
}
