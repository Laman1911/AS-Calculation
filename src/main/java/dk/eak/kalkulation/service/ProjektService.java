package dk.eak.kalkulation.service;

import dk.eak.kalkulation.model.Projekt;
import dk.eak.kalkulation.repository.ProjektRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service layer for Projekt operations.
 * Handles business logic for project management.
 */
@Service
public class ProjektService {

    private final ProjektRepository repo;

    public ProjektService(ProjektRepository repo) {
        this.repo = repo;
    }

    /**
     * Get all projects.
     *
     * @return list of all projects
     */
    public List<Projekt> getAll() {
        return repo.getAll();
    }

    /**
     * Get a project by ID.
     *
     * @param id the project ID
     * @return the project
     */
    public Projekt getById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Project ID must be valid");
        }
        return repo.getById(id);
    }

    /**
     * Create a new project.
     *
     * @param projekt the project to create
     */
    public void create(Projekt projekt) {
        validateProjekt(projekt);
        repo.create(projekt);
    }

    /**
     * Update an existing project.
     *
     * @param projekt the project to update
     */
    public void update(Projekt projekt) {
        validateProjekt(projekt);
        if (projekt.getProjectId() <= 0) {
            throw new IllegalArgumentException("Project ID must be valid");
        }
        repo.update(projekt);
    }

    /**
     * Delete a project by ID.
     *
     * @param id the project ID to delete
     */
    public void delete(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Project ID must be valid");
        }
        repo.delete(id);
    }

    /**
     * Validate projekt fields.
     *
     * @param projekt the projekt to validate
     */
    private void validateProjekt(Projekt projekt) {
        if (projekt == null) {
            throw new IllegalArgumentException("Projekt cannot be null");
        }
        if (projekt.getName() == null || projekt.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be empty");
        }
        if (projekt.getStartDate() != null && projekt.getEndDate() != null) {
            if (projekt.getEndDate().isBefore(projekt.getStartDate())) {
                throw new IllegalArgumentException("End date cannot be before start date");
            }
        }
    }
}

