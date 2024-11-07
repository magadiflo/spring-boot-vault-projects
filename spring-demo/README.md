# [Vault Configuration](https://spring.io/guides/gs/vault-config)

**Fuentes relacionadas**

- [Spring Microservices in Action 2021](https://github.com/magadiflo/spring-microservices-in-action-2021/blob/main/05.configuration-with-spring-cloud-config-server.md)

## Instalación

Descargamos el ejecutable de la siguiente dirección
[Install Vault - windows](https://developer.hashicorp.com/vault/install#windows) y lo colocamos en el disco `C:/` en un
directorio con la versión descargada, en mi caso será `C:\vault_1.18.1`.

Si listamos el directorio veremos que ya tenemos nuestro `vault.exe` en el directorio.

```bash
C:\vault_1.18.1
$ ls
vault.exe*
```

## Configura variables de entorno

Realizar las siguientes configuraciones nos va a permitir usar la línea de comando para interactuar con `Vault`.

- Agregar el directorio `C:\vault_1.18.1` en el `Path` del `System variables`. Damos en editar y agregamos la ruta.
- Agregar la variable `VAULT_ADDR` al `System variables` con el valor `http://127.0.0.1:8200`. Damos en New... y creamos
  la nueva variable.

## Inicia servidor de Vault en modo desarrollo

```bash
C:\vault_1.18.1

$ vault server -dev -dev-root-token-id root
==> Vault server configuration:

Administrative Namespace:
             Api Address: http://127.0.0.1:8200
                     Cgo: disabled
         Cluster Address: https://127.0.0.1:8201
   Environment Variables: , , , , , , , , ALLUSERSPROFILE, ANSICON, ANSICON_DEF, APPDATA, CLINK_COMPLETIONS_DIR, CMDER_ALIASES, CMDER_CLINK, CMDER_CONFIGURED, CMDER_CONFIG_DIR, CMDER_INIT_END, CMDER_INIT_START, CMDER_ROOT, CMDER_SHELL, CMDER_USER_FLAGS, COLUMNS, COMPUTERNAME, ChocolateyInstall, ChocolateyLastPathUpdate, ComSpec, CommonProgramFiles, CommonProgramFiles(x86), CommonProgramW6432, ConEmuANSI, ConEmuAnsiLog, ConEmuArgs, ConEmuArgs2, ConEmuBackHWND, ConEmuBaseDir, ConEmuBaseDirShort, ConEmuBuild, ConEmuCfgDir, ConEmuConfig, ConEmuDir, ConEmuDrawHWND, ConEmuDrive, ConEmuHWND, ConEmuHooks, ConEmuPID, ConEmuPalette, ConEmuServerPID, ConEmuTask, ConEmuWorkDir, ConEmuWorkDrive, DataGrip, DriverData, EFC_10264, ESC, GIT_INSTALL_ROOT, GIT_VERSION_USER, HOME, HOMEDRIVE, HOMEPATH, IntelliJ IDEA Community Edition, JAVA_HOME, LANG, LINES, LOCALAPPDATA, LOGONSERVER, MAVEN_HOME, NUMBER_OF_PROCESSORS, NVM_HOME, NVM_SYMLINK, OLD_PATH, OS, OneDrive, PASSWORD_MS_CONFIG_SERVER, PATHEXT, PLINK_PROTOCOL, POSH_INSTALLER, POSH_THEMES_PATH, PROCESSOR_ARCHITECTURE, PROCESSOR_IDENTIFIER, PROCESSOR_LEVEL, PROCESSOR_REVISION, PROMPT, PSModulePath, PUBLIC, Path, ProgramData, ProgramFiles, ProgramFiles(x86), ProgramW6432, SESSIONNAME, SVN_SSH, SystemDrive, SystemRoot, TEMP, TMP, USERDOMAIN, USERDOMAIN_ROAMINGPROFILE, USERNAME, USERPROFILE, USER_BUILD, USER_MAJOR, USER_MINOR, USER_PATCH, ZES_ENABLE_SYSMAN, add_path, add_to_path, aliases, architecture_bits, ccall, cexec, clink_architecture, clink_dummy_capture_env, currenArgu, debug_output, depth, fast_init, feFlagName, feNot, find_query, found, full_path, git_executable, git_locale, lib_base, lib_console, lib_git, lib_path, lib_profile, max_depth, nix_tools, path_position, position, print_debug, print_error, print_verbose, print_warning, time_init, user_aliases, verbose_output, windir
              Go Version: go1.22.8
              Listener 1: tcp (addr: "127.0.0.1:8200", cluster address: "127.0.0.1:8201", disable_request_limiter: "false", max_request_duration: "1m30s", max_request_size: "33554432", tls: "disabled")
               Log Level:
                   Mlock: supported: false, enabled: false
           Recovery Mode: false
                 Storage: inmem
                 Version: Vault v1.18.1, built 2024-10-29T14:21:31Z
             Version Sha: f479e5c85462477c9334564bc8f69531cdb03b65

==> Vault server started! Log data will stream in below:

2024-11-06T14:53:46.697-0500 [INFO]  proxy environment: http_proxy="" https_proxy="" no_proxy=""
2024-11-06T14:53:46.697-0500 [INFO]  incrementing seal generation: generation=1
2024-11-06T14:53:46.697-0500 [WARN]  no `api_addr` value specified in config or in VAULT_API_ADDR; falling back to detection if possible, but this value should be manually set
2024-11-06T14:53:46.697-0500 [INFO]  core: Initializing version history cache for core
2024-11-06T14:53:46.698-0500 [INFO]  events: Starting event system
2024-11-06T14:53:46.705-0500 [INFO]  core: security barrier not initialized
2024-11-06T14:53:46.706-0500 [INFO]  core: security barrier initialized: stored=1 shares=1 threshold=1
2024-11-06T14:53:46.706-0500 [INFO]  core: post-unseal setup starting
2024-11-06T14:53:46.714-0500 [INFO]  core: loaded wrapping token key
2024-11-06T14:53:46.714-0500 [INFO]  core: successfully setup plugin runtime catalog
2024-11-06T14:53:46.714-0500 [INFO]  core: successfully setup plugin catalog: plugin-directory=""
2024-11-06T14:53:46.714-0500 [INFO]  core: no mounts; adding default mount table
2024-11-06T14:53:46.716-0500 [INFO]  core: successfully mounted: type=cubbyhole version="v1.18.1+builtin.vault" path=cubbyhole/ namespace="ID: root. Path: "
2024-11-06T14:53:46.717-0500 [INFO]  core: successfully mounted: type=system version="v1.18.1+builtin.vault" path=sys/ namespace="ID: root. Path: "
2024-11-06T14:53:46.717-0500 [INFO]  core: successfully mounted: type=identity version="v1.18.1+builtin.vault" path=identity/ namespace="ID: root. Path: "
2024-11-06T14:53:46.719-0500 [INFO]  core: successfully mounted: type=token version="v1.18.1+builtin.vault" path=token/ namespace="ID: root. Path: "
2024-11-06T14:53:46.719-0500 [INFO]  rollback: Starting the rollback manager with 256 workers
2024-11-06T14:53:46.719-0500 [INFO]  rollback: starting rollback manager
2024-11-06T14:53:46.721-0500 [INFO]  core: restoring leases
2024-11-06T14:53:46.721-0500 [INFO]  expiration: lease restore complete
2024-11-06T14:53:46.721-0500 [INFO]  identity: entities restored
2024-11-06T14:53:46.721-0500 [INFO]  identity: groups restored
2024-11-06T14:53:46.723-0500 [INFO]  core: Recorded vault version: vault version=1.18.1 upgrade time="2024-11-06 19:53:46.7230092 +0000 UTC" build date=2024-10-29T14:21:31Z
2024-11-06T14:53:46.723-0500 [INFO]  core: post-unseal setup complete
2024-11-06T14:53:46.723-0500 [INFO]  core: root token generated
2024-11-06T14:53:46.723-0500 [INFO]  core: pre-seal teardown starting
2024-11-06T14:53:46.723-0500 [INFO]  rollback: stopping rollback manager
2024-11-06T14:53:46.724-0500 [INFO]  core: pre-seal teardown complete
2024-11-06T14:53:46.724-0500 [INFO]  core.cluster-listener.tcp: starting listener: listener_address=127.0.0.1:8201
2024-11-06T14:53:46.724-0500 [INFO]  core.cluster-listener: serving cluster requests: cluster_listen_address=127.0.0.1:8201
2024-11-06T14:53:46.724-0500 [INFO]  core: post-unseal setup starting
2024-11-06T14:53:46.724-0500 [INFO]  core: loaded wrapping token key
2024-11-06T14:53:46.724-0500 [INFO]  core: successfully setup plugin runtime catalog
2024-11-06T14:53:46.724-0500 [INFO]  core: successfully setup plugin catalog: plugin-directory=""
2024-11-06T14:53:46.726-0500 [INFO]  core: successfully mounted: type=system version="v1.18.1+builtin.vault" path=sys/ namespace="ID: root. Path: "
2024-11-06T14:53:46.726-0500 [INFO]  core: successfully mounted: type=identity version="v1.18.1+builtin.vault" path=identity/ namespace="ID: root. Path: "
2024-11-06T14:53:46.726-0500 [INFO]  core: successfully mounted: type=cubbyhole version="v1.18.1+builtin.vault" path=cubbyhole/ namespace="ID: root. Path: "
2024-11-06T14:53:46.728-0500 [INFO]  core: successfully mounted: type=token version="v1.18.1+builtin.vault" path=token/ namespace="ID: root. Path: "
2024-11-06T14:53:46.728-0500 [INFO]  rollback: Starting the rollback manager with 256 workers
2024-11-06T14:53:46.728-0500 [INFO]  rollback: starting rollback manager
2024-11-06T14:53:46.728-0500 [INFO]  core: restoring leases
2024-11-06T14:53:46.729-0500 [INFO]  expiration: lease restore complete
2024-11-06T14:53:46.730-0500 [INFO]  identity: entities restored
2024-11-06T14:53:46.730-0500 [INFO]  identity: groups restored
2024-11-06T14:53:46.730-0500 [INFO]  core: post-unseal setup complete
2024-11-06T14:53:46.730-0500 [INFO]  core: vault is unsealed
2024-11-06T14:53:46.734-0500 [INFO]  expiration: revoked lease: lease_id=auth/token/root/h9ee180f6f303e9abffe5b922f9e147bb8793cf9e1d7173024bf7581517b64b24
2024-11-06T14:53:46.772-0500 [INFO]  core: successful mount: namespace="" path=secret/ type=kv version="v0.20.0+builtin"
WARNING! dev mode is enabled! In this mode, Vault runs entirely in-memory
and starts unsealed with a single unseal key. The root token is already
authenticated to the CLI, so you can immediately begin using Vault.

You may need to set the following environment variables:

PowerShell:
    $env:VAULT_ADDR="http://127.0.0.1:8200"
cmd.exe:
    set VAULT_ADDR=http://127.0.0.1:8200

The unseal key and root token are displayed below in case you want to
seal/unseal the Vault or re-authenticate.

Unseal Key: v+bjsXq8HWNTkPE8H9RrBvW7NEEk37lfqRwOg/FrZ5g=
Root Token: root

Development mode should NOT be used in production installations!
```

La clave de apertura y el token raíz se muestran en el resultado anterior en caso de que deseemos sellar/abrir la Bóveda
o volver a autenticarse.

Si solo ejecutamos `vault server -dev` en automático `Vault` nos crea un `Root Token`, pero en nuestro caso hemos
iniciado el servidor con un `Root Token` definido.

## Almacenando Configuraciones en Vault

Notar que `spring-demo` es el nombre de la aplicación definida en la configuración `spring.application.name`.

Ahora puede almacenar pares `clave-valor` de configuración dentro de `Vault`. A continuación se almacenarán
configuraciones para el perfil por defecto de nuestra aplicación `spring-demo`.

````bash
C:\vault_1.18.1
$ vault kv put secret/spring-demo demo.password=my-password-123 demo.username=my-user
===== Secret Path =====
secret/data/spring-demo

======= Metadata =======
Key                Value
---                -----
created_time       2024-11-06T20:02:56.4075146Z
custom_metadata    <nil>
deletion_time      n/a
destroyed          false
version            1
````

Almacenamos configuraciones para el perfil `dev`.

````bash
C:\vault_1.18.1
$ vault kv put secret/spring-demo/dev demo.password=user-dev demo.username=pass-dev
======= Secret Path =======
secret/data/spring-demo/dev

======= Metadata =======
Key                Value
---                -----
created_time       2024-11-06T20:22:33.6898947Z
custom_metadata    <nil>
deletion_time      n/a
destroyed          false
version            1
````

Almacenamos configuraciones para el perfil `prod`.

````bash
C:\vault_1.18.1
λ vault kv put secret/spring-demo/prod demo.password=user-prod demo.username=pass-prod
======== Secret Path ========
secret/data/spring-demo/prod

======= Metadata =======
Key                Value
---                -----
created_time       2024-11-06T20:27:28.1167862Z
custom_metadata    <nil>
deletion_time      n/a
destroyed          false
version            1
````

## Dependencias del proyecto de Spring Boot

Se agregó la dependencia de `spring-boot-starter-web` por si optamos por realizar algún endpoint.

````xml
<!--Spring Boot 3.3.5-->
<!--Java 21-->
<!--Spring Cloud Version 2023.0.3-->
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-vault-config</artifactId>
    </dependency>

    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
````

## Configura tu aplicación

````yml
spring:
  application:
    name: spring-demo
  profiles:
    active: prod

  cloud:
    vault:
      host: localhost
      port: 8200
      token: root
      scheme: http
      kv:
        enabled: true
  config:
    import: vault://
````

- `spring.application.name`, define el nombre de la aplicación, en este caso, `spring-demo`. Este nombre se usa en
  varias configuraciones, como el registro de aplicaciones en servicios de descubrimiento o al leer secretos de Vault.
- `spring.profiles.active`, activa el perfil `dev`, lo que permite que la aplicación utilice configuraciones específicas
  de desarrollo. En `Spring Boot`, los perfiles ayudan a configurar entornos específicos, como `dev`, `test`, o `prod`.
- `spring.cloud.vault`, este bloque configura la integración con `Vault`, especificando cómo conectarse y autenticar la
  aplicación.
    - `token: root`, define el token de autenticación que la aplicación usará para acceder a `Vault`. Aquí se ha
      definido un token con privilegios de administrador (`root`), común en entornos de desarrollo o prueba. En
      producción, se recomienda usar tokens con permisos limitados.
    - `scheme: http`, especifica el esquema de conexión a `Vault`, que en este caso es `http`. Esto es común en entornos
      de desarrollo o prueba, pero en producción, se recomienda `https` para cifrar la comunicación entre la aplicación
      y `Vault`.
    - `kv.enabled: true`, activa el uso del `KV (Key-Value)` `Secret Engine` en `Vault`, que permite almacenar y acceder
      a secretos en formato `clave-valor`. Es el mecanismo más sencillo de `Vault` para almacenar secretos como
      contraseñas, API keys, etc.
- `spring.config.import: vault://`, esta propiedad le indica a `Spring Boot` que debe importar las configuraciones desde
  `Vault`. Cuando se usa el prefijo `vault://`, `Spring` intenta conectarse a `Vault` usando las configuraciones de
  `spring.cloud.vault` y extraer los secretos como si fueran propiedades de configuración.

Con esta configuración, tu aplicación:

- Se conecta a Vault utilizando el esquema http, autenticándose con el token root.
- Extrae configuraciones definidas en Vault, específicamente aquellas almacenadas en el motor KV, y las pone a
  disposición de tu aplicación como propiedades.
- Activa el perfil dev para utilizar configuraciones de desarrollo.

- Esta integración te permite centralizar y proteger secretos en Vault, manteniéndolos separados del código fuente y
  gestionándolos desde un solo lugar.

## Define tu clase de configuración

Crea una configuración simple para tu aplicación Spring:

````java

@Getter
@Setter
@ConfigurationProperties(prefix = "demo")
public class MyConfiguration {
    private String username;
    private String password;
    private String tokenApp;
}
````

## Crear una clase de aplicación

Aquí se crea una clase de aplicación con todos los componentes.

````java

@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(MyConfiguration.class)
@SpringBootApplication
public class SpringDemoApplication implements CommandLineRunner {

    private final MyConfiguration configuration;

    public static void main(String[] args) {
        SpringApplication.run(SpringDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("----------------------------------------");
        log.info("Configuration properties");
        log.info("   demo.username is {}", configuration.getUsername());
        log.info("   demo.password is {}", configuration.getPassword());
        log.info("   demo.token-app is {}", configuration.getTokenApp());
        log.info("----------------------------------------");
    }
}
````

## Ejecuta aplicación con un perfil

Las propiedades de configuración se vinculan según los perfiles activados. `Spring Cloud Vault` construye una ruta de
contexto de `Vault` a partir de `spring.application.name`, que es `spring-demo`, y agrega el nombre del perfil `(dev)`,
de modo que al habilitar el perfil `dev` se obtendrán propiedades de configuración adicionales de
`secret/spring-demo/dev`.

````properties
spring.profiles.active=dev
````

Si ejecutamos la aplicacíon con el perfil `dev` seleccionado, veremos que nos trae las configuraciones de dicho perfil.

````bash
[spring-demo] [           main] d.magadiflo.app.SpringDemoApplication    : ----------------------------------------
[spring-demo] [           main] d.magadiflo.app.SpringDemoApplication    : Configuration properties
[spring-demo] [           main] d.magadiflo.app.SpringDemoApplication    :    demo.username is pass-dev
[spring-demo] [           main] d.magadiflo.app.SpringDemoApplication    :    demo.password is user-dev
[spring-demo] [           main] d.magadiflo.app.SpringDemoApplication    : ----------------------------------------
````

## Agrega una nueva configuración al perfil por defecto

Agregaremos una nueva configuración al perfil por defecto.

````bash
C:\vault_1.18.1
$ vault kv put secret/spring-demo demo.token-app=123456-123456-123456
===== Secret Path =====
secret/data/spring-demo

======= Metadata =======
Key                Value
---                -----
created_time       2024-11-06T20:41:14.4704659Z
custom_metadata    <nil>
deletion_time      n/a
destroyed          false
version            4
````

Ahora, en la clase de configuración agregamos la nueva propiedad que será mapeada a la nueva configuración agregada.

````java

@Getter
@Setter
@ConfigurationProperties(prefix = "demo")
public class MyConfiguration {
    private String username;
    private String password;
    private String tokenApp;
}
````

En la clase principal imprimimos la nueva propiedad.

````java

@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(MyConfiguration.class)
@SpringBootApplication
public class SpringDemoApplication implements CommandLineRunner {

    private final MyConfiguration configuration;

    public static void main(String[] args) {
        SpringApplication.run(SpringDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("----------------------------------------");
        log.info("Configuration properties");
        log.info("   demo.username is {}", configuration.getUsername());
        log.info("   demo.password is {}", configuration.getPassword());
        log.info("   demo.token-app is {}", configuration.getTokenApp());
        log.info("----------------------------------------");
    }
}
````

## Ejecutando aplicación con perfil por defecto

Ejecutamos la aplicación sin enviarle ningún perfil, es decir, se ejecutará con el perfil por defecto.

````bash
[spring-demo] [           main] d.magadiflo.app.SpringDemoApplication    : Started SpringDemoApplication in 7.465 seconds (process running for 8.455)
[spring-demo] [           main] d.magadiflo.app.SpringDemoApplication    : ----------------------------------------
[spring-demo] [           main] d.magadiflo.app.SpringDemoApplication    : Configuration properties
[spring-demo] [           main] d.magadiflo.app.SpringDemoApplication    :    demo.username is my-user
[spring-demo] [           main] d.magadiflo.app.SpringDemoApplication    :    demo.password is my-password-123
[spring-demo] [           main] d.magadiflo.app.SpringDemoApplication    :    demo.token-app is 123456-123456-123456
[spring-demo] [           main] d.magadiflo.app.SpringDemoApplication    : ----------------------------------------
````

Ahora, cambiamos el perfil a dev en la configuración `spring.profiles.active=dev` y veamos qué nos imprime la
aplicación.

````bash
[spring-demo] [           main] d.magadiflo.app.SpringDemoApplication    : ----------------------------------------
[spring-demo] [           main] d.magadiflo.app.SpringDemoApplication    : Configuration properties
[spring-demo] [           main] d.magadiflo.app.SpringDemoApplication    :    demo.username is pass-dev
[spring-demo] [           main] d.magadiflo.app.SpringDemoApplication    :    demo.password is user-dev
[spring-demo] [           main] d.magadiflo.app.SpringDemoApplication    :    demo.token-app is 123456-123456-123456
[spring-demo] [           main] d.magadiflo.app.SpringDemoApplication    : ----------------------------------------
````

Como resultado nos muestra las configuraciones del perfil `dev`, pero además, como en el perfil `dev` no tenemos
la configuración del `demo.token-app`, el valor lo toma del perfil por defecto.