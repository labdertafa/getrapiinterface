package com.laboratorio.getrapiinterface.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 08/09/2024
 * @updated 08/09/2024
 */

@Getter @Setter @AllArgsConstructor
public class GettrRelationship {
    private String userId;
    private boolean following;
    private boolean followedBy;
}