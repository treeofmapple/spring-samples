import http from 'k6/http';
import { check, group } from 'k6';

const BASE_URL = 'http://localhost:8003/v1';

export function handleProductPost(sku, name, description, price) {
  return group("POST_REQUEST_PRODUCT", function () {
    const payload = JSON.stringify({ sku, name, description, price })
    const res = http.post(`${BASE_URL}/product`, payload, {
      headers: { 'Content-Type': 'application/json' },
      tags: { name: 'CreateProduct' },
    });
    check(res, { 'POST Product Success': (r) => r.status === 201 });
    return res;
  });
}

export function handleProductGet(productId, productSku, page) {
  return group("GET_REQUEST_PRODUCT", function () {
    const resId = http.get(`${BASE_URL}/product/${productId}`, {
      tags: { name: 'GetById' }
    });

    check(resId, {
      'Get by Product ID status is 200': (r) => r.status === 200,
      'Has correct ID': (r) => r.json().id === productId,
    });

    const resSku = http.get(`${BASE_URL}/product?sku=${productSku}`, {
      tags: { name: 'GetBySku' }
    });

    check(resSku, {
      'Get by Sku status is 200': (r) => r.status === 200,
    });

    const resPage = http.get(`${BASE_URL}/product/search?page=${page}`, {
      tags: { name: 'SearchProduct' },
    });

    check(resPage, {
      'Search Product successful (200)': (r) => r.status === 200,
      'Has data': (r) => r.json().content.length > 0,
    });

    return { resId, resSku, resPage };
  })
}

export function handleProductPut(id, sku, name, description, price) {
  return group("PUT_REQUEST_PRODUCT", function () {
    const payload = JSON.stringify({ productId: id, sku:sku, name: name, description: description, price: price });
    const res = http.put(`${BASE_URL}/product`, payload, {
      headers: { 'Content-Type': 'application/json' },
      tags: { name: 'UpdateProduct' },
    });
    check(res, { 'PUT Product Success': (r) => r.status === 200 });
    return res;
  });
}

export function handleProductDelete(id) {
  return group("DELETE_REQUEST_PRODUCT", function () {
    const res = http.del(`${BASE_URL}/product/${id}`, null, {
      tags: { name: 'DeleteProduct' },
    });
    check(res, { 'DELETE Product Success': (r) => r.status === 204 || r.status === 200 });
  })
}
