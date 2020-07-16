package com.owesome.facerecognition.utils;

import com.owesome.facerecognition.thrift.FaceRecognitionService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class FaceRecognitionThriftClient {
    @Autowired
    private Environment env;

    public FaceRecognitionThriftWrapper getThriftClient() throws TTransportException {
        FaceRecognitionThriftWrapper wrapper = new FaceRecognitionThriftWrapper();
        TTransport transport = new TSocket(env.getProperty("facerecognition.thrift.server.host"),Integer.parseInt(env.getProperty("facerecognition.thrift.server.port")));
        transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);
        FaceRecognitionService.Client client = new FaceRecognitionService.Client(protocol);

        wrapper.setClient(client);
        wrapper.setTransport(transport);

        return  wrapper;
    }

    public void closeThriftClient(FaceRecognitionThriftWrapper wrapper){
        wrapper.getTransport().close();
    }
}
