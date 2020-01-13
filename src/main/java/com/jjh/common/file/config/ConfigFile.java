package com.jjh.common.file.config;

import com.amazonaws.regions.Regions;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@ConfigurationProperties(prefix="file")
@PropertySource("classpath:properties/configFile.properties")
public class ConfigFile {

    private int selectSavingWay ;
    private int selectSavingFtp;
    private int selectSavingStorage;
    private int lengthFilename;
    private int selectSavingAws  ;
    private int maxFilesCnt ;
    private int selectAwsRegion;

    private String settingFtpServer;
    private String settingFtpUserId;
    private String settingFtpPassword;
    private String imageServerUrl;
    private String stroageLoc;
    private String settingAwsAccessKey ;
    private String settingAwsSecretKey ;
    private String urlStorage;
    private String urlAwsS3 ;
    private String settingAwsImageBucketName ;
    private String settingAwsFileBucketName ;

    private int selectCategory0; //0
    private int selectCategory1; //1
    private int selectCategory2; //2
    private int selectCategory3; //3
    private int selectCategory4; //4
    private int selectCategory5; //5
    private int selectCategory6; //6
    private int selectCategory7; //7
    private int selectCategory8; //8

    private String subUrlCategory0;
    private String subUrlCategory1;
    private String subUrlCategory2;
    private String subUrlCategory3;
    private String subUrlCategory4;
    private String subUrlCategory5;
    private String subUrlCategory6;
    private String subUrlCategory7;
    private String subUrlCategory8;

    private int extCategory1; //1
    private int extCategory2; //2
    private int extCategory3; //3

    private String extImage;       //extCategory1
    private String extDoc;         //extCategory2
    private String extImageDoc;    //extCategory3

    /**
     *
     * @param select
     * @return
     */
    public boolean checkImageYn(int select) {
        if(select == this.selectCategory0) {
            return true;
        }
        else if (select == this.selectCategory1) {
            return true;
        }
        else if (select == this.selectCategory2) {
            return true;
        }
        else if (select == this.selectCategory3) {
            return true;
        }
        else if (select == this.selectCategory4) {
            return true;
        }
        else if (select == this.selectCategory5) {
            return true;
        }
        else if (select == this.selectCategory6) {
            return true;
        }
        else if (select == this.selectCategory7) {
            return true;
        }

        return false;
    }

    /**
     *
     * @param select
     * @return
     */
    public String getSubUrlBySelection(int select) {

        if(select == this.selectCategory0) {
            return subUrlCategory0;
        }
        else if (select == this.selectCategory1) {
            return subUrlCategory1;
        }
        else if (select == this.selectCategory2) {
            return subUrlCategory2;
        }
        else if (select == this.selectCategory3) {
            return subUrlCategory3;
        }
        else if (select == this.selectCategory4) {
            return subUrlCategory4;
        }
        else if (select == this.selectCategory5) {
            return subUrlCategory5;
        }
        else if (select == this.selectCategory6) {
            return subUrlCategory6;
        }
        else if (select == this.selectCategory7) {
            return subUrlCategory7;
        }
        else if (select == this.selectCategory8) {
            return subUrlCategory8;
        }

        return subUrlCategory0;
    }

    /**
     *
     * @return
     */
    public String getHostName() {

        if(selectSavingWay == selectSavingFtp) {
            return this.imageServerUrl;
        }
        else if(selectSavingWay == selectSavingStorage) {
            return this.urlStorage;
        }

        return this.urlAwsS3;
    }

    public int getSelectNoCategory() {
        return selectCategory0;
    }

    /**
     * AWS Get
     *
     * @return
     */
    public Regions getRegions() {

        switch(selectAwsRegion) {
            case 0:
                return Regions.AP_NORTHEAST_1;
            case 1:
                return Regions.AP_NORTHEAST_2;
            case 2:
                return Regions.AP_SOUTH_1;
            case 3:
                return Regions.AP_SOUTHEAST_1;
            case 4:
                return Regions.AP_SOUTHEAST_2;
            case 5:
                return Regions.CA_CENTRAL_1;
            case 6:
                return Regions.CN_NORTH_1;
            case 7:
                return Regions.CN_NORTHWEST_1;
            case 8:
                return Regions.EU_CENTRAL_1;
            case 9:
                return Regions.EU_WEST_1;
            case 10:
                return Regions.EU_WEST_2;
            case 11:
                return Regions.EU_WEST_3;
            case 12:
                return Regions.GovCloud;
            case 13:
                return Regions.SA_EAST_1;
            case 14:
                return Regions.US_EAST_1;
            case 15:
                return Regions.US_EAST_2;
            case 16:
                return Regions.US_WEST_1;
            case 17:
                return Regions.US_WEST_2;
        }

        return Regions.AP_NORTHEAST_1;
    }

    /**
     * 파일 확장자를 체크할
     * @param category
     * @return
     */
    public String[] getCheckExtension(int category) {
        if(category == getExtCategory1()) {
            return getExtImage().split(",");
        } else if(category == getExtCategory2()) {
            return getExtDoc().split(",");
        } else if(category == getExtCategory3()) {
            return getExtImageDoc().split(",");
        } else {
            return null;
        }
    }
}
