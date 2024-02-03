package com.example.edi_intconvert.entity;

import lombok.Data;
import org.milyn.smooks.edi.unedifact.model.r41.UNB41;
import org.milyn.smooks.edi.unedifact.model.r41.UNH41;
import org.milyn.smooks.edi.unedifact.model.r41.UNT41;
import org.milyn.smooks.edi.unedifact.model.r41.UNZ41;

@Data
public class EdifactJsonEntity {
    private UNB41 interchangeHeader;
    private UNH41 messageHeader;
    private Object message;
    private UNT41 messageTrailer;
    private UNZ41 interchangeTrailer;
}
