# Notas de Refactorización - Sesión 8

## 1. Problema identificado
Repartidos entre las clases `Cliente.java`, `Tecnico.java` y `Solicitud.java` SonarCloud ha detectado doce Code Smells de severidad media, por campos privados que no se usan mas alla del constructor.

## 2. Métrica asociada
12 Code Smells de mantenibilidad con severidad media y un total de 1 hora de deuda técnica.

## 3. Riesgo potencial si no se corrige
Los atributos privados no usados (Dead Code) entorpecen la mantenibilidad aumentando el tamaño del codigo, dificultando su comprensión y reduciendo la prevención contra bugs.

## Después de la refactorización
1. Qué métrica mejoró: Se redujeron los Code Smells a cero y se eliminó la hora de deuda técnica.
2. Qué técnica de refactor se aplicó: "Self Encapsulate Field" (crear getters y/o setters para atributos privados).
3. Qué beneficio aporta a mantenimiento futuro: Código mas robusto ante errores. Aumento en la facilidad de comprensión del código.