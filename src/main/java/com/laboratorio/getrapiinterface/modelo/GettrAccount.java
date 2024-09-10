package com.laboratorio.getrapiinterface.modelo;

import com.laboratorio.getrapiinterface.utiles.GettrApiConfig;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 05/09/2024
 * @updated 09/09/2024
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
    
    public boolean isSeguidorPotencial() {
        String accountId = GettrApiConfig.getInstance().getProperty("usuario_gettr");
        if (this._id.equals(accountId)) {
            return false;
        }
        
        if (this.flg < 2) {
            return false;
        }

        return 2 * this.flw >= this.flg;
    }
    
    public boolean isFuenteSeguidores() {
        GettrApiConfig config = GettrApiConfig.getInstance();
        int umbral = Integer.parseInt(config.getProperty("umbral_fuente_seguidores"));
        return this.flg >= umbral;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.username);
        hash = 29 * hash + Objects.hashCode(this._id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GettrAccount other = (GettrAccount) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        return Objects.equals(this._id, other._id);
    }

    @Override
    public String toString() {
        return "GettrAccount{" + "ico=" + ico + ", location=" + location + ", cdate=" + cdate + ", username=" + username + ", udate=" + udate + ", bgimg=" + bgimg + ", flw=" + flw + ", status=" + status + ", nickname=" + nickname + ", xversion=" + xversion + ", website=" + website + ", flg=" + flg + ", dsc=" + dsc + ", lang=" + lang + ", ousername=" + ousername + ", _t=" + _t + ", _id=" + _id + ", vrf=" + vrf + '}';
    }
}