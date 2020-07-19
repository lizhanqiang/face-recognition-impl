package com.owesome.facerecognition.controller;

import com.owesome.facerecognition.entity.FaceFeatureRepoEntity;
import com.owesome.facerecognition.mapper.FaceRecognitionMapper;
import com.owesome.facerecognition.thrift.FaceRecognitionResult;
import com.owesome.facerecognition.thrift.GeneralResponse;
import com.owesome.facerecognition.utils.FaceRecognitionThriftClient;
import com.owesome.facerecognition.utils.FaceRecognitionThriftWrapper;
import com.owesome.facerecognition.utils.HttpResponse;
import com.owesome.facerecognition.utils.PagingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author lizhanq
 * face recognition api service
 */
@Controller
@RequestMapping(value = "/api/v1/face", produces = "application/json")
@CrossOrigin(value = "*",allowCredentials = "true")
class FaceRecognitionController {

    @Autowired
    private FaceRecognitionMapper faceRecognitionMapper;

    @Autowired
    FaceRecognitionThriftClient thriftClient;

    /**
     *  query face list by condition,eg: face id、face name
     * @param pageIndex index of page
     * @param pageSize  size of page
     * @param faceEntity query condition
     * @return face list that meets the query condition
     */
    @PostMapping("/list")
    @ResponseBody
    HttpResponse queryFaceListByCondition(@RequestBody FaceFeatureRepoEntity faceEntity,
                                          @RequestParam("pageIndex") Integer pageIndex,
                                          @RequestParam("pageSize") Integer pageSize){

        Integer offset = (pageIndex - 1) * pageSize;
        List<FaceFeatureRepoEntity> faces = faceRecognitionMapper.queryFaceListByCondition(faceEntity,pageSize, offset);
        Integer totalRecord = faceRecognitionMapper.queryFaceCountByCondition(faceEntity);

        PagingResult pagingResult = new PagingResult();
        pagingResult.setPageSize(pageSize);
        pagingResult.setPageIndex(pageIndex);
        pagingResult.setTotalRecord(totalRecord);
        pagingResult.setPageRecords(faces);
        return HttpResponse.success(pagingResult);
    }

    /**
     * insert face of a person into face feature table in the database
     * @param image image of the person
     * @param name  name of the person
     * @return  status of the operation
     * @throws Exception when communicate with thrift server,sth wrong occurred
     */
    @PostMapping("/add")
    @ResponseBody
    HttpResponse addFace(@RequestParam("image") MultipartFile image,@RequestParam("name")String name) throws Exception{
        //get base64 encoding of face image
        String imageBase64Str = Base64Utils.encodeToString(image.getBytes());

        //create transport to thrift server
        FaceRecognitionThriftWrapper wrapper = thriftClient.getThriftClient();
        //invoke thrift server's method over rpc
        GeneralResponse rpcResponse = wrapper.getClient().addFace(name, imageBase64Str);

        //close transport
        thriftClient.closeThriftClient(wrapper);
        return HttpResponse.success(rpcResponse.code);
    }


    /**
     * delete face data of a person with the given face id
     * @param faceId face id of the person
     * @return status of the operation
     * @throws Exception when communicate with thrift server,sth wrong occurred
     */
    @DeleteMapping("/delete/{faceId}")
    @ResponseBody
    HttpResponse delFace(@PathVariable("faceId") String faceId) throws Exception {
        //create transport to thrift server
        FaceRecognitionThriftWrapper wrapper = thriftClient.getThriftClient();
        //invoke thrift server's method over rpc
        GeneralResponse resp = wrapper.getClient().delFace(faceId);
        //close transport
        thriftClient.closeThriftClient(wrapper);
        return HttpResponse.success(resp.code);
    }

    /**
     * given one face image of a person, the method can tell who she/he is
     * @param image image of a person
     * @return face recognition result(name、bounding box、landmark)
     * @throws Exception when communicate with thrift server,sth wrong occurred
     */
    @PostMapping("/recognize")
    @ResponseBody
    HttpResponse recFace(@RequestParam("image") MultipartFile image) throws Exception{
        //get base64 encoding of face image
        String imageBase64Str = Base64Utils.encodeToString(image.getBytes());
        //create transport to thrift server
        FaceRecognitionThriftWrapper wrapper = thriftClient.getThriftClient();
        //invoke thrift server's method over rpc
        FaceRecognitionResult result = wrapper.getClient().recFace(imageBase64Str);
        //close transport
        thriftClient.closeThriftClient(wrapper);

        return HttpResponse.success(result);
    }


}
