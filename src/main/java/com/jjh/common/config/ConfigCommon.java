package com.jjh.common.config;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@ConfigurationProperties(prefix="com")
@PropertySource("classpath:properties/configCommon.properties")
public class ConfigCommon {

    private Mail mail;
    @Setter @Getter
    public static class Mail {
        private String adminaddress;
    }

    /* 도메인 */
    private Domain domain;
    @Setter @Getter
    public static class Domain {
        private String admin;
        private String front;
    }

    private Mst mst;
    @Setter @Getter
    public static class Mst {
        private String banner;
        private String bannerType;
    }

    private Dtl dtl;
    @Setter @Getter
    public static class Dtl {
        private String bannerTop;
        private String bannerContent;
    }

    private String encodeKey;
    private String aes128Key;

}
