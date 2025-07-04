package com.laboratorio.api;

import com.laboratorio.clientapilibrary.utils.ReaderConfig;
import com.laboratorio.getrapiinterface.GettrAccountApi;
import com.laboratorio.getrapiinterface.exception.GettrApiException;
import com.laboratorio.getrapiinterface.impl.GettrAccountApiImpl;
import com.laboratorio.getrapiinterface.modelo.GettrAccount;
import com.laboratorio.getrapiinterface.modelo.GettrRelationship;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 05/09/2024
 * @updated 06/06/2025
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GettrAccountApiTest {
    private static GettrAccountApi accountApi;
    
    @BeforeEach
    public void initTest() {
        ReaderConfig config = new ReaderConfig("config//gettr_api.properties");
        String accessToken = config.getProperty("access_token");
        String accountId = config.getProperty("usuario_gettr");
        accountApi = new GettrAccountApiImpl(accountId, accessToken);
    }
    
    @Test
    public void getAccountByUsername() {
        String username = "mayara2022";
        
        GettrAccount account = accountApi.getAccountByUsername(username);
        
        assertEquals(username, account.getUsername());
    }
    
    @Test
    public void getAccountByInvalidUsername()  {
        String username = "rimugu_LKDNLDSNJLAS";
        
        assertThrows(GettrApiException.class, () -> {
            accountApi.getAccountByUsername(username);
        });
    }
    
    @Test
    public void getFollowers() {
        String username = "rimugu";
        
        List<GettrAccount> accounts = accountApi.getFollowers(username).getAccounts();
        
        assertTrue(accounts.size() > 80);
    }
    
    @Test
    public void getFollowersInvalidUser()  {
        String userId = "rimugu_LKDNLDSNJLAS";
        
        assertThrows(GettrApiException.class, () -> {
            accountApi.getFollowers(userId);
        });
    }
    
    @Test
    public void getFollowings() {
        String username = "rimugu";
        
        List<GettrAccount> accounts = accountApi.getFollowings(username).getAccounts();
        
        assertTrue(accounts.size() > 50);
    }
    
    @Test
    public void getFollowingsInvalidUser()  {
        String userId = "rimugu_LKDNLDSNJLAS";
        
        assertThrows(GettrApiException.class, () -> {
            accountApi.getFollowings(userId);
        });
    }
    
    @Test @Order(1)
    public void followAccount()  {
        String userId = "rimugu";
        
        boolean result = accountApi.followAccount(userId);
        
        assertTrue(result);
    }
    
    @Test
    public void followInvalidAccount()  {
        String userId = "rimugu_LKDNLDSNJLAS";
        
        assertThrows(GettrApiException.class, () -> {
            accountApi.followAccount(userId);
        });
    }
    
    @Test @Order(2)
    public void unFollowAccount()  {
        String userId = "rimugu";
        
        boolean result = accountApi.unfollowAccount(userId);
        
        assertTrue(result);
    }
    
    @Test
    public void unfollowInvalidAccount()  {
        String userId = "rimugu_LKDNLDSNJLAS";
        
        assertThrows(GettrApiException.class, () -> {
            accountApi.unfollowAccount(userId);
        });
    }
    
    @Test
    public void checkrelationship() {
        String userId = "ian56a";
        
        GettrRelationship relationship = accountApi.checkrelationship(userId);
        
        assertTrue(relationship.isFollowedBy());
        assertTrue(relationship.isFollowing());
        assertEquals(userId, relationship.getUserId());
    }
}