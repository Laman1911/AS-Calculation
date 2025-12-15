package dk.eak.kalkulation.controller;

import dk.eak.kalkulation.model.TimeEntry;
import dk.eak.kalkulation.repository.OpgaveRepository;
import dk.eak.kalkulation.repository.TimeEntryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/tasks/{opgaveId}/time")
public class TimeEntryController {

    private final TimeEntryRepository timeRepo;
    private final OpgaveRepository opgaveRepo;

    public TimeEntryController(TimeEntryRepository timeRepo, OpgaveRepository opgaveRepo) {
        this.timeRepo = timeRepo;
        this.opgaveRepo = opgaveRepo;
    }

    @GetMapping
    public String list(@PathVariable int opgaveId, Model model) {
        var task = opgaveRepo.findById(opgaveId);

        TimeEntry form = new TimeEntry();
        form.setOpgaveId(opgaveId);
        form.setWorkDate(LocalDate.now());
        form.setHours(1);

        model.addAttribute("task", task);
        model.addAttribute("entries", timeRepo.findByOpgaveId(opgaveId));
        model.addAttribute("timeForm", form);
        return "time_entries";
    }

    @PostMapping
    public String create(@PathVariable int opgaveId, @ModelAttribute("timeForm") TimeEntry t) {
        t.setOpgaveId(opgaveId);
        if (t.getWorkDate() == null) t.setWorkDate(LocalDate.now());
        if (t.getHours() == null) t.setHours(0);
        timeRepo.create(t);
        return "redirect:/tasks/" + opgaveId + "/time";
    }

    @PostMapping("/{timeEntryId}/delete")
    public String delete(@PathVariable int opgaveId, @PathVariable int timeEntryId) {
        timeRepo.delete(timeEntryId);
        return "redirect:/tasks/" + opgaveId + "/time";
    }
}
