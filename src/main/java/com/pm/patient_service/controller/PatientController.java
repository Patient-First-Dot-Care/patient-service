package com.pm.patient_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pm.patient_service.dto.PatientRequestDTO;
import com.pm.patient_service.dto.PatientResponseDTO;
import com.pm.patient_service.dto.validators.CreatePatientValidationGroup;
import com.pm.patient_service.service.PatientService;

import jakarta.validation.groups.Default;

@RestController
@RequestMapping("/patients")
public class PatientController {
  private final PatientService patientService;

  public PatientController(PatientService patientService) {
    this.patientService = patientService;
  }

  @GetMapping
  public ResponseEntity<List<PatientResponseDTO>> get() {
    List<PatientResponseDTO> patients = patientService.get();
    return ResponseEntity.ok().body(patients);
  }

  @PostMapping
  public ResponseEntity<PatientResponseDTO> create(@Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
    PatientResponseDTO patientResponseDTO = patientService.create(patientRequestDTO);
    return ResponseEntity.ok().body(patientResponseDTO);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PatientResponseDTO> update(@PathVariable UUID id, @Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
    PatientResponseDTO patientResponseDTO = patientService.update(id, patientRequestDTO);
    return ResponseEntity.ok().body(patientResponseDTO);
  }
}
