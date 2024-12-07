package com.hospital.tm.service;
import com.hospital.tm.model.Patient;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class TicketingService {
    private final LinkedHashMap<Integer, Patient> waitingQueue = new LinkedHashMap<>();
    private final LinkedHashMap<Integer, Patient> consultationQueue = new LinkedHashMap<>();
    private final LinkedHashMap<Integer, Patient> pharmacyQueue = new LinkedHashMap<>();

    // Add a patient to the waiting queue
    public synchronized String addPatient(Patient patient) {
        if (waitingQueue.containsKey(patient.getId())) {
            return "Patient already in the waiting queue.";
        }
        waitingQueue.put(patient.getId(), patient);
        return "Patient added to the waiting queue.";
    }

    // Move the next patient to consultation
    public synchronized String moveToConsultation() {
        if (waitingQueue.isEmpty()) {
            return "No patients in the waiting queue.";
        }
        Map.Entry<Integer, Patient> nextPatient = waitingQueue.entrySet().iterator().next();
        waitingQueue.remove(nextPatient.getKey());
        consultationQueue.put(nextPatient.getKey(), nextPatient.getValue());
        return "Patient " + nextPatient.getValue().getName() + " moved to consultation queue.";
    }

    // Move the next patient to pharmacy
    public synchronized String moveToPharmacy() {
        if (consultationQueue.isEmpty()) {
            return "No patients in the consultation queue.";
        }
        Map.Entry<Integer, Patient> nextPatient = consultationQueue.entrySet().iterator().next();
        consultationQueue.remove(nextPatient.getKey());
        pharmacyQueue.put(nextPatient.getKey(), nextPatient.getValue());
        return "Patient " + nextPatient.getValue().getName() + " moved to pharmacy queue.";
    }

    // Get the current queue details
    public synchronized Map<Integer, Patient> getQueue(String queueType) {
        switch (queueType.toLowerCase()) {
            case "waiting":
                return waitingQueue;
            case "consultation":
                return consultationQueue;
            case "pharmacy":
                return pharmacyQueue;
            default:
                throw new IllegalArgumentException("Invalid queue type: " + queueType);
        }
    }
}