package com.example.edi_intconvert.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EdifactDto {
    private String idSender;
    private boolean signatureMark;
    private String edifact;
}
