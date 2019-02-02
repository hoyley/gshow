
import client from './Client.js'

export default {

    allEmployees() {
        return client({method: 'GET', path: '/api/employees'})
            .then(r => {
                return r.entity._embedded.employees
            });
    },

    employee(num) {
        return client({method: 'GET', path: '/api/employees/' + num})
            .then(r => {
                debugger;
                return r.entity._embedded.employees
            });
    }
}