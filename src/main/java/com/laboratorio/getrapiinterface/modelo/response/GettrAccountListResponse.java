package com.laboratorio.getrapiinterface.modelo.response;

import com.laboratorio.getrapiinterface.modelo.GettrAccount;
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
public class GettrAccountListResponse {
    private List<GettrAccount> accounts;
    private String cursor;
}