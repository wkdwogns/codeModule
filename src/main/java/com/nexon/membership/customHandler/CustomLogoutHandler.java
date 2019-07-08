package com.nexon.membership.customHandler;

import com.nexon.common.type.ReturnType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Component
public class CustomLogoutHandler implements LogoutSuccessHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        /*if(authentication != null) {
            System.out.println(authentication.getName());
        }
        //perform other required operation
        String URL = request.getContextPath() + "/page/mobile/membership/login";
        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect(URL);*/



        logger.info("Loout Success Handler");

        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = response.getWriter();

        writer.write(ReturnType.RTN_TYPE_OK.getStrValue() );
        writer.flush();

    }

}
