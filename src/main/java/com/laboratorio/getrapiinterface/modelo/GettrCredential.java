package com.laboratorio.getrapiinterface.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 05/09/2024
 * @updated 05/09/2024
 */

@Getter @Setter @AllArgsConstructor
public class GettrCredential {
    private String user;
    private String token;
}