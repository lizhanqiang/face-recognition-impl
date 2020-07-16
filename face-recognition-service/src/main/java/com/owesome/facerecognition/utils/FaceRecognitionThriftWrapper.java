package com.owesome.facerecognition.utils;

import com.owesome.facerecognition.thrift.FaceRecognitionService;
import org.apache.thrift.transport.TTransport;

public class FaceRecognitionThriftWrapper {
    private TTransport transport;
    private FaceRecognitionService.Client client;

    public TTransport getTransport() {
        return transport;
    }

    public void setTransport(TTransport transport) {
        this.transport = transport;
    }

    public FaceRecognitionService.Client getClient() {
        return client;
    }

    public void setClient(FaceRecognitionService.Client client) {
        this.client = client;
    }
}
