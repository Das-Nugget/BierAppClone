# Beer App – Dev README
---

## 1. Architektur-Überblick

Die App ist grob in drei Schichten aufgeteilt:

1. **UI (Jetpack Compose Screens)**
    - Zeichnen nur die Oberfläche.
    - Reagieren auf Klicks.
    - Holen ihr ViewModel mit `hiltViewModel()`.

2. **ViewModels (MVVM)**
    - Halten den UI-Zustand (State).
    - Enthalten Business-Logik für den Screen.
    - Rufen Daten-/Serviceklassen auf (z. B. `JsonFileManager`, DAOs, Repositories).
    - Geben Ergebnisse als `StateFlow` oder `SharedFlow` an die UI.

---
StateFlow – für UI-Zustände 
- Hält **immer den aktuellen Wert** (z. B. Liste, Ladezustand, Auswahl).
- UI beobachtet ihn per `collectAsState()` → automatische Aktualisierung.
- Ersetzt `LiveData`.
- Beispiel: aktuelles Beer, History-Liste, Ladezustand.
---
  SharedFlow – für einmalige Events
- Speichert **keinen letzten Wert**.
- Wird für UI-Aktionen genutzt, die **einmal** passieren sollen:
    - Toasts
    - Navigation
    - File Picker öffnen
    - Export/Import-Ergebnisse
- UI sammelt Events in `LaunchedEffect`.
---
3. **Daten- / Service-Schicht**
    - `AppDatabase` (Room) + DAOs (`BeerDao`, `RatingDao`, `TasteDao`).
    - Hilt-Module (`DatabaseModule`) zum Bereitstellen dieser Objekte.

**Wichtig:**  
Die UI kennt nur das ViewModel.  
Das ViewModel kennt Services/Repos/DAOs.  
Hilt erstellt die Objekte im Hintergrund.

---

## 2. Hilt 

**Hilt** ist ein Dependency Injection Framework für Android.

Statt überall selber `new` (oder `Foo()` in Kotlin) aufzurufen und Singletons zu basteln:

- schreiben wir nur, **was eine Klasse braucht** → im Konstruktor
- und sagen Hilt einmalig **wie man bestimmte Dinge baut** → in Modulen.

Beispiele:

- `AppDatabase` wird von Hilt als Singleton erstellt.
- `BeerDao`, `RatingDao`, `TasteDao` werden von Hilt bereitgestellt.
- **DAOs nie selbst erzeugen.**
- Alles kommt automatisch per **Konstruktorinjektion**:
- `JsonFileManager` bekommt `Context` + `BeerDao` von Hilt.
- ViewModels bekommen ihre Abhängigkeiten von Hilt.

```kotlin
class MyRepo @Inject constructor(
private val beerDao: BeerDao
)



