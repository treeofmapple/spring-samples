import { Component, OnInit } from '@angular/core';
import { Employee } from './model/employee';
import { EmployeeService } from './service/employee-service';
import { HttpErrorResponse } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { EmployeeUpdate } from './model/employeeUpdate';
import { EmployeeSearch } from './model/search';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [EmployeeService],
})
export class AppComponent implements OnInit {
  public employees!: Employee[];
  public employee!: Employee;
  public editEmployee!: Employee;
  public deleteEmployee!: Employee;

  constructor(private employeeService: EmployeeService) {}

  ngOnInit(): void {
    this.getEmployees(0);
  }

  public getEmployees(page: number): void {
    this.employeeService.getEmployeeByPage(page).subscribe({
      next: (response: Employee[]) => {
        this.employees = response;
      },
      error: (error: HttpErrorResponse) => {
        alert(error.message);
      },
    });
  }

  public findById(employeeId: number): void {
    this.employeeService.findById(employeeId).subscribe({
      next: (response: Employee) => {
        this.employee = response;
      },
      error: (error: HttpErrorResponse) => {
        alert(error.message);
      },
    });
  }

  public onAddEmployee(addForm: NgForm): void {
    document.getElementById('add-employee-form')?.click();
    this.employeeService.addEmployee(addForm.value).subscribe({
      next: (response: Employee) => {
        this.findById(response.id);
        addForm.reset();
      },
      error: (error: HttpErrorResponse) => {
        alert(error.message);
        addForm.reset();
      },
    });
  }

  public onUpdateEmployee(employee: EmployeeUpdate): void {
    this.employeeService.updateEmployee(employee.id, employee).subscribe({
      next: (response: Employee) => {
        this.findById(response.id);
      },
      error: (error: HttpErrorResponse) => {
        alert(error.message);
      },
    });
  }

  public onDeleteEmployee(employeeId: number): void {
    this.employeeService.removeEmployee(employeeId).subscribe({
      next: (response: void) => {
        this.getEmployees(0);
      },
      error: (error: HttpErrorResponse) => {
        alert(error.message);
      },
    });
  }

  public searchEmployee(params: EmployeeSearch): void {
    this.employeeService.searchEmployees(params).subscribe({
      next: (response: Employee[]) => {
        this.employees = response;
      },
      error: (error: HttpErrorResponse) => {
        alert(error.message)
      }
    })
  }

  public onOpenModal(employee: Employee, mode: String): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'model');
    if (mode === 'add') {
      button.setAttribute('data-target', '#addEmployeeModal');
    }
    if (mode === 'edit') {
      this.editEmployee = employee;
      button.setAttribute('data-target', '#updateEmployeeModal');
    }
    if (mode === 'delete') {
      this.deleteEmployee = employee;
      button.setAttribute('data-target', '#deleteEmployeeModal');
    }
    container?.appendChild(button);
    button.click();
  }
}
