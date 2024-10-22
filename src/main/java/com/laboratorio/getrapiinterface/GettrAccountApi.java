package com.laboratorio.getrapiinterface;

import com.laboratorio.getrapiinterface.modelo.GettrAccount;
import com.laboratorio.getrapiinterface.modelo.GettrRelationship;
import com.laboratorio.getrapiinterface.modelo.response.GettrAccountListResponse;
import java.util.List;

/**
 *
 * @author Rafael
 * @version 1.2
 * @created 05/09/2024
 * @updated 22/10/2024
 */
public interface GettrAccountApi {
    GettrAccount getAccountById(String userId);
    GettrAccount getAccountByUsername(String username);
    
    GettrAccountListResponse getFollowers(String userId);
    GettrAccountListResponse getFollowers(String userId, int quantity);
    GettrAccountListResponse getFollowers(String userId, int quantity, String posicionInicial);
    
    List<String> getFollowersIds(String userId);
    
    GettrAccountListResponse getFollowings(String userId);
    GettrAccountListResponse getFollowings(String userId, int quantity);
    GettrAccountListResponse getFollowings(String userId, int quantity, String posicionInicial);
    
    List<String> getFollowingsIds(String userId);
    
    // Seguir a un usuario
    boolean followAccount(String userId);
    // Dejar de seguir a un usuario
    boolean unfollowAccount(String userId);
    
    // Chequea la relación con otro usuario
    GettrRelationship checkrelationship(String userId);
}