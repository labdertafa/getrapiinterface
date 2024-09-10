package com.laboratorio.getrapiinterface;

import com.laboratorio.getrapiinterface.modelo.response.GettrNotificationListResponse;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 10/09/2024
 * @updated 10/09/2024
 */
public interface GettrNotificationApi {
    GettrNotificationListResponse getAllNotifications(); 
    GettrNotificationListResponse getAllNotifications(String posicionInicial);
}