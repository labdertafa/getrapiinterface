package com.laboratorio.getrapiinterface;

import com.laboratorio.getrapiinterface.modelo.GettrStatus;
import java.util.List;

/**
 *
 * @author Rafael
 * @version 1.2
 * @created 07/09/2024
 * @updated 17/10/2024
 */
public interface GettrStatusApi {
    GettrStatus postStatus(String text);
    GettrStatus postStatus(String text, String filePath);
    String uploadImage(String filePath, String mimeType);
    boolean deleteStatus(String id);
    
    List<GettrStatus> getGlobalTimeline(int quantity);
}