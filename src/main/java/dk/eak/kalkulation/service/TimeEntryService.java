package dk.eak.kalkulation.service;

import dk.eak.kalkulation.model.TimeEntry;
import dk.eak.kalkulation.repository.TimeEntryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service layer for TimeEntry operations.
 * Handles business logic for time tracking and entry management.
 */
@Service
public class TimeEntryService {

    private final TimeEntryRepository repository;

    public TimeEntryService(TimeEntryRepository repository) {
        this.repository = repository;
    }

    /**
     * Get all time entries for a given task.
     *
     * @param opgaveId the task ID
     * @return list of time entries
     */
    public List<TimeEntry> getByOpgaveId(int opgaveId) {
        if (opgaveId <= 0) {
            throw new IllegalArgumentException("Opgave ID must be valid");
        }
        return repository.findByOpgaveId(opgaveId);
    }

    /**
     * Get a time entry by ID.
     *
     * @param timeEntryId the time entry ID
     * @return the time entry
     */
    public TimeEntry getById(int timeEntryId) {
        if (timeEntryId <= 0) {
            throw new IllegalArgumentException("Time Entry ID must be valid");
        }
        return repository.findById(timeEntryId);
    }

    /**
     * Get total hours logged for a project.
     *
     * @param projektId the project ID
     * @return total hours
     */
    public int getTotalHoursByProject(int projektId) {
        if (projektId <= 0) {
            throw new IllegalArgumentException("Projekt ID must be valid");
        }
        return repository.sumHoursByProjektId(projektId);
    }

    /**
     * Create a new time entry.
     *
     * @param timeEntry the time entry to create
     */
    public void create(TimeEntry timeEntry) {
        validateTimeEntry(timeEntry);
        repository.create(timeEntry);
    }

    /**
     * Update an existing time entry.
     *
     * @param timeEntry the time entry to update
     */
    public void update(TimeEntry timeEntry) {
        validateTimeEntry(timeEntry);
        if (timeEntry.getTimeEntryId() == null || timeEntry.getTimeEntryId() <= 0) {
            throw new IllegalArgumentException("Time Entry ID must be valid");
        }
        repository.update(timeEntry);
    }

    /**
     * Delete a time entry by ID.
     *
     * @param timeEntryId the time entry ID to delete
     */
    public void delete(int timeEntryId) {
        if (timeEntryId <= 0) {
            throw new IllegalArgumentException("Time Entry ID must be valid");
        }
        repository.delete(timeEntryId);
    }

    /**
     * Validate time entry fields.
     *
     * @param timeEntry the time entry to validate
     */
    private void validateTimeEntry(TimeEntry timeEntry) {
        if (timeEntry == null) {
            throw new IllegalArgumentException("TimeEntry cannot be null");
        }
        if (timeEntry.getOpgaveId() == null || timeEntry.getOpgaveId() <= 0) {
            throw new IllegalArgumentException("Opgave ID must be valid");
        }
        if (timeEntry.getWorkDate() == null) {
            throw new IllegalArgumentException("Work date cannot be null");
        }
        if (timeEntry.getWorkDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Work date cannot be in the future");
        }
        if (timeEntry.getHours() == null || timeEntry.getHours() <= 0) {
            throw new IllegalArgumentException("Hours must be greater than 0");
        }
        if (timeEntry.getHours() > 24) {
            throw new IllegalArgumentException("Hours cannot exceed 24 per day");
        }
    }
}
