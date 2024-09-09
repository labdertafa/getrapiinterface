package com.laboratorio.getrapiinterface;

import com.laboratorio.getrapiinterface.modelo.GettrAccount;
import com.laboratorio.getrapiinterface.modelo.GettrRelationship;
import java.util.List;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 05/09/2024
 * @updated 08/09/2024
 */
public interface GettrAccountApi {
    GettrAccount getAccountByUsername(String username);
    
    List<GettrAccount> getFollowers(String userId);
    List<GettrAccount> getFollowers(String userId, int quantity);
    
    List<GettrAccount> getFollowings(String userId);
    List<GettrAccount> getFollowings(String userId, int quantity);
    
    // Seguir a un usuario
    boolean followAccount(String userId);
    // Dejar de seguir a un usuario
    boolean unfollowAccount(String userId);
    
    // Chequea la relación con otro usuario
    GettrRelationship checkrelationship(String userId);
}