package com.pm.patient_service.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pm.patient_service.dto.PatientRequestDTO;
import com.pm.patient_service.dto.PatientResponseDTO;
import com.pm.patient_service.exception.EmailAlreadyExistsException;
import com.pm.patient_service.exception.PatientNotFoundException;
import com.pm.patient_service.mapper.PatientMapper;
import com.pm.patient_service.model.Patient;
import com.pm.patient_service.repository.PatientRepository;

@Service
public class PatientService {
  private final PatientRepository patientRepository;

  public PatientService(PatientRepository patientRepository) {
    this.patientRepository = patientRepository;
  }

  public List<PatientResponseDTO> get() {
    List<Patient> patients = patientRepository.findAll();
    List<PatientResponseDTO> patientDTOs = patients.stream()
        .map(PatientMapper::toDTO)
        .toList();
    return patientDTOs;
  }

  public PatientResponseDTO create(PatientRequestDTO patientRequestDTO) {
    if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
      throw new EmailAlreadyExistsException("A patient with the email address " + patientRequestDTO.getEmail() + " already exists.");
    }
    Patient patient = PatientMapper.toModel(patientRequestDTO);
    Patient newPatient = patientRepository.save(patient);
    PatientResponseDTO patientDTO = PatientMapper.toDTO(newPatient);
    return patientDTO;
  }

  public PatientResponseDTO update(UUID id, PatientRequestDTO patientRequestDTO) {
    Patient patient = patientRepository.findById(id)
        .orElseThrow(() -> new PatientNotFoundException("Patient with id " + id + " not found."));
    
    if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
      throw new EmailAlreadyExistsException("A patient with the email address " + patientRequestDTO.getEmail() + " already exists.");
    }

    patient.setName(patientRequestDTO.getName());
    patient.setAddress(patientRequestDTO.getAddress());
    patient.setEmail(patientRequestDTO.getEmail());
    patient.setDateOfBirth(LocalDate.parse((patientRequestDTO.getDateOfBirth())));

    Patient updatedPatient = patientRepository.save(patient);
    PatientResponseDTO patientDTO = PatientMapper.toDTO(updatedPatient);

    return patientDTO;
  }
}
