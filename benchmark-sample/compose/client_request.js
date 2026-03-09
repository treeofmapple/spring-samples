import http from 'k6/http';
import { check, group } from 'k6';

const BASE_URL = 'http://localhost:8003/v1';

export function handleClientPost(name, cpf) {
  return group("POST_REQUEST_CLIENT", function () {
    const payload = JSON.stringify({ name, cpf })
    const res = http.post(`${BASE_URL}/client`, payload, {
      headers: { 'Content-Type': 'application/json' },
      tags: { name: 'CreateClient' },
    });
    check(res, { 'POST Client Success': (r) => r.status === 201 });
    return res;
  });
}

export function handleClientGet(clientId, clientCpf, page) {
  return group("GET_REQUEST_CLIENT", function () {
    const resId = http.get(`${BASE_URL}/client/${clientId}`, {
      tags: { name: 'GetById' }
    });

    check(resId, {
      'Get by Client ID status is 200': (r) => r.status === 200,
      'Has correct ID': (r) => r.json().id === clientId,
    });

    const resCpf = http.get(`${BASE_URL}/client?cpf=${clientCpf}`, {
      tags: { name: 'GetByCpf' }
    });

    check(resCpf, {
      'Get by CPF status is 200': (r) => r.status === 200,
    });

    const resPage = http.get(`${BASE_URL}/client/search?page=${page}`, {
      tags: { name: 'SearchClients' },
    });

    check(resPage, {
      'Search Client successful (200)': (r) => r.status === 200,
      'Has data': (r) => r.json().content.length > 0,
    });

    return { resId, resCpf, resPage };
  })
}

export function handleClientPut(id, name, cpf) {
  return group("PUT_REQUEST_CLIENT", function () {
    const payload = JSON.stringify({ clientId: id, name: name, cpf: cpf });
    const res = http.put(`${BASE_URL}/client`, payload, {
      headers: { 'Content-Type': 'application/json' },
      tags: { name: 'UpdateClient' },
    });
    check(res, { 'PUT Client Success': (r) => r.status === 200 });
    return res;
  });
}

export function handleClientDelete(id) {
  return group("DELETE_REQUEST_CLIENT", function () {
    const res = http.del(`${BASE_URL}/client/${id}`, null, {
      tags: { name: 'DeleteClient' },
    });
    check(res, { 'DELETE Client Success': (r) => r.status === 204 || r.status === 200 });
  })
}
