package com.coralogix.calculator.services;

import com.coralogix.calculator.model.WorldTimeServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class WorldTimeServiceImpl implements WorldTimeService{

    @Autowired
    private RestTemplate restTemplate;

    private String serviceUrl = "http://worldtimeapi.org/api/timezone/America/Argentina/Buenos_Aires";
    @Override
    public WorldTimeServiceResponse getData() {
        WorldTimeServiceResponse response = restTemplate.getForObject(serviceUrl, WorldTimeServiceResponse.class);
        return response;
    }
}
