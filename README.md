# Cuentas bancarias (ahorros y corriente) – Manejo de excepciones en Java

Ejercicio de POO avanzada: clase abstracta `Cuenta`, subclases `Ahorros` y `Corriente`,
polimorfismo, excepciones propias (verificadas y no verificadas) y `try / catch / finally`.

## Estructura
```
src/
├── Entidad/       Cuenta, Ahorros, Corriente, Titular, Banco
├── Excepciones/   SaldoInsuficienteException, CuentaNoEncontradaException,
│                  MontoInvalidoException, DatoInvalidoException
└── app/           App (menú), Pruebas (26 pruebas automáticas)
```

## Compilar y ejecutar (Java 17 o superior)
```
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out app.App        # menú
java -cp out app.Pruebas    # pruebas
```
En Windows (PowerShell):
```
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out app.App
```
En NetBeans o VS Code basta con abrir la carpeta `src` y ejecutar `app.App`.
