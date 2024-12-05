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

## Relación entre [secret_id_ttl, token_ttl, token_max_ttl] y [role-id y secret-id]

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
   $ vault write auth/approle/role/spring-boot-vault-approle-dev-role policies=spring-boot-vault-approle-dev-policy
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

   **Nota**
   > Cuando generemos el `role-id` y `secret-id` a partir del rol anterior, los valores no tendrán una fecha de
   > caducidad, tendrán un tiempo de vida ilimitado. Si queremos asignarle un tiempo de vida debemos crear el rol con
   > algunas opciones adicionales. Ver la sección `Configura tiempo de vida de secret-id y tokens en AppRole.`


4. Obtenemos las credenciales del `AppRole`.
    - Obtener el Role ID
   ````bash
   $ vault read auth/approle/role/spring-boot-vault-approle-dev-role/role-id
   Key        Value
   ---        -----
   role_id    cd268cb1-d711-359d-ba9e-76de1d6a507a
   ````

    - Generar el Secret ID

   ````bash
   $ vault write -f auth/approle/role/spring-boot-vault-approle-dev-role/secret-id
   Key                   Value
   ---                   -----
   secret_id             9e2bf4d8-2654-2f32-a7ed-36b4e8cb938e
   secret_id_accessor    f4792b09-b6a0-9169-1f8a-60463b958a67
   secret_id_num_uses    0
   secret_id_ttl         0s
   ````

**Nota**
> Si queremos tener roles y políticas para otros perfiles simplemente debemos repetir los pasos anteriores teniendo en
> cuenta el perfil.

## Algunos comandos para interactuar con Vault, políticas y roles

- Listar políticas

   ````bash
   $ vault policy list
   default
   spring-boot-vault-approle-dev-policy
   root
   ````

- Listar roles

   ````bash
   $ vault list auth/approle/role
   Keys
   ----
   spring-boot-vault-approle-dev-role
   ````

- Eliminar una política

  ````bash
  $ vault policy delete spring-boot-vault-approle-dev-policy
  Success! Deleted policy: spring-boot-vault-approle-dev-policy
  ````

- Eliminar un rol

   ````bash
   $ vault delete auth/approle/role/spring-boot-vault-approle-dev-role
   Success! Data deleted (if it existed) at: auth/approle/role/spring-boot-vault-approle-dev-role
   ````

- Detalles del rol

   ````bash
   $ vault read auth/approle/role/spring-boot-vault-approle-dev-role
   Key                        Value
   ---                        -----
   bind_secret_id             true
   local_secret_ids           false
   policies                   [spring-boot-vault-approle-dev-policy]
   secret_id_bound_cidrs      <nil>
   secret_id_num_uses         0
   secret_id_ttl              0s
   token_bound_cidrs          []
   token_explicit_max_ttl     0s
   token_max_ttl              0s
   token_no_default_policy    false
   token_num_uses             0
   token_period               0s
   token_policies             [spring-boot-vault-approle-dev-policy]
   token_ttl                  0s
   token_type                 default
   ````

## Configura tiempo de vida de secret-id y tokens en AppRole

A continuación se muestra el comando para crear un `AppRole` asociado a la política
`spring-boot-vault-approle-dev-policy` pero asignándole un tiempo de vida `(TTL)` al `secret-id` y `tokens`. Es
importante recordar que el `token` en `Vault` se genera utilizando el `secret-id` y el `role-id` de un `AppRole`.

En el siguiente comando el `TTL (Time To Live)` del `token` y del `secret-id` está definido por las opciones que usamos
en el siguiente comando.

````bash
$ vault write auth/approle/role/spring-boot-vault-approle-dev-role policies=spring-boot-vault-approle-dev-policy secret_id_ttl=1h token_ttl=1h token_max_ttl=4h
````

**Donde**

- `secret_id_ttl=1h`: Esto define el tiempo de vida del secret-id. En este caso, el secret-id expirará después de 1
  hora. Una vez que expire, necesitarás generar un nuevo secret-id para autenticarte nuevamente.


- `token_ttl=1h`: Esto define el tiempo de vida del token generado. En este caso, el token expirará después de 1 hora.
  Una vez que expire, la aplicación necesitará generar un nuevo token utilizando un role-id y un secret-id válidos.


- `token_max_ttl=4h`: Esto establece un límite máximo para la duración del token, que puede extenderse hasta un máximo
  de 4 horas. Si el token se genera con un TTL menor (por ejemplo, 1 hora como configuraste), se aplicará ese TTL, pero
  el token no podrá durar más de 4 horas, aunque su TTL inicial se haya configurado a menos.
