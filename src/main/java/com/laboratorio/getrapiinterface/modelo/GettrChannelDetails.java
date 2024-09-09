package com.laboratorio.getrapiinterface.modelo;

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
public class GettrChannelDetails {
    private String url;
    private String p_create_url;
    private String path;
    private String mime_type;
    private String p_part_url;
    private String p_complete_url;
}