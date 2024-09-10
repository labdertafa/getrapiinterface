package com.laboratorio.getrapiinterface.modelo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 10/09/2024
 * @updated 10/09/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GettrNotification {
    private long msgId;
    private String act;
    private long cdate;
    private String ttxt;
    private String rtype;
    private String rid;
    private String rtxt;
    private String lsurl;
    private String ruid;
    private List<GettrNotifUserInfo> othr;
    private int count;
}