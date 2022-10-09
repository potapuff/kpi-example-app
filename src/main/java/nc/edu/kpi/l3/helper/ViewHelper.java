package nc.edu.kpi.l3.helper;

import io.javalin.http.Context;
import nc.edu.kpi.l3.helper.exception.HttpException;
import nc.edu.kpi.l3.helper.utils.ExceptionUtils;

import java.util.HashMap;

public final class ViewHelper {

    public static void userError(HttpException exception, final Context context) {
        System.out.println(exception.getMessage());
        exception.printStackTrace();
        String stacktrace = ExceptionUtils.stacktrace(exception);
        userError(context, exception.getCode(), exception.getMessage(), exception.getIcon(), stacktrace);
    }

    public static void userError(final Context context, final Integer code,
                                 final String message, final String icon, final String stacktrace) {
        var model = new HashMap<String, Object>();
        model.put("code", code.toString());
        model.put("text", message);
        model.put("icon", icon);
        if (!Keys.isProduction()) {
            model.put("stacktrace", stacktrace);
        }
        context.status(code);
        context.render("/velocity/show.vm", model);
    }

    public static String decorateText(String text){
       return text.toUpperCase().replaceFirst("AND","<br/><span class='small'>and</span><br/>");
    }

}
