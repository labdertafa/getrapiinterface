package com.laboratorio.getrapiinterface.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.laboratorio.clientapilibrary.model.ApiMethodType;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.clientapilibrary.model.ApiResponse;
import com.laboratorio.clientapilibrary.utils.ImageMetadata;
import com.laboratorio.clientapilibrary.utils.PostUtils;
import com.laboratorio.getrapiinterface.GettrStatusApi;
import com.laboratorio.getrapiinterface.exception.GettrApiException;
import com.laboratorio.getrapiinterface.modelo.GettrCredential;
import com.laboratorio.getrapiinterface.modelo.GettrStatus;
import com.laboratorio.getrapiinterface.modelo.GettrStatusHeader;
import com.laboratorio.getrapiinterface.modelo.GettrStatusListIndex;
import com.laboratorio.getrapiinterface.modelo.request.GettrPostRequest;
import com.laboratorio.getrapiinterface.modelo.response.GettrDeletePostResponse;
import com.laboratorio.getrapiinterface.modelo.response.GettrPostResponse;
import com.laboratorio.getrapiinterface.modelo.response.GettrStatusListResponse;
import com.laboratorio.getrapiinterface.modelo.response.GettrUploadChannel;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael
 * @version 1.2
 * @created 07/09/2024
 * @updated 13/10/2024
 */
public class GettrStatusApiImpl extends GettrBaseApi implements GettrStatusApi {
    public GettrStatusApiImpl(String accountId, String accessToken) {
        super(accountId, accessToken);
    }
    
    @Override
    public GettrStatus postStatus(String text) {
        String endpoint = this.apiConfig.getProperty("postStatus_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("postStatus_ok_status"));    
        
        try {
            GettrPostRequest postRequest = new GettrPostRequest(this.accountId, text);
            String jsonPost = this.gson.toJson(postRequest, GettrPostRequest.class);
            
            String uri = endpoint;
            GettrCredential credential = new GettrCredential(this.accountId, this.accessToken);
            String credentialStr = gson.toJson(credential);
            
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.POST);
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("X-app-auth", credentialStr);
            request.addJsonFormData("content", jsonPost);
            
            ApiResponse response = this.client.executeApiRequest(request);

            GettrPostResponse postResponse = this.gson.fromJson(response.getResponseStr(), GettrPostResponse.class);
            return postResponse.getResult().getData();
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new GettrApiException(GettrAccountApiImpl.class.getName(), e.getMessage());
        }
    }

    @Override
    public GettrStatus postStatus(String text, String filePath) {
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
            
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.POST);
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("X-app-auth", credentialStr);
            request.addJsonFormData("content", jsonPost);
            
            ApiResponse response = this.client.executeApiRequest(request);
            GettrPostResponse postResponse = this.gson.fromJson(response.getResponseStr(), GettrPostResponse.class);
            return postResponse.getResult().getData();
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
            ApiRequest request1 = new ApiRequest(uri, okStatus1, ApiMethodType.GET);
            request1.addApiHeader("Authorization", this.accessToken);
            request1.addApiHeader("filename", this.getFileName(filePath));
            request1.addApiHeader("userid", this.accountId);
            
            ApiResponse response1 = this.client.executeApiRequest(request1);
            GettrUploadChannel uploadChannel = this.gson.fromJson(response1.getResponseStr(), GettrUploadChannel.class);
            
            log.debug("Tengo el canal para subir la imagen: " + uploadChannel.getGcs().getUrl());
            
            // Se crea el identificador de la imagen a subir
            ApiRequest request2 = new ApiRequest(uploadChannel.getGcs().getUrl(), okStatus2, ApiMethodType.POST, "");
            request2.addApiHeader("Content-Type", mimeType);
            request2.addApiHeader("X-Goog-Resumable", "start");
            
            ApiResponse response2 = this.client.executeApiRequest(request2);
            
            String imageId = null;
            List<String> headerList = response2.getHttpHeaders().get("X-GUploader-UploadID");
            if ((headerList != null) && (!headerList.isEmpty())) {
                imageId = headerList.get(0);
            }

            if (imageId == null) {
                throw new GettrApiException(GettrAccountApiImpl.class.getName(), "Ha ocurrido un subiendo la imagen " + filePath);
            }
            
            log.debug("He obtenido el ID de la imagen: " + imageId);
            
