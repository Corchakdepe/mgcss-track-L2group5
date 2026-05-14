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



# Notas de Refactorización - Sesión 9

## 1. Problema identificado
SonarCloud mide una cobertura del 67.74% en el nuevo código, siendo necesaria al menos un 80% para pasar el Quality Gate.

## 2. Métrica asociada
67.74% de cobertura de nuevo código.

## 3. Riesgo potencial si no se corrige
Además de no poder fusionar los nuevos cambios en la rama principal, el codigo cuyo comportamiento no ha sido comprobado por tests puede tener un comportamiento inesperado antes condiciones nuevas no medidas, introduciendo errores no contemplados a medida que se añade mas codigo.

## Después de la refactorización
Se aumentó la cobertura del nuevo codigo de un 67.74% a un 80.65%.

### Consideración técnica
Aún existen lineas no cubiertas en `EstadoChangeEntity`, concretamente se trata de los constructores. No están cubiertos deliberadamente, ya que para usar los constructores en un test especifico, habria que o bien cambiar/añadir reglas de negocio, o insertar complejidad innecesaria. Ante la potencial deuda tecnica no prevista, se asume.