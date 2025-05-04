package com.laboratorio.getrapiinterface.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.laboratorio.clientapilibrary.ApiClient;
import com.laboratorio.clientapilibrary.model.ApiMethodType;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.clientapilibrary.model.ApiResponse;
import com.laboratorio.clientapilibrary.utils.ReaderConfig;
import com.laboratorio.getrapiinterface.exception.GettrApiException;
import com.laboratorio.getrapiinterface.modelo.GettrAccount;
import com.laboratorio.getrapiinterface.modelo.GettrAccountListIndex;
import com.laboratorio.getrapiinterface.modelo.response.GettrAccountListResponse;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 05/09/2024
 * @updated 04/05/2025
 */
public class GettrBaseApi {
    protected static final Logger log = LogManager.getLogger(GettrBaseApi.class);
    protected final ApiClient client;
    protected final String accountId;
    protected final String accessToken;
    protected final ReaderConfig apiConfig;
    protected final Gson gson;

    public GettrBaseApi(String accountId, String accessToken) {
        this.client = new ApiClient();
        this.accountId = accountId;
        this.accessToken = accessToken;
        this.apiConfig = new ReaderConfig("config//gettr_api.properties");
        this.gson = new Gson();
    }
    
    protected void logException(Exception e) {
        log.error("Error: " + e.getMessage());
        if (e.getCause() != null) {
            log.error("Causa: " + e.getCause().getMessage());
        }
    }
    
    // Función que devuelve una página de seguidores o seguidos de una cuenta
    public GettrAccountListResponse getAccountPage(String uri, int okStatus, String posicionInicial) {
        try {
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.GET);
            if (posicionInicial != null) {
                request.addApiPathParam("cursor", posicionInicial);
            }
            request.addApiHeader("Content-Type", "application/json");
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            JsonObject jsonObject = JsonParser.parseString(response.getResponseStr()).getAsJsonObject();
            JsonObject jsonObjectResult = jsonObject.get("result").getAsJsonObject();
            
            GettrAccountListIndex listIndex = this.gson.fromJson(jsonObjectResult.get("data"), GettrAccountListIndex.class);
            log.debug("Número de cuentas obtenidas en la respuesta: " + listIndex.getList().size());
            
            JsonObject jsonObjectDetail = jsonObjectResult.get("aux").getAsJsonObject();
            String cursor = jsonObjectDetail.get("cursor").getAsString();
            log.debug("Valor obtenido para el cursor: " + cursor);
            
            JsonObject jsonObjectUserInfo = jsonObjectDetail.get("uinf").getAsJsonObject();
            
            List<GettrAccount> accounts = new ArrayList<>();
            for (String username : listIndex.getList()) {
                GettrAccount account = this.gson.fromJson(jsonObjectUserInfo.get(username), GettrAccount.class);
                accounts.add(account);
                log.debug("Valores obtenidos para el usuario: " + account.toString());
            }
            
            return new GettrAccountListResponse(accounts, cursor);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new GettrApiException(GettrBaseApi.class.getName(), e.getMessage());
        }
    }
    
    public GettrAccountListResponse getAccountList(String uri, int okStatus, int quantity, String posicionInicial) {
        List<GettrAccount> accounts = null;
        boolean continuar = true;
        String cursor = posicionInicial;
        
        try {
            do {
                GettrAccountListResponse accountListResponse = this.getAccountPage(uri, okStatus, cursor);
                if (accounts == null) {
                    accounts = accountListResponse.getAccounts();
                } else {
                    accounts.addAll(accountListResponse.getAccounts());
                }
                
                cursor = accountListResponse.getCursor();
                log.debug("Cantidad: " + quantity + ". Recuperados: " + accounts.size() + ". Cursor: " + cursor);
                if (quantity > 0) {
                    if ((accounts.size() >= quantity) || (cursor.equals("0"))) {
                        continuar = false;
                    }
                } else {
                    if (cursor.equals("0")) {
                        continuar = false;
                    }
                }
            } while (continuar);

            if (quantity == 0) {
                return new GettrAccountListResponse(accounts, cursor);
            }
            
            return new GettrAccountListResponse(accounts.subList(0, Math.min(quantity, accounts.size())), cursor);
        } catch (Exception e) {
            throw e;
        }
    }
}