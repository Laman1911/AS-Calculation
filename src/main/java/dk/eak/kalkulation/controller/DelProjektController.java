package dk.eak.kalkulation.controller;

import dk.eak.kalkulation.model.DelProjekt;
import dk.eak.kalkulation.repository.DelProjektRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/delprojekter")
public class DelProjektController {

    private final DelProjektRepository repository;

    public DelProjektController(DelProjektRepository repository) {
        this.repository = repository;
    }

    // 🔹 CREATE DELPROJEKT
    @PostMapping("/opret")
    public String create(@RequestParam int projectId,
                         @RequestParam String name,
                         @RequestParam String description) {

        DelProjekt dp = new DelProjekt();
        dp.setProjectId(projectId);
        dp.setName(name);
        dp.setDescription(description);

        repository.create(dp);

        return "redirect:/projekter/" + projectId;
    }

    //
    @GetMapping("/slet/{id}")
    public String delete(@PathVariable int id,
                         @RequestParam int projectId) {

        repository.delete(id);
        return "redirect:/projekter/" + projectId;
    }
}
