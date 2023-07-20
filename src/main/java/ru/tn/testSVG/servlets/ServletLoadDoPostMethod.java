package ru.tn.testSVG.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.tn.testSVG.beans.InMDataBeanLocal;
import ru.tn.testSVG.model.MnemonicData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Метод doPost для сервлетов загрузки мгновенных и архивных данных
 */
class ServletLoadDoPostMethod {
    private static final Logger LOG = Logger.getLogger(ServletLoadDoPostMethod.class.getName());


    static void doPost(HttpServletRequest req, HttpServletResponse resp, InMDataBeanLocal bean) throws IOException {
        String objId = req.getParameter("objId");
        String sessionID = req.getParameter("sessionID");

        try {
            Integer.parseInt(objId);
            String login = bean.getUser(sessionID);
            if (login == null) {
                login = "ADMIN";
                LOG.log(Level.WARNING, "Out of input login. Login set to Admin");
            }
            List<MnemonicData> mData = bean.getData(objId, login);
            ObjectMapper mapper = new ObjectMapper();
            LOG.log(Level.INFO, mapper.writeValueAsString(mData));

            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            resp.getWriter().write(mapper.writeValueAsString(mData));

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error with objectId");
        }
    }
}
