package br.com.chonide.api.http;

public enum HttpContent {

    JSON("application/json"), TEXT("text/plain");

    private String type;

    HttpContent(String type) {
        this.type = type;
    }

    /**
     * @return {@link #type}
     */
    public String getType() {
        return this.type;
    }
}
