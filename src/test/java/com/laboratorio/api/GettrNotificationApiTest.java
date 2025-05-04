package com.laboratorio.api;

import com.laboratorio.clientapilibrary.utils.ReaderConfig;
import com.laboratorio.getrapiinterface.GettrNotificationApi;
import com.laboratorio.getrapiinterface.impl.GettrNotificationApiImpl;
import com.laboratorio.getrapiinterface.modelo.response.GettrNotificationListResponse;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 10/09/2024
 * @updated 04/05/2025
 */
public class GettrNotificationApiTest {
    private static GettrNotificationApi notificationApi;
    
    @BeforeEach
    public void initTest() {
        ReaderConfig config = new ReaderConfig("config//gettr_api.properties");
        String accessToken = config.getProperty("access_token");
        String accountId = config.getProperty("usuario_gettr");
        notificationApi = new GettrNotificationApiImpl(accountId, accessToken);
    }
    
    @Test
    public void getAllNotifications() {
        GettrNotificationListResponse response = notificationApi.getAllNotifications();
        
        assertEquals(null, response.getCursor());
        assertTrue(response.getNotifications().size() > 20);
    }
    
    @Test
    public void getAllNotificationsWithInitialPosition() {
        String lastNotif = "1725722527660";
        
        GettrNotificationListResponse response = notificationApi.getAllNotifications(lastNotif);
        
        assertEquals(null, response.getCursor());
        assertTrue(!response.getNotifications().isEmpty());
    }
}