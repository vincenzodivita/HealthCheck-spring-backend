package it.contrader.converter;

import it.contrader.dto.*;
import org.springframework.stereotype.Component;

@Component
public class AddUrineTestConverter {

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


    public UrineTestDTO toUrineTest(AddBloodTestDTO dto) {
        return dto != null ? UrineTestDTO.builder()
                .id(dto.getId())
                .color(dto.getColor())
                .ph(dto.getPh())
                .protein(dto.getProtein())
                .hemoglobine(dto.getHemoglobine())
                .build() : null;
    }
}