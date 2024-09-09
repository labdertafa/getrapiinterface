package com.laboratorio.getrapiinterface.modelo.request;

import com.laboratorio.clientapilibrary.utils.ImageMetadata;
import com.laboratorio.getrapiinterface.modelo.GettrStatus;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 07/09/2024
 * @updated 09/09/2024
 */

@Getter @Setter
public class GettrPostRequest {
    private GettrStatus data;
    private String serial;

    public GettrPostRequest(String userId, String text) {
        this.data = new GettrStatus(userId, text);
        this.serial = "post";
    }
    
    public GettrPostRequest(String userId, String text, String imagePath, ImageMetadata imageMetadata) {
        this.data = new GettrStatus(userId, text, imagePath, imageMetadata);
        this.serial = "post";
    }
}