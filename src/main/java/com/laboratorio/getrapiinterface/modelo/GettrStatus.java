package com.laboratorio.getrapiinterface.modelo;

import com.laboratorio.clientapilibrary.utils.ElementoPost;
import com.laboratorio.clientapilibrary.utils.ImageMetadata;
import com.laboratorio.clientapilibrary.utils.PostUtils;
import com.laboratorio.clientapilibrary.utils.TipoElementoPost;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 07/09/2024
 * @updated 09/09/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GettrStatus {
    private GettrAcl acl;
    private String _t;
    private String txt;
    private long update;
    private long cdate;
    private String uid;
    private GettrRichText rich_txt;
    private List<String> htgs;
    private String txt_lang;        // Idioma
    private String _id;             // Identificador del post (aparece después de publicado)

    // Campos cuando hay una url (metadata de la URL)
    private String dsc;
    private String previmg;
    private String prevsrc;
    private String ttl;
    
    // Campos cuando hay una imagen
    private List<String> imgs;
    private String main;
    private Integer vid_wid;
    private Integer vid_hgt;
    private List<GettrMetadata> meta;
    
    // Otros posibles elementos
    private List<String> utgs;
    private List<String> vtgs;
    private List<String> sound_ids;
    private List<String> sticker_ids;

    public GettrStatus(String userId, String text) {
        boolean hasUrl = false;
        
        this.acl = new GettrAcl("acl");
        this._t = "post";
        this.txt = text;
        this.update = Instant.now().getEpochSecond();
        this.cdate = Instant.now().getEpochSecond();
        this.uid = userId;
        List<ElementoPost> elementos = PostUtils.extraerElementosPost(text);
        this.rich_txt = new GettrRichText();
        this.htgs = new ArrayList<>();
        for (ElementoPost elemento : elementos) {
            this.rich_txt.addElement(elemento);
            if (elemento.getType() == TipoElementoPost.Tag) {
                this.htgs.add(elemento.getContenido());
            }
            if ((elemento.getType() == TipoElementoPost.Link) && (!hasUrl)) {
                hasUrl = true;
                this.prevsrc = elemento.getContenido();
                Map<String, String> metadata = PostUtils.getUrlMetadata(elemento.getContenido());
                if (metadata.get("title") != null) {
                    this.ttl = metadata.get("title");
                }
                if (metadata.get("description") != null) {
                    this.dsc = metadata.get("description");
                }
                if (metadata.get("previmg") != null) {
                    this.previmg = metadata.get("previmg");
                }
            }
        }
        this.txt_lang = "es";
    }
    
    public GettrStatus(String userId, String text, String imagePath, ImageMetadata imageMetadata) {
        this.acl = new GettrAcl("acl");
        this._t = "post";
        this.txt = text;
        this.update = Instant.now().getEpochSecond();
        this.cdate = Instant.now().getEpochSecond();
        this.uid = userId;
        List<ElementoPost> elementos = PostUtils.extraerElementosPost(text);
        this.rich_txt = new GettrRichText();
        this.htgs = new ArrayList<>();
        for (ElementoPost elemento : elementos) {
            this.rich_txt.addElement(elemento);
            if (elemento.getType() == TipoElementoPost.Tag) {
                this.htgs.add(elemento.getContenido());
            }
        }
        this.txt_lang = "es";
        
        // Atributos propios de las imágenes
        this.imgs = new ArrayList<>();
        this.imgs.add(imagePath);
        this.main = imagePath;
        this.vid_wid = imageMetadata.getWidth();
        this.vid_hgt = imageMetadata.getHeight();
        this.meta = new ArrayList<>();
        this.meta.add(new GettrMetadata(this.vid_wid, this.vid_hgt));
    }

    @Override
    public String toString() {
        return "GettrStatus{" + "acl=" + acl + ", _t=" + _t + ", txt=" + txt + ", update=" + update + ", cdate=" + cdate + ", uid=" + uid + ", rich_txt=" + rich_txt + ", htgs=" + htgs + ", txt_lang=" + txt_lang + ", _id=" + _id + ", dsc=" + dsc + ", previmg=" + previmg + ", prevsrc=" + prevsrc + ", ttl=" + ttl + ", imgs=" + imgs + ", main=" + main + ", vid_wid=" + vid_wid + ", vid_hgt=" + vid_hgt + ", meta=" + meta + ", utgs=" + utgs + ", vtgs=" + vtgs + ", sound_ids=" + sound_ids + ", sticker_ids=" + sticker_ids + '}';
    }
}