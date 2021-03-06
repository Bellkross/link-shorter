package web;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import shorter.AppConfig;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyDispatcherServlet extends HttpServlet {

    private AnnotationConfigApplicationContext webCtx;

    @Override
    public void init() throws ServletException {
        webCtx = new AnnotationConfigApplicationContext();
        webCtx.setParent((ApplicationContext)
                getServletContext().getAttribute(AppConfig.class.getName()));
        webCtx.register(getAppConfigClass());
        webCtx.register(getWebConfigClass());
        webCtx.refresh();
    }

    private Class<?> getAppConfigClass() {
        return AppConfig.class;
    }

    private Class<?> getWebConfigClass() {
        String contextConfigClass =
                getInitParameter("contextConfigLocation");

        try {
            return Class.forName(contextConfigClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        String controllerName = getControllerNameFromRequest(req);
        MyController controller =
                (MyController) webCtx.getBean(controllerName);
        controller.handleRequest(req, resp);
    }

    private String getControllerNameFromRequest(final HttpServletRequest req) {
        String s = req.getRequestURI();
        return s.substring(s.lastIndexOf("/") + 1);
    }
}
