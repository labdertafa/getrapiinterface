package com.laboratorio.getrapiinterface.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 05/09/2024
 * @updated 05/09/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GettrAccount {
    private String ico;
    private String location;
    private long cdate;
    private String username;
    private long udate;
    private String bgimg;
    private int flw;
    private String status;
    private String nickname;
    private String xversion;
    private String website;
    private int flg;
    private String dsc;
    private String lang;
    private String ousername;
    private String _t;
    private String _id;
    private String vrf;
    // private GettrPremium premium;

    @Override
    public String toString() {
        return "GettrAccount{" + "ico=" + ico + ", location=" + location + ", cdate=" + cdate + ", username=" + username + ", udate=" + udate + ", bgimg=" + bgimg + ", flw=" + flw + ", status=" + status + ", nickname=" + nickname + ", xversion=" + xversion + ", website=" + website + ", flg=" + flg + ", dsc=" + dsc + ", lang=" + lang + ", ousername=" + ousername + ", _t=" + _t + ", _id=" + _id + ", vrf=" + vrf + '}';
    }
}