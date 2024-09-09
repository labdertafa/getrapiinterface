package com.laboratorio.api;

import com.laboratorio.getrapiinterface.GettrStatusApi;
import com.laboratorio.getrapiinterface.impl.GettrStatusApiImpl;
import com.laboratorio.getrapiinterface.modelo.response.GettrPostResponse;
import com.laboratorio.getrapiinterface.utiles.GettrApiConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 08/09/2024
 * @updated 09/09/2024
 */
public class GettrStatusApiTest {
    private static GettrStatusApi statusApi;
    private static String accountId;
    
    @BeforeEach
    public void initTest() {
        GettrApiConfig config = GettrApiConfig.getInstance();
        String accessToken = config.getProperty("access_token");
        accountId = config.getProperty("usuario_gettr");
        statusApi = new GettrStatusApiImpl(accountId, accessToken);
    }
    
    @Test
    public void postTextStatus() {
        String texto = "Creando una publicación de prueba con una URL desde el navegador a ver que tal va:\n\nhttps://laboratoriorafa.mooo.com:8443/WebLaboratorio/\n\n#siguemeytesigo #Followback\n";
        
        GettrPostResponse response = statusApi.postStatus(texto);
        
        assertTrue(response.getRc().equals("OK"));
        assertEquals(accountId, response.getResult().getData().getUid());
    }
    
    @Test
    public void uploadImage() {
        String filePath = "C:\\Users\\rafa\\Pictures\\Formula_1\\Spa_1950.jpg";
        
        String result = statusApi.uploadImage(filePath, "image/jpeg");
        
        assertTrue(!result.isEmpty());
    }
    
    @Test
    public void postImageStatus() {
        String texto = "Creando una publicación de prueba con una URL desde el navegador a ver que tal va:\n\nhttps://laboratoriorafa.mooo.com:8443/WebLaboratorio/\n\n#siguemeytesigo #Followback\n";
        String filePath = "C:\\Users\\rafa\\Pictures\\Formula_1\\Circuit_Aintree_1955.png";
        
        GettrPostResponse response = statusApi.postStatus(texto, filePath);
        
        assertTrue(response.getRc().equals("OK"));
        assertEquals(accountId, response.getResult().getData().getUid());
    }
}