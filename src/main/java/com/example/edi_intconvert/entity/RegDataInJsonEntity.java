package com.example.edi_intconvert.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.Data;

@Data
@Entity
@Table(name = "reg_data_in_json", schema = "edi_reg")
public class RegDataInJsonEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "sender_code")
    private String senderCode;

    @Column(name = "date_write", updatable = false, insertable = false)
    private Date dateWrite;

    @Column(name = "message_json")
    private String convertedJson;

}