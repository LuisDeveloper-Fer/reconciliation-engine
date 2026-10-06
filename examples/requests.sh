#!/usr/bin/env bash
set -euo pipefail
BASE="${BASE:-http://localhost:8080}"
curl -i -X POST "$BASE/api/reconciliations" -H 'Content-Type: application/json' --data '{"internalCsv":"reference,amount,currency\nTX001,100.00,PEN\nTX002,50.00,USD","providerCsv":"reference,amount,currency\nTX001,99.00,PEN\nTX003,50.00,USD"}'
curl -i "$BASE/api/reconciliations"
