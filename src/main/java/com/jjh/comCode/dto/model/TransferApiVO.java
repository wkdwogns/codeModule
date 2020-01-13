package com.jjh.comCode.dto.model;

import lombok.Data;

@Data
public class TransferApiVO {
    private String contractId;
    private String from;
    private String to;
    private String pw;
    private String memo;
    private String quantity;

    @Override
    public String toString() {
        return "{from: "+from+" , to: "+to+", quantity:"+quantity+", memo:"+memo+"}";
    }
}
