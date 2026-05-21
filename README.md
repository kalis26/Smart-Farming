# Smart Farming - Object-Oriented Programming Practical Work

Smart Farming is a Java application designed for the Object-Oriented Programming practical work at ESI. The project models the management of an intelligent farm organized into geographic zones, monitored by sensors, and supervised through readings, alerts, production reports, and maintenance workflows.

The implementation follows the practical-work statement closely: zones own their domain entities, sensors periodically send readings to the application, and the system automatically generates alerts when readings exceed configured thresholds or when GPS positions leave an assigned zone.

## Project Context

The farm is divided into three kinds of geographic zones:

- **Crop zones** for cultures such as cereals, vegetables, and fruits.
- **Livestock zones** for ruminants or poultry, with biometric and GPS monitoring.
- **Aquaculture zones** for aquatic species in basins, monitored through water sensors.

Each zone has a unique code, a name, an activity status, a geographic boundary, hosted entities, sensors, readings, alerts, and production information.

The goal of the project is to demonstrate an object-oriented model that uses inheritance, abstraction, composition, encapsulation, enums, domain-specific behavior, and clear responsibility separation.

## Main Features

- Manage crop, livestock, and aquaculture zones.
- Enforce unique zone codes when registering zones.
- Assign cultures, animals, basins, and sensors through their owning zones.
- Model ruminants and poultry as specialized animals.
- Store structured health events for illnesses and weight changes.
- Configure feeding programs for livestock and aquaculture zones.
- Model environmental, soil, biometric, water, and GPS sensors.
- Simulate periodic sensor readings every 10 seconds.
- Generate automatic alerts from abnormal readings.
- Detect GPS positions outside a zone boundary.
- Suspend and reactivate zones while preserving defaillant sensors.
- Repair defaillant sensors explicitly through maintenance.
- Display zone summaries, latest readings, active alerts, and production reports.
- Add zones, entities, and sensors at runtime through an interactive console menu.

## Object-Oriented Design

### Zone Hierarchy

`Zone` is an abstract base class shared by:

- `ZoneCulture`
- `ZoneElevage`
- `ZoneAquacole`

Common zone responsibilities include:

- storing zone identity and geographic boundaries
- owning sensors, readings, and alerts
- checking GPS containment with `contientPosition`
- suspending active sensors during maintenance
- reactivating only sensors suspended by the zone

Zone-specific responsibilities remain inside the correct subclass:

- `ZoneCulture` owns cultures and crop yield.
- `ZoneElevage` owns animals, validates livestock type, and stores a feeding program.
- `ZoneAquacole` owns basins, aquaculture production, and a feeding program.

### Animal Model

`Animal` is abstract and is specialized into:

- `Ruminant`
- `Volaille`

Each animal stores:

- unique identifier
- species
- age
- weight
- health status
- structured health-event history

Health events are represented by `EvenementSanitaire`, which records illnesses, health-state changes, and weight evolution.

### Sensor Taxonomy

`Capteur` is the abstract root of the sensor hierarchy.

Numeric sensors extend `CapteurNumerique`, while GPS sensors extend `CapteurGPS`.

The numeric sensor hierarchy is split by domain:

- `CapteurEnvironnemental`
  - `CapteurTemperatureAir`
  - `CapteurHumiditeAir`
  - `CapteurPluviometrie`
- `CapteurSol`
  - `CapteurPHSol`
  - `CapteurHumiditeSol`
  - `CapteurAzote`
- `CapteurBiometrique`
  - `CapteurTemperatureCorporelle`
  - `CapteurActivite`
- `CapteurEau`
  - `CapteurTemperatureEau`
  - `CapteurOxygeneDissous`
  - `CapteurPHEau`
- `CapteurGPS`

Every sensor has a unique code inside its zone, a status, and threshold configuration when applicable.

## Readings and Alerts

Sensors send readings to the application through the system coordinator.

Numeric sensors produce `ReleveNumerique` values with units. GPS sensors produce `ReleveGPS` values containing latitude and longitude.

`SmartFarmingSystem` receives readings through `recevoirReleve`, stores them, evaluates their severity, and generates alerts automatically when necessary.

Severity rules:

- `Normal`: value inside the accepted threshold range.
- `Avertissement`: value outside thresholds by up to 10% of the configured range.
- `Critique`: value outside thresholds by more than 10%, or GPS position outside the zone boundary.

Every alert is associated with the reading that triggered it and can be consulted, acknowledged, or removed.

## Periodic Simulation

The application includes a background periodic simulation.

When started from the menu, active sensors send readings every 10 seconds. Each cycle prints a compact summary:

```text
Cycle #3 | Releves: +12 | Alertes: +2 | Total alertes actives: 5
```

The simulation uses a single-threaded scheduler and keeps the menu usable while readings continue to arrive.

## Interactive Console Menu

The current application entry point is `smartfarming.Main`, which provides a console interface for:

- starting and stopping periodic simulation
- launching a manual reading cycle
- adding zones
- adding cultures
- adding livestock animals
- adding aquaculture basins
- adding compatible sensors to zones
- displaying zone summaries
- displaying latest readings
- displaying active alerts
- generating production reports
- demonstrating validations and maintenance behavior

The domain model is independent from the interface layer, which keeps the project ready to be connected to a desktop GUI.

## Project Structure

```text
src/main/java/smartfarming
|-- alertes      # Alert model and alert lifecycle
|-- capteurs     # Sensor hierarchy and sensor-generated readings
|-- entites      # Cultures, animals, basins, feeding programs, health events
|-- enums        # Domain enums
|-- mesures      # Numeric and GPS readings
|-- rapports     # Production reports
|-- system       # Application coordinator
|-- zones        # Zone hierarchy
`-- Main.java    # Interactive console entry point
```

Additional documentation is available in:

```text
docs/
|-- class-diagram.puml
|-- class-diagram.png
`-- implementation-overview.md
```

## How to Compile and Run

This project uses plain Java and does not require external dependencies.

From the repository root:

```powershell
$sources = Get-ChildItem -Path src\main\java -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -d out $sources
java -cp out smartfarming.Main
```

On Unix-like systems:

```bash
find src/main/java -name "*.java" > sources.txt
javac -d out @sources.txt
java -cp out smartfarming.Main
```

## Example Workflow

1. Start the application.
2. Display the initial zone summary.
3. Add a new crop, livestock, or aquaculture zone.
4. Add entities such as cultures, ruminants, poultry, or basins.
5. Add compatible sensors to the selected zone.
6. Start the periodic simulation.
7. Inspect active alerts and latest readings.
8. Generate production reports.
9. Stop the simulation before exiting.

## Design Principles

The project emphasizes:

- **Encapsulation**: each class owns and protects its state.
- **Inheritance**: zones, animals, and sensors are modeled through meaningful hierarchies.
- **Composition**: zones contain entities and sensors; alerts reference readings; animals contain health events.
- **Single responsibility**: zones assign entities and sensors, sensors produce readings, and the system coordinates application-level histories and alerts.
- **Domain-driven modeling**: class names and responsibilities follow the Smart Farming statement.

## Repository Description

Java OOP Smart Farming system with zone management, sensor hierarchies, periodic readings, automatic alerts, production reports, and an interactive console menu.
