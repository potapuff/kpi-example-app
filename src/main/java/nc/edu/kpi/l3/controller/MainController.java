package nc.edu.kpi.l3.controller;

import io.javalin.http.Context;
import io.javalin.validation.ValidationException;
import nc.edu.kpi.l3.helper.Keys;
import nc.edu.kpi.l3.helper.ViewHelper;
import nc.edu.kpi.l3.helper.exception.HttpException;
import nc.edu.kpi.l3.helper.exception.NotFoundException;
import nc.edu.kpi.l3.model.Message;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class MainController {

    public static void index(Context context) {
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("icon", Keys.get("DEFAULT_ICON"));
            model.put("text", ViewHelper.decorateText(Keys.get("DEFAULT_MESSAGE")));
            context.render("/velocity/show.vm", model);
        } catch (Exception ex) {
        new HttpException(ex);
    }
    }

    public static void show(Context context) {
        Map<String, Object> model = new HashMap<>();
        try {
            Integer messageId = context.pathParamAsClass("id", Integer.class).get();
            Message message = Message.find(messageId);
            model.put("icon", Keys.get("DEFAULT_ICON"));
            model.put("text", ViewHelper.decorateText(message.message));
        } catch (ValidationException ex) {
            throw new NotFoundException();
        } catch (SQLException ex) {
            throw new HttpException(ex);
        }
        context.render("/velocity/show.vm", model);
    }

    public static void create(Context context) {
        try {
            Message message = new Message(context.formParam("message"));
            message.save();
            context.redirect("/"+message.id);
        } catch (Exception ex) {
            new HttpException(ex);
        }
    }

}
