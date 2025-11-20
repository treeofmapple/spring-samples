import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Employee } from '../model/employee';
import { environment } from '../../environments/environment';
import { PageEmployeeResponse } from '../model/employeePageResponse';
import { EmployeeRequest } from '../model/employeeRequest';
import { EmployeeUpdate } from '../model/employeeUpdate';
import { EmployeeSearch } from '../model/search';

@Injectable({
  providedIn: 'root',
})
export class EmployeeService {
  private apiServerUrl = environment.apiBaseUrl;
  private apiVersion = environment.apiVersion;
  private apiObject = environment.apiObjectEmployee;

  constructor(private http: HttpClient) {}

  public getEmployeeByPage(page: number): Observable<Employee[]> {
    const params = new HttpParams().set('page', page.toString());
    return this.http
    .get<PageEmployeeResponse>(`${this.apiServerUrl}${this.apiVersion}${this.apiObject}/page`, { params })
    .pipe(map((response) => response.content));
  }

  public searchEmployees(params: EmployeeSearch): Observable<Employee[]> {
    let queryParams = new HttpParams().set('page', params.page.toString());

    if (params.name) {
      queryParams = queryParams.set('name', params.name);
    }

    if (params.email) {
      queryParams = queryParams.set('email', params.email);
    }

    if (params.jobTitle) {
      queryParams = queryParams.set('jobTitle', params.jobTitle);
    }

    if (params.employeeCode) {
      queryParams = queryParams.set('employeeCode', params.employeeCode);
    }

    return this.http
      .get<PageEmployeeResponse>(`${this.apiServerUrl}${this.apiVersion}${this.apiObject}/search`, { params: queryParams })
      .pipe(map((response) => response.content));
  }

  public addEmployee(employeeData: EmployeeRequest): Observable<Employee> {
    return this.http.post<Employee>(
      `${this.apiServerUrl}${this.apiVersion}${this.apiObject}/create`,
      employeeData
    );
  }

  public updateEmployee(
    employeeId: number,
    employeeData: EmployeeUpdate
  ): Observable<Employee> {
    return this.http.put<Employee>(
      `${this.apiServerUrl}${this.apiVersion}${this.apiObject}/update/${employeeId}`,
      employeeData
    );
  }

  public removeEmployee(employeeId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiServerUrl}${this.apiVersion}${this.apiObject}/delete/${employeeId}`);
  }

  public findById(employeeId: number): Observable<Employee> {
    return this.http.get<Employee>(`${this.apiServerUrl}${this.apiVersion}${this.apiObject}/${employeeId}`);
  }

  public generateNewCode(employeeId: number): Observable<Employee> {
    return this.http.post<Employee>(
      `${this.apiServerUrl}${this.apiVersion}${this.apiObject}/generate/${employeeId}`,
      null
    );
  }
}
