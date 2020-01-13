package com.jjh.common.util;


import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;
import java.util.Map;

/**
 * Created by MJ on 2018. 8. 23..
 */

public class VelocityUtil {

    public static String getEmailVerifyNumberContents(Map<String, Object> param) {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();

        Template template = velocityEngine.getTemplate("templates/velocity/verifyNumber.vm", "UTF-8");
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("emailVerify", param.get("emailVerify"));

        StringWriter stringWriter = new StringWriter();
        template.merge(velocityContext, stringWriter);

        return stringWriter.toString();

    }

    public static String getEmailVerifyContents(Map<String, Object> param) {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();

        Template template = velocityEngine.getTemplate("templates/velocity/emailVerify.vm", "UTF-8");
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("emailVerify", param.get("emailVerify"));

        StringWriter stringWriter = new StringWriter();
        template.merge(velocityContext, stringWriter);

        return stringWriter.toString();

    }

    public static String getEmailChangeContents(Map<String, Object> param) {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();

        Template template = velocityEngine.getTemplate("templates/velocity/pwdChange.vm", "UTF-8");
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("pwd", param.get("pwd"));

        StringWriter stringWriter = new StringWriter();
        template.merge(velocityContext, stringWriter);

        return stringWriter.toString();

    }

}
