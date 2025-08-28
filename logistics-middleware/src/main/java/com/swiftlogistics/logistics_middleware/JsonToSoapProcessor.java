package com.swiftlogistics.logistics_middleware;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.Processor;
import java.util.Map;

public class JsonToSoapProcessor implements Processor {
    private static final Logger LOG = LoggerFactory.getLogger(JsonToSoapProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        // Assume incoming body is a Map from the JSON
        Map<String, Object> jsonBody = exchange.getIn().getBody(Map.class);
        String customerId = (String) jsonBody.get("customerId");
        String orderId = (String) jsonBody.get("orderId");

        // Construct the SOAP XML payload.
        String soapPayload = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:cms=\"http://cms-service.com/v1\">" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                "<cms:createOrderRequest>" +
                "<cms:customerId>" + customerId + "</cms:customerId>" +
                "<cms:orderId>" + orderId + "</cms:orderId>" +
                "</cms:createOrderRequest>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        LOG.info("Transforming JSON to CMS SOAP request:\n{}", soapPayload);
        exchange.getIn().setBody(soapPayload);
    }
}
