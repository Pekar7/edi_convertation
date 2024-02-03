package com.example.edi_intconvert.service.convert;


import com.example.edi_intconvert.dto.MessageDtoKfk;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.milyn.edi.unedifact.d97a.APERAK.Aperak;
import org.milyn.edi.unedifact.d97a.D97AInterchangeFactory;
import org.milyn.edi.unedifact.d97a.IFCSUM.Ifcsum;
import org.milyn.edi.unedifact.d97a.IFTMIN.Iftmin;
import org.milyn.smooks.edi.unedifact.model.UNEdifactInterchange;
import org.milyn.smooks.edi.unedifact.model.r41.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class EdifactConvertService implements ConvertDocument {

    @Override
    public void convertDocumentToJson(MessageDtoKfk message) {
        log.info(message.getId() + " " + message.getData());
    }

    @Override
    public void convJsonToDocument(MessageDtoKfk message) {
        log.info(message.getId() + " " + message.getData());
    }
}
