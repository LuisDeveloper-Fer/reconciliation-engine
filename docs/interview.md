# Demostración en cinco minutos

## Problema — 30 segundos
El registro interno y el reporte de un proveedor pueden discrepar. El motor compara ambos y explica las diferencias, sin redondear importes ni elegir qué sistema tiene razón.

## Experimento — 2 minutos
Carga los ejemplos del panel: hay un importe distinto y referencias ausentes. Repite el mismo lote y obtendrás el mismo ID.

## Decisión — 1 minuto
Los duplicados tienen prioridad porque vuelven ambiguo el emparejamiento. Se compara moneda antes que importe. Una diferencia es evidencia para revisión, no autorización para corregir un balance. Un algoritmo de agrupación acotado evita introducir un motor de reglas genérico.

## Discusión
- ¿Qué operación es atómica?
- ¿Qué ocurre entre confirmar una escritura y enviar una respuesta?
- ¿Qué impide agotar recursos?
- ¿Qué cambia al ejecutar dos réplicas?
- ¿Qué mide el dashboard y qué no permite concluir?

## Límites que conviene explicar
CSV deliberadamente limitado: reference,amount,currency, sin comillas ni escapes. Máximo 1000 filas y 200000 caracteres por reporte. El orden/formato cambia el hash: la deduplicación es por contenido exacto, no semántica. No corrige operaciones ni convierte monedas.
