package com.laboratorio.getrapiinterface;

import com.laboratorio.getrapiinterface.modelo.GettrStatus;
import com.laboratorio.getrapiinterface.modelo.response.GettrPostResponse;
import java.util.List;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 07/09/2024
 * @updated 13/10/2024
 */
public interface GettrStatusApi {
    GettrPostResponse postStatus(String text);
    GettrPostResponse postStatus(String text, String filePath);
    String uploadImage(String filePath, String mimeType);
    boolean deleteStatus(String id);
    
    List<GettrStatus> getGlobalTimeline(int quantity);
}