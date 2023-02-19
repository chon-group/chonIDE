package org.masos.embed.sysconfig.api.http;

public enum HttpEncoding {

    UTF_8("UTF-8"),
    ISO_8859_1("ISO-8859-1");

    private String type;

    HttpEncoding(String type) {
        this.type = type;
    }

    /**
     * @return {@link #type}
     */
    public String getType() {
        return this.type;
    }
}
