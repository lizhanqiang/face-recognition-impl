import { NgModule } from '@angular/core';
import { SharedModule } from '@shared';
import { FaceRoutingModule } from './face-routing.module';
import { FaceFacelistComponent } from './facelist/facelist.component';
import { FaceFacerecComponent } from './facerec/facerec.component';
import {NzUploadModule} from "ng-zorro-antd";
import { FaceFaceaddComponent } from './faceadd/faceadd.component';
import {ReactiveFormsModule} from "@angular/forms";

const COMPONENTS = [
  FaceFacelistComponent,
  FaceFacerecComponent,
  FaceFaceaddComponent];
const COMPONENTS_NOROUNT = [];

@NgModule({
  imports: [
    SharedModule,
    FaceRoutingModule,
    NzUploadModule,
    ReactiveFormsModule
  ],
  declarations: [
    ...COMPONENTS,
    ...COMPONENTS_NOROUNT
  ],
})
export class FaceModule { }
