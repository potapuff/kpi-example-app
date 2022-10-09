package nc.edu.kpi.l3.helper.exception;

import nc.edu.kpi.l3.helper.Keys;

public class HttpException extends RuntimeException {
    private static final String DEFAULT_ICON = "bug.png";

    public HttpException() {
        super();
    }

    public HttpException(Exception ex){
        super(Keys.get("ERROR_MESSAGE"), ex);
    }

    public HttpException(String message) {
        super(message);
    }

    public HttpException(String message, Exception ex) {
        super(message, ex);
    }

    public Integer getCode() {
        return 500;
    }

    public String getIcon() {
        return DEFAULT_ICON;
    }

}
