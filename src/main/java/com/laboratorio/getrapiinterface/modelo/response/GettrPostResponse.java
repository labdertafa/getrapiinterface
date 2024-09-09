package com.laboratorio.getrapiinterface.modelo.response;

import com.laboratorio.getrapiinterface.modelo.request.GettrPostRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 07/09/2024
 * @updated 07/09/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GettrPostResponse {
    private String _t;
    private String rc;
    private GettrPostRequest result;
}