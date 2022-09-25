package org.masos.embed.sysconfig.model.http;

public enum Encoding {

    UTF_8("UTF-8");

    private String type;

    Encoding(String type) {
        this.type = type;
    }

    /**
     * @return {@link #type}
     */
    public String getType() {
        return this.type;
    }
}
