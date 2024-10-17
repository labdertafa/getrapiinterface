package com.laboratorio.api;

import com.laboratorio.getrapiinterface.GettrStatusApi;
import com.laboratorio.getrapiinterface.impl.GettrStatusApiImpl;
import com.laboratorio.getrapiinterface.modelo.GettrStatus;
import com.laboratorio.getrapiinterface.utiles.GettrApiConfig;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 08/09/2024
 * @updated 17/10/2024
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GettrStatusApiTest {
    protected static final Logger log = LogManager.getLogger(GettrStatusApiTest.class);
    private static GettrStatusApi statusApi;
    private static String accountId;
    private static String postId;
    
    @BeforeEach
    public void initTest() {
        GettrApiConfig config = GettrApiConfig.getInstance();
        String accessToken = config.getProperty("access_token");
        accountId = config.getProperty("usuario_gettr");
        statusApi = new GettrStatusApiImpl(accountId, accessToken);
    }
    
    @Test @Order(1)
    public void postTextStatus() {
        String texto = "Creando una publicación de prueba con una URL desde el navegador a ver que tal va:\n\nhttps://laboratoriorafa.mooo.com:8443/WebLaboratorio/\n\n#siguemeytesigo #Followback\n";
        
        GettrStatus status = statusApi.postStatus(texto);
        postId = status.get_id();
        
        assertEquals(accountId, status.getUid());
    }
    
    @Test @Order(2)
    public void deleteStatus() {
        boolean result = statusApi.deleteStatus(postId);
        
        assertTrue(result);
    }
    
    @Test
    public void uploadImage() {
        String filePath = "C:\\Users\\rafa\\Pictures\\Formula_1\\Spa_1950.jpg";
        
        String result = statusApi.uploadImage(filePath, "image/jpeg");
        
        assertTrue(!result.isEmpty());
    }
    
    @Test @Order(3)
    public void postImageStatus() {
        String texto = "Creando una publicación de prueba con una URL desde el navegador a ver que tal va:\n\nhttps://laboratoriorafa.mooo.com:8443/WebLaboratorio/\n\n#siguemeytesigo #Followback\n";
        String filePath = "C:\\Users\\rafa\\Pictures\\Formula_1\\Circuit_Aintree_1955.png";
        
        GettrStatus status = statusApi.postStatus(texto, filePath);
        postId = status.get_id();
        
        assertEquals(accountId, status.getUid());
    }
    
    @Test @Order(4)
    public void deleteStatusWithImage() {
        boolean result = statusApi.deleteStatus(postId);
        
        assertTrue(result);
    }
    
    @Test
    public void getGlobalTimeline() {
        int quantity = 50;
        
        List<GettrStatus> statuses = statusApi.getGlobalTimeline(quantity);
        int i = 0;
        for (GettrStatus status : statuses) {
            i++;
            log.info(i + "-) Status: " + status.toString());
        }
        
        assertEquals(quantity, statuses.size());
    }
}