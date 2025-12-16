package dk.eak.kalkulation.controller;

import dk.eak.kalkulation.model.Opgave;
import dk.eak.kalkulation.repository.OpgaveRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/projects/{projektId}/tasks")
public class OpgaveController {

    private final OpgaveRepository repo;

    public OpgaveController(OpgaveRepository repo) { this.repo = repo; }

    @GetMapping
    public String list(@PathVariable int project_id, Model model) {
        model.addAttribute("projektId", project_id);
        model.addAttribute("tasks", repo.findByProjektId(project_id));
        model.addAttribute("taskForm", new Opgave());
        return "tasks";
    }

    @PostMapping
    public String create(@PathVariable int project_id, @ModelAttribute("taskForm") Opgave o) {
        o.setProject_id(project_id);
        if (o.getEstimatedHours() == null) o.setEstimatedHours(0);
        repo.create(o);
        return "redirect:/projects/" + project_id + "/tasks";
    }

    @PostMapping("/{opgaveId}/delete")
    public String delete(@PathVariable int project_id, @PathVariable int opgaveId) {
        repo.delete(opgaveId);
        return "redirect:/projects/" + project_id + "/tasks";
    }
}
