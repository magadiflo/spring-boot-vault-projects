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

## Autenticación AppRole

El `authentication: TOKEN` es el método de autenticación predeterminado. Si se divulga un token, una parte no deseada
obtiene acceso a Vault y puede acceder a los secretos del cliente previsto.

> Usar el método de autenticación por `token` en la configuración es algo que deberíamos evitar en un entorno de
> producción, ya que tiene permisos de administrador sobre todo `Vault`. En su lugar, podemos usar el `AppRole` con
> `políticas` específicas para limitar el acceso y mantener la seguridad.

La autenticación `AppRole` consta de dos tokens (secretos) difíciles de adivinar: `RoleId` y `SecretId`.