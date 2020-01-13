package com.jjh.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

/**
 *
 * Resttemplate Service
 *
 *
 */
@Service
public class RestTemplageService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RestTemplate restTemplate;

    public String post(URI url, Map<String, Object> params) throws Exception {
        ResponseEntity<String> result = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);

            result = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            logger.info("restTemplate [result]" + result);
        } catch (Exception e) {
            logger.error("restTemplate [Exception]" + e);
            logger.error("restTemplate [Exception]", e);
            throw new Exception(e);
        }

        return result.getBody();
    }

    public String get(String url) throws Exception {
        ResponseEntity<String> result = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            logger.info("restTemplate [result]" + result);
        } catch (Exception e) {
            logger.error("restTemplate [Exception]" + e);
            logger.error("restTemplate [Exception]", e);
            throw new Exception(e);
        }
        return result.getBody();
    }
}

