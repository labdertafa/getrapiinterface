package com.laboratorio.getrapiinterface;

import com.laboratorio.getrapiinterface.modelo.response.GettrPostResponse;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 07/09/2024
 * @updated 07/09/2024
 */
public interface GettrStatusApi {
    GettrPostResponse postStatus(String text);
    GettrPostResponse postStatus(String text, String filePath);
    String uploadImage(String filePath, String mimeType);
}