package com.laboratorio.getrapiinterface.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.getrapiinterface.GettrNotificationApi;
import com.laboratorio.getrapiinterface.exception.GettrApiException;
import com.laboratorio.getrapiinterface.modelo.GettrCredential;
import com.laboratorio.getrapiinterface.modelo.GettrNotification;
import com.laboratorio.getrapiinterface.modelo.response.GettrNotificationListResponse;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 10/09/2024
 * @updated 23/09/2024
 */
public class GettrNotificationApiImpl extends GettrBaseApi implements GettrNotificationApi {
    public GettrNotificationApiImpl(String accountId, String accessToken) {
        super(accountId, accessToken);
    }

    @Override
    public GettrNotificationListResponse getAllNotifications() {
        return this.getAllNotifications(null);
    }
    
    // Función que devuelve una página de notificaciones de una cuenta
    private GettrNotificationListResponse getNotificationPage(String uri, int okStatus, String posicionInicial) {
        try {
            GettrCredential credential = new GettrCredential(this.accountId, this.accessToken);
            String credentialStr = gson.toJson(credential);
            
            ApiRequest request = new ApiRequest(uri, okStatus);
            request.addApiPathParam("max", "20");
            if (posicionInicial != null) {
                request.addApiPathParam("tagUp", posicionInicial);
            }
            
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("X-app-auth", credentialStr);
            
            String jsonStr = this.client.executeGetRequest(request);
            
            JsonObject jsonObject = JsonParser.parseString(jsonStr).getAsJsonObject();
            JsonObject jsonObjectResult = jsonObject.get("result").getAsJsonObject();
            String cursor = jsonObjectResult.get("next").getAsString();
            
            JsonArray jsonArray = jsonObjectResult.get("recordList").getAsJsonArray();
            Type listType = new TypeToken<List<GettrNotification>>() {}.getType();
            List<GettrNotification> notifications = this.gson.fromJson(jsonArray, listType);
            String nuevaPosicionInicial = null;
            if (!notifications.isEmpty()) {
                nuevaPosicionInicial = Long.toString(notifications.get(0).getCdate());
            }
            
            return new GettrNotificationListResponse(nuevaPosicionInicial, cursor, notifications);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new GettrApiException(GettrNotificationApiImpl.class.getName(), e.getMessage());
        }
    }

    @Override
    public GettrNotificationListResponse getAllNotifications(String posicionInicial) {
        String endpoint = this.apiConfig.getProperty("getNotifications_endpoint");
        String complementoUrl = this.apiConfig.getProperty("getNotifications_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getNotifications_ok_status"));
        String nuevaPosicionInicial = posicionInicial;
        
        List<GettrNotification> notifications = null;
        boolean continuar = true;
        String cursor = null;
        
        try {
            String uri = endpoint + "/" + this.accountId + "/" + complementoUrl;
            
            do {
                GettrNotificationListResponse notificationListResponse = this.getNotificationPage(uri, okStatus, cursor);
                if (notifications == null) {
                    if (notificationListResponse.getLastNotif() != null) {
                        nuevaPosicionInicial = notificationListResponse.getLastNotif();
                    }
                    notifications = notificationListResponse.getNotifications();
                } else {
                    notifications.addAll(notificationListResponse.getNotifications());
                }
                
                cursor = notificationListResponse.getCursor();
                log.debug("getFollowers. Recuperados: " + notifications.size() + ". Cursor: " + cursor);
                if (notificationListResponse.getNotifications().isEmpty()) {
                    continuar = false;
                } else {
                    if (cursor.equals("0")) {
                        continuar = false;
                    }
                    
                    if (posicionInicial != null) {
                        List<GettrNotification> list = notificationListResponse.getNotifications();
                        for (int i = list.size() - 1; i >= 0; i--) {
                            long notiftime = list.get(i).getCdate();
                            if (notiftime < Long.parseLong(posicionInicial)) {
                                continuar = false;
                                break;
                            }
                        }
                    }
                }
            } while (continuar);
            
            if (posicionInicial == null) {
                return new GettrNotificationListResponse(nuevaPosicionInicial, null, notifications);
            }
            
            List<GettrNotification> filtredNotifications = notifications.stream()
                    .filter(n -> n.getCdate() > Long.parseLong(posicionInicial))
                    .collect(Collectors.toList());
            log.debug("filtredNotificationsList size: " + filtredNotifications.size());
            return new GettrNotificationListResponse(nuevaPosicionInicial, null, filtredNotifications);
        } catch (Exception e) {
            throw e;
        }
    }
}