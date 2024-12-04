# Método de autenticación AppRole

---

El `método de autenticación AppRole` permite que las máquinas o aplicaciones se autentiquen con `roles` definidos por
`Vault`. El diseño abierto de `AppRole` permite un conjunto variado de flujos de trabajo y configuraciones para manejar
una gran cantidad de aplicaciones.

Un `AppRole` representa un conjunto de `políticas de Vault` y restricciones de inicio de sesión que se deben cumplir
para recibir un token con esas políticas. El alcance puede ser tan estrecho o amplio como se desee. Se puede crear un
`AppRole` para una máquina en particular, o incluso para un usuario en particular en esa máquina, o para un servicio
distribuido en varias máquinas. Las credenciales necesarias para iniciar sesión correctamente dependen de las
restricciones establecidas en el `AppRole` asociado con las credenciales.

Un `AppRole` es un mecanismo de autenticación basado en roles diseñado para aplicaciones y servicios en `Vault`.
Consiste en:

- `Role ID`: Un identificador estático del rol (es público).
- `Secret ID`: Una credencial secreta que actúa como contraseña (es privada y puede rotarse).

Estos dos elementos se combinan para autenticar una aplicación o servicio.

## Autenticación AppRole

El `authentication: TOKEN` es el método de autenticación predeterminado. Si se divulga un token, una parte no deseada
obtiene acceso a Vault y puede acceder a los secretos del cliente previsto.

> Usar el método de autenticación por `token` en la configuración es algo que deberíamos evitar en un entorno de
> producción, ya que tiene permisos de administrador sobre todo `Vault`. En su lugar, podemos usar el `AppRole` con
> `políticas` específicas para limitar el acceso y mantener la seguridad.

La autenticación `AppRole` consta de dos tokens (secretos) difíciles de adivinar: `RoleId` y `SecretId`.

## Configurar AppRole en Vault

1. Habilita el Auth Backend de AppRole (si no lo está). `AppRole` permite autenticar aplicaciones mediante una
   combinación de un `role-id` y un `secret-id`.

   ````bash
   $ vault auth enable approle
   ````

2. Crea políticas específicas para cada perfil. Permite el acceso solo al path necesario. Por ejemplo, para el perfil
   `dev` creamos el archivo `spring-boot-vault-approle-dev-policy.hcl` donde colocamos las siguientes políticas.

   ````bash
   path "spring-microservices/data/spring-boot-vault-approle" {
      capabilities = ["read"]
   }
   
   path "spring-microservices/data/spring-boot-vault-approle/dev" {
      capabilities = ["read"]
   }
   ````

   **Donde**

    - En `Vault`, los capabilities (capacidades) son permisos que determinan las operaciones que un cliente puede
      realizar en una ruta específica de datos o configuración. Estos permisos se especifican en las políticas asociadas
      a roles o tokens.
    - `read`, permite leer los datos almacenados en la ruta especificada. Esto es necesario para que las aplicaciones u
      usuarios puedan recuperar valores secretos o configuraciones almacenadas en una ruta.
    -

   Ejecuta el siguiente comando para aplicar las políticas.

   ````bash
   $ vault policy write spring-boot-vault-approle-dev-policy spring-boot-vault-approle-dev-policy.hcl
   ````

