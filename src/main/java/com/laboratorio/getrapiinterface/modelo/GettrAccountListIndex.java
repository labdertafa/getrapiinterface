package com.laboratorio.getrapiinterface.modelo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 05/09/2024
 * @updated 05/09/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GettrAccountListIndex {
    private long update;
    private String _t;
    private long cdate;
    private List<String> list;
    private String _id;
}