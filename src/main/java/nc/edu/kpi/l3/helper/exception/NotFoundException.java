package nc.edu.kpi.l3.helper.exception;

import nc.edu.kpi.l3.helper.Keys;

public class NotFoundException extends HttpException {

    public NotFoundException(){
        super(Keys.get("NOT_FOUND_MESSAGE"));
    }
    public NotFoundException(String message) {
        super(message);
    }

    public String getIcon() {
        return "telescope.png";
    }

    public Integer getCode() {
        return 404;
    }

}
