import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import EmployeeService from '../../services/employee.service';
import AuthService from '../../services/auth.service';

const EmployeesList = () => {
  const [employees, setEmployees] = useState([]);
  const [currentEmployee, setCurrentEmployee] = useState(null);
  const [currentIndex, setCurrentIndex] = useState(-1);
  const [searchName, setSearchName] = useState('');
  const [user, setUser] = useState(null);

  useEffect(() => {
    retrieveEmployees();
    const currentUser = AuthService.getCurrentUser();
    setUser(currentUser);
  }, []);

  const retrieveEmployees = () => {
    EmployeeService.getAll()
      .then(response => {
        setEmployees(response.data);
      })
      .catch(e => {
        console.log(e);
      });
  };

  const refreshList = () => {
    retrieveEmployees();
    setCurrentEmployee(null);
    setCurrentIndex(-1);
  };

  const setActiveEmployee = (employee, index) => {
    setCurrentEmployee(employee);
    setCurrentIndex(index);
  };

  const removeAllEmployees = () => {
    EmployeeService.removeAll()
      .then(response => {
        refreshList();
      })
      .catch(e => {
        console.log(e);
      });
  };

  const findByName = () => {
    EmployeeService.findByName(searchName)
      .then(response => {
        setEmployees(response.data);
      })
      .catch(e => {
        console.log(e);
      });
  };

  return (
    <div className="list row">
      <div className="col-md-8">
        <div className="input-group mb-3">
          <input
            type="text"
            className="form-control"
            placeholder="Search by name"
            value={searchName}
            onChange={e => setSearchName(e.target.value)}
          />
          <div className="input-group-append">
            <button
              className="btn btn-outline-secondary"
              type="button"
              onClick={findByName}
            >
              Search
            </button>
          </div>
        </div>
      </div>
      <div className="col-md-6">
        <h4>Employees List</h4>

        <ul className="list-group">
          {employees &&
            employees.map((employee, index) => (
              <li
                className={
                  'list-group-item ' + (index === currentIndex ? 'active' : '')
                }
                onClick={() => setActiveEmployee(employee, index)}
                key={index}
              >
                {employee.firstName} {employee.lastName}
              </li>
            ))}
        </ul>

        {user && user.roles.includes('ROLE_ADMIN') && (
          <button
            className="m-3 btn btn-sm btn-danger"
            onClick={removeAllEmployees}
          >
            Remove All
          </button>
        )}
      </div>
      <div className="col-md-6">
        {currentEmployee ? (
          <div>
            <h4>Employee</h4>
            <div>
              <label>
                <strong>Name:</strong>
              </label>{' '}
              {currentEmployee.firstName} {currentEmployee.lastName}
            </div>
            <div>
              <label>
                <strong>Email:</strong>
              </label>{' '}
              {currentEmployee.email}
            </div>
            <div>
              <label>
                <strong>Department:</strong>
              </label>{' '}
              {currentEmployee.department}
            </div>
            <div>
              <label>
                <strong>Position:</strong>
              </label>{' '}
              {currentEmployee.position}
            </div>
            <div>
              <label>
                <strong>Salary:</strong>
              </label>{' '}
              {currentEmployee.salary}
            </div>

            <Link
              to={'/employees/' + currentEmployee.id}
              className="badge badge-warning"
            >
              Edit
            </Link>
          </div>
        ) : (
          <div>
            <br />
            <p>Please click on an Employee...</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default EmployeesList;
