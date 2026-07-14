# SmartHealth Monitor 🫀

**UTNG · DGSW · Desarrollo de Aplicaciones Multiplataforma (Wear OS + Android TV)**
Alumno: Chavero Martínez Noé · Grupo: GIDS6092

---

## Descripción

SmartHealth Monitor es una aplicación multiplataforma de monitoreo de salud en tiempo real. Integra un **smartwatch Wear OS** como sensor de frecuencia cardíaca, un **smartphone Android** como centro de datos y notificaciones, una **pantalla de TV Android TV** como panel de visualización y **Chromecast** para transmisión remota de media.

---

## Módulos del proyecto

| Módulo | Plataforma | Descripción |
|--------|-----------|-------------|
| `app`  | 📱 Smartphone | Dashboard + Historial + Cast SDK |
| `wear` | ⌚ Wear OS | Sensor FC + WatchFace |
| `tv`   | 📺 Android TV | Catálogo + Detalle + ExoPlayer |

---

## Arquitectura — SmartHealth Monitor

```
Sensor PPG (Wear OS)
    │  Health Services API
    ▼
PassiveListenerService (wear)
    │  MessageClient (BLE)
    ▼
WearListenerService (app)
    │  SmartHealthRepository
    ▼
StateFlow<Int> (fcActual)  ──────────────────────────────────┐
    │                                                        │
    ▼                                                        ▼
DashboardViewModel (app)              TvViewModel (tv)
    │  collectAsState()                    │  collectAsState()
    ▼                                      ▼
DashboardScreen (Compose)          TvCatalogScreen (Compose TV)
    └── CastButton ──► Chromecast (Remote Playback)

Room DB (LecturaFC)  ◄──  Repository  ──►  Flow<List<LecturaFC>>
                                                │
                          ┌─────────────────────┴──────────┐
                          ▼                                ▼
               HistorialScreen (app)        TvCatalogScreen (tv)
```

---

## Stack tecnológico

| Tecnología | Uso |
|-----------|-----|
| **Jetpack Compose** | UI en app y Wear OS |
| **Compose for TV** + Leanback | UI en Android TV |
| **Health Services API** | Sensor FC real en Wear OS |
| **Wearable Data Layer** (MessageClient) | Comunicación BLE wearable → teléfono |
| **Room DB** | Persistencia del historial de FC |
| **StateFlow / Flow** | Datos reactivos en toda la arquitectura |
| **Media3 / ExoPlayer** | Reproducción de video en Android TV |
| **Cast SDK** | Transmisión a Chromecast desde el teléfono |
| **Navigation Compose** | Navegación en app y TV |

---

## Tags de versiones

| Tag | Sesión | Descripción |
|-----|--------|-------------|
| `v1.0.0` | S1 | Proyecto base |
| `v1.1.0` | S7 | Health Services API + Room DB |
| `v2.0.0` | S11 | Android TV Leanback |
| `v2.1.0` | S12 | TV DetailScreen + ExoPlayer |
| `v2.2.0` | S13 | Cast SDK + README |

---

## Capturas de pantalla

| App (Smartphone) | Wear OS | Android TV |
|---|---|---|
| Dashboard + CastButton | FC en tiempo real | Catálogo de lecturas |

---

## Cómo correr el proyecto

1. Clonar el repositorio
2. Abrir en Android Studio Hedgehog o superior
3. Seleccionar el módulo (`app`, `wear`, o `tv`) en el dropdown
4. Correr en el emulador correspondiente

> **Nota:** Para probar la comunicación Wear → App, emparejar ambos emuladores en Device Manager.