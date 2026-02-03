package dk.eak.kalkulation.service;

import dk.eak.kalkulation.model.Opgave;
import dk.eak.kalkulation.repository.OpgaveRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for Opgave (Task) operations.
 * Handles business logic for task management.
 */
@Service
public class OpgaveService {

    private final OpgaveRepository repo;

    public OpgaveService(OpgaveRepository repo) {
        this.repo = repo;
    }

    /**
     * Get all tasks for a given project.
     *
     * @param projectId the project ID
     * @return list of tasks
     */
    public List<Opgave> getByProjectId(int projectId) {
        if (projectId <= 0) {
            throw new IllegalArgumentException("Project ID must be valid");
        }
        return repo.findByProjectId(projectId);
    }

    /**
     * Get a task by ID.
     *
     * @param opgaveId the task ID
     * @return the task
     */
    public Opgave getById(int opgaveId) {
        if (opgaveId <= 0) {
            throw new IllegalArgumentException("Opgave ID must be valid");
        }
        return repo.findById(opgaveId);
    }

    /**
     * Get all tasks for a given subproject.
     *
     * @param delProjektId the subproject ID
     * @return list of tasks
     */
    public List<Opgave> getByDelProjektId(int delProjektId) {
        if (delProjektId <= 0) {
            throw new IllegalArgumentException("DelProjekt ID must be valid");
        }
        return repo.findByDelProjektId(delProjektId);
    }

    /**
     * Create a new task.
     *
     * @param opgave the task to create
     */
    public void create(Opgave opgave) {
        validateOpgave(opgave);
        repo.create(opgave);
    }

    /**
     * Update an existing task.
     *
     * @param opgave the task to update
     */
    public void update(Opgave opgave) {
        validateOpgave(opgave);
        if (opgave.getOpgaveId() == null || opgave.getOpgaveId() <= 0) {
            throw new IllegalArgumentException("Opgave ID must be valid");
        }
        repo.update(opgave);
    }

    /**
     * Delete a task by ID.
     *
     * @param opgaveId the task ID to delete
     */
    public void delete(int opgaveId) {
        if (opgaveId <= 0) {
            throw new IllegalArgumentException("Opgave ID must be valid");
        }
        repo.delete(opgaveId);
    }

    /**
     * Validate opgave fields.
     *
     * @param opgave the opgave to validate
     */
    private void validateOpgave(Opgave opgave) {
        if (opgave == null) {
            throw new IllegalArgumentException("Opgave cannot be null");
        }
        if (opgave.getProject_id() == null || opgave.getProject_id() <= 0) {
            throw new IllegalArgumentException("Project ID must be valid");
        }
        if (opgave.getName() == null || opgave.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Task name cannot be empty");
        }
        if (opgave.getEstimatedHours() == null || opgave.getEstimatedHours() < 0) {
            throw new IllegalArgumentException("Estimated hours must be non-negative");
        }
    }
}

