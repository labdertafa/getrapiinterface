package com.laboratorio.getrapiinterface.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 13/10/2024
 * @updated 13/10/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GettrStatusHeader {
    private long update;
    private String _t;
    private long cdate;
    private String receiver_id;
    private GettrActivity activity;
    private String _id;
    private String action;
}