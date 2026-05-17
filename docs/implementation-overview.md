# SmartFarming Implementation Overview

This document summarizes the current Java implementation for the Smart Farming TP.

## 1) High-level architecture

The project is organized around domain objects rather than a central class doing everything:

- `smartfarming.zones`: zone hierarchy and zone-owned assignment of entities/sensors.
- `smartfarming.capteurs`: sensor hierarchy, sensor statuses, and sensor-sent readings.
- `smartfarming.mesures`: numeric and GPS readings.
- `smartfarming.alertes`: alerts generated from readings.
- `smartfarming.entites`: cultures, animals, feeding programs, basins, and health events.
- `smartfarming.system`: application coordinator (`SmartFarmingSystem`) for histories, alerts, and reports.
- `smartfarming.Main`: deterministic console demo.

The main flow is:

1) Create zones with geographic boundaries.
2) Assign cultures, animals, basins, and sensors through the concerned zone.
3) Ask sensors to periodically send readings to the application.
4) Let the system store readings and generate alerts automatically.
5) Generate production reports.

## 2) Zones and ownership

`Zone` is abstract and has three concrete subclasses: `ZoneCulture`, `ZoneElevage`, and `ZoneAquacole`.

Common `Zone` responsibilities:

- Owns its sensors, readings, and alerts.
- Stores a rectangular geographic boundary: `latitudeMin`, `latitudeMax`, `longitudeMin`, `longitudeMax`.
- Provides `contientPosition(PositionGPS)` for GPS boundary checks.
- Suspends only active sensors and remembers which sensors were suspended by the zone.
- Reactivates only sensors suspended by the zone; defaillant sensors require explicit maintenance.

Zone-specific behavior:

- `ZoneCulture` owns `Culture` objects and crop yield.
- `ZoneElevage` owns `Animal` objects, has a `TypeElevage` (`RUMINANTS` or `VOLAILLE`), validates animal type, and owns a `ProgrammeAlimentation`.
- `ZoneAquacole` owns `Bassin` objects, harvest weight, and its own `ProgrammeAlimentation`.

## 3) Animals, feeding, and health

`Animal` is abstract and has two subclasses:

- `Ruminant`
- `Volaille`

Each animal has species, age, weight, health status, and a structured health history.

Health history is modeled with `EvenementSanitaire`, which stores:

- event id and date/time
- concerned animal
- description
- health status
- old/new weight when the event is a weight evolution

`ProgrammeAlimentation` stores feeding type and quantity per meal, and is used by livestock and aquaculture zones.

## 4) Sensor taxonomy

`Capteur` is abstract. Numeric sensors extend `CapteurNumerique`; GPS extends `CapteurGPS`.

Semantic numeric sensor families:

- `CapteurEnvironnemental`: air temperature, air humidity, rainfall.
- `CapteurSol`: soil pH, soil humidity, nitrogen.
- `CapteurBiometrique`: body temperature and activity in steps per minute.
- `CapteurEau`: water temperature, dissolved oxygen, water pH.

Concrete sensors used in the demo:

- `CapteurTemperatureAir`
- `CapteurHumiditeAir`
- `CapteurPluviometrie`
- `CapteurPHSol`
- `CapteurHumiditeSol`
- `CapteurAzote`
- `CapteurTemperatureCorporelle`
- `CapteurActivite`
- `CapteurTemperatureEau`
- `CapteurOxygeneDissous`
- `CapteurPHEau`
- `CapteurGPS`

Each numeric sensor keeps its own reading history and can send a reading through `envoyerReleve(Random)`.

## 5) Readings and alerts

`SmartFarmingSystem` is the application coordinator. It does not own cultures, animals, or sensors.

Key responsibilities:

- `ajouterZone(Zone zone)`: registers a zone.
- `recevoirReleve(Releve releve)`: central entry point for readings sent by sensors.
- `simulerCycleReleves(Random random)`: asks every active sensor in every registered zone to send one reading.
- `enregistrerReleveNumerique(...)` and `enregistrerReleveGPS(...)`: convenience methods that route through `recevoirReleve`.
- `alertesActives()` and `alertesFiltrees(...)`: alert consultation.
- `genererRapportProduction(...)`: production summary.

Automatic alert rules:

- Numeric reading inside thresholds: `NiveauGravite.Normal`.
- Numeric reading outside thresholds by at most 10% of the configured range: `Avertissement`.
- Numeric reading outside thresholds by more than 10%: `Critique`.
- GPS reading outside the zone boundary: critical alert.

Every generated alert is linked to the reading that triggered it.

## 6) Main demo

`Main` demonstrates:

- zone-owned assignment of cultures, animals, basins, and sensors
- ruminant-zone validation rejecting poultry
- feeding programs for livestock and aquaculture
- structured health events for illness and weight changes
- one deterministic periodic reading cycle with `Random(2026)`
- automatic warning and critical alerts
- GPS boundary alert
- defaillant sensor maintenance with `reparer()`
- production reports including feeding programs

## 7) Notes

- The project remains a plain Java console project with no external dependency.
- Real timers/threads are intentionally avoided; deterministic cycles are easier to present and test.
- Some older generic sensor classes may remain for compatibility, but the demo and UML use the semantic taxonomy expected by the assignment.
