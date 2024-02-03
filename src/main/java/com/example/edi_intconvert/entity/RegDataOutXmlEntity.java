package com.example.edi_intconvert.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

import lombok.Data;

@Data
@Entity
@Table(name = "reg_data_out_xml")
public class RegDataOutXmlEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "recipient_code")
    private String recipientCode;

    @Column(name = "date_write", updatable = false, insertable = false)
    private Date dateWrite;

    @Column(name = "message_xml")
    private String messageXml;


}