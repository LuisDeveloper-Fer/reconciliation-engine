"""Exercise the local Compose stack with fictional data; Python standard library only."""
import json
import time
import uuid
import urllib.request
import urllib.error

BASE = 'http://localhost:8080'

def request(route, body=None, headers=None):
    payload = None if body is None else json.dumps(body).encode()
    req = urllib.request.Request(BASE + route, data=payload, headers={'Content-Type':'application/json', **(headers or {})})
    try:
        with urllib.request.urlopen(req, timeout=5) as response:
            raw=response.read()
            return response.status, json.loads(raw) if raw else {}
    except urllib.error.HTTPError as error:
        raw=error.read()
        return error.code, json.loads(raw) if raw else {}

for _ in range(90):
    try:
        if request('/actuator/health')[0] == 200: break
    except OSError: pass
    time.sleep(2)
else: raise RuntimeError('API did not become ready')

for _ in range(30):
    try:
        with urllib.request.urlopen('http://localhost:4200',timeout=5) as response:
            assert b'<app-root>' in response.read(), 'Angular shell missing'
            break
    except OSError: time.sleep(1)
else: raise RuntimeError('Frontend did not become ready')

body = {'internalCsv': 'reference,amount,currency\nA,10.00,PEN', 'providerCsv': 'reference,amount,currency\nA,9.00,PEN'}
status, item = request('/api/reconciliations', body)
assert status == 200 and item['discrepancyCount'] == 1, (status,item)
assert request('/api/reconciliations', body)[1]['id'] == item['id']

print('reconciliation-engine: HTTP smoke passed')
