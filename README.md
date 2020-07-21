# face-recognition
从0开始人脸识别系统，主要包含如下模块

* face-recognition-service  
人脸识别Java接口层，采用[spring boot](https://spring.io/projects/spring-boot) 实现，主要职责是接受web前端的请求，并通过thrift rpc请求**face_recognition_thrift_server**模块执行具体的人脸识别任务
* face-recognition-web  
人脸识别Web前端，采用阿里的[ng-alain](https://ng-alain.com/zh) 实现，主要职责是调用**face-recognition-service**模块完成前端的人脸列表展示、添加/删除人脸、人脸比对
* face_recognition_thrift_server  
人脸识别模块，采用非常流行的[face_recognition](https://github.com/ageitgey/face_recognition) 人脸识别库，目前在github上斩获35.4k star
* sample_faces  
人脸识别样例图片
* thrift_idl  
人脸识别[thrift](http://thrift.apache.org/) 接口定义文件，该文件作为**face-recognition-service**模块和**face_recognition_thrift_server**模块的接口约定
