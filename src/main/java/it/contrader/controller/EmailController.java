package it.contrader.controller;


import it.contrader.dto.TestEmailDTO;

import it.contrader.dto.TestPostDTO;
import it.contrader.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/email")
@CrossOrigin(origins = "http://localhost:4200")
public class EmailController {

    @Autowired
    private EmailService service;


    @PostMapping(value = "/sendEmail")
    public ResponseEntity<?> sendEmail(@RequestBody TestEmailDTO dto) {
        try {
            String subject = "Nuovo test inserito";
            service.sendEmailTest(dto, subject);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("C'e' stato un problema con l'invio della mail.");
        }
    }

    @PostMapping(value = "/sendValidateEmail")
    public void sendPatientEmailValidate(@RequestBody TestEmailDTO dto) {
        String subject = "Test validato";
        service.sendPatientEmailValidate(dto, subject);
    }
}

