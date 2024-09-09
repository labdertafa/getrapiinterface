package com.laboratorio.getrapiinterface.modelo;

import com.laboratorio.clientapilibrary.utils.ElementoPost;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 07/09/2024
 * @updated 09/09/2024
 */

@Getter @Setter @AllArgsConstructor
public class GettrRichText {
    private List<GettrParagraph> ops;

    public GettrRichText() {
        this.ops = new ArrayList<>();
    }

    public void addElement(ElementoPost element) {
        this.ops.add(new GettrParagraph(element));
    }       
}