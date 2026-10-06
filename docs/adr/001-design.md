# ADR 001 — RECONCILIATION

Estado: aceptada · 2026-10-05

## Contexto
El registro interno y el reporte de un proveedor pueden discrepar. El motor compara ambos y explica las diferencias, sin redondear importes ni elegir qué sistema tiene razón.

## Decisión
Los duplicados tienen prioridad porque vuelven ambiguo el emparejamiento. Se compara moneda antes que importe. Una diferencia es evidencia para revisión, no autorización para corregir un balance. Un algoritmo de agrupación acotado evita introducir un motor de reglas genérico.

## Alternativas
Separar más microservicios o incorporar un broker agregaría despliegue y operación fuera del objetivo. Concentrar todo en el controlador dificultaría probar fallos y razonar sobre el contrato. Se elige una aplicación pequeña con API, casos de uso y adaptadores diferenciados.

## Consecuencias
CSV deliberadamente limitado: reference,amount,currency, sin comillas ni escapes. Máximo 1000 filas y 200000 caracteres por reporte. El orden/formato cambia el hash: la deduplicación es por contenido exacto, no semántica. No corrige operaciones ni convierte monedas.

## Validación
Cinco clases de diferencia, igualdad decimal independiente de escala y CSV malformado.
