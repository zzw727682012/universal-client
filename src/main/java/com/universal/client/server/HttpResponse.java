package com.universal.client.server;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse implements Serializable {

    public enum CODE {
        CODE_OK(200, "OK"),
        CODE_404(404, "Not Found"),
        CODE_500(500, "Internal Server Error"),
        CODE_400(400, "Bad Request"),
        CODE_405(405, "Method Not Allowed"),
        CODE_502(502, "Bad Gateway");
        private Integer code;
        private String messgae;
        private CODE(Integer code, String messgae) {
            this.code = code;
            this.messgae = messgae;
        }
        @Override
        public String toString() {
            return code + " " + messgae;
        }
    }

    private static final long serialVersionUID = 1L;
    private String version = "HTTP/1.1";
    private String messageData;
    private String status;
    private Map<String,String> headers;

    public HttpResponse(CODE code) {
        this.headers = new LinkedHashMap<>();
        this.headers.put("content-type", "application/json;charset=utf-8");
        this.headers.put("content-length", "0");
        this.headers.put("Date", new Date().toString());
        this.headers.put("Server", "SimpleHttpServer");
        this.status = code.toString();
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "version='" + version + '\'' +
                ", messageData='" + messageData + '\'' +
                ", status='" + status + '\'' +
                ", headers=" + headers +
                '}';
    }
}
