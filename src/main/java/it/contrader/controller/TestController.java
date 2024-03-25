package it.contrader.controller;


import it.contrader.converter.AddBloodTestConverter;
import it.contrader.converter.AddUrineTestConverter;

import it.contrader.converter.TestPostConverter;
import it.contrader.dto.*;

import it.contrader.model.Test;

import it.contrader.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
@CrossOrigin(origins = "http://localhost:4200")
public class TestController extends AbstractController<TestDTO> {
    @Autowired
    private TestService service;

    @Autowired
    private EmailService senderService;

    @Autowired
    private EmailController emailController;


    @Autowired
    private TestPostConverter converter;

    @Autowired
    private RegistryService registryService;

    @Autowired
    private UserService userService;

    @Autowired
    private BloodTestService bloodTestService;

    @Autowired
    private UrineTestService urineTestService;

    @Autowired
    private AddBloodTestConverter addBloodTestConverter;

    @Autowired
    private AddUrineTestConverter addUrineTestConverter;

    @Autowired
    private PdfController pdfController;

    @GetMapping("/findAllByDoctor")
    public List<TestDTO> findAllByDoctorId(@RequestParam Long doctor) { return service.findAllByDoctorId(doctor);}


    @GetMapping("/findAllByPatient")
    public List<TestDTO> findAllByPatient(@RequestParam Long patient) { return service.findAllByPatientId(patient);}

    @GetMapping("/findAllByType")
    public List<TestDTO> findAllByType(@RequestParam Test.TestType type) { return service.findAllByType(type);}

    @GetMapping("/findAllByDate")
    public List<TestDTO> findAllByDate(@RequestParam String date) { return service.findAllByDate(date);}

    @GetMapping("/findAllByIsChecked")
    public List<TestDTO> findAllByIsChecked(@RequestParam Boolean isChecked) { return service.findAllByIsChecked(isChecked);}

    @GetMapping("/findAllPatientCfByDoctorId")
    public List<TestDTO> findAllPatientCfByDoctorId(@RequestParam Long doctor) { return service.findAllPatientCfByDoctorId(doctor); }



    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestBody ValidateDTO dto) {

        ResponseEntity<?> validateDto = service.validate(dto.getIdTest(), dto.getIdUser());
        if (validateDto.getStatusCode().equals(HttpStatus.OK)) {
            TestPostDTO responseDto = service.readPost(dto.getIdTest());
            RegistryDTO doctor = registryService.read(responseDto.getDoctor());
            RegistryDTO patient = registryService.read(responseDto.getPatient());
            UserDTO doctorMail = userService.read(doctor.getIdUser());
            UserDTO patientMail = userService.read(patient.getIdUser());
            String fileName = patient.getCf() + "-" + responseDto.getId() + ".pdf";
            File pdfFile = new File(fileName);
            if (!pdfFile.exists()) {
                pdfController.createTestPdf(responseDto.getId());
            }
            TestEmailDTO emailDTO = new TestEmailDTO(doctorMail.getEmail(), doctor.getName(), doctor.getSurname(), patientMail.getEmail(), patient.getName(), patient.getSurname(), fileName);
            emailController.sendPatientEmailValidate(emailDTO);
        }


        return validateDto;
    }

    @GetMapping("/findAllByDataAndPatientId")
    public List<TestDTO> findAllByDateAndPatientId(@RequestParam("patient") Long patient, @RequestParam("date") String date) {return service.findAllByDateAndPatientId(date, patient);}

    @GetMapping("/findAllByTypeAndPatientId")
    public List<TestDTO> findAllByTypeAndPatientId(@RequestParam("patient") Long patient, @RequestParam("type") Test.TestType type) {return service.findAllByTypeAndPatientId(type, patient);}

    @GetMapping("/findAllByIsCheckedAndPatientId")
    public List<TestDTO> findAllByIsCheckedAndPatientId(@RequestParam("patient") Long patient, @RequestParam("isChecked")Boolean isChecked) {return service.findAllByIsCheckedAndPatientId(isChecked, patient);}

    @GetMapping("/findAllByDateAndDoctorId")
    public List<TestDTO> findAllByDateAndDoctorId(@RequestParam("date") String date, @RequestParam("doctor") Long doctor) { return service.findAllByDateAndDoctorId(date, doctor); }

    @GetMapping("/findAllByTypeAndDoctorId")
    public List<TestDTO> findAllByTypeAndDoctorId(@RequestParam("type") Test.TestType type, @RequestParam("doctor") Long   doctor) { return service.findAllByTypeAndDoctorId(type, doctor) ;  }

    @GetMapping("/findAllByIsCheckedAndDoctorId")
    public List<TestDTO> findAllByIsCheckedAndDoctorId(@RequestParam("isChecked") Boolean isChecked, @RequestParam("doctor") Long doctor) {return service.findAllByIsCheckedAndDoctorId(isChecked, doctor); }

    @PutMapping("/edit")
    public TestPostDTO update(@RequestBody TestPostDTO dto){
        return service.update(dto);
    }

    @PostMapping("/add")
    public TestPostDTO insert(@RequestBody TestPostDTO dto) {
        return service.insert(dto);

    }
    @PostMapping("/addReport")
    public void addReport(@RequestBody AddBloodTestDTO dto) {
        TestPostDTO testPostDTO = addBloodTestConverter.toTestPost(dto);
        TestPostDTO responseDto = insert(testPostDTO);
        BloodTestDTO bloodTestDTO = new BloodTestDTO();
        UrineTestDTO urineTestDTO = new UrineTestDTO();
        if (responseDto.getType().equals(Test.TestType.BLOODTEST)) {
            bloodTestDTO = addBloodTestConverter.toBloodTest(dto);
            bloodTestDTO.setIdTest(responseDto.getId());
            BloodTestDTO responseBloodDto = bloodTestService.insert(bloodTestDTO);
        } else {
            urineTestDTO = addUrineTestConverter.toUrineTest((dto));
            urineTestDTO.setIdTest(responseDto.getId());
            UrineTestDTO responseUrineDto = urineTestService.insert(urineTestDTO);
        }

        RegistryDTO doctor = registryService.read(responseDto.getDoctor());
        RegistryDTO patient = registryService.read(responseDto.getPatient());
        UserDTO doctorMail = userService.read(doctor.getIdUser());
        UserDTO patientMail = userService.read(patient.getIdUser());
        String fileName = patient.getCf() + "-" + responseDto.getId() + ".pdf";
        TestEmailDTO emailDTO = new TestEmailDTO(doctorMail.getEmail(), doctor.getName(), doctor.getSurname(), patientMail.getEmail(), patient.getName(), patient.getSurname(), fileName);

        pdfController.createTestPdf(responseDto.getId());
        emailController.sendEmail(emailDTO);
    }




}