            // Se sube la imagen
            File file = new File(filePath);
            ApiRequest request3 = new ApiRequest(uploadChannel.getGcs().getUrl(), okStatus3, ApiMethodType.PUT, file);
            request3.addApiPathParam("upload_id", imageId);
            request3.addApiHeader("Content-Type", mimeType);
            String md5Str = PostUtils.calculateMD5Base64(filePath);
            request3.addApiHeader("content-md5", md5Str);
            
            this.client.executeApiRequest(request3);
            
            String rutaImagen = extraerRutaImagen(uploadChannel.getGcs().getUrl());
            log.debug("He subido la imagen correctamente: " + rutaImagen);
            
            return rutaImagen;
        } catch (GettrApiException e) {
            throw e;
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new GettrApiException(GettrAccountApiImpl.class.getName(), e.getMessage());
        }
    }

    @Override
    public boolean deleteStatus(String id) {
        String endpoint = this.apiConfig.getProperty("deleteStatus_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("deleteStatus_ok_status"));    
        
        try {
            String uri = endpoint + "/" + id;
            GettrCredential credential = new GettrCredential(this.accountId, this.accessToken);
            String credentialStr = gson.toJson(credential);
            
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.DELETE);
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("X-app-auth", credentialStr);
            
            ApiResponse response = this.client.executeApiRequest(request);
            GettrDeletePostResponse postResponse = this.gson.fromJson(response.getResponseStr(), GettrDeletePostResponse.class);
            
            return postResponse.isResult();
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new GettrApiException(GettrAccountApiImpl.class.getName(), e.getMessage());
        }
    }
    
    private GettrStatusListResponse getTimeLinePage(String uri, int okStatus, String posicionInicial) {
        try {
            GettrCredential credential = new GettrCredential(this.accountId, this.accessToken);
            String credentialStr = gson.toJson(credential);
            
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.GET);
            request.addApiPathParam("max", "20");
            request.addApiPathParam("dir", "fwd");
            request.addApiPathParam("incl", "posts|stats|userinfo|shared|liked|pvotes");
            if (posicionInicial != null) {
                request.addApiPathParam("cursor", posicionInicial);
            }
            request.addApiHeader("Content-Type", "application/json");
            request.addApiHeader("X-app-auth", credentialStr);
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            JsonObject jsonObject = JsonParser.parseString(response.getResponseStr()).getAsJsonObject();
            JsonObject jsonObjectResult = jsonObject.get("result").getAsJsonObject();
            
            GettrStatusListIndex listIndex = gson.fromJson(jsonObjectResult.get("data"), GettrStatusListIndex.class);
            log.debug("Número de cuentas obtenidas en la respuesta: " + listIndex.getList().size());
            
            JsonObject jsonObjectDetail = jsonObjectResult.get("aux").getAsJsonObject();
            String cursor = jsonObjectDetail.get("cursor").getAsString();
            log.info("Valor obtenido para el cursor: " + cursor);
            
            JsonObject jsonObjectStatusInfo = jsonObjectDetail.get("post").getAsJsonObject();
            
            List<GettrStatus> statuses = new ArrayList<>();
            for (GettrStatusHeader statusHeader : listIndex.getList()) {
                GettrStatus status = gson.fromJson(jsonObjectStatusInfo.get(statusHeader.getActivity().get_id()), GettrStatus.class);
                statuses.add(status);
            }
            
            return new GettrStatusListResponse(statuses, cursor);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new GettrApiException(GettrBaseApi.class.getName(), e.getMessage());
        }
    }

    @Override
    public List<GettrStatus> getGlobalTimeline(int quantity) {
        String endpoint = this.apiConfig.getProperty("getGlobalTimeLine_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getGlobalTimeLine_ok_status"));
        
        List<GettrStatus> statuses = null;
        boolean continuar = true;
        String cursor = null;
        
        try {
            String uri = endpoint;
            
            do {
                GettrStatusListResponse statusListResponse = this.getTimeLinePage(uri, okStatus, cursor);
                log.info("Elementos recuperados total: " + statusListResponse.getAccounts().size());
                if (statuses == null) {
                    statuses = statusListResponse.getAccounts();
                } else {
                    statuses.addAll(statusListResponse.getAccounts());
                }
                
                cursor = statusListResponse.getCursor();
                log.info("getGlobalTimeline. Recuperados: " + statuses.size() + ". Cursor: " + cursor);
                if (statusListResponse.getAccounts().isEmpty()) {
                    continuar = false;
                } else {
                    if ((cursor == null) || (statuses.size() >= quantity)) {
                        continuar = false;
                    }
                }
            } while (continuar);
            
            return statuses.subList(0, Math.min(quantity, statuses.size()));
        } catch (Exception e) {
            throw e;
        }
    }
}