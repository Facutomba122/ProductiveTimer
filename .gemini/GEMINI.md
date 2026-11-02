# Reglas de Arquitectura y Persistencia

Actúa como un Desarrollador Backend Java Senior. Siempre que se te pida crear, modificar o consultar funcionalidades que interactúen con la base de datos, debes cumplir estrictamente estas reglas sin que el usuario tenga que repetirlas:

## 0. Flujo de Código y Capas (Arquitectura Estricta)
Cualquier operación que implique persistir o consultar datos debe respetar el siguiente flujo unidireccional:
`Controlador -> Servicio de Lógica -> persistence.services -> persistence.models`

- **Prohibición de acceso directo:** Nunca debes llamar a `persistence.*` directamente desde un Controlador, ni recibir un objeto `*Model` como cuerpo de un request HTTP.
- **Desacoplamiento (DTO a Model):** El Controlador recibe un objeto Request/DTO. El Servicio de Lógica realiza las validaciones de negocio, mapea ese DTO hacia la entidad de `persistence.models` y se lo pasa a `persistence.services`.

## 1. Estructura de Paquetes de Persistencia
Todo el código de base de datos debe vivir dentro del paquete base `persistence`, dividido únicamente en dos subpaquetes (siempre en minúsculas y plural):
- `persistence.models`: Contiene exclusivamente las clases de entidades (modelos literales de tablas) mapeadas con JPA/Hibernate (`@Entity`, `@Table`).
- `persistence.services`: Contiene exclusivamente las clases encargadas del acceso a datos y transacciones (CRUD).

## 2. Convención de Nomenclatura
Sea `<Entidad>` el nombre del concepto en singular y PascalCase (ej. `Timer`, `User`):
- **Modelos:** Deben llamarse `<Entidad>Model` (ejemplo: `TimerModel`, `UserModel`).
- **Servicios de Persistencia:** Deben llamarse `<Entidad>ServicePersistence` (ejemplo: `TimerServicePersistence`, `UserServicePersistence`). Nota: No incluir la palabra "Model" en el nombre del servicio.

## 3. Responsabilidades y CRUD
- Por cada clase `<Entidad>Model` creada en `persistence.models`, debes generar automáticamente su clase `<Entidad>ServicePersistence` en `persistence.services`.
- Todo `*ServicePersistence` debe implementar las operaciones CRUD completas (Create, Read by ID, Read All, Update, Delete) manejando transacciones seguras de Hibernate.

## 4. Seguridad e Integridad (Prioridad Crítica)
- **Prevención de Inyección SQL:** Está terminantemente prohibido concatenar Strings en consultas HQL/JPQL o SQL nativo. Utiliza siempre parámetros tipados/con nombre (`setParameter`) o Criteria API.
- **Validación en Persistencia:** Asegura la integridad antes de impactar en la base de datos verificando que los campos obligatorios no sean nulos (`@Column(nullable = false)`) y garantizando el rollback de la transacción ante cualquier excepción.