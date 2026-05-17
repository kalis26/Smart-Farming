# SmartFarming Implementation Overview

This document summarizes the current Java implementation so it can be explained clearly in a presentation.

## 1) High-level architecture

The project is organized into these packages:

- `smartfarming.system`: application service layer (`SmartFarmingSystem`).
- `smartfarming.zones`: zone hierarchy and zone-specific behavior.
- `smartfarming.capteurs`: sensor hierarchy and sensor logic.
- `smartfarming.mesures`: reading types and GPS position.
- `smartfarming.alertes`: alert model and state.
- `smartfarming.entites`: domain entities (animal, culture, bassin).
- `smartfarming.rapports`: production report object.
- `smartfarming.enums`: enums for types, status, and units.
- `smartfarming.Main`: runnable demo that exercises the system.

The main flow is:

1) Create zones and entities.
2) Assign cultures, animals, bassins, and sensors through the concerned zone objects.
3) Record automatic numeric and GPS readings.
4) Generate alerts when numeric readings exceed thresholds or GPS readings leave the zone boundary.
5) Produce a report per zone.

## 2) Core service: SmartFarmingSystem

File: `src/main/java/smartfarming/system/SmartFarmingSystem.java`

Responsibilities:

- Stores all zones, readings, alerts, and reports in lists.
- Registers zones at application level.
- Records sensor readings and pushes them to the zone history.
- Generates alerts when numeric readings are out of range or GPS readings leave their zone.
- Filters reading history by zone, sensor, or time.
- Returns active alerts (non-acquitted) and filtered alerts.
- Generates a production report with zone-specific metrics.

Key methods:

- `ajouterZone(Zone zone)`: registers a zone.
- `enregistrerReleveNumerique(CapteurNumerique capteur, double valeur)`:
  - Checks sensor status.
  - Creates a numeric reading via `capteur.creerReleve`.
  - Stores it in system + zone history.
  - Evaluates thresholds and creates an alert if needed.
- `enregistrerReleveGPS(CapteurGPS capteur, double lat, double lon)`:
  - Creates a GPS reading and stores it in system + zone history.
  - Checks the position against the rectangular geographic boundary of the zone.
  - Creates a critical alert when the position is outside the assigned zone.
- `alertesActives()`:
  - Returns alerts sorted by newest, excluding `StatutAlerte.Acquittee`.
- `supprimerAlerte(Alerte alerte)`:
  - Removes alert from system list and from the zone list if present.
- `genererRapportProduction(Zone zone, LocalDate debut, LocalDate fin)`:
  - Builds a string summary with zone-specific production values.

## 3) Zone hierarchy

Files:

- `zones/Zone.java` (abstract base)
- `zones/ZoneCulture.java`
- `zones/ZoneElevage.java`
- `zones/ZoneAquacole.java`

Base class responsibilities (`Zone`):

- Common fields: `code`, `nom`, `status`.
- Geographic boundary fields: `latitudeMin`, `latitudeMax`, `longitudeMin`, `longitudeMax`.
- Owns lists of `capteurs`, `releves`, `alertes`.
- `ajouterCapteur`, `ajouterReleve`, `ajouterAlerte`.
- `contientPosition(PositionGPS position)` verifies that a GPS position is inside the zone rectangle.
- `suspendre()` and `reactiver()`:
  - Change the zone status.
  - `suspendre()` suspends only active sensors and remembers which sensors were suspended by the zone.
  - `reactiver()` reactivates only sensors previously suspended by the zone.
  - Defaillant sensors and manually suspended sensors are not repaired or reactivated by a zone transition.

Zone-specific behavior:

- `ZoneCulture`:
  - Stores cultures and a yield value (`rendement`).
  - `enregistrerProduction(double rendementKgHa)`.
- `ZoneElevage`:
  - Stores animals, feeding program, milk and egg production.
  - `definirProgrammeAlimentation(...)` and `enregistrerProduction(...)`.
