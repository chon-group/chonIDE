package br.com.chonide.api.dto;

public class StartMasResponse {

    private String message;

    private String logmonitor;

    private String mindmonitor;

    public StartMasResponse(String message, String logmonitor, String mindmonitor) {
        this.message = message;
        this.logmonitor = logmonitor;
        this.mindmonitor = mindmonitor;
    }

    /**
     * @return {@link #message}
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * @param message {@link #message}
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return {@link #logmonitor}
     */
    public String getLogmonitor() {
        return this.logmonitor;
    }

    /**
     * @param logmonitor {@link #logmonitor}
     */
    public void setLogmonitor(String logmonitor) {
        this.logmonitor = logmonitor;
    }

    /**
     * @return {@link #mindmonitor}
     */
    public String getMindmonitor() {
        return this.mindmonitor;
    }

    /**
     * @param mindmonitor {@link #mindmonitor}
     */
    public void setMindmonitor(String mindmonitor) {
        this.mindmonitor = mindmonitor;
    }
}
