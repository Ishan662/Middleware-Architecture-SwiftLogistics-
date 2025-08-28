package com.swiftlogistics.logistics_middleware;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.Processor;

public class TcpToJsonProcessor implements Processor {
    private static final Logger LOG = LoggerFactory.getLogger(TcpToJsonProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        String tcpMessage = exchange.getIn().getBody(String.class);

        // Assuming the TCP message is a simple string, e.g., "SHIPMENT-001".
        String shipmentId = tcpMessage.trim();
        String jsonPayload = String.format("{\"shipmentId\": \"%s\"}", shipmentId);

        LOG.info("Transforming WMS TCP data to ROS JSON payload:\n{}", jsonPayload);
        exchange.getIn().setBody(jsonPayload);
    }
}
