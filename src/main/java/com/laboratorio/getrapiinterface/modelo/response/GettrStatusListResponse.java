package com.laboratorio.getrapiinterface.modelo.response;

import com.laboratorio.getrapiinterface.modelo.GettrStatus;
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
public class GettrStatusListResponse {
    private List<GettrStatus> accounts;
    private String cursor;
}