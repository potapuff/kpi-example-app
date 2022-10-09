package nc.edu.kpi.l3;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinVelocity;
import nc.edu.kpi.l3.controller.MainController;
import nc.edu.kpi.l3.helper.ViewHelper;
import nc.edu.kpi.l3.helper.Keys;
import nc.edu.kpi.l3.helper.exception.HttpException;

import java.io.File;

public class Server {

        private final Javalin app;
        {
            JavalinVelocity.init(null);
            app = Javalin.create(
                            config -> config.staticFiles.add("/public", Location.CLASSPATH))
                    .exception(HttpException.class, ViewHelper::userError)
                    .exception(Exception.class,
                            (e, ctx) ->
                                    ViewHelper.userError(new HttpException(e), ctx))
                    .get("/", MainController::index)
                    .get("/{id}", MainController::show)
                    .post("/", MainController::create);
        }

    public static void main(final String[] args) {
        var file = new File(args.length < 1 ? "config.properties" : args[0]);
        Keys.loadParams(file);

        new Server().start(Integer.parseInt(Keys.get("APP.PORT")));
    }

    public void start(final int port) {
        this.app.start(port);
    }

    @SuppressWarnings("unused")
    public void stop() {
        this.app.stop();
    }

}
