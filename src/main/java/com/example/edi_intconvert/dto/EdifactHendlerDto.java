package com.example.edi_intconvert.dto;

import com.example.edi_intconvert.entity.EdifactJsonEntity;
import com.example.edi_intconvert.enums.EdifactType;
import lombok.Data;

@Data
public class EdifactHendlerDto {
    private EdifactType typeMessage;
    private boolean signatureMark;
    private String recipientId;
    private EdifactJsonEntity edifactMessage;
}