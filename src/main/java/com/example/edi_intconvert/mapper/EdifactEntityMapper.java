package com.example.edi_intconvert.mapper;

import com.example.edi_intconvert.entity.EdifactJsonEntity;
import com.example.edi_intconvert.dto.EdifactHendlerDto;
import com.example.edi_intconvert.entity.RegDataInJsonEntity;
import com.example.edi_intconvert.entity.RegDataOutXmlEntity;
import com.example.edi_intconvert.enums.EdifactType;
import org.milyn.smooks.edi.unedifact.model.r41.UNB41;
import org.milyn.smooks.edi.unedifact.model.r41.UNH41;
import org.milyn.smooks.edi.unedifact.model.r41.UNT41;
import org.milyn.smooks.edi.unedifact.model.r41.UNZ41;
import org.springframework.stereotype.Component;

@Component
public class EdifactEntityMapper {

    /**
     * @param messageType Edifact type enum String
     * @param jsonEntity  Edifact Java object
     * @param id identity Edifact
     * @param signatureMark Edifact isNeed crypt
     * @return MessageEdifactEntity
     */
    public EdifactHendlerDto createEdifactDto(
            EdifactType messageType,
            EdifactJsonEntity jsonEntity,
            String id,
            boolean signatureMark)
    {
        EdifactHendlerDto edifactHendlerDto = new EdifactHendlerDto();
        edifactHendlerDto.setTypeMessage(messageType);
        edifactHendlerDto.setSignatureMark(signatureMark);
        edifactHendlerDto.setRecipientId(id);
        edifactHendlerDto.setEdifactMessage(jsonEntity);
        return edifactHendlerDto;
    }

    /**
     * Creates a JsonEntity object based on the passed parameters.
     *
     * @param interchangeHeader  UNB41 object with exchange header data.
     * @param interchangeTrailer object UNZ41 with the data of the final segment of the exchange.
     * @param edifact            an Edifact object with Aperak/Ifcsum/Iftmin message data.
     * @param messageHeader      UNH41 object with message header data.
     * @param messageTrailer     UNT41 object with the data of the end segment of the message.
     * @return the JsonEntity object created based on the passed parameters.
     */
    public EdifactJsonEntity createJsonEntity(
            UNB41 interchangeHeader,
            UNZ41 interchangeTrailer,
            Object edifact,
            UNH41 messageHeader,
            UNT41 messageTrailer)
    {
        EdifactJsonEntity jsonEntity = new EdifactJsonEntity();
        jsonEntity.setInterchangeHeader(interchangeHeader);
        jsonEntity.setInterchangeTrailer(interchangeTrailer);
        jsonEntity.setMessage(edifact);
        jsonEntity.setMessageHeader(messageHeader);
        jsonEntity.setMessageTrailer(messageTrailer);
        return jsonEntity;
    }

    public RegDataInJsonEntity toRegDataInJson(String id, String message, String recipientCode) {
        RegDataInJsonEntity regDataInReportEntity = new RegDataInJsonEntity();
        regDataInReportEntity.setId(id);
        regDataInReportEntity.setConvertedJson(message);
        regDataInReportEntity.setSenderCode(recipientCode);
        return regDataInReportEntity;
    }

    public RegDataOutXmlEntity toRegDataOutXml(String id, String message, String recipientCode) {
        RegDataOutXmlEntity regDataOutXmlEntity = new RegDataOutXmlEntity();
        regDataOutXmlEntity.setId(id);
        regDataOutXmlEntity.setMessageXml(message);
        regDataOutXmlEntity.setRecipientCode("---");

        return regDataOutXmlEntity;
    }
}
