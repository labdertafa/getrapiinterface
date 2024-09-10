package com.laboratorio.getrapiinterface.modelo.response;

import com.laboratorio.getrapiinterface.modelo.GettrNotification;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 10/09/2024
 * @updated 10/09/2024
 */

@Getter @Setter @AllArgsConstructor
public class GettrNotificationListResponse {
    private String lastNotif;
    private String cursor;
    private List<GettrNotification> notifications;
}