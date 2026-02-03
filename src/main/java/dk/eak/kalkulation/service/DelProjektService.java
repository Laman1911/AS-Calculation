package dk.eak.kalkulation.service;

import dk.eak.kalkulation.model.DelProjekt;
import dk.eak.kalkulation.repository.DelProjektRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for DelProjekt operations.
 * Handles business logic for subproject management.
 */
@Service
public class DelProjektService {

    private final DelProjektRepository repository;

    public DelProjektService(DelProjektRepository repository) {
        this.repository = repository;
    }

    /**
     * Get all subprojects for a given project.
     *
     * @param projectId the project ID
     * @return list of subprojects
     */
    public List<DelProjekt> getByProjectId(int projectId) {
        return repository.findByProjektId(projectId);
    }

    /**
     * Get a subproject by ID.
     *
     * @param delProjektId the subproject ID
     * @return the subproject
     */
    public DelProjekt getById(int delProjektId) {
        return repository.findById(delProjektId);
    }

    /**
     * Create a new subproject.
     *
     * @param delProjekt the subproject to create
     */
    public void create(DelProjekt delProjekt) {
        if (delProjekt == null) {
            throw new IllegalArgumentException("DelProjekt cannot be null");
        }
        if (delProjekt.getProjectId() == null || delProjekt.getProjectId() <= 0) {
            throw new IllegalArgumentException("Project ID must be valid");
        }
        if (delProjekt.getName() == null || delProjekt.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        repository.create(delProjekt);
    }

    /**
     * Update an existing subproject.
     *
     * @param delProjekt the subproject to update
     */
    public void update(DelProjekt delProjekt) {
        if (delProjekt == null) {
            throw new IllegalArgumentException("DelProjekt cannot be null");
        }
        if (delProjekt.getDelProjektId() == null || delProjekt.getDelProjektId() <= 0) {
            throw new IllegalArgumentException("DelProjekt ID must be valid");
        }
        if (delProjekt.getName() == null || delProjekt.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        repository.update(delProjekt);
    }

    /**
     * Delete a subproject by ID.
     *
     * @param delProjektId the subproject ID to delete
     */
    public void delete(int delProjektId) {
        if (delProjektId <= 0) {
            throw new IllegalArgumentException("DelProjekt ID must be valid");
        }
        repository.delete(delProjektId);
    }
}
