package com.laboratorio.getrapiinterface.modelo;

import com.laboratorio.clientapilibrary.utils.ElementoPost;
import com.laboratorio.clientapilibrary.utils.TipoElementoPost;
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

@Getter @Setter @NoArgsConstructor
public class GettrParagraph {
    private GettrAttribute attributes;
    private String insert;

    public GettrParagraph(ElementoPost element) {
        this.insert = element.getContenido();
        this.attributes = null;
        if ((element.getType() == TipoElementoPost.Link) || (element.getType() == TipoElementoPost.Tag)) {
            this.attributes = new GettrAttribute(element.getContenido());
        }
    }
}