import cv2
import base64
import uuid
import numpy as np
import face_recognition
import server.face_repo_dao as dao
from thrift_gen import FaceRecognitionService
from thrift_gen.ttypes import Point, GeneralResponse, FaceRecognitionResult, FaceRecognitionItem
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer
from thrift.transport import TSocket, TTransport

# the ip address and port thrift server listens on
thrift_server_listen_host = '0.0.0.0'
thrift_server_listen_port = 5050

# initialize known face encodings
face_list = dao.query_face_list()
face_repo_encodings = []
face_repo_names = []
face_repo_ids = []
for (id, name, feature) in face_list:
    face_repo_encodings.append(np.fromstring(feature, sep=','))
    face_repo_names.append(name)
    face_repo_ids.append(id)


# thrift server implementation
class FaceRecognitionThriftHandler:
    # insert face into face feature tables
    def addFace(self, name, imgBase64):
        resp = GeneralResponse()
        resp.code = 200
        resp.msg = "success"

        # convert face image base64 encoding into opencv format 
        img_str = base64.b64decode(bytes(imgBase64, 'utf-8'))
        img_np = np.fromstring(img_str, dtype=np.uint8)
        img_cv2 = cv2.imdecode(img_np, cv2.IMREAD_COLOR)
        # convert image color pattern from BGR which opencv uses into RGB which face_recognition uses
        img_rgb = img_cv2[:, :, ::-1]

        # obtain face bounding box
        face_locations = face_recognition.face_locations(img_rgb)

        if len(face_locations) > 1 or face_locations is None:
            resp.code = 400
            resp.msg = 'the image you offer contains at least 2 faces'
        else:
            face_encodings = face_recognition.face_encodings(img_rgb, face_locations)

            face_feature = ','.join([str(i) for i in face_encodings[0]])
            try:
                face_id = str(uuid.uuid4())
                dao.add_face(face_id, name, face_feature, imgBase64)
                face_repo_encodings.append(face_encodings[0])
                face_repo_names.append(name)
                face_repo_ids.append(face_id)
            except Exception as e:
                resp.code = 500
                resp.msg = 'when insert face feature into database,exception occurred'
        return resp

    def delFace(self, face_id):
        resp = GeneralResponse()
        resp.code = 200
        resp.msg = 'success'
        try:
            dao.delete_face(face_id)
            del_idx = face_repo_ids.index(face_id)
            face_repo_encodings.pop(del_idx)
            face_repo_ids.pop(del_idx)
            face_repo_names.pop(del_idx)
        except Exception as e:
            resp.code = 500
            resp.msg = 'when delete face from database, exception occurred'
        return resp

    def recFace(self, imgBase64):
        resp = FaceRecognitionResult()
        resp.code = 200
        resp.msg = 'success'
        resp.faceList = []

        img_str = base64.b64decode(bytes(imgBase64, 'utf-8'))
        img_np = np.fromstring(img_str, dtype=np.uint8)
        img_cv2 = cv2.imdecode(img_np, cv2.IMREAD_COLOR)
        img_rgb = img_cv2[:, :, ::-1]

        face_locations = face_recognition.face_locations(img_rgb)
        face_encodings = face_recognition.face_encodings(img_rgb, face_locations)
        face_landmarks = face_recognition.face_landmarks(img_rgb, face_locations)

        for face_location, face_encoding, face_landmark in zip(face_locations, face_encodings, face_landmarks):
            matches = face_recognition.compare_faces(face_repo_encodings, face_encoding)
            name = None
            face_distances = face_recognition.face_distance(face_repo_encodings, face_encoding)
            best_match_index = np.argmin(face_distances)
            if matches[best_match_index]:
                name = face_repo_names[best_match_index]

            if name is not None:
                cur_face = FaceRecognitionItem()
                cur_face.name = name
                cur_face.boundingbox = ','.join([str(i) for i in face_location])
                cur_face.landmarks = {}
                for cur_feature, cur_coop in face_landmark.items():
                    coop_lst = []
                    for (p_x, p_y) in cur_coop:
                        point = Point()
                        point.x = p_x
                        point.y = p_y
                        coop_lst.append(point)
                    cur_face.landmarks[cur_feature] = coop_lst
                resp.faceList.append(cur_face)
        return resp


if __name__ == '__main__':
    handler = FaceRecognitionThriftHandler()
    processor = FaceRecognitionService.Processor(handler)
    transport = TSocket.TServerSocket(thrift_server_listen_host, thrift_server_listen_port)
    transport_factory = TTransport.TBufferedTransportFactory()
    protocol_factory = TBinaryProtocol.TBinaryProtocolFactory()

    server = TServer.TThreadPoolServer(processor, transport, transport_factory, protocol_factory)
    print('人脸识别Thrift Server启动成功.......')
    server.serve()
