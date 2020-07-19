import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { FaceFacelistComponent } from './facelist/facelist.component';
import { FaceFacerecComponent } from './facerec/facerec.component';
import { FaceFaceaddComponent } from './faceadd/faceadd.component';

const routes: Routes = [

  { path: 'facelist', component: FaceFacelistComponent },
  { path: 'facerec', component: FaceFacerecComponent },
  { path: 'faceadd', component: FaceFaceaddComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FaceRoutingModule { }
