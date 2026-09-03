# Inkubator-Monitor

> Projekt-lokale Ergänzung zur globalen `~/.claude/CLAUDE.md`.
> Was dort steht (Arbeitsweise, Ton, keine Alleingänge bei Commits), gilt weiter und wird hier
> nicht wiederholt. Diese Datei hält **nur, was für dieses Projekt gilt**.

---

## Was das ist

Ein simuliertes Laborgerät in Java: ein CO₂-Inkubator, der Zellkulturen auf einer Zieltemperatur
hält, seine Messwerte aus einem eigenen Thread meldet und von einer JavaFX-Oberfläche überwacht
wird.

Es gibt keine echte Hardware. Der Sensor wird simuliert — aber so, dass die Nebenläufigkeit echt
ist: Der Messwert entsteht auf einem anderen Thread als dem, der die Oberfläche zeichnet. Genau an
dieser Grenze liegt der interessante Teil des Projekts.

## Wofür

Patrick kommt aus C# (~2 Jahre, u. a. Unity3D) und arbeitet sich in Java ein. Dieses Projekt ist
der praktische Teil davon: **ein Vorhaben mit genug Umfang, dass echte Entscheidungen anfallen** —
statt einer Sammlung von Tutorial-Schnipseln, bei denen alles schon vorgegeben ist.

Der Anspruch ist bewusst nicht „möglichst viele Features", sondern **jede Entscheidung im Code
begründen können**. Ein kleines Projekt, das durchdacht ist, ist hier mehr wert als ein großes,
das nur läuft.

### Warum ausgerechnet dieses Vorhaben

Es bringt vier Themen zusammen, die sich schlecht isoliert lernen lassen, und zwar in ihrer
natürlichen Reihenfolge:

- **Observer-Muster** — Java hat kein `event`-Schlüsselwort wie C#. Wer Ereignisse will, baut sie.
- **Nebenläufigkeit** — ein Gerät, das im Hintergrund misst, erzwingt die Auseinandersetzung.
- **UI-Anbindung** — JavaFX erlaubt Zugriffe nur vom eigenen Thread. Die Brücke muss man bauen.
- **Testbarkeit** — der Kern ist nur testbar, wenn die Schichten sauber getrennt sind.

Jedes Thema ergibt sich aus dem vorherigen. Das ist der Grund für dieses Projekt und nicht für
irgendein anderes.

## Herkunft des Codes

Der Observer-Baukasten (`Event`, `EventSupport`, `Subscription`) stammt aus einem Vorläufer-Projekt
und wurde nach einem Review überarbeitet. Zwei Befunde von dort sind eingeflossen:

- `close()` war nicht idempotent — mehrfaches Aufrufen konnte fremde Abos entfernen, weil
  `List.remove(Object)` über `equals()` sucht und nicht über Identität.
- Fehler aus Listenern gingen ohne Stacktrace nach `System.err` — dadurch war die Klasse weder
  gut zu debuggen noch zu testen.

---

## Architektur

Drei Pakete, Abhängigkeiten **nur nach unten**:

```
ui        JavaFX. Kennt device und events.
          ↑ Thread-Grenze: jeder Zugriff von unten nach oben über Platform.runLater(…)
device    Der Inkubator + Sensor-Simulation. Kennt events. Kennt die UI nicht.
events    Observer-Baukasten. Kennt nichts.
```

**Die Regel, die alles trägt:** `events` und `device` haben keine UI-Abhängigkeit. Deshalb laufen
ihre Tests ohne laufendes Fenster, und deshalb wäre die Oberfläche austauschbar, ohne die
Gerätelogik anzufassen. Wenn ein Vorschlag diese Richtung umkehren würde, ist er falsch — auch
wenn er kürzer ist.

## Bewusste Entscheidungen

Diese sind getroffen und werden nicht ohne Anlass neu aufgerollt:

