package com.laboratorio.getrapiinterface.impl;

import com.google.gson.JsonSyntaxException;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.getrapiinterface.GettrAccountApi;
import com.laboratorio.getrapiinterface.exception.GettrApiException;
import com.laboratorio.getrapiinterface.modelo.GettrAccount;
import com.laboratorio.getrapiinterface.modelo.GettrCredential;
import com.laboratorio.getrapiinterface.modelo.GettrRelationship;
import com.laboratorio.getrapiinterface.modelo.response.GettrAccountListResponse;
import com.laboratorio.getrapiinterface.modelo.response.GettrAccountResponse;
import com.laboratorio.getrapiinterface.modelo.response.GettrFollowResponse;
import com.laboratorio.getrapiinterface.modelo.response.GettrRelationshipResponse;
import java.util.List;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 05/09/2024
 * @updated 08/09/2024
 */
public class GettrAccountApiImpl extends GettrBaseApi implements GettrAccountApi {
    public GettrAccountApiImpl(String accountId, String accessToken) {
        super(accountId, accessToken);
    }

    @Override
    public GettrAccount getAccountByUsername(String username) {
        String endpoint = this.apiConfig.getProperty("getAccountByUsername_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getAccountByUsername_ok_status"));
        
        try {
            String url = endpoint + "/" + username;
            ApiRequest request = new ApiRequest(url, okStatus);
            request.addApiHeader("Content-Type", "application/json");
            
            String jsonStr = this.client.executeGetRequest(request);
            
            GettrAccountResponse accountResponse = this.gson.fromJson(jsonStr, GettrAccountResponse.class);
            return accountResponse.getResult().getData();
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new GettrApiException(GettrAccountApiImpl.class.getName(), e.getMessage());
        }
    }

    @Override
    public GettrAccountListResponse getFollowers(String userId) {
        return this.getFollowers(userId, 0);
    }

    @Override
    public GettrAccountListResponse getFollowers(String userId, int quantity) {
        String endpoint = this.apiConfig.getProperty("getFollowers_endpoint");
        String complementoUrl = this.apiConfig.getProperty("getFollowers_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getFollowers_ok_status"));
        String uri = endpoint + "/" + userId + "/" + complementoUrl;
        
        return this.getAccountList(uri, okStatus, quantity, null);
    }

    @Override
    public GettrAccountListResponse getFollowings(String userId) {
        return this.getFollowings(userId, 0);
    }

    @Override
    public GettrAccountListResponse getFollowings(String userId, int quantity) {
        String endpoint = this.apiConfig.getProperty("getFollowings_endpoint");
        String complementoUrl = this.apiConfig.getProperty("getFollowings_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getFollowings_ok_status"));
        String uri = endpoint + "/" + userId + "/" + complementoUrl;
        
        return this.getAccountList(uri, okStatus, quantity, null);
    }

    @Override
    public boolean followAccount(String userId) {
        String endpoint = this.apiConfig.getProperty("followAccount_endpoint");
        String complementoUrl = this.apiConfig.getProperty("followAccount_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("followAccount_ok_status"));
        
        try {
            String uri = endpoint + "/" + this.accountId + "/" + complementoUrl + "/" + userId;
            GettrCredential credential = new GettrCredential(this.accountId, this.accessToken);
            String credentialStr = gson.toJson(credential);
            
            ApiRequest request = new ApiRequest(uri, okStatus);
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("X-app-auth", credentialStr);
            
            String jsonStr = this.client.executePostRequest(request);
            GettrFollowResponse response = this.gson.fromJson(jsonStr, GettrFollowResponse.class);
            
            return response.getRc().equals("OK");
        } catch (Exception e) {
            throw new GettrApiException(GettrAccountApiImpl.class.getName(), e.getMessage());
        }
    }

    @Override
    public boolean unfollowAccount(String userId) {
        String endpoint = this.apiConfig.getProperty("unfollowAccount_endpoint");
        String complementoUrl = this.apiConfig.getProperty("unfollowAccount_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("unfollowAccount_ok_status"));
        
        try {
            String uri = endpoint + "/" + this.accountId + "/" + complementoUrl + "/" + userId;
            GettrCredential credential = new GettrCredential(this.accountId, this.accessToken);
            String credentialStr = gson.toJson(credential);
            
            ApiRequest request = new ApiRequest(uri, okStatus);
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("X-app-auth", credentialStr);
            
            String jsonStr = this.client.executePostRequest(request);
            GettrFollowResponse response = this.gson.fromJson(jsonStr, GettrFollowResponse.class);
            
            return response.getRc().equals("OK");
        } catch (Exception e) {
            throw new GettrApiException(GettrAccountApiImpl.class.getName(), e.getMessage());
        }
    }

    @Override
    public GettrRelationship checkrelationship(String userId) {
        String endpoint = this.apiConfig.getProperty("checkrelationships_endpoint");
        String complementoUrl = this.apiConfig.getProperty("checkrelationships_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("checkrelationships_ok_status"));
        
        try {
            String uri1 = endpoint + "/" + this.accountId + "/" + complementoUrl + "/" + userId;
            GettrCredential credential = new GettrCredential(this.accountId, this.accessToken);
            String credentialStr = gson.toJson(credential);
            
            ApiRequest request1 = new ApiRequest(uri1, okStatus);
            request1.addApiHeader("Content-Type", "application/json");
            request1.addApiHeader("X-app-auth", credentialStr);
            
            String jsonStr1 = this.client.executeGetRequest(request1);
            GettrRelationshipResponse response1 = this.gson.fromJson(jsonStr1, GettrRelationshipResponse.class);
            if (!response1.getRc().equals("OK")) {
                throw new Exception("Ha ocurrido un error verificando la relación con " + userId);
            }
            boolean following = false;
            if (response1.getResult().equalsIgnoreCase("y")) {
                following = true;
            }
            
            String uri2 = endpoint + "/" + userId + "/" + complementoUrl + "/" + this.accountId;
            
            ApiRequest request2 = new ApiRequest(uri2, okStatus);
            request2.addApiHeader("Content-Type", "application/json");
            request2.addApiHeader("X-app-auth", credentialStr);
            
            String jsonStr2 = this.client.executeGetRequest(request2);
            GettrRelationshipResponse response2 = this.gson.fromJson(jsonStr2, GettrRelationshipResponse.class);
            if (!response2.getRc().equals("OK")) {
                throw new Exception("Ha ocurrido un error verificando la relación con " + userId);
            }
            boolean followedBy = false;
            if (response2.getResult().equalsIgnoreCase("y")) {
                followedBy = true;
            }
            
            return new GettrRelationship(userId, following, followedBy);
        } catch (Exception e) {
            throw new GettrApiException(GettrAccountApiImpl.class.getName(), e.getMessage());
        }
    }
}