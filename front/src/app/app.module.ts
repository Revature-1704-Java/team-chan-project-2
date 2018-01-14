import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';


import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { SearchComponent } from './search/search.component';
import { ViewComponent } from './view/view.component';
import { CatalogComponent } from './catalog/catalog.component';
import { ThreadComponent } from './thread/thread.component';
import { ThreadService } from './shared/thread.service';
import { HttpClient } from '@angular/common/http/src/client';


@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    SearchComponent,
    ViewComponent,
    CatalogComponent,
    ThreadComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot([
      {path: '', component: ViewComponent}
    ])
  ],
  providers: [ThreadService],
  bootstrap: [AppComponent]
})
export class AppModule { }
