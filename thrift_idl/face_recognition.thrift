namespace java com.owesome.facerecognition.thrift

struct Point{
    1: required i32 x;
    2: required i32 y;
}

struct FaceRecognitionItem{
    1: required string name;
    2: required string boundingbox;
    3: required map<string, list<Point>> landmarks;
}

struct FaceRecognitionResult{
    1: required i32 code;
    2: optional string msg;
    3: list<FaceRecognitionItem> faceList;
}

struct GeneralResponse {
    1: required i32 code;
    2: optional string msg;
    3: optional string payload;
}

service FaceRecognitionService{
    GeneralResponse addFace(1: string name, 2: string imgBase64);
    GeneralResponse delFace(1: string face_id);
    FaceRecognitionResult recFace(1: string imgBase64);
}
