package backend.academy.loganalyzer.data;

public enum HttpStatusCode {
    UNDEFINED(-1, "???"),

    CONTINUE(100, "Continue"),
    SWITCHING_PROTOCOLS(101, "Switching Protocols"),

    OK(200, "OK"),
    CREATED(201, "Created"),
    ACCEPTED(202, "Accepted"),
    NO_CONTENT(204, "No Content"),

    PARTIAL_CONTENT(206, "Partial Content"),


    MOVED_PERMANENTLY(301, "Moved Permanently"),
    FOUND(302, "Found"),
    NOT_MODIFIED(304, "Not Modified"),

    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    CONFLICT(409, "Conflict"),
    RANGE_NOT_SATISFIABLE(416, "Range Not Satisfiable"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    NOT_IMPLEMENTED(501, "Not Implemented"),
    BAD_GATEWAY(502, "Bad Gateway"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    GATEWAY_TIMEOUT(504, "Gateway Timeout");

    private final int code;
    private final String description;

    HttpStatusCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static HttpStatusCode of(int code) {
        for (HttpStatusCode httpStatusCode : HttpStatusCode.values()) {
            if (httpStatusCode.code == code) {
                return httpStatusCode;
            }
        }
        return UNDEFINED;
    }

    @Override
    public String toString() {
        return description;
    }
}
