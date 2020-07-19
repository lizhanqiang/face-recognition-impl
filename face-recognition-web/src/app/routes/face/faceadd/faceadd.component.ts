import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FaceService} from "../../../services/face.service";
import {Location} from "@angular/common";
import {NzMessageService} from "ng-zorro-antd";
import {FormBuilder, Validators} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-face-faceadd',
  templateUrl: './faceadd.component.html',
})
export class FaceFaceaddComponent implements OnInit {

  faceForm = this.formBuilder.group({
    'name':[null, Validators.required],
    'image':[null, Validators.required]
  })

  constructor(private faceService: FaceService,
               private location: Location,
               private cd: ChangeDetectorRef,
               private router: Router,
               private message: NzMessageService,
               private formBuilder: FormBuilder) { }

  ngOnInit() { }

  formSubmit(){
    let formData = new FormData();
    formData.append("image", this.faceForm.get('image').value);
    formData.append('name',this.faceForm.get('name').value);
    this.faceService.addFace(formData).subscribe(res=>{
      if(res.payload == null || res.payload == ""){
        this.message.create('error', '图片无法识别出人脸或有多张人脸导致添加失败，请更换图片后重新上传');
      }else{
        this.router.navigateByUrl("/face/facelist");
      }
    });
  }

  resetForm(){
    this.faceForm.reset();
  }

  onFaceImageChange(evt){
    const reader = new FileReader();
    if(evt.target.files && evt.target.files.length){
      let file = evt.target.files[0];
      reader.readAsArrayBuffer(file);
      reader.onload = ()=>{
        this.faceForm.get('image').setValue(file);
        this.cd.markForCheck();
      }
    }
  }

}