| Entscheidung | Begründung |
|---|---|
| **Java 21 LTS**, nicht 25 | Die Version, die in Firmen tatsächlich läuft. Kostet das kurze `void main()` aus Java 25 — dafür wird die klassische `public static void main(String[] args)` geübt. |
| **Gradle** mit Kotlin-DSL | Verbreiteter Standard; `build.gradle.kts` ist typsicher und wird von der IDE unterstützt. |
| **JavaFX statt Swing** | Der aktuelle UI-Baukasten. Layout-Denken überträgt sich aus WPF. |
| **Oberfläche in Java-Code, kein FXML** | Ein Werkzeug weniger, das schiefgehen kann. FXML bringt für diesen Umfang keinen Vorteil. |
| **Keine Fremdbibliotheken** | Nur JDK, JavaFX, JUnit. Alles andere verwischt den Eigenanteil. |
| **`CopyOnWriteArrayList` im EventSupport** | Sichere Iteration, während sich ein Listener abmeldet. Kostet bei jeder Änderung eine Kopie — bei einer Handvoll Listenern belanglos. |
| **Rechenmodell statt Sensor** hinter `TemperatureSource` | Die Methode `nextTemperature(current, target)` rechnet den nächsten Wert aus, statt ihn abzulesen, und merkt sich nichts. Dadurch ist sie ohne Zeitgeber und ohne Thread prüfbar (M-4). Austauschbar sind damit verschiedene Verläufe, nicht echte Hardware — dafür bräuchte es eine Methode ohne Argumente, eine Antwort auf Lesefehler, eine Lebensdauer und womöglich die umgekehrte Richtung. Die Naht hat genau einen Benutzer (`TemperatureSampler`), die Entscheidung bleibt also umkehrbar. |
| **`TemperatureSampler` merkt sich keinen Zustand** | Er holt den aktuellen Wert bei jedem Takt aus dem `Incubator`, statt eine eigene Kopie zu führen. Zwei Stellen, die dieselbe Wahrheit behaupten, laufen sonst auseinander, sobald jemand anders `updateTemperature` aufruft. |
| **Oberfläche in zwei Klassen** | `IncubatorMonitorApp` hält Lebenszyklus und Verdrahtung, `MonitorView` baut die Elemente. Eine Klasse hätte drei Aufgaben vermischt; getrennt sitzt die Thread-Brücke an zwei Methoden statt verstreut im Layout-Code. |
| **`MonitorView` kennt den `Incubator` nicht** | Sie bekommt Zahlen und Wahrheitswerte; Eingaben meldet sie über `DoubleConsumer` bzw. `Runnable` nach oben, verdrahtet wird in der App. Damit gilt die Abhängigkeitsrichtung auch innerhalb von `ui`, und die Bereichsprüfung 0–100 bleibt allein im Gerät: Die Oberfläche fängt dessen `IllegalArgumentException` und färbt das Feld rot, statt die Regel zu wiederholen. |
| **Anzeige mit `Locale.ROOT`** | `%.2f` zeigt sonst je nach Systemsprache ein Komma, während `Double.parseDouble` im Eingabefeld nur den Punkt akzeptiert — man könnte nicht eintippen, was direkt darüber steht. Die Oberfläche ist durchgehend englisch beschriftet, also ist der Punkt die passende Seite des Widerspruchs. |

## Umfangsgrenzen

Kein Netzwerk, keine Datenbank, keine Persistenz, kein Multi-Modul-Aufbau. Nichts davon würde hier
etwas zeigen, was nicht schon gezeigt ist — und jedes davon kostet Zeit, die in die vier Kernthemen
gehört. **Vorschläge in diese Richtung bitte abwehren, nicht umsetzen.**

---

## Lernaspekte

Der eigentliche Ertrag des Projekts. Wenn ein Abschnitt fertig ist, sollte der jeweilige Punkt
erklärbar sein — nicht nur funktionieren.

**Nebenläufigkeit**
- Thread-sichere Bausteine ergeben noch kein thread-sicheres System. Die `CopyOnWriteArrayList`
  schützt die Listener-Liste; der Messwert im Gerät liegt außerhalb davon und braucht seine eigene
  Absicherung.
- `volatile` sichert Sichtbarkeit, nicht Atomarität. Prüfen-und-Setzen braucht mehr.
- `fire()` wird **außerhalb** eines Sperrbereichs aufgerufen — fremder Listener-Code darf niemals
  unter einer eigenen Sperre laufen.
- `ScheduledExecutorService` statt `new Thread()`: sauber startbar und vor allem sauber beendbar.
- `scheduleWithFixedDelay` bricht die **ganze Serie** ab, wenn die Aufgabe eine Ausnahme wirft —
  lautlos, weil sie in einem `Future` landet, das niemand abfragt. Der Rumpf braucht deshalb sein
  eigenes `try`/`catch`.
- `scheduleWithFixedDelay` (Pause nach dem Ende) statt `scheduleAtFixedRate` (feste Termine, die
  nachgeholt werden). Nachgeholte Messwerte sind wertlos und stauen einen langsamen Listener nur
  weiter zu.
- Ein `ScheduledExecutorService` erzeugt **keine** Daemon-Threads. Ohne eigene `ThreadFactory` mit
  `setDaemon(true)` bleibt die Anwendung nach dem Schließen des Fensters im Hintergrund hängen.
- Eine `InterruptedException` fängt man nie, ohne das Signal mit
  `Thread.currentThread().interrupt()` wiederherzustellen — sonst verschluckt man die Nachricht
  „du sollst aufhören".
- Ein Schloss nützt nur, wenn **alle** Zugriffe dasselbe benutzen — die lesenden ebenso wie die
  schreibenden. Ein eigenes privates `Object` als Schloss statt `this`, damit von außen niemand
  mitsperren kann.
- Ein Test kann eine Wettlaufsituation **nicht widerlegen**. Gemessen am ungesicherten Code trat
  sie in etwa 1 von 200 Durchläufen auf; ein grüner Testlauf beweist also nichts. Solche Tests
  sind Wächter gegen späteres Entfernen der Absicherung, die Sicherheit selbst kommt aus der
  Begründung. Die Ausnahme ist ein Test, der auf eine **Blockade** zielt: Der schlägt zuverlässig
  fehl, weil er auf ein Zeitlimit läuft statt auf einen Zufall.

