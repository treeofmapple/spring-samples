import http from 'k6/http';
import { check, sleep, group } from 'k6';

const BASE_URL = 'http://localhost:8000/v1';

export const options = {
  stages: [
    { duration: '1m', target: 220 },
    { duration: '3m', target: 400 },
    { duration: '30s', target: 80 },
  ],

  thresholds: {
    'http_req_duration': ['p(95)<500'],
    'http_req_failed': ['rate<0.10'],
    'checks': ['rate>0.80'],
  },
};


export default function () {
  const uniqueId = `${__VU}-${__ITER}`;

  const clientCpf = `cpf-${uniqueId}`;
  const clientName = `Test Client ${uniqueId}`;
  const productSku = `SKU-${uniqueId}`;
  const productName = `Test Product ${uniqueId}`;
  const productPrice = parseFloat((Math.random() * (250 - 50) + 50).toFixed(2));

  let createdClient;
  let createdProduct;

  group('01_Client_Management', function () {
    const createClientPayload = JSON.stringify({ name: clientName, cpf: clientCpf });
    const resCreateClient = http.post(`${BASE_URL}/client/create`, createClientPayload, { headers: { 'Content-Type': 'application/json' } });
    check(resCreateClient, { 'Client created successfully (201)': (r) => r.status === 201 });
    if (resCreateClient.status === 201) {
        try {
            createdClient = resCreateClient.json();
        } catch (e) {
            console.error(`Failed to parse JSON for created client: ${resCreateClient.body}`);
            createdClient = null;
        }
    } else if (resCreateClient.status !== 201) {
        console.error(`Client creation failed. Status: ${resCreateClient.status}. Body: ${resCreateClient.body}. CPF: ${clientCpf}`);
        createdClient = null;
    }
    sleep(0.5);

    if (createdClient) {
      const resFindClientByCpf = http.get(`${BASE_URL}/client/get?cpf=${clientCpf}`, {
        name: '/client/get?cpf=[cpf]',
      });
      check(resFindClientByCpf, { 'Client found by CPF (200)': (r) => r.status === 200 });
      sleep(0.5);

      if (createdClient.id) {
        const resFindClientById = http.get(`${BASE_URL}/client/${createdClient.id}`, {
          name: '/client/{id}',
        });
        check(resFindClientById, { 'Client found by ID (200)': (r) => r.status === 200 });
        sleep(0.5);
      } else {
        console.warn(`Client ID is missing after creation, skipping 'Find Client by ID' or it will use undefined. CPF: ${clientCpf}. Response from create: ${JSON.stringify(createdClient)}`);
      }
    } else {
        console.warn(`Client object is not available (creation might have failed). CPF: ${clientCpf}`);
    }

    const resFindAllClients = http.get(`${BASE_URL}/client/all`);
    check(resFindAllClients, { 'Find all clients successful (200)': (r) => r.status === 200 });
    sleep(0.5);
  });

  group('02_Product_Management', function () {
    const createProductPayload = JSON.stringify({ sku: productSku, name: productName, description: "A benchmark product", price: productPrice });
    const resCreateProduct = http.post(`${BASE_URL}/product/create`, createProductPayload, { headers: { 'Content-Type': 'application/json' } });
    check(resCreateProduct, { 'Product created successfully (201)': (r) => r.status === 201 });
    if (resCreateProduct.status === 201) {
        try {
            createdProduct = resCreateProduct.json();
        } catch (e) {
            console.error(`Failed to parse JSON for created product: ${resCreateProduct.body}`);
            createdProduct = null;
        }
    } else {
        console.error(`Product creation failed. Status: ${resCreateProduct.status}. Body: ${resCreateProduct.body}. SKU: ${productSku}`);
        createdProduct = null;
    }
    sleep(0.5);

    if (createdProduct && createdProduct.id && createdProduct.sku) {
      const resFindProductBySku = http.get(`${BASE_URL}/product/get?sku=${createdProduct.sku}`, {
        name: '/product/get?sku=[sku]',
      });
      check(resFindProductBySku, { 'Product found by SKU (200)': (r) => r.status === 200 });
      sleep(0.5);

      const resFindProductById = http.get(`${BASE_URL}/product/${createdProduct.id}`, {
        name: '/product/{id}',
      });
      check(resFindProductById, { 'Product found by ID (200)': (r) => r.status === 200 });
      sleep(0.5);
    } else if (createdProduct) {
        console.warn(`Product created but ID or SKU is missing. SKU: ${productSku}. Response: ${JSON.stringify(createdProduct)}`);
    } else {
        console.warn(`Product object is not available (creation might have failed). SKU: ${productSku}`);
    }

    const resFindAllProducts = http.get(`${BASE_URL}/product/all`);
    check(resFindAllProducts, { 'Find all products successful (200)': (r) => r.status === 200 });
    sleep(0.5);
  });

  group('04_Cleanup', function() {
    if (createdProduct && createdProduct.sku) {
      const resDeleteProduct = http.del(`${BASE_URL}/product/delete?sku=${createdProduct.sku}`, {
        name: '/product/delete?sku=[sku]',
      });
      check(resDeleteProduct, { 'Product deleted successfully by SKU (204)': (r) => r.status === 204 });
      sleep(0.5);
    }

    if (clientCpf) {
      const resDeleteClient = http.del(`${BASE_URL}/client/delete?cpf=${clientCpf}`, {
        name: '/client/delete?cpf=[cpf]',
      });
      check(resDeleteClient, { 'Client deleted successfully by CPF (204)': (r) => r.status === 204 });
    }
  });
}
