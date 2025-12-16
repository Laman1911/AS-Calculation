package dk.eak.kalkulation.service;

import dk.eak.kalkulation.model.Opgave;
import dk.eak.kalkulation.repository.OpgaveRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpgaveService {

    private final OpgaveRepository repo;

    public OpgaveService(OpgaveRepository repo) {
        this.repo = repo;
    }

    public List<Opgave> getByProjectId(int projectId) {
        return repo.findByProjectId(projectId);
    }

    public void create(Opgave o) {
        repo.create(o);
    }

    public void delete(int id) {
        repo.delete(id);
    }
}
