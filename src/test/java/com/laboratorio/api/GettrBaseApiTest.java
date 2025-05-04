package com.laboratorio.api;

import com.laboratorio.clientapilibrary.utils.ReaderConfig;
import com.laboratorio.getrapiinterface.impl.GettrAccountApiImpl;
import com.laboratorio.getrapiinterface.impl.GettrBaseApi;
import com.laboratorio.getrapiinterface.modelo.response.GettrAccountListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 05/09/2024
 * @updated 04/05/2025
 */
public class GettrBaseApiTest {
    private static GettrBaseApi baseApi;
    
    @BeforeEach
    public void initTest() {
        ReaderConfig config = new ReaderConfig("config//gettr_api.properties");
        String accessToken = config.getProperty("access_token");
        String accountId = config.getProperty("usuario_gettr");
        baseApi = new GettrAccountApiImpl(accountId, accessToken);
    }
    
    @Test
    public void getAccountPageTest() {
        ReaderConfig config = new ReaderConfig("config//gettr_api.properties");
        String endpoint = config.getProperty("getFollowers_endpoint");
        String complementoUrl = config.getProperty("getFollowers_complemento_url");
        int okStatus = Integer.parseInt(config.getProperty("getFollowers_ok_status"));
        
        String uri = endpoint + "/labrafa/" + complementoUrl;
        
        GettrAccountListResponse response = baseApi.getAccountPage(uri, okStatus, null);
        
        assertTrue(!response.getAccounts().isEmpty());
    }
    
    @Test
    public void getAccountListTest() {
        ReaderConfig config = new ReaderConfig("config//gettr_api.properties");
        String endpoint = config.getProperty("getFollowers_endpoint");
        String complementoUrl = config.getProperty("getFollowers_complemento_url");
        int okStatus = Integer.parseInt(config.getProperty("getFollowers_ok_status"));
        
        String uri = endpoint + "/cokyMendoza/" + complementoUrl;
        
        GettrAccountListResponse response = baseApi.getAccountList(uri, okStatus, 0, null);
        
        assertTrue(response.getAccounts().size() > 100);
    }
}