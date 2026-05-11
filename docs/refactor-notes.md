# Notas de Refactorización - Sesión 8

## 1. Problema identificado
Repartidos entre las clases `Cliente.java`, `Tecnico.java` y `Solicitud.java` SonarCloud ha detectado doce Code Smells de severidad media, por campos privados que no se usan mas alla del constructor.

## 2. Métrica asociada
12 Code Smells de mantenibilidad con severidad media y un total de 1 hora de deuda técnica.

## 3. Riesgo potencial si no se corrige
Los atributos privados no usados (Dead Code) dificultan la mantenibilidad aumentando el tamaño del codigo, dificultando su comprensión y reduciendo la prevención contra bugs.