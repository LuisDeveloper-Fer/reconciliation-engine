# API — Reconciliation Engine

Base: http://localhost:8080. Content-Type: application/json.

| Método | Ruta | Contrato |
| --- | --- | --- |
| POST | `/api/reconciliations` | 200 ejecución nueva/existente; 400 CSV inválido |
| GET | `/api/reconciliations/{id}` | Resultado persistido |
| GET | `/api/reconciliations` | Últimas 20 ejecuciones |

## Request
```json
{
  "internalCsv": "reference,amount,currency\nTX001,100.00,PEN\nTX002,50.00,USD",
  "providerCsv": "reference,amount,currency\nTX001,99.00,PEN\nTX003,50.00,USD"
}
```

## Response (campos relevantes)
```json
{
  "id": "sha256-de-los-reportes",
  "internalRows": 2,
  "providerRows": 2,
  "discrepancyCount": 3,
  "reportJson": "[{\"reference\":\"TX001\",\"rule\":\"AMOUNT_MISMATCH\",\"explanation\":\"Internal=100.00, provider=99.00\"}]"
}
```

## Errores
400 indica validación o formato inválido; 404 indica recurso inexistente. Los estados específicos se detallan en la tabla. ProblemDetail se usa para errores de negocio y validación donde aplica; autenticación puede devolver cuerpo vacío y WWW-Authenticate. Los clientes deben usar códigos, no parsear mensajes internos.

CSV: reference,amount,currency. Referencia hasta 64 caracteres; importe no negativo con hasta 9 enteros y 2 decimales; PEN/USD/EUR. No hay soporte de comillas CSV. reportJson contiene el array serializado de diferencias.

