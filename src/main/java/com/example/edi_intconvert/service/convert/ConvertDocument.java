package com.example.edi_intconvert.service.convert;

import com.example.edi_intconvert.dto.MessageDtoKfk;
import org.springframework.stereotype.Component;

@Component
public interface ConvertDocument {
    void convertDocumentToJson(MessageDtoKfk message);
    void convJsonToDocument(MessageDtoKfk message);
}
