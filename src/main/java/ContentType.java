public enum ContentType {

    JSON("application/json"),
    PLAIN("text/plain"),
    HTML("text/html");

    private final String contentType;

    ContentType(String contentType) {
        this.contentType = contentType;
    }

    public String get() {
        return contentType;
    }
}
