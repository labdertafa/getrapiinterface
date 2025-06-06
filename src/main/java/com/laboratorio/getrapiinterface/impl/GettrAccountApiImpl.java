package com.laboratorio.getrapiinterface.impl;

import com.laboratorio.clientapilibrary.model.ApiMethodType;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.clientapilibrary.model.ApiResponse;
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
import java.util.stream.Collectors;

/**
 *
 * @author Rafael
 * @version 1.3
 * @created 05/09/2024
 * @updated 06/06/2025
 */
public class GettrAccountApiImpl extends GettrBaseApi implements GettrAccountApi {
    public GettrAccountApiImpl(String accountId, String accessToken) {
        super(accountId, accessToken);
    }
    
    @Override
    public GettrAccount getAccountById(String userId) {
        return this.getAccountByUsername(userId);
    }

    @Override
    public GettrAccount getAccountByUsername(String username) {
        String endpoint = this.apiConfig.getProperty("getAccountByUsername_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getAccountByUsername_ok_status"));
        
        try {
            String url = endpoint + "/" + username;
            ApiRequest request = new ApiRequest(url, okStatus, ApiMethodType.GET);
            request.addApiHeader("Content-Type", "application/json");
            
            ApiResponse response = this.client.executeApiRequest(request);
            log.debug("Response getAccountByUsername: {}", response.getResponseStr());
            
            GettrAccountResponse accountResponse = this.gson.fromJson(response.getResponseStr(), GettrAccountResponse.class);
            return accountResponse.getResult().getData();
        } catch (Exception e) {
            throw new GettrApiException("Error recuperando los datos del usuario Gettr con username: " + username, e);
        }
    }

    @Override
    public GettrAccountListResponse getFollowers(String userId) {
        return this.getFollowers(userId, 0);
    }

    @Override
    public GettrAccountListResponse getFollowers(String userId, int quantity) {
        return this.getFollowers(userId, quantity, null);
    }
    
    @Override
    public GettrAccountListResponse getFollowers(String userId, int quantity, String posicionInicial) {
        String endpoint = this.apiConfig.getProperty("getFollowers_endpoint");
        String complementoUrl = this.apiConfig.getProperty("getFollowers_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getFollowers_ok_status"));
        String uri = endpoint + "/" + userId + "/" + complementoUrl;
        
        return this.getAccountList(uri, okStatus, quantity, posicionInicial);
    }
    
    @Override
    public List<String> getFollowersIds(String userId) {
        GettrAccountListResponse response = this.getFollowers(userId, 0, null);
        return response.getAccounts().stream()
                .map(account -> account.get_id())
                .collect(Collectors.toList());
    }

    @Override
    public GettrAccountListResponse getFollowings(String userId) {
        return this.getFollowings(userId, 0);
    }

    @Override
    public GettrAccountListResponse getFollowings(String userId, int quantity) {
        return this.getFollowings(userId, quantity, null);
    }
    
    @Override
    public GettrAccountListResponse getFollowings(String userId, int quantity, String posicionInicial) {
        String endpoint = this.apiConfig.getProperty("getFollowings_endpoint");
        String complementoUrl = this.apiConfig.getProperty("getFollowings_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getFollowings_ok_status"));
        String uri = endpoint + "/" + userId + "/" + complementoUrl;
        
        return this.getAccountList(uri, okStatus, quantity, posicionInicial);
    }
    
    @Override
    public List<String> getFollowingsIds(String userId) {
        GettrAccountListResponse response = this.getFollowings(userId, 0, null);
        return response.getAccounts().stream()
                .map(account -> account.get_id())
                .collect(Collectors.toList());
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
            
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.POST);
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("X-app-auth", credentialStr);
            
            ApiResponse response = this.client.executeApiRequest(request);
            log.debug("Response followAccount: {}", response.getResponseStr());
            GettrFollowResponse followResponse = this.gson.fromJson(response.getResponseStr(), GettrFollowResponse.class);
            
            return followResponse.getRc().equals("OK");
        } catch (Exception e) {
            throw new GettrApiException("Error siguiendo al usuario Gettr con id: " + userId, e);
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
            
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.POST);
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("X-app-auth", credentialStr);
            
            ApiResponse response = this.client.executeApiRequest(request);
            log.debug("Response unfollowAccount: {}", response.getResponseStr());
            GettrFollowResponse followResponse = this.gson.fromJson(response.getResponseStr(), GettrFollowResponse.class);
            
            return followResponse.getRc().equals("OK");
        } catch (Exception e) {
            throw new GettrApiException("Error dejando de seguir al usuario Gettr con id: " + userId, e);
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
            
            ApiRequest request1 = new ApiRequest(uri1, okStatus, ApiMethodType.GET);
            request1.addApiHeader("Content-Type", "application/json");
            request1.addApiHeader("X-app-auth", credentialStr);
            
            ApiResponse response1 = this.client.executeApiRequest(request1);
            log.debug("Response checkrelationship 1: {}", response1.getResponseStr());
            GettrRelationshipResponse relationshipResponse1 = this.gson.fromJson(response1.getResponseStr(), GettrRelationshipResponse.class);
            if (!relationshipResponse1.getRc().equals("OK")) {
                throw new GettrApiException("Ha ocurrido un error verificando la relación con " + userId);
            }
            boolean following = false;
            if (relationshipResponse1.getResult().equalsIgnoreCase("y")) {
                following = true;
            }
            
            String uri2 = endpoint + "/" + userId + "/" + complementoUrl + "/" + this.accountId;
            
            ApiRequest request2 = new ApiRequest(uri2, okStatus, ApiMethodType.GET);
            request2.addApiHeader("Content-Type", "application/json");
            request2.addApiHeader("X-app-auth", credentialStr);
            
            ApiResponse response2 = this.client.executeApiRequest(request2);
            log.debug("Response checkrelationship 2: {}", response2.getResponseStr());
            GettrRelationshipResponse relationshipResponse2 = this.gson.fromJson(response2.getResponseStr(), GettrRelationshipResponse.class);
            if (!relationshipResponse2.getRc().equals("OK")) {
                throw new GettrApiException("Ha ocurrido un error verificando la relación con " + userId);
            }
            boolean followedBy = false;
            if (relationshipResponse2.getResult().equalsIgnoreCase("y")) {
                followedBy = true;
            }
            
            return new GettrRelationship(userId, following, followedBy);
        } catch (GettrApiException e) {
            throw e;
        } catch (Exception e) {
            throw new GettrApiException("Error comprobando la relación con el usuario Gettr con id: " + userId, e);
        }
    }
}