import http from 'k6/http';
import { check, group } from 'k6';

const BASE_URL = 'http://localhost:8003/v1';

export function handleAddItemOrder(orderId, productSku, quantity) {
  return group("POST_REQUEST_ADD_ITEM_ORDER", function () {
    const payload = JSON.stringify({ productSku: productSku, quantity: Math.floor(quantity) });
    const res = http.post(`${BASE_URL}/item/${orderId}`, payload, {
      headers: { 'Content-Type': 'application/json' },
      tags: { name: 'AddItemToOrder' },
    });
    check(res, { 'Add Item Success (201)': (r) => r.status === 201 });
    return res;
  });
}

export function handleRemoveItemOrder(orderId, orderItemId) {
  return group("DELETE_REQUEST_REMOVE_ITEM_ORDER", function () {
    const res = http.del(`${BASE_URL}/item/${orderId}/${orderItemId}`, null, {
      tags: { name: 'RemoveItemFromOrder' },
    });
    check(res, { 'Remove Item Success': (r) => r.status === 204 });
    return res;
  });
}

export function handleOrderPost(cpf) {
  return group("POST_REQUEST_ORDER", function () {
    const payload = JSON.stringify({ cpf })
    const res = http.post(`${BASE_URL}/order`, payload, {
      headers: { 'Content-Type': 'application/json' },
      tags: { name: 'CreateOrder' },
    });
    check(res, { 'POST Order Success': (r) => r.status === 201 });
    return res;
  });
}

export function handleOrderGet(orderId, page) {
  return group("GET_REQUEST_ORDER", function () {
    const resId = http.get(`${BASE_URL}/order/${orderId}`, {
      tags: { name: 'GetById' }
    });

    check(resId, {
      'Get by Order ID status is 200': (r) => r.status === 200,
      'Has correct ID': (r) => r.json().id === orderId,
    });

    const resPage = http.get(`${BASE_URL}/order/search?page=${page}`, {
      tags: { name: 'SearchOrder' },
    });

    check(resPage, {
      'Search Order successful (200)': (r) => r.status === 200,
      'Has data': (r) => r.json().content.length > 0,
    });

    return { resId, resPage };
  })
}

export function handleOrderPut(id, cpf) {
  return group("PUT_REQUEST_ORDER", function () {
    const payload = JSON.stringify({ orderId: id, cpf: cpf });
    const res = http.put(`${BASE_URL}/order`, payload, {
      headers: { 'Content-Type': 'application/json' },
      tags: { name: 'UpdateOrder' },
    });
    check(res, { 'PUT Order Success': (r) => r.status === 200 });
    return res;
  });
}

export function handleOrderDelete(id) {
  return group("DELETE_REQUEST_ORDER", function () {
    const res = http.del(`${BASE_URL}/order/${id}`, null, {
      tags: { name: 'DeleteOrder' },
    });
    check(res, { 'DELETE Order Success': (r) => r.status === 204 || r.status === 200 });
  })
}
