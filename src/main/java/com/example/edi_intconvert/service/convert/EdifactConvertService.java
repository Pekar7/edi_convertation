package com.example.edi_intconvert.service.convert;


import com.example.edi_intconvert.dao.MessageDao;
import com.example.edi_intconvert.dto.EdifactDto;
import com.example.edi_intconvert.dto.MessageDtoKfk;
import com.example.edi_intconvert.entity.EdifactJsonEntity;
import com.example.edi_intconvert.dto.EdifactHendlerDto;
import com.example.edi_intconvert.entity.RegDataInJsonEntity;
import com.example.edi_intconvert.enums.EdifactType;
import com.example.edi_intconvert.mapper.EdifactEntityMapper;
import com.example.edi_intconvert.service.kafka.KafkaSenderService;
import com.example.edi_intconvert.utils.DBUtils;
import com.example.edi_intconvert.utils.StatusCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.milyn.edi.unedifact.d97a.APERAK.Aperak;
import org.milyn.edi.unedifact.d97a.D97AInterchangeFactory;
import org.milyn.edi.unedifact.d97a.IFCSUM.Ifcsum;
import org.milyn.edi.unedifact.d97a.IFTMIN.Iftmin;
import org.milyn.smooks.edi.unedifact.model.UNEdifactInterchange;
import org.milyn.smooks.edi.unedifact.model.r41.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class EdifactConvertService implements ConvertDocument {

    @PersistenceContext
    EntityManager entityManager;
    private final KafkaSenderService kafkaService;
    private final EdifactEntityMapper entityMapper;
    private final DBUtils dbUtils;

    @Autowired
    public EdifactConvertService(KafkaSenderService kafkaService, EdifactEntityMapper entityMapper, DBUtils dbUtils) {
        this.kafkaService = kafkaService;
        this.entityMapper = entityMapper;
        this.dbUtils = dbUtils;
    }

    @Transactional(transactionManager = "TM_EDI_INT", propagation = Propagation.REQUIRES_NEW)
    @Override
    public void convertDocumentToJson(MessageDtoKfk message) {
        try {
            log.info(message.getId() + " " + message.getData());
            ObjectMapper objectMapper = new ObjectMapper();

            String dataDto = message.getData();
            EdifactDto edifactDto = objectMapper.readValue(dataDto, EdifactDto.class);

            String id = message.getId(); // id Edi system
            String edifact = edifactDto.getEdifact(); // Edi document Edifact
            String correctEdifact = refactorEdifactJson(edifact); //Refactor UNOA in UNOY

            EdifactHendlerDto edifactHendlerDto;
            MessageDao messageDao = new MessageDao();
            messageDao.setId(id);
            messageDao.setData(correctEdifact);

            D97AInterchangeFactory factory = D97AInterchangeFactory.getInstance();
            try (InputStream stream = new ByteArrayInputStream(correctEdifact.getBytes(StandardCharsets.UTF_8))) {
                UNEdifactInterchange interchange = factory.fromUNEdifact(stream);
                if (interchange instanceof UNEdifactInterchange41) {
                    UNEdifactInterchange41 interchange41 = (UNEdifactInterchange41) interchange;

                    for (UNEdifactMessage41 messageWithControlSegments : interchange41.getMessages()) {
                        UNB41 interchangeHeader = interchange41.getInterchangeHeader();
                        UNZ41 interchangeTrailer = interchange41.getInterchangeTrailer();
                        Object messageObj = messageWithControlSegments.getMessage();
                        UNH41 messageHeader = messageWithControlSegments.getMessageHeader();
                        UNT41 messageTrailer = messageWithControlSegments.getMessageTrailer();

                        if (messageObj instanceof Aperak) {
                            Aperak aperak = (Aperak) messageObj;
                            EdifactJsonEntity jsonEntity = entityMapper.createJsonEntity(
                                    interchangeHeader,
                                    interchangeTrailer,
                                    aperak,
                                    messageHeader,
                                    messageTrailer
                            );
                            if (dbUtils.checkDuplicateID(messageDao, "reg_data_in_json")) {
                                edifactHendlerDto = entityMapper.createEdifactDto(EdifactType.APERAK, jsonEntity, id, edifactDto.isSignatureMark());
                                String resultJsonAperak = javaToJson(edifactHendlerDto);
                                kafkaService.sendEdifactToHendler(id, resultJsonAperak);
                                dbUtils.updateRegDataIn(StatusCode.CONV_OK, messageDao);

                                RegDataInJsonEntity regDataInJsonEntity = entityMapper.toRegDataInJson(id, resultJsonAperak, "APERAK");
                                entityManager.persist(regDataInJsonEntity);
                                log.info("Successfully convert " + EdifactType.APERAK + " to JSON ID: " + id + " CONTRL REG_DATA_IN_JSON: " + resultJsonAperak);
                            }
                        }

                        if (messageObj instanceof Ifcsum) {
                            Ifcsum ifcsum = (Ifcsum) messageObj;
                            EdifactJsonEntity jsonEntity = entityMapper.createJsonEntity(
                                    interchangeHeader,
                                    interchangeTrailer,
                                    ifcsum,
                                    messageHeader,
                                    messageTrailer
                            );
                            if (dbUtils.checkDuplicateID(messageDao, "reg_data_in_json")) {
                                edifactHendlerDto = entityMapper.createEdifactDto(EdifactType.IFCSUM, jsonEntity, id, edifactDto.isSignatureMark());
                                String resultJsonIfcsum = javaToJson(edifactHendlerDto);
                                kafkaService.sendEdifactToHendler(id, resultJsonIfcsum);
                                dbUtils.updateRegDataIn(StatusCode.CONV_OK, messageDao);

                                RegDataInJsonEntity regDataInJsonEntity = entityMapper.toRegDataInJson(id, resultJsonIfcsum, "IFCSUM");
                                entityManager.persist(regDataInJsonEntity);
                                log.info("Successfully convert " + EdifactType.IFCSUM + " to JSON ID: " + id + " CONTRL REG_DATA_IN_JSON: " + resultJsonIfcsum);
                            }
                        }

                        if (messageObj instanceof Iftmin) {
                            Iftmin iftmin = (Iftmin) messageObj;
                            EdifactJsonEntity jsonEntity = entityMapper.createJsonEntity(
                                    interchangeHeader,
                                    interchangeTrailer,
                                    iftmin,
                                    messageHeader,
                                    messageTrailer
                            );
                            if (dbUtils.checkDuplicateID(messageDao, "reg_data_in_json")) {
                                edifactHendlerDto = entityMapper.createEdifactDto(EdifactType.IFTMIN, jsonEntity, id, edifactDto.isSignatureMark());
                                String resultJsonIftmin = javaToJson(edifactHendlerDto);
                                kafkaService.sendEdifactToHendler(id, resultJsonIftmin);
                                dbUtils.updateRegDataIn(StatusCode.CONV_OK, messageDao);

                                RegDataInJsonEntity regDataInJsonEntity = entityMapper.toRegDataInJson(id, resultJsonIftmin, "IFTMIN");
                                entityManager.persist(regDataInJsonEntity);
                                log.info("Successfully convert " + EdifactType.IFTMIN + " to JSON ID: " + id + " CONTRL REG_DATA_IN_JSON: " + resultJsonIftmin);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.info("Fail in convertDocumentToJson: " + e.getMessage());
        }
    }

    @Override
    public void convJsonToDocument(MessageDtoKfk message) {
        log.info(message.getId() + " " + message.getData());
    }

    /**
     * @param text Проверка Edifact документа на наличие сегмента UNOA
     * @return замена UNOA на UNOY для конвертации
     */
    private String refactorEdifactJson(String text) {
        if (text.substring(3, 8).contains("UNOA")) {
            return text.replaceFirst("UNOA", "UNOY");
        } else {
            return text;
        }
    }

    /**
     * @param text Проверка Edifact документа на наличие сегмента UNOY
     * @return замена UNOY на UNOA для конвертации
     */
    private String refactorJsonEdifact(String text) {
        if (text.substring(3, 8).contains("UNOY")) {
            return text.replaceFirst("UNOY", "UNOA");
        } else {
            return text;
        }
    }

    /**
     * @param edifactMsg message in edifact format
     * @return returns a string in json format
     */
    static String javaToJson(Object edifactMsg) throws JsonProcessingException {
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(edifactMsg);
    }
}
