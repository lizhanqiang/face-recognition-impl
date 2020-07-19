import {Component, ElementRef, OnInit} from '@angular/core';
import {FaceService} from "../../../services/face.service";
import {Location} from '@angular/common';
import {NzUploadFile} from "ng-zorro-antd";

@Component({
  selector: 'app-face-facerec',
  templateUrl: './facerec.component.html',
})
export class FaceFacerecComponent implements OnInit {


  face_recognition_endpoint = "http://localhost:8888/api/v1/face/recognize";

  //画布
  canvas;

  //画笔
  canvasCtx;

  //识别结果
  faceRecResult;

  constructor(private faceService: FaceService,
               public location: Location,
               private el: ElementRef) { }

  ngOnInit() {
    this.canvas = this.el.nativeElement.querySelector("canvas#imagecanvas");
    this.canvasCtx = this.canvas.getContext("2d");
  }

  handlerUploadChange(info: {file: NzUploadFile}):void{
    if(info.file.status == "done"){
      this.getImageBase64(info.file!.originFileObj!, info.file.response);
    } else if(info.file.status == "uploading"){
      console.log("uploading......")
    }
  }

  private getImageBase64(img: File, response): void{
    this.faceRecResult = JSON.stringify(response, null, 4);

    const reader = new FileReader();
    reader.addEventListener('load', ()=>{
      let faceImageObj = new Image();
      faceImageObj.src = reader.result!.toString();

      faceImageObj.addEventListener('load', ()=>{
        const color_list = ['red','green','blue','purple','pink','cyan','yellow','orange','brown'];

        let innerCanvasCtx = this.canvasCtx;
        const canvasWidth = this.canvas.width;
        const canvasHeight = this.canvas.height;
        this.canvasCtx.clearRect(0,0,canvasWidth,canvasHeight);
        const imgWidth = faceImageObj.width;
        const imgHeight = faceImageObj.height;
        const horiScale = imgWidth/canvasWidth;
        const vertScale = imgHeight/canvasHeight;
        let maxScale = Math.max(horiScale, vertScale);
        if(maxScale<=1){
          maxScale = 1;
        }

        innerCanvasCtx.drawImage(faceImageObj,0,0,imgWidth/maxScale,imgHeight/maxScale);
        console.log(response);
        const faces = response.payload.faceList;
        faces.forEach(function(face){
          innerCanvasCtx.beginPath();
          innerCanvasCtx.lineWidth = "3";
          innerCanvasCtx.strokeStyle = "red";
          const boundingbox = face.boundingbox.split(",");
          const leftTopX = boundingbox[3];
          const leftTopY = boundingbox[0];
          const rightBottomX = boundingbox[1];
          const rightBottomY = boundingbox[2];
          innerCanvasCtx.rect(leftTopX/maxScale,leftTopY/maxScale,(rightBottomX-leftTopX)/maxScale,(rightBottomY-leftTopY)/maxScale);
          innerCanvasCtx.stroke();

          innerCanvasCtx.font = 'italic 18px Arial';
          innerCanvasCtx.fillStyle = 'red';
          innerCanvasCtx.fillText(face.name,(leftTopX+20)/maxScale,(leftTopY-5)/maxScale);

          let landmark_idx = 0;
          for(let mark in face.landmarks){
            const landmark_points = face.landmarks[mark];
            innerCanvasCtx.fillStyle=color_list[landmark_idx++];
            landmark_points.forEach(function(point,index){
              innerCanvasCtx.beginPath();
              innerCanvasCtx.arc(parseInt(point.x)/maxScale,parseInt(point.y)/maxScale,2,0,2*Math.PI);
              innerCanvasCtx.fill();
            });
          }

        })
      })
    })
    reader.readAsDataURL(img);
  }

}
