package dk.eak.kalkulation.controller;

import dk.eak.kalkulation.model.Opgave;
import dk.eak.kalkulation.repository.OpgaveRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/opgaver")
public class OpgaveController {

    private final OpgaveRepository repo;

    public OpgaveController(OpgaveRepository repo) {
        this.repo = repo;
    }

    // 📄 TASKS PAGE
    @GetMapping("/{projectId}")
    public String tasks(@PathVariable int projectId, Model model) {
        model.addAttribute("projectId", projectId);
        model.addAttribute("opgaver", repo.findByProjectId(projectId));
        return "tasks";
    }

    // 💾 SAVE TASK
    @PostMapping("/{projectId}/opret")
    public String saveTask(
            @PathVariable int projectId,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam int estimatedHours,
            @RequestParam(required = false) LocalDate deadline
    ) {
        Opgave o = new Opgave();
        o.setProject_id(projectId);
        o.setName(name);
        o.setDescription(description);
        o.setEstimatedHours(estimatedHours);
        o.setDeadline(deadline);

        repo.create(o);

        return "redirect:/opgaver/" + projectId;
    }

    // ❌ DELETE
    @GetMapping("/slet/{id}")
    public String delete(@PathVariable int id, @RequestParam int projectId) {
        repo.delete(id);
        return "redirect:/opgaver/" + projectId;
    }
}
