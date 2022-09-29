package org.masos.embed.sysconfig.model.dto;

import com.google.gson.Gson;

public abstract class DTO {

    public String toJson(){
        return new Gson().toJson(this);
    }

}
