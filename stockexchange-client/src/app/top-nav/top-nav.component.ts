import { Component, OnInit } from '@angular/core';
import { Http, RequestOptions,Headers } from '@angular/http';

@Component({
  selector: 'app-top-nav',
  templateUrl: './top-nav.component.html',
  styleUrls: ['./top-nav.component.css']
})
export class TopNavComponent implements OnInit {

  constructor(private http:Http) { }

  ngOnInit() {
    this.loadbalance()
  }

   number:any


  loadbalance() {
    
    let headers = new Headers({ 'Content-Type': 'application/json' });
    let options = new RequestOptions({ headers: headers });
    let username = "Rajith";
    let balance = {
        "username":username
    }
    this.http.post("http://localhost:8080/bank/balance", JSON.stringify(balance),options).
         subscribe((data) => {
           this.number = data.json();
         });
  }

}
