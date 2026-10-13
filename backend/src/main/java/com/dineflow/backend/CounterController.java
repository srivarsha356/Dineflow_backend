package com.dineflow.backend;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/counters")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://localhost:5174"
})
public class CounterController {

    private final CounterRepository counterRepository;

    public CounterController(CounterRepository counterRepository) {
        this.counterRepository = counterRepository;
    }

    @GetMapping
    public List<Counter> getAllCounters() {
        return counterRepository.findAll();
    }

    @PostMapping
    public Counter addCounter(@RequestBody Counter counter) {
        return counterRepository.save(counter);
    }

    @PutMapping("/{id}/queue")
    public Counter updateQueue(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> body) {

        Counter counter = counterRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Counter not found"));

        Integer queueLength = body.get("queueLength");

        if (queueLength == null || queueLength < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Queue length must be zero or greater");
        }

        counter.setQueueLength(queueLength);

        return counterRepository.save(counter);
    }
    @DeleteMapping("/{id}")
public void deleteCounter(@PathVariable Long id) {
    if (!counterRepository.existsById(id)) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Counter not found");
    }

    counterRepository.deleteById(id);
}
}