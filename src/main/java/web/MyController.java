package web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MyController {

    void handleRequest(
            HttpServletRequest request,
            HttpServletResponse response)
            throws Exception;
}