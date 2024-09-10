package com.laboratorio.getrapiinterface.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 10/09/2024
 * @updated 10/09/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GettrNotifUserInfo {
    private String i;   // UserID
    private String n;   // Username
    private String o;
    private String p;   // User picture
    private String l;
}