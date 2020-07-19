import { Component, OnInit } from '@angular/core';
import {FormBuilder} from "@angular/forms";
import {FaceService} from "../../../services/face.service";

@Component({
  selector: 'app-face-facelist',
  templateUrl: './facelist.component.html',
})
export class FaceFacelistComponent implements OnInit {

  facelist;

  pageSize=6;
  pageIndex=1;
  totalRecord;

  faceForm = this.formBuilder.group({
    name: ['']
  })



  constructor(private faceService: FaceService, private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.queryFaceList();
  }

  reset():void{
    this.faceForm.reset();
  }

  formQuery(){
    this.pageIndex = 1;
    this.queryFaceList();
  }

  deleteFace(fid){
    this.faceService.deleteFace(fid).subscribe(res=>{
      this.queryFaceList();
    })
  }

  queryFaceList(){
    this.faceService.queryFaceListByCondition(this.faceForm.value, this.pageIndex,this.pageSize).subscribe(res=>{
      this.facelist = res.payload.pageRecords;
      this.totalRecord = res.payload.totalRecord
    })
  }


}
