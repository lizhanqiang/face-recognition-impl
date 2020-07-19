import { Injectable } from '@angular/core';
import {_HttpClient} from '@delon/theme'

@Injectable({
  providedIn: 'root'
})
export class FaceService {

  service_host = "http://localhost:8888/";

  constructor(private http: _HttpClient) { }

  queryFaceListByCondition(faceEntiry, pageIndex,pageSize){
    return this.http.post(this.service_host+'api/v1/face/list?pageIndex='+pageIndex+'&pageSize='+pageSize,faceEntiry)
  }

  deleteFace(face_id){
    return this.http.delete(this.service_host+'api/v1/face/delete/'+face_id);
  }

  addFace(faceParam){
    return this.http.post(this.service_host+'api/v1/face/add',faceParam)
  }

}
