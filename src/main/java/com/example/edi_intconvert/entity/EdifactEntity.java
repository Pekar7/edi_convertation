package com.example.edi_intconvert.entity;

import lombok.Data;

@Data
public class EdifactEntity {
    private boolean signatureMark;
    private String recipientId;
    private String edifact;
}