package com.hospital.tm.controller;

import com.hospital.tm.model.ArchivedPatient;
import com.hospital.tm.model.Patient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class TicketingController {

    private final Map<String, LinkedHashMap<Integer, Patient>> queues = new HashMap<>();
    private final List<ArchivedPatient> archive = new ArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1000);

    public TicketingController() {
        queues.put("waiting", new LinkedHashMap<>());
        queues.put("consultation", new LinkedHashMap<>());
        queues.put("pharmacy", new LinkedHashMap<>());
        archive.add(new ArchivedPatient("2024-12-06", new Patient(999, "Thomas", "Fever")));
    }

    @PostMapping("/addPatient")
    public ResponseEntity<?> addPatient(@RequestBody Patient patient) {
        if (queues.get("waiting").containsKey(patient.getId())) {
            return ResponseEntity.badRequest().body("Patient with the same ID already exists in the queue.");
        }
        int id = idGenerator.getAndIncrement(); //
        patient.setId(id);
        queues.get("waiting").put(id, patient);
        return ResponseEntity.ok("Patient added to the waiting queue.");
    }

    @GetMapping("/queue/{queueName}")
    public ResponseEntity<?> getQueue(@PathVariable String queueName) {
        if (!queues.containsKey(queueName)) {
            return ResponseEntity.badRequest().body("Invalid queue name.");
        }
        return ResponseEntity.ok(queues.get(queueName).values());
    }

    @PostMapping("/movePatient")
    public ResponseEntity<?> movePatient(@RequestParam String fromQueue,
                                         @RequestParam String toQueue,
                                         @RequestParam Integer patientId) {
        if(toQueue.equalsIgnoreCase("remove")){
            Patient patient = queues.get(fromQueue).remove(patientId);
            return ResponseEntity.ok("Patient removed successfully.");
        }
        if (!queues.containsKey(fromQueue) || !queues.containsKey(toQueue)) {
            return ResponseEntity.badRequest().body("Invalid queue names.");
        }

        Patient patient = queues.get(fromQueue).remove(patientId);
        if (patient == null) {
            return ResponseEntity.badRequest().body("Patient not found in the source queue.");
        }

        queues.get(toQueue).put(patientId, patient);
        return ResponseEntity.ok("Patient moved successfully.");
    }

    @PostMapping("/archivePatient")
    public ResponseEntity<?> archivePatient(@RequestParam Integer patientId) {
        Patient patient = queues.get("pharmacy").remove(patientId);
        if (patient == null) {
            return ResponseEntity.badRequest().body("Patient not found in the pharmacy queue.");
        }

        ArchivedPatient archivedPatient = new ArchivedPatient();
        archivedPatient.setPatient(patient);
        archivedPatient.setCompletedDate(LocalDate.now().toString());
        archive.add(archivedPatient);

        return ResponseEntity.ok("Patient archived successfully.");
    }

    @GetMapping("/archive")
    public ResponseEntity<?> getArchive() {
        return ResponseEntity.ok(archive);
    }

}