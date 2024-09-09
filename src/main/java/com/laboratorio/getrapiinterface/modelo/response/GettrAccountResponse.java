package com.laboratorio.getrapiinterface.modelo.response;

import com.laboratorio.getrapiinterface.modelo.GettrAccountResult;
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
public class GettrAccountResponse {
    private String _t;
    private String rc;
    private GettrAccountResult result;
}