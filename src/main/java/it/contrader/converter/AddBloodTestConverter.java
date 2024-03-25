package it.contrader.converter;

import it.contrader.dto.AddBloodTestDTO;
import it.contrader.dto.BloodTestDTO;
import it.contrader.dto.TestPostDTO;
import it.contrader.model.BloodTest;
import it.contrader.model.Test;
import org.springframework.stereotype.Component;

@Component
public class AddBloodTestConverter {

    public TestPostDTO toTestPost(AddBloodTestDTO dto) {
        return dto != null ? TestPostDTO.builder()
                .id(dto.getId())
                .doctor(dto.getDoctor())
                .patient(dto.getPatient())
                .isChecked(false)
                .date(dto.getDate())
                .type(dto.getType())
                .build() : null;
    }


    public BloodTestDTO toBloodTest(AddBloodTestDTO dto) {
        return dto != null ? BloodTestDTO.builder()
                .id(dto.getId())
                .redBloodCell(dto.getRedBloodCell())
                .whiteBloodCell(dto.getWhiteBloodCell())
                .platelets(dto.getPlatelets())
                .hemoglobin(dto.getHemoglobin())
                .build() : null;
    }
}