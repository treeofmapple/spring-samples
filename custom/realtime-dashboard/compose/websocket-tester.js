const { Client } = require('@stomp/stompjs');
const SockJS = require('sockjs-client');

const socket = new SockJS('http://localhost:8000/vehicle');
const client = new Client({
  webSocketFactory: () => socket,
  reconnectDelay: 5000,
  debug: (msg) => console.log(msg),
});

client.onConnect = () => {
  console.log('Connected');
  client.subscribe('/topic/telemetry', (message) => {
    console.log('Received:', message.body);
  });

  client.publish({ destination: '/streaming/some', body: 'Hello STOMP via SockJS' });
};

client.onStompError = (frame) => {
  console.error('Broker error:', frame.headers['message']);
};

client.activate();

// npm install @stomp/stompjs ws