3. Crea un `AppRole` por perfil. Creamos un `AppRole` asociado a la política `spring-boot-vault-approle-dev-policy`.

   ````bash
   $ vault write auth/approle/role/spring-boot-vault-approle-dev-role policies=spring-boot-vault-approle-dev-policy secret_id_ttl=1h token_ttl=1h token_max_ttl=4h
   ````
   **Donde**

    - `vault write`, escribe o crea datos en un endpoint especificado en `Vault`. En este caso, está escribiendo
      (creando o configurando) una nueva `AppRole` en la ruta `auth/approle/role/spring-boot-vault-approle-dev-role`.
    - `auth/approle/role/spring-boot-vault-approle-dev-role`, es el endpoint en `Vault` donde se configura un nuevo
      `AppRole`.
    - `auth/approle/role`, es la base de configuración para los roles de `AppRole`.
    - `spring-boot-vault-approle-dev-role`, el nombre del `AppRole` que estamos creando. Este nombre es único dentro del
      espacio de roles en `Vault`.
    - `policies=spring-boot-vault-approle-dev-policy`, es el nombre de la política que define qué operaciones y rutas
      están permitidas para este rol.
    - `secret_id_ttl=1h`, define el tiempo de vida (Time To Live) de los Secret IDs generados para este AppRole. El
      valor `1h` significa que cada Secret ID generado será válido por 1 hora desde su creación.
    - `token_ttl=1h`, establece el tiempo de vida inicial de los tokens generados cuando una aplicación o servicio se
      autentica con este AppRole.
    - `token_max_ttl=4h`, establece el tiempo de vida máximo de los tokens generados. El valor `4h` significa que un
      token puede renovarse hasta un máximo de 4 horas desde su emisión inicial.

4. Obtenemos las credenciales del `AppRole`.
    - Obtener el Role ID
   ````bash
   $ vault read auth/approle/role/spring-boot-vault-approle-dev-role/role-id
   Key        Value
   ---        -----
   role_id    8896c215-9501-e217-28e7-0801d164e95b
   ````

    - Generar el Secret ID

   ````bash
   $ vault write -f auth/approle/role/spring-boot-vault-approle-dev-role/secret-id
   Key                   Value
   ---                   -----
   secret_id             967655ad-602f-d698-861c-f5615f2aeb9d
   secret_id_accessor    3f7c0e4b-9a3b-4c1b-36f8-bed55408fc0b
   secret_id_num_uses    0
   secret_id_ttl         1h
   ````

**Nota**
> Si queremos tener roles y políticas para otros perfiles simplemente debemos repetir los pasos anteriores teniendo en
> cuenta el perfil.

## ¿Qué sucede exactamente si omites los valores en el siguiente comando?

El comando original sería:

````bash
$ vault write auth/approle/role/spring-boot-vault-approle-dev-role policies=spring-boot-vault-approle-dev-policy secret_id_ttl=1h token_ttl=1h token_max_ttl=4h
````

Ahora, qué pasa si usamos el siguiente comando omitiendo `secret_id_ttl=1h token_ttl=1h token_max_ttl=4h`.

````bash
$ vault write auth/approle/role/spring-boot-vault-approle-dev-role policies=spring-boot-vault-approle-dev-policy
````

1. El `AppRole` se creará exitosamente.
2. `Vault` asignará los valores predeterminados globales para:
    - `secret_id_ttl`, puede ser indefinido o configurado (generalmente no hay límite si no se especifica).
    - `token_ttl`, normalmente, será 32 días.
    - `token_max_ttl`, normalmente, será 32 días.
3. Los tokens generados tendrán una mayor duración, lo cual podría ser un riesgo en entornos de producción, ya que se
   reduce la frecuencia de rotación de credenciales.

## Relación entre secret_id_ttl, token_ttl, token_max_ttl y role-id y secret-id

Los valores de `secret_id_ttl`, `token_ttl` y `token_max_ttl` tienen relación con la forma en que se gestionan las
credenciales generadas para tu aplicación al autenticarse mediante `AppRole`. Aquí explicamos detalladamente qué son y
cómo interactúan con el `role_id` y el `secret_id`.

### Conceptos básicos en AppRole

1. `role_id`:

    - Es un identificador estático asociado a un `AppRole`.
    - Es similar a un "nombre de usuario" que tu aplicación usa para autenticarse.
    - Es público, por lo que no requiere tanto cuidado como el secret_id.

2. `secret_id`:

    - Es un valor sensible asociado al AppRole, similar a una "contraseña".
    - Es usado junto con el `role_id` para generar un `token de autenticación`.
    - El tiempo de vida del secret_id está controlado por `secret_id_ttl`.

3. `token`:

    - Es el resultado de la autenticación exitosa usando `role_id + secret_id`.
    - Permite a la aplicación acceder a los secretos y configuraciones en Vault según las políticas asignadas.
    - Su tiempo de vida está controlado por `token_ttl` y `token_max_ttl`.

