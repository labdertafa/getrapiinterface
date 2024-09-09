package com.laboratorio.getrapiinterface.impl;

import com.google.gson.JsonSyntaxException;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.clientapilibrary.utils.ImageMetadata;
import com.laboratorio.clientapilibrary.utils.PostUtils;
import com.laboratorio.getrapiinterface.GettrStatusApi;
import com.laboratorio.getrapiinterface.exception.GettrApiException;
import com.laboratorio.getrapiinterface.modelo.GettrCredential;
import com.laboratorio.getrapiinterface.modelo.request.GettrPostRequest;
import com.laboratorio.getrapiinterface.modelo.response.GettrPostResponse;
import com.laboratorio.getrapiinterface.modelo.response.GettrUploadChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.ws.rs.core.Response;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 07/09/2024
 * @updated 09/09/2024
 */
public class GettrStatusApiImpl extends GettrBaseApi implements GettrStatusApi {
    public GettrStatusApiImpl(String accountId, String accessToken) {
        super(accountId, accessToken);
    }
    
    @Override
    public GettrPostResponse postStatus(String text) {
        String endpoint = this.apiConfig.getProperty("postStatus_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("postStatus_ok_status"));    
        
        try {
            GettrPostRequest postRequest = new GettrPostRequest(this.accountId, text);
            String jsonPost = this.gson.toJson(postRequest, GettrPostRequest.class);
            
            String uri = endpoint;
            GettrCredential credential = new GettrCredential(this.accountId, this.accessToken);
            String credentialStr = gson.toJson(credential);
            
            ApiRequest request = new ApiRequest(uri, okStatus);
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("X-app-auth", credentialStr);
            request.addJsonFormData("content", jsonPost);
            
            String jsonStr = this.client.executePostRequest(request);
            return this.gson.fromJson(jsonStr, GettrPostResponse.class);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new GettrApiException(GettrAccountApiImpl.class.getName(), e.getMessage());
        }
    }

    @Override
    public GettrPostResponse postStatus(String text, String filePath) {
        String endpoint = this.apiConfig.getProperty("postStatus_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("postStatus_ok_status"));    
        
        try {
            ImageMetadata imageMetadata = PostUtils.extractImageMetadata(filePath);
            
            String imagePath = this.uploadImage(filePath, imageMetadata.getMimeType());
            
            GettrPostRequest postRequest = new GettrPostRequest(this.accountId, text, imagePath, imageMetadata);
            String jsonPost = this.gson.toJson(postRequest, GettrPostRequest.class);
            
            String uri = endpoint;
            GettrCredential credential = new GettrCredential(this.accountId, this.accessToken);
            String credentialStr = gson.toJson(credential);
            
            ApiRequest request = new ApiRequest(uri, okStatus);
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("X-app-auth", credentialStr);
            request.addJsonFormData("content", jsonPost);
            
            String jsonStr = this.client.executePostRequest(request);
            return this.gson.fromJson(jsonStr, GettrPostResponse.class);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new GettrApiException(GettrAccountApiImpl.class.getName(), e.getMessage());
        }
    }
    
    private String getFileName(String filePath) {
        Path path = Paths.get(filePath);
        return path.getFileName().toString();
    }
    
    private String extraerRutaImagen(String url) {
        int pos1 = url.indexOf("s3sync") + 7;
        int pos2 = url.indexOf("?");
        
        if ((pos1 == -1) || (pos2 == -1)) {
            return null;
        }
        
        return url.substring(pos1, pos2);
    }

    @Override
    public String uploadImage(String filePath, String mimeType) {
        String endpoint = this.apiConfig.getProperty("UploadImage_endpoint");
        int okStatus1 = Integer.parseInt(this.apiConfig.getProperty("UploadImage_channel_ok_status"));    
        int okStatus2 = Integer.parseInt(this.apiConfig.getProperty("UploadImage_createId_ok_status"));    
        int okStatus3 = Integer.parseInt(this.apiConfig.getProperty("UploadImage_ok_status"));    
        
        try {
            // Solicitar un canal para subir la imagen
            String uri = endpoint;
            ApiRequest request1 = new ApiRequest(uri, okStatus1);
            request1.addApiHeader("Authorization", this.accessToken);
            request1.addApiHeader("filename", this.getFileName(filePath));
            request1.addApiHeader("userid", this.accountId);
            
            String jsonStr = this.client.executeGetRequest(request1);
            GettrUploadChannel uploadChannel = this.gson.fromJson(jsonStr, GettrUploadChannel.class);
            
            log.debug("Tengo el canal para subir la imagen: " + uploadChannel.getGcs().getUrl());
            
            // Se crea el identificador de la imagen a subir
            ApiRequest request2 = new ApiRequest(uploadChannel.getGcs().getUrl(), okStatus2);
            request2.addApiHeader("Content-Type", mimeType);
            request2.addApiHeader("X-Goog-Resumable", "start");
            // Response response = this.client.getResponsePostRequest(request2);
            Response response = this.client.getResponsePostRequest(request2);
            String imageId = response.getHeaderString("X-GUploader-UploadID");
            response.close();
            if (imageId == null) {
                throw new Exception("Ha ocurrido un subiendo la imagen " + filePath);
            }
            
            log.debug("He obtenido el ID de la imagen: " + imageId);
            
            // Se sube la imagen
            ApiRequest request3 = new ApiRequest(uploadChannel.getGcs().getUrl(), okStatus3);
            request3.setBinaryFile(filePath);
            request3.addApiPathParam("upload_id", imageId);
            String md5Str = PostUtils.calculateMD5Base64(filePath);
            request3.addApiHeader("content-md5", md5Str);
            this.client.getResponsePutRequest(request3);
            
            String rutaImagen = extraerRutaImagen(uploadChannel.getGcs().getUrl());
            log.debug("He subido la imagen correctamente: " + rutaImagen);
            
            return rutaImagen;
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new GettrApiException(GettrAccountApiImpl.class.getName(), e.getMessage());
        }
    }
}