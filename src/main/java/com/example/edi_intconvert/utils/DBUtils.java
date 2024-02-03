package com.example.edi_intconvert.utils;

import com.example.edi_intconvert.dao.MessageDao;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigInteger;

/**
 * @author RybakovSV
 */
@Component
@Slf4j
public class DBUtils {

    @PersistenceContext
    EntityManager entityManager;

    /**
     * @param errorCode - объект статуса, содержащий код и сообщение статуса
     * @param msg - объект сообщения кафки "ключ-значение"
     */
    public void updateRegDataIn(StatusCode errorCode, MessageDao msg){
        try {
            Query query = entityManager.createNativeQuery("UPDATE edi_reg.reg_data_in SET status = ?, error_message = ? WHERE id = ?");
            query.setParameter(1, errorCode.getCode());
            query.setParameter(2, errorCode.getDescription());
            query.setParameter(3, msg.getId());
            query.executeUpdate();
            log.info("Update status in RegDataIn table with ID: " + msg.getId());
        } catch (Exception e) {
            log.error("Ошибка в методе updateRegDataIn with ID: " + msg.getId());
            log.error(e.getMessage());
        }
    }

    /**
     * @param errorCode - объект статуса, содержащий код и сообщение статуса
     * @param msg - объект сообщения кафки "ключ-значение"
     */
    public void updateRegDataOut(StatusCode errorCode, MessageDao msg){

        try {
            Query query = entityManager.createNativeQuery("UPDATE edi_reg.reg_data_out SET status = ?, error_message = ? WHERE id = ?");
            query.setParameter(1, errorCode.getCode());
            query.setParameter(2, errorCode.getDescription()); //todo кроме сообщения из errorCode дописать в таблицу саму ошибку из лога
            query.setParameter(3, msg.getId());
            query.executeUpdate();
        } catch (Exception e) {
            log.error("Ошибка в методе updateRegDataOut");
            log.error(e.getMessage());
        }
    }

    /**
     * @param msg - входящее сообщение
     * @param nameTable - имя проверяемой таблицы в БД
     * @return возвращает True, если дублирование не обнаружено
     */
    public boolean checkDuplicateID(MessageDao msg, String nameTable) {
        boolean duplicationMark = true;
        try {
            Query query = entityManager.createNativeQuery("select count(*) from edi_reg." + nameTable + " where id=?");
            query.setParameter(1, msg.getId());
            if (((Long) query.getSingleResult()).intValue() != 0) {
                duplicationMark = false;
                log.warn("Попытка повторной записи сообщения " + msg.getId() + " в таблицу " + nameTable);
            }
        } catch (Exception e) {
            log.error("Ошибка при проверке дублирования ID в таблице " + nameTable, e);
            duplicationMark = false;
        }
        return duplicationMark;
    }

}
