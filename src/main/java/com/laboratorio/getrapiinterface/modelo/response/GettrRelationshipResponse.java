package com.laboratorio.getrapiinterface.modelo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 08/09/2024
 * @updated 08/09/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GettrRelationshipResponse {
    private String _t;
    private String rc;
    private String result;
}