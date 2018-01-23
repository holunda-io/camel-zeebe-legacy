package io.zeebe.camel.example.amqp.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {

    private String content;
    private Date timestamp;

}
