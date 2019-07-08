package com.nexon.common.mail;

import com.nexon.common.config.ConfigCommon;
import com.nexon.common.mail.dto.res.MailReq;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.URLDataSource;
import javax.mail.Multipart;
import javax.mail.internet.*;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class MailHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ConfigCommon configCommon;

    @Autowired
    public JavaMailSender Sender;


    /**
     *  이메일 전송 메소드
     *
     *  가장 단순하게 이메일을 전송하는 방식
     *
     * @param toEmail
     * @param subject
     * @param text
     * @throws Exception
     */
    public void sendEmail(String fromMail, String toEmail, String subject, String text)  throws Exception {
        MimeMessage mimeMessage = Sender.createMimeMessage();
        mimeMessage.setContent(text, "text/html;charset=UTF-8");
        mimeMessage.setSubject(MimeUtility.encodeText(subject, "utf-8", "B"));
        mimeMessage.setFrom(new InternetAddress(fromMail));
        mimeMessage.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(toEmail));
        mimeMessage.saveChanges();

        logger.info("mail send subject=="+subject);
        logger.info("mail send header subject=="+mimeMessage.getHeader("Subject")[0]);

        Sender.send(mimeMessage);
    }


    /**
     *  이메일 전송 메소드
     *
     *  첨부파일 포함 메일 발송
     *
     * @param toEmail
     * @param subject
     * @param text
     * @param fileList
     * @throws Exception
     */
    public void sendEmailAttach(String toEmail, String subject, String text, ArrayList<HashMap<String, Object>> fileList)  throws Exception {

        MimeMessage mimeMessage = Sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        MimeBodyPart contentMbp = new MimeBodyPart();
        Multipart multipart = new MimeMultipart();

        mimeMessage.setSubject(MimeUtility.encodeText(subject, "utf-8", "B"));
        contentMbp.setContent(text, "text/html;charset=utf-8");
        multipart.addBodyPart(contentMbp);


        if(fileList != null && fileList.size() > 0){
            for(Map<String, Object> fileMap : fileList){
                if(fileMap != null){
                    MimeBodyPart fileMbp = new MimeBodyPart();
                    URL url = new URL(fileMap.get("fileUrl").toString());
                    URLDataSource fds = new URLDataSource(url);

                    fileMbp.setDataHandler(new DataHandler(fds));
                    fileMbp.setFileName(fileMap.get("fileOriginName").toString());

                    multipart.addBodyPart(fileMbp);
                }
            }
        }

        mimeMessage.setContent(multipart);
        helper.setTo(toEmail);
        Sender.send(mimeMessage);
    }


    /* 메일 발송 */
    public void sendVolocityMail(MailReq mailReq, VelocityContext velocityContext) throws Exception {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();

        Template template = velocityEngine.getTemplate(mailReq.getVelocityPath(), "UTF-8");

        StringWriter stringWriter = new StringWriter();
        template.merge(velocityContext, stringWriter);

        this.sendEmail(configCommon.getMail().getAdminaddress(), mailReq.getToEmail(), mailReq.getMailTitle(), stringWriter.toString());

    }

    /* 메일 첨부파일 발송 */
    public void sendVolocityAttachMail(MailReq mailReq, VelocityContext velocityContext, ArrayList<HashMap<String,Object>> fileinfo) throws Exception{
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();

        Template template = velocityEngine.getTemplate(mailReq.getVelocityPath(), "UTF-8");

        StringWriter stringWriter = new StringWriter();
        template.merge(velocityContext, stringWriter);

        this.sendEmailAttach(mailReq.getToEmail(), mailReq.getMailTitle(), stringWriter.toString(), fileinfo);

    }
}
