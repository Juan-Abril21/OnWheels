# Wheels - Aplicación de Reserva de Rutas

## Descripción
Wheels es una aplicación móvil desarrollada en Java que permite a los usuarios crear y reservar rutas compartidas de viaje. La aplicación utiliza Firebase para la autenticación de usuarios, almacenamiento de datos y lógica de negocio.

## Características Principales
- **Autenticación de Usuarios**: Sistema seguro de registro e inicio de sesión.
- **Creación de Rutas**: Los usuarios pueden crear nuevas rutas especificando información del vehículo, horarios y capacidad.
- **Reserva de Cupos**: Posibilidad de reservar lugares disponibles en rutas existentes.
- **Gestión de Reservas**: Visualización y administración de todas las reservas realizadas por el usuario.
- **Información de Vehículos**: Registro y visualización de detalles de los vehículos utilizados en las rutas.

## Tecnologías Utilizadas
- **Lenguaje**: Java
- **Backend**: Firebase
  - Authentication para gestión de usuarios
  - Realtime Database / Firestore para almacenamiento de datos
  - Cloud Functions para lógica del servidor (opcional)
- **UI/UX**: Interfaces nativas de Android

## Requisitos
- Android 6.0 (Marshmallow) o superior
- Conexión a Internet
- Servicios de Google Play actualizados

## Instalación
1. Descarga el archivo APK desde la sección de releases
2. Habilita la instalación de aplicaciones de orígenes desconocidos en tu dispositivo Android
3. Instala la aplicación siguiendo las instrucciones en pantalla

## Configuración
1. Registra una nueva cuenta o inicia sesión con credenciales existentes
2. Completa tu perfil de usuario con los datos solicitados
3. ¡Listo para usar la aplicación!

## Uso Básico
### Crear una Ruta
1. Desde la pantalla principal, selecciona "Crear Ruta"
2. Completa la información requerida (origen, destino, horario, capacidad, etc.)
3. Agrega los detalles de tu vehículo
4. Confirma la creación de la ruta

### Reservar un Cupo
1. Navega hasta "Buscar Rutas" en el menú principal
2. Filtra las rutas disponibles según tus necesidades
3. Selecciona una ruta y verifica los detalles
4. Presiona "Reservar" y confirma tu reserva

### Gestionar Reservas
1. Accede a "Mis Reservas" desde el menú de usuario
2. Visualiza todas tus reservas activas
3. Selecciona una reserva para ver detalles o cancelarla

## Estructura del Proyecto
- `/app` - Código fuente principal
- `/app/src/main/java` - Código Java
- `/app/src/main/res` - Recursos de la aplicación
- `/firebase` - Configuración y reglas de Firebase

## Contribución
1. Haz un fork del repositorio
2. Crea una rama para tu funcionalidad (`git checkout -b feature/nueva-funcionalidad`)
3. Haz commit de tus cambios (`git commit -m 'Añadir nueva funcionalidad'`)
4. Haz push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

## Contacto
- Correo: [jdac149@gmail.com]
- GitHub: [Juan-Abril21]