- `ZoneAquacole`:
  - Stores bassins, harvest weight, and total animal count.
  - `enregistrerProduction(...)` and `getNombreTotalAnimaux()`.

## 4) Sensor hierarchy (Capteurs)

Base class: `Capteur` (abstract)

- Fields: `id`, `nom`, `statut`, `zone`.
- Status actions: `activer`, `suspendre`, `signalerDefaillance`, `reparer`.
- `estActif()` check used by the system before recording a reading.
- `reparer()` represents maintenance and is the explicit way to return a defaillant sensor to active status.

Numeric sensors: `CapteurNumerique`

- Fields: `unite`, `seuilMin`, `seuilMax`.
- `creerReleve(double valeur)`:
  - Throws if sensor is inactive.
  - Creates a `ReleveNumerique` with a new UUID and timestamp.
- `evaluerAlerte(ReleveNumerique releve)`:
  - If value is out of range, creates a critical `Alerte` with message and zone.

Concrete sensor types (all extend `CapteurNumerique` unless noted):

- `CapteurTemperature`
- `CapteurHumidite`
- `CapteurPH`
- `CapteurPluviometrie`
- `CapteurAzote`
- `CapteurOxygeneDissous`
- `CapteurActivite`
- `CapteurPoids`
- `CapteurGPS` (extends `Capteur` directly, used for location only)

## 5) Readings and positions

Files:

- `mesures/Releve.java` (abstract base)
- `mesures/ReleveNumerique.java`
- `mesures/ReleveGPS.java`
- `mesures/PositionGPS.java`

Behavior:

- `Releve` provides `id`, `dateHeure`, `capteur` with default UUID/time if not set.
- `ReleveNumerique` adds `valeur` and `unite`.
- `ReleveGPS` adds `position`.

## 6) Alerts

File: `alertes/Alerte.java`

- Fields: `id`, `dateHeure`, `message`, `statut`, `zone`, `releveDeclencheur`.
- `acquitter()` sets status to `StatutAlerte.Acquittee`.
- `creerAlerte(...)` factory creates a critical alert with timestamp + UUID.

Alert status enum:

- `StatutAlerte`: `Acquittee`, `Avertissement`, `Critique`.

## 7) Entities (domain objects)

- `Animal`:
  - Fields: id, espece, age, poids, etatSante.
  - Health history (`historiqueSante`) logs state and weight changes with timestamps.
- `Culture`:
  - Fields: id, type, plantation date, harvest date, growth stage, pH/humidity range.
- `Bassin`:
  - Fields: id, species, animal count, feeding program.

These objects are added to zones via the system.

## 8) Reporting

File: `rapports/RapportProduction.java`

- Stores report metadata (period, zone) and a `contenu` string.
- The system builds the `contenu` string with zone-specific indicators:
  - Culture zone: yield + number of cultures.
  - Elevage zone: milk + egg production + animal count.
  - Aquacole zone: harvest weight + bassin count + total animals.

## 9) Main demo

File: `Main.java`

The demo performs:

- Creates 3 zones (culture, elevage, aquacole), each with rectangular GPS bounds.
- Adds a culture, an animal, a bassin, and sensors through zone methods.
- Generates deterministic automatic readings with a fixed `Random` seed.
- Generates numeric readings (some out of range to trigger alerts).
- Generates GPS readings, including one outside the livestock zone to trigger an alert.
- Acquits one alert and checks active alerts.
- Marks one sensor defaillant, suspends/reactivates a zone, and shows that maintenance through `reparer()` is required.
- Generates and prints production reports for all zones.

## 10) Notes and assumptions

- Alerts are triggered by numeric readings exceeding thresholds and GPS readings outside zone boundaries.
- Zone suspension cascades only to active attached sensors.
- Defaillant sensors are preserved during zone suspension/reactivation and require explicit maintenance.
- Alert filtering in `alertesActives()` excludes acquitted alerts.

If you want a shorter summary or diagrams, this file can be extended.
