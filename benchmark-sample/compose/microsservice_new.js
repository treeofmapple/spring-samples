import { sleep, group } from 'k6';
import { handleClientPost, handleClientGet, handleClientPut, handleClientDelete } from './client_request.js';
import { handleProductGet, handleProductPost, handleProductPut, handleProductDelete } from './product_request.js';
import {
  handleOrderPost,
  handleAddItemOrder,
  handleOrderGet,
  handleOrderPut,
  handleRemoveItemOrder,
  handleOrderDelete
} from './order_request.js';

export const options = {
    stages: [
        { duration: '60s', target: 120 },
        { duration: '90s', target: 400 },
        { duration: '30s', target: 80 },
    ],
    thresholds: {
        'http_req_duration': ['p(80)<800'],
        'http_req_failed': ['rate<0.20'],
        'checks': ['rate>0.80'],
    },
};

export default function () {
  const runId = Date.now().toString().slice(-6);
  const uniqueId = `${runId}-${__VU}-${__ITER}`;
  const clientCpf = `cpf-${uniqueId}`;
  const clientName = `Test Client ${uniqueId}`;

  let clientId = null;
  let productId = null;
  let orderId = null;
  let orderItemId = null;

  group('01_Client_Management', function () {
    const res = handleClientPost(clientName, clientCpf);
    if (res.status === 201) {
      clientId = res.json().id;
      handleClientGet(clientId, clientCpf, 0);
      sleep(0.5);

      handleClientPut(clientId, clientName, clientCpf);
    }
    sleep(1);
  });

  const productSku = `SKU-${uniqueId}`;
  const productName = `Test Product ${uniqueId}`;
  const productDescription = `wawawawawawawawawaawawa ${uniqueId}`;
  const productPrice = parseFloat((Math.random() * (250 - 50) + 50).toFixed(2));

  group('02_Product_Management', function () {
    const res = handleProductPost(productSku, productName, productDescription, productPrice);
    if (res.status === 201) {
      productId = res.json().id;
      handleProductGet(productId, productSku, 0);
      sleep(0.5);

      handleProductPut(productId, productSku, productName, productDescription, productPrice);
    }
    sleep(1);
  });

  const randomQuantity = (Math.random() * 20);

  group('03_Order_Management', function () {
    if (clientId && productSku) {
      const resOrder = handleOrderPost(clientCpf);
      if (resOrder.status === 201) {
        orderId = resOrder.json().id;
        const resItem = handleAddItemOrder(orderId, productSku, randomQuantity);
        if (resItem.status === 201) {
          orderItemId = resItem.json().id;
        }
        handleOrderGet(orderId, 0);
        handleOrderPut(orderId, clientCpf);
      }
    }
    sleep(1);
  });

  group('04_DB_Cleanup', function () {
    if (orderId && orderItemId) {
      handleRemoveItemOrder(orderId, orderItemId);
    }
    if (orderId) {
      handleOrderDelete(orderId);
    }
    if (productId) {
      handleProductDelete(productId);
    }
    if (clientId) {
      handleClientDelete(clientId);
    }
    sleep(1);
  });

}
