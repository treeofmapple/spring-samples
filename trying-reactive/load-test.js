import http from 'k6/http';
import { check, sleep } from 'k6';
import { randomString } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

/*
export const options = {
    vus: 10,
    duration: '100s',
};
*/

export const options = {
    scenarios: {
        mass_creation: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                { duration: '30s', target: 90 },
                { duration: '1m', target: 200 },
                { duration: '0m', target: 0 },
            ],
            exec: 'createOnly',
        },
        database_cleanup: {
            executor: 'per-vu-iterations',
            exec: 'deleteAll',
            vus: 1,
            iterations: 1,
            startTime: '1m35s',
        },
        user_lifecycle: {
            executor: 'constant-vus',
            vus: 10,
            duration: '2m',
            exec: 'lifecycleTest',
        },
    },
    thresholds: {
        'http_req_duration{name:Create}': ['p(95)<400'],
        'http_req_failed': ['rate<0.01'],
    },
};

const BASE_URL = 'http://localhost:8000/v1/user';

const params = (name) => ({
    headers: { 'Content-Type': 'application/json' },
    tags: { name: name },
});

let createdIds = [];

export function createOnly() {
    const uniqueId = randomString(8);
    const payload = JSON.stringify({
        nickname: `test_${uniqueId}`,
        email: `test_${uniqueId}@stress.com`,
        password: 'password123'
    });

    const res = http.post(BASE_URL, payload, params('Create'));
    
    if (check(res, { 'created': (r) => r.status === 201 })) {
        createdIds.push(res.json().id);
    }
    
    sleep(0.1);
}

export function deleteAll() {
    console.log("Starting cleanup phase...");
    
    const searchRes = http.get(`${BASE_URL}/search?page=0`, params('Cleanup-Fetch'));
    const users = searchRes.json().content;

    if (users && users.length > 0) {
        console.log(`Found ${users.length} users to delete.`);
        
        users.forEach(user => {
            const delRes = http.del(`${BASE_URL}/${user.id}`, null, params('Cleanup-Delete'));
            check(delRes, { 'deleted': (r) => r.status === 204 });
        });
    } else {
        console.log("No users found to delete.");
    }
}

export function lifecycleTest() {
    const uniqueId = randomString(8);
    const payload = JSON.stringify({
        nickname: `life_${uniqueId}`,
        email: `life_${uniqueId}@test.com`,
        password: 'password123'
    });

    const createRes = http.post(BASE_URL, payload, params('Create'));
    if (check(createRes, { 'status is 201': (r) => r.status === 201 })) {
        const userId = createRes.json().id;

        if (userId) {
            http.get(`${BASE_URL}/${userId}`, params('GetById'));

            const updatePayload = JSON.stringify({
                userId: userId,
                bio: 'Living the lifecycle'
            });
            http.put(BASE_URL, updatePayload, params('Update'));

            http.del(`${BASE_URL}/${userId}`, null, params('Delete'));
        }
    }
    sleep(Math.random() * 1.5 + 0.5);
}
