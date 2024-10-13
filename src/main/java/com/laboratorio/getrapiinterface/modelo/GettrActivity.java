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
 * @created 13/10/2024
 * @updated 13/10/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GettrActivity {
    private int cdate;
    private String init_lvl;
    private String src_type;
    private String action;
    private String tgt_type;
    private String tgt_id;
    private String tgt_oid;
    private List<String> rpstIds;
    private List<String> rusrIds;
    private String pstid;
    private String uid;
    private String _id;
}