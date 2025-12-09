package dk.eak.kalkulation.service;

import dk.eak.kalkulation.model.Projekt;
import dk.eak.kalkulation.repository.ProjektRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProjektService {

    @Autowired
    private ProjektRepository repo;

    public List<Projekt> getAll() {
        return repo.getAll();
    }

    public Projekt getById(int id) {
        return repo.getById(id);
    }

    public void create(Projekt p) {
        repo.create(p);
    }

    public void update(Projekt p) {
        repo.update(p);
    }

    public void delete(int id) {
        repo.delete(id);
    }
}