**Ressourcen und Lebensdauer**
- `Subscription` ist dasselbe Muster wie RAII in C++ und `IDisposable` in C#. Der Unterschied:
  Java stößt es explizit an (`close()` bzw. try-with-resources), weil der Garbage Collector keinen
  definierten Zeitpunkt hat.
- `AutoCloseable.close()` ohne `throws` neu deklarieren — sonst erzwingt die geprüfte Ausnahme ein
  `catch` bei jedem Aufrufer.

**Java-Eigenheiten, die es in C# nicht gibt**
- `==` ist bei Objekten **immer** Referenzvergleich; es gibt kein Operator-Overloading. Wer
  `equals` überschreibt, überschreibt `hashCode` mit.
- Geprüfte vs. ungeprüfte Ausnahmen (`Exception` vs. `RuntimeException`).
- Type Erasure: Generics verschwinden zur Laufzeit. Kein `new T[]`, kein `instanceof List<String>`.
- Lambdas erfassen nur effektiv finale Variablen.
- Wildcards an der Verwendungsstelle (`? super T`) statt Varianz an der Deklaration (`in`/`out`).

**UI**
- Der JavaFX Application Thread ist das Gegenstück zum WPF-Dispatcher. `Platform.runLater(…)`
  entspricht `Dispatcher.BeginInvoke()`.
- Layout über `BorderPane`/`VBox`/`GridPane` — nicht über `FlowPane`.

---

## Arbeitsweise in diesem Projekt

**Tests.** Für alles in `events` und `device` gilt: erst der Test, der den Fehler zeigt, dann die
Reparatur. Nicht als Dogma, sondern weil ein Test, den man nie hat fehlschlagen sehen, nichts
beweist. JUnit 5, Zusicherungen in der Reihenfolge `assertEquals(erwartet, tatsächlich)`.
Die UI-Schicht wird nicht automatisiert getestet — das wäre für diesen Umfang unverhältnismäßig.

**Generierter Code wird nicht übernommen, bevor er verstanden ist.** Das ist die zentrale Regel
hier. Wenn ich etwas vorschlage, das Patrick noch nicht einordnen kann, ist die Erklärung Teil des
Vorschlags — nicht ein optionaler Nachtrag. Lieber eine Klasse weniger und die dafür durchdrungen.
Umgekehrt gilt: Ein Vorschlag von mir ist nicht dadurch richtig, dass er von mir kommt. Widerspruch
ist erwünscht, und wenn Patrick eine Stelle anders lösen will, wird das diskutiert, nicht
weggenickt.

**Erklärtiefe.** C#-Wissen ist vorhanden und wird vorausgesetzt — Klassen, Interfaces, Generics,
Lambdas, Vererbung brauchen keine Einführung. Erklärt wird, **wo Java anders funktioniert als C#**.
Die Brücke zur bekannten Sprache ist der schnellste Weg, nicht der bequemste.

**Code auf Englisch**, Bezeichner und Kommentare inklusive. Diese Datei und die Gespräche auf
Deutsch.

**Umfang halten.** Wenn ein Vorschlag die Umfangsgrenzen oben überschreitet, gehört er in den
Abschnitt „Offene Punkte" — nicht in den Code.

---

## Offene Punkte

Bewusst nicht gelöst, mit Grund:

- **Reentranz beim Feuern.** Ruft ein Listener während `fire()` selbst eine Zustandsänderung aus,
  verschachteln sich die Ereignisse. Eine Warteschlange würde das lösen; für den aktuellen Umfang
  ist es dokumentiertes Verhalten statt ungewollter Zufall.
- **Kein Rückstau-Schutz.** Ein langsamer Listener bremst den Sensor-Thread. Bei einem echten Gerät
  bräuchte es Entkopplung über eine Queue.
- **Zwei Antworten auf Listener-Fehler im selben Paket.** Der `EventSupport` nimmt einen
  Fehler-Handler entgegen, der `TemperatureSampler` schreibt den Stacktrace stumpf nach
  `System.err`. Konsequent wäre auch hier ein hereingereichter Handler; das kostet einen
  Konstruktorparameter, den bisher niemand braucht.
- **Ein Listener, der `close()` aufruft, blockiert eine Sekunde.** Der Sampler-Thread steckt dann
  in `updateTemperature` → `fire()` → Listener → `close()` und wartet auf das Schloss, das der
  beendende Thread hält — der wiederum in `awaitTermination` auf genau diesen Thread wartet. Es
  löst sich nach dem Zeitlimit von selbst auf, kostet aber eine Sekunde Stillstand. Verwandt mit
  I-8, nur andersherum: Fremder Code läuft nicht *unter* der Sperre, sondern *gegen* sie.
- **Fehlerbehandlung in der UI-Schicht** ist minimal. Der Kern ist abgesichert, die Oberfläche nicht.
- **Keine automatisierten UI-Tests.** Siehe oben.
