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
 * @created 13/10/2024
 * @updated 13/10/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GettrStatusListIndex {
    private long update;
    private String _t;
    private long cdate;
    private List<GettrStatusHeader> list;
    private String _id;
}