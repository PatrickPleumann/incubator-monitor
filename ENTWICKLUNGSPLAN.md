# Entwicklungsplan

> Arbeitsdokument zum Projekt `incubator-monitor`. Was das Projekt ist und warum es so geschnitten ist,
> steht in `CLAUDE.md`. **Hier steht, was gebaut wird, in welcher Reihenfolge und woran man
> erkennt, dass ein Abschnitt fertig ist.**
>
> Der Plan ist eine Spezifikation, keine Lösung. Signaturen und Verhalten sind vorgegeben,
> die Umsetzung nicht. Wo eine Anforderung unklar oder falsch ist, wird sie geändert — das
> gehört zum Prozess und wird unten protokolliert.

---

## Arbeitsablauf pro Aufgabe

Jede Aufgabe läuft in derselben Reihenfolge ab. Der Ablauf ist der eigentliche Ertrag des
Projekts; ohne ihn wäre es nur Code.

| Schritt | Was passiert |
|---|---|
| **1 — Spezifikation lesen** | Die Anforderung unten. Wenn eine Stelle unklar ist: klären, **bevor** getippt wird. Eine unklare Anforderung wird sonst zum Rätselraten im Test. |
| **2 — Tests schreiben** | Für jede spezifizierte Methode die Tests aus der Aufgabe. **Vor** der Implementierung. |
| **3 — Tests rot sehen** | Der Test muss einmal fehlschlagen. Ein Test, den man nie hat scheitern sehen, beweist nichts — er könnte an der falschen Stelle prüfen oder gar nicht ausgeführt werden. |
| **4 — Implementieren** | So einfach, dass die Tests grün werden. Nicht mehr. |
| **5 — Review (optional, auf Zuruf)** | Siehe unten. Ergebnis kommt ins Review-Protokoll. |
| **6 — Commit** | Ein Commit pro Aufgabe, Nachricht auf Englisch. Der Verlauf zeigt dann die Reihenfolge „Test → Implementierung". |

**Ausnahme:** Etappe 1 (Gerüst) und Etappe 4 (Oberfläche) laufen ohne automatisierte Tests.
Warum, steht dort jeweils.

### Was ein Review liefert

Reviews werden **von mir angefordert**, nicht automatisch durchgeführt. Ein Review beantwortet
drei Fragen in dieser Reihenfolge:

1. **Was ist gut** — und zwar begründet, nicht als Höflichkeit.
2. **Was ist schlecht** — mit Angabe, was konkret schiefgehen kann.
3. **Wie ändert man es** — mit Alternativen, wo es mehrere vertretbare Wege gibt.

Nicht jeder Befund wird übernommen. Was verworfen wird, wird **mit Grund** ins Protokoll
eingetragen. Das ist der interessantere Teil: Ein Review, dessen Befunde alle blind übernommen
werden, war kein Review.

### Formen der Zusammenarbeit

Vier Formen, jede mit einem anderen Zweck. Sie werden alle **von mir angefordert** — keine läuft
automatisch mit.

**1 — Spezifikation klären, bevor getippt wird.**
Vor einer Aufgabe kurz durchgehen, was die Anforderungen konkret bedeuten. Fünf Minuten, die eine
Sackgasse mittendrin ersparen. Wenn eine Anforderung nach dem Klären immer noch schief ist, wird
sie geändert und im Änderungsprotokoll vermerkt.

**2 — Entwurf vorab durchsprechen.**
Ich beschreibe, wie ich es bauen will — welche Typen, welche Verantwortung wo. Rückmeldung kommt,
**bevor** Code entsteht: wo es klemmt, was es später kostet, welche Alternative es gibt. Das ist
die wertvollste Form und die, die man am ehesten überspringt, weil Tippen sich nach Fortschritt
anfühlt. Pflicht bei Aufgabe 3.3, wo der Schnitt absichtlich offen ist.

**3 — Steckenbleiben.**
Fehlermeldung, Stacktrace oder das Verhalten, das nicht passt. Ziel ist, den Fehler zu
*verstehen*, nicht ihn wegzubekommen — die Antwort erklärt also, warum es schiefging, und nicht
nur, was man tippen muss.

**4 — Abfragen.**
Nach einer Etappe stelle ich die Fragen, die zu dem passen, was gerade gebaut wurde. Nicht als
Prüfung, sondern weil eine Antwort, die man einmal laut formuliert hat, beim zweiten Mal sitzt.
Wo es hakt, ist die Stelle, an der der Code funktioniert, aber noch nicht verstanden ist —
und genau das kann die Standortbestimmung unten **nicht** sehen. Ergebnisse mit Substanz kommen
ins Review-Protokoll.

### Standortbestimmung

Wenn unklar ist, wo die Arbeit gerade steht, wird der Stand **aus dem Code abgeleitet** — nicht
aus der Erinnerung. Ablauf:

1. Vorhandene Klassen und Tests gegen die Anforderungs-IDs der Etappen abgleichen.
2. Testlauf: Was ist grün, was fehlt, was ist rot?
3. Ergebnis: *„Fertig sind die Aufgaben X, Y, Z. Als Nächstes kommt Aufgabe N — sie besteht aus
   diesen Punkten."* Dazu die Anforderungen und Tests der nächsten Aufgabe im Klartext, damit
   nicht zurückgeblättert werden muss.
4. Die Aufgabenliste unten wird dabei aktualisiert und mit Datum versehen.

**Was diese Bestimmung nicht kann:** Sie misst, was im Code steht — ob ein Typ existiert, ob ein
Test dazu läuft. Ob eine Entscheidung *verstanden* ist, sieht man daran nicht. Dafür ist das
Review da, und dafür ist Etappe 5 da.

---

## Aufgabenliste

Stand: **noch nicht begonnen** (31.08.2026). Die Liste zeigt den Stand der letzten
Standortbestimmung, nicht zwingend den Stand von jetzt.

**Etappe 1 — Gerüst**
- [ ] 0.0 Git-Repository anlegen, `.gitignore` für Java/Gradle/IntelliJ
- [ ] 1.1 Gradle-Projekt mit Kotlin-DSL und Java-21-Toolchain
- [ ] 1.2 JavaFX einbinden
- [ ] 1.3 JUnit 5 einbinden
- [ ] 1.4 Paketstruktur und leeres Fenster

**Etappe 2 — Observer-Baukasten**
- [ ] 2.1 Code unverändert übernehmen
- [ ] 2.2 Spezifikation gelesen und offene Fragen geklärt
- [ ] 2.3 Sieben Tests geschrieben, Test 4 und 5 rot gesehen
- [ ] 2.4 Beide Befunde repariert, alle Tests grün

**Etappe 3 — Gerät und Nebenläufigkeit**
- [ ] 3.1 `TemperatureChangedEvent`
- [ ] 3.2 `Incubator` (I-1 bis I-8)
- [ ] 3.3 `TemperatureModel`, `DriftingTemperatureModel`, `SensorSimulation`
- [ ] 3.4 Zehn Tests, darunter Test 5 (Sperrbereich) und 6 (mehrere Threads)

**Etappe 4 — Oberfläche**
- [ ] 4.1 Aufbau (U-1 bis U-6)
- [ ] 4.2 Thread-Brücke (U-7 bis U-9)
- [ ] 4.3 Abnahmeliste von Hand durchgegangen

**Etappe 5 — Abrunden**
- [ ] 5.1 README mit den vier Abschnitten
- [ ] 5.2 Frischer Klon baut, testet und startet

---

## Etappen im Überblick

| # | Etappe | Paket | Ergebnis |
|---|---|---|---|
| 1 | Gerüst | — | `gradlew run` öffnet ein leeres Fenster, `gradlew test` läuft |
| 2 | Observer-Baukasten | `events` | Ereignisse zustellen und abbestellen, getestet |
| 3 | Gerät und Nebenläufigkeit | `device` | Ein Inkubator, der aus einem eigenen Thread meldet |
| 4 | Oberfläche | `ui` | JavaFX-Fenster, das Messwerte anzeigt, ohne abzustürzen |
| 5 | Abrunden | — | README, frischer Start, offene Punkte benannt |

**Jede Etappe endet in einem vorzeigbaren Zustand.** Was da ist, läuft; die Tests sind grün.
Wenn die Arbeit nach Etappe 3 endet, ist das ein Projekt ohne Oberfläche — kein halbfertiges
Projekt.

---

# Etappe 1 — Gerüst

**Ziel:** Ein Gradle-Projekt, das startet und testet. Sonst nichts.

**Fertig heißt:**
- `gradlew run` öffnet ein Fenster mit Titel und schließt sauber.
- `gradlew test` läuft grün durch (mit einem einzigen, trivialen Test).
- Die drei Pakete existieren, auch wenn sie noch leer sind.

**Warum hier keine Tests im eigentlichen Sinn:** Es gibt noch keine Logik. Der eine triviale
Test hat einen anderen Zweck — er beweist, dass die Testinfrastruktur überhaupt läuft. Ohne ihn
merkt man erst in Etappe 2, dass `useJUnitPlatform()` fehlt, und sucht den Fehler im falschen
Code.

### Aufgabe 0.0 — Repository anlegen

Steht vor allem anderen. Ohne Repository hat der Verlauf, auf dem dieser ganze Plan aufbaut,
keinen Ort.

**Spezifikation**
- `git init` im Projektverzeichnis.
- `.gitignore` für Java, Gradle und IntelliJ: `build/`, `.gradle/`, `out/`, `.idea/`, `*.iml`.
- `CLAUDE.md` und `ENTWICKLUNGSPLAN.md` wandern mit und sind Teil des ersten Commits.
- Erster Commit **vor** der ersten Zeile Code.

**Tipps**
1. Der erste Commit sollte den leeren Ausgangszustand festhalten, nicht ein halbfertiges Gerüst.
   Danach ist jeder Schritt sichtbar.
2. `gradle/wrapper/gradle-wrapper.jar` wird von manchen `.gitignore`-Vorlagen ausgeschlossen. Der
   Wrapper **muss** ins Repo — sonst scheitert Aufgabe 5.2.

### Aufgabe 1.1 — Gradle-Projekt aufsetzen

**Spezifikation**
- Build-Datei: `build.gradle.kts` (Kotlin-DSL, nicht Groovy).
- Java-Version über eine **Toolchain** auf 21 festgelegt — nicht über `sourceCompatibility`.
- Gradle Wrapper (`gradlew`, `gradlew.bat`, `gradle/wrapper/`) liegt im Repo.
- Verzeichnisse: `src/main/java`, `src/test/java`.

**Tipps**
1. `gradle init` mit dem Typ „application" nimmt dir den Großteil ab.
2. Eine Toolchain sagt Gradle: „besorg mir ein JDK 21, egal womit du selbst läufst." Das ist der
   Unterschied zu C#, wo die Zielversion in der `.csproj` steht und das SDK vom System kommt.
3. Der Wrapper gehört ins Repo — er ist der Grund, warum das Projekt auf einem fremden Rechner
   ohne Gradle-Installation baut. Genau das prüfst du in Etappe 5.

### Aufgabe 1.2 — JavaFX einbinden

**Spezifikation**
- Plugin `org.openjfx.javafxplugin`, Modul `javafx.controls`.
- JavaFX-Version passend zu Java 21.
- Der `application`-Block nennt die Hauptklasse.

**Tipps**
1. JavaFX ist seit Java 11 **nicht** mehr Teil des JDK. Das Plugin lädt die passenden
   plattformspezifischen Pakete — deshalb ist es die einfachste Lösung.
2. Wenn beim Start `Error: JavaFX runtime components are missing` kommt: Das ist der klassische
   Modulpfad-Fehler. Das Plugin soll ihn gerade verhindern — prüfe, ob es wirklich angewendet
   wird.

### Aufgabe 1.3 — JUnit 5 einbinden

**Spezifikation**
- Abhängigkeit `org.junit.jupiter:junit-jupiter` im `testImplementation`-Bereich.
- Im `test`-Block: `useJUnitPlatform()`.
- Ein Test, der `assertEquals(2, 1 + 1)` prüft. Dieser Test wird in Etappe 2 wieder gelöscht.

**Tipps**
1. Ohne `useJUnitPlatform()` findet Gradle die Tests nicht und meldet trotzdem Erfolg — die
   häufigste stille Falle beim ersten Gradle-Projekt.
2. Argumentreihenfolge ist `assertEquals(erwartet, tatsächlich)` — umgekehrt zu manchen
   NUnit-Gewohnheiten. Falsch herum ist die Fehlermeldung irreführend.

### Aufgabe 1.4 — Paketstruktur und leeres Fenster

**Spezifikation**
- Pakete: `<basis>.events`, `<basis>.device`, `<basis>.ui`.
- In `ui` eine Klasse, die `javafx.application.Application` erweitert, ein Fenster mit Titel
  „Incubator Monitor" und fester Startgröße öffnet.
- Einstiegspunkt: `public static void main(String[] args)`.

**Tipps**
1. Die lange `main`-Signatur ist auf Java 21 Pflicht — das kurze `void main()` aus neueren
   Versionen gibt es hier nicht.
2. `start(Stage)` ist der eigentliche Einstiegspunkt der Anwendung; `main` reicht nur an
   `launch(args)` weiter. Das entspricht `App.xaml.cs` in WPF.

---

# Etappe 2 — Observer-Baukasten (`events`)

**Ziel:** Ereignisse zustellen, abbestellen, und beides so, dass es unter Fehlbedienung nicht
kaputtgeht.

**Fertig heißt:** Alle Tests aus 2.2 sind grün, und mindestens zwei davon waren vorher rot.

**Reihenfolge — wichtig:** Der Code aus dem Vorläuferprojekt wird **zuerst unverändert
übernommen**, dann werden die Tests dagegen geschrieben, dann wird repariert. Nicht andersherum.
Nur so zeigt der Verlauf, dass die Tests echte Fehler gefunden haben statt nachträglich zu
bestehendem Code gebaut worden zu sein.

### Aufgabe 2.1 — Code übernehmen, unverändert

**Spezifikation**
- `Event`, `EventSupport`, `Subscription`, `TemperatureChangedEvent` nach `<basis>.events` bzw.
  `<basis>.device` verschieben.
- Paket-Deklaration und Sichtbarkeiten anpassen, sonst **nichts** ändern.
- `Main.java` aus dem Vorläuferprojekt wird nicht übernommen.
- Commit-Nachricht macht kenntlich, dass es sich um übernommenen Code handelt.

**Tipps**
1. Widerstehe dem Reflex, dabei schon aufzuräumen. Der unreparierte Zustand ist die Grundlage
   für den roten Test.
2. `Subscription` ist im Vorläufer eine innere Deklaration mit falscher Einrückung — als
   eigenständige Datei im Paket `events` neu anlegen.

### Aufgabe 2.2 — Spezifikation der drei Typen

#### `Subscription`

```java
public interface Subscription extends AutoCloseable {
    @Override
    void close();          // ohne throws
}
```

| Anforderung | Verhalten |
|---|---|
| S-1 | `close()` beendet genau **dieses** Abo. |
| S-2 | `close()` ist **idempotent**: Der zweite und jeder weitere Aufruf ändert nichts. |
| S-3 | `close()` darf niemals ein Abo eines anderen Aufrufers beenden — auch dann nicht, wenn der andere denselben Listener registriert hat. |
| S-4 | `close()` deklariert kein `throws`. |

**Tipps**
1. S-3 ist der eigentliche Kern. Frage dich: Woran erkennt die Abmeldung, *welches* Abo gemeint
   ist? Solange sie den Listener sucht, kann sie zwei gleiche nicht unterscheiden.
2. Zu S-4: `AutoCloseable.close()` deklariert `throws Exception`. Beim Überschreiben darf man die
   Ausnahme weglassen (nicht hinzufügen) — sonst bräuchte jeder Aufrufer ein `catch`. Das ist der
   Unterschied zu C#, wo `IDisposable.Dispose()` von vornherein nichts wirft.

#### `Event<T>`

```java
public interface Event<T> {
    Subscription subscribe(Consumer<? super T> listener);
}
```

| Anforderung | Verhalten |
|---|---|
| E-1 | Gibt nie `null` zurück. |
| E-2 | `subscribe(null)` wirft `NullPointerException`. |
| E-3 | Derselbe Listener darf mehrfach abonnieren. Jedes Abo zählt eigenständig und wird bei jedem Feuern einzeln bedient. |

**Tipp**
1. `? super T` heißt: Ein `Consumer<Object>` darf ein `Event<TemperatureChangedEvent>`
   abonnieren. In C# stünde die Varianz an der Deklaration (`in T`), in Java steht sie an der
   Verwendungsstelle.

#### `EventSupport<T> implements Event<T>`

```java
public class EventSupport<T> implements Event<T> {
    public EventSupport() { … }
    public EventSupport(Consumer<RuntimeException> errorHandler) { … }

    @Override public Subscription subscribe(Consumer<? super T> listener) { … }
    public void fire(T event) { … }
}
```

| Anforderung | Verhalten |
|---|---|
| ES-1 | `fire(event)` stellt an alle Listener zu, die **zum Zeitpunkt des Aufrufs** registriert sind, in Registrierungsreihenfolge. |
| ES-2 | Meldet sich ein Listener **während** eines laufenden `fire()` ab, läuft die aktuelle Zustellung ungestört zu Ende. |
| ES-3 | Wirft ein Listener eine `RuntimeException`, wird sie an den Fehler-Handler übergeben und die Zustellung an die **übrigen** Listener fortgesetzt. |
| ES-4 | Ohne angegebenen Handler: Standardverhalten schreibt die Ausnahme **mit Stacktrace** nach `System.err`. |
| ES-5 | `fire(null)` ist erlaubt, wenn `T` das zulässt — keine eigene Prüfung. |
| ES-6 | Es gibt **keine** öffentliche Methode, die die Anzahl der Listener verrät. |

**Tipps**
1. Zu ES-3/ES-4: Der Fehler-Handler ist der Grund, warum sich Fehlverhalten überhaupt testen
   lässt. Auf `System.err` kann ein Test nicht sinnvoll zugreifen — auf einen übergebenen
   `Consumer` schon.
2. Zu ES-6: Die Versuchung ist groß, für die Tests ein `listenerCount()` einzubauen. Tu es nicht.
   Ein Test, der internen Zustand abfragt, hält am Aufbau fest statt am Verhalten; er bricht bei
   jedem Umbau, ohne dass etwas kaputt wäre. Prüfe stattdessen, ob Ereignisse ankommen.
3. Zu ES-2: `CopyOnWriteArrayList` erledigt das bereits — die Entscheidung ist getroffen
   (siehe `CLAUDE.md`). Der Test dazu ist trotzdem nötig: Er hält fest, dass dieses Verhalten
   *zugesichert* ist und nicht zufällig aus der gewählten Liste fällt.

### Aufgabe 2.3 — Die Tests

Sieben Tests, jeder prüft **eine** Zusicherung. Test 4 und 5 müssen gegen den übernommenen Code
**rot** sein — das ist ihr Zweck.

| # | Test | Prüft |
|---|---|---|
| 1 | Ein Listener bekommt das gefeuerte Ereignis, unverändert | ES-1 |
| 2 | Drei Listener bekommen alle, in Registrierungsreihenfolge | ES-1 |
| 3 | Nach `close()` bekommt der Listener nichts mehr | S-1 |
| 4 | **Zwei gleiche Listener, einer meldet sich zweimal ab — der andere bekommt weiterhin** | S-2, S-3 |
| 5 | **Ein werfender Listener: der Handler bekommt die Ausnahme, die übrigen Listener bekommen ihr Ereignis** | ES-3 |
| 6 | `subscribe(null)` wirft `NullPointerException` | E-2 |
| 7 | Nach einem try-with-resources-Block kommt nichts mehr an | S-1 |

**Tipps**
1. Für Test 4 brauchst du zwei Listener, die aus Sicht von `equals()` nicht unterscheidbar sind.
   Eine Lambda zweimal *derselben* Variable zuzuweisen und beide zu abonnieren reicht.
2. Für Test 5 ist der Handler ein `Consumer<RuntimeException>`, der die Ausnahme in eine Variable
   der Testmethode legt. Die Variable muss dafür effektiv final sein — ein einelementiges Array
   oder ein `AtomicReference` ist der übliche Weg. In C# ginge die Zuweisung direkt; Javas Lambdas
   erfassen nur effektiv finale Variablen.
3. `assertThrows(NullPointerException.class, () -> …)` ist das Gegenstück zu
   `Assert.Throws<T>()`.

### Aufgabe 2.4 — Reparieren

Erst jetzt. Beide Befunde, jeder in einem eigenen Commit, damit im Verlauf sichtbar bleibt,
welcher Test welchen Fehler gefunden hat.

**Tipp**
1. Nach jedem Fix: Der zugehörige Test wird grün, **alle anderen bleiben grün**. Wenn ein
   anderer Test kippt, ist das eine Information — nicht nur ein Ärgernis.

---

# Etappe 3 — Gerät und Nebenläufigkeit (`device`)

**Ziel:** Ein Inkubator, dessen Messwert aus einem eigenen Thread kommt, und dessen Zustand das
aushält.

**Fertig heißt:** Alle Tests aus 3.4 grün, darunter zwei, die ohne saubere Absicherung nicht
zuverlässig durchlaufen. Kein `Thread.sleep` als Synchronisationsmittel in den Tests.

### Aufgabe 3.1 — `TemperatureChangedEvent`

**Spezifikation**

```java
public record TemperatureChangedEvent(double previousCelsius, double currentCelsius) { }
```

Reiner Datentyp, keine Logik, keine Prüfungen.

**Tipp**
1. Ein `record` erzeugt `equals`, `hashCode` und `toString` mit — das Gegenstück zum C#-`record`.
   Genau deshalb eignet es sich hier: Im Test lässt sich das erwartete Ereignis direkt
   vergleichen.

### Aufgabe 3.2 — `Incubator`

**Spezifikation**

```java
public class Incubator {
    public Incubator(double targetCelsius, double toleranceCelsius) { … }

    public Event<TemperatureChangedEvent> temperatureChanged() { … }

    public double  getCurrentTemperature()   { … }
    public double  getTargetTemperature()    { … }
    public void    setTargetTemperature(double celsius) { … }
    public void    updateTemperature(double celsius)    { … }
    public boolean isWithinTolerance()       { … }
}
```

| Anforderung | Verhalten |
|---|---|
| I-1 | Konstruktor wirft `IllegalArgumentException`, wenn `targetCelsius` nicht endlich oder außerhalb `[0, 100]` liegt, oder wenn `toleranceCelsius` negativ oder nicht endlich ist. |
| I-2 | `setTargetTemperature` prüft wie I-1 und ändert sonst nichts am Messwert. |
| I-3 | `updateTemperature(c)` setzt den Messwert und feuert `temperatureChanged` **nur dann**, wenn sich der Wert um mehr als `0.001` geändert hat. |
| I-4 | Das gefeuerte Ereignis enthält den Wert **vor** und **nach** der Änderung. |
| I-5 | `isWithinTolerance()` ist `true`, wenn `|current − target| <= tolerance` — Grenze eingeschlossen. |
| I-6 | Alle Lesemethoden sind aus jedem Thread aufrufbar und liefern nie einen halb geschriebenen Zustand. |
| I-7 | Das Prüfen-und-Setzen in `updateTemperature` ist **atomar**: Zwei gleichzeitige Aufrufe dürfen nicht beide „unverändert" sehen und beide schreiben. |
| I-8 | `fire()` wird **außerhalb** jedes Sperrbereichs aufgerufen. |

**Tipps**
1. Zu I-6 vs. I-7: `volatile` sichert **Sichtbarkeit** — ein Schreibvorgang wird von anderen
   Threads gesehen. Es macht **nicht** atomar. „Lies den alten Wert, vergleiche, schreib den
   neuen" sind drei Schritte; dazwischen kann ein anderer Thread dazwischenfunken. Für I-7
   brauchst du mehr.
2. Zu I-8: Das ist die wichtigste Regel des ganzen Projekts. Fremder Listener-Code darf **nie**
   unter deiner Sperre laufen — er kann beliebig lange dauern, zurück in dein Objekt greifen und
   dich verklemmen. Praktische Folge: Innerhalb der Sperre entscheidest du, *ob* und *was*
   gefeuert wird; gefeuert wird danach.
3. Zu I-3: `0.001` als benannte Konstante, nicht als Zahl im Code. `double` direkt auf Gleichheit
   zu prüfen ist ohnehin unzuverlässig — deshalb der Schwellwert.

### Aufgabe 3.3 — Sensor-Simulation

**Diese Aufgabe gibt absichtlich keine Typen vor.** Was gebaut werden muss, steht unten als
Verhalten; **wie du es schneidest, entscheidest du.** Der Schnitt ist die eigentliche
Entwurfsentscheidung dieser Etappe — und wer sie trifft, kann sie später auch begründen. Überall
sonst im Plan stehen Signaturen; hier nicht, und zwar mit Absicht.

**Was gebaut wird:** Etwas, das im Sekundentakt neue Messwerte erzeugt und dem `Incubator`
meldet. Die Werte schwanken um den Sollwert und weichen gelegentlich stärker ab.

| Anforderung | Verhalten |
|---|---|
| M-1 | Ein erzeugter Wert bewegt sich in Richtung Sollwert und trägt eine kleine zufällige Schwankung. |
| M-2 | Die Werteerzeugung ist **reproduzierbar**: Bei gleicher Ausgangslage kommt zweimal dieselbe Folge heraus. |
| M-3 | Die Werte bleiben in einem plausiblen Band um den Sollwert (Vorgabe: ±5 °C). |
| M-4 | Die Werteerzeugung ist **ohne laufenden Zeitgeber und ohne Threads** prüfbar. |
| SI-1 | Gestartet ruft die Simulation im angegebenen Takt `incubator.updateTemperature(...)` auf. |
| SI-2 | Ein zweiter Start bei laufender Simulation ist wirkungslos — kein zweiter Takt, keine Ausnahme. |
| SI-3 | Das Beenden stoppt den Takt und wartet bis zu 1 Sekunde auf das Ende der laufenden Ausführung. |
| SI-4 | Nach dem Beenden kommt kein weiterer Aufruf bei `Incubator` an. |
| SI-5 | Das Beenden ist idempotent. |
| SI-6 | Der Zeitgeber ist ein `ScheduledExecutorService`, kein selbst gebauter Thread. |
| SI-7 | Der Typ, der den Takt hält, ist `AutoCloseable` — er wird in `ui` per try-with-resources oder beim Fensterschluss beendet. |

**Bevor du tippst:** Schreib in zwei, drei Sätzen auf, welche Typen du anlegen willst und warum.
Das ist der Punkt, an dem sich ein Entwurfsgespräch lohnt (Form 2 unten). Ich habe einen eigenen
Schnitt im Kopf — den zeige ich dir erst, wenn deiner steht. Vorher wäre er die Antwort statt ein
Vergleich.

**Tipps**
1. M-4 ist die Anforderung, die den Schnitt bestimmt. Frag dich: Was genau will ich prüfen, wenn
   kein Zeitgeber läuft — und liegt das gerade in einem Typ, der ohne Zeitgeber gar nicht
   existieren kann?
2. Zu M-2: Reproduzierbarkeit heißt praktisch, dass die Zufallsquelle **nicht** in der Klasse
   entstehen darf, die sie benutzt. Wer sie hereingibt, kann sie im Test mit festem Startwert
   belegen. Dasselbe Prinzip wie Dependency Injection in C#, nur ohne Container.
3. Zu SI-3: Der übliche Ablauf ist `shutdown()` → `awaitTermination(...)` → notfalls
   `shutdownNow()`. Wichtig ist, dass das Beenden wirklich zurückkehrt und nicht ewig wartet.
4. Zu SI-6: Ein `ScheduledExecutorService` erzeugt standardmäßig **keine** Daemon-Threads — die
   Anwendung beendet sich dann beim Schließen des Fensters nicht. Das ist eine der Stellen, an
   denen man den Fehler einmal selbst sehen sollte.

### Aufgabe 3.4 — Die Tests

| # | Test | Prüft |
|---|---|---|
| 1 | Konstruktor und `setTargetTemperature` werfen bei ungültigen Werten (`NaN`, `-1`, `101`) | I-1, I-2 |
| 2 | `updateTemperature` feuert mit korrektem alten und neuen Wert | I-3, I-4 |
| 3 | Zweimal derselbe Wert (und ein Wert innerhalb von `0.001`) feuert **nicht** | I-3 |
| 4 | `isWithinTolerance` genau an der Grenze ist `true` | I-5 |
| 5 | **Ein Listener, der aus `fire()` heraus `getCurrentTemperature()` aufruft, blockiert nicht** | I-8 |
| 6 | **Mehrere Threads rufen gleichzeitig `updateTemperature`; ein zählender Listener erhält genau so viele Ereignisse wie tatsächliche Änderungen** | I-7 |
| 7 | Die Werteerzeugung liefert bei gleicher Ausgangslage zweimal dieselbe Folge — **ohne laufenden Zeitgeber** | M-2, M-4 |
| 8 | Über viele Schritte bleiben die Werte im Band um den Sollwert | M-3 |
| 9 | Die gestartete Simulation ruft `updateTemperature` auf | SI-1 |
| 10 | Nach dem Beenden kommt kein weiterer Aufruf an; zweites Beenden ist harmlos | SI-4, SI-5 |

Test 7 ist zugleich die Probe auf deinen Schnitt aus 3.3: Wenn er sich nicht schreiben lässt,
ohne einen Zeitgeber zu starten, ist die Werteerzeugung noch nicht frei genug geschnitten.

**Tipps**
1. Test 5 ist der Beweis für Regel I-8 und braucht ein Zeitlimit — `assertTimeoutPreemptively`.
   Ohne Zeitlimit hängt der Testlauf, statt zu scheitern.
2. Für Test 6 und 9: `CountDownLatch` statt `Thread.sleep`. Ein `sleep` macht Tests langsam und
   trotzdem unzuverlässig — es prüft die Geschwindigkeit des Rechners, nicht den Code. Ein Latch
   wartet auf das Ereignis selbst.
3. Test 6 ist heikel formuliert: Bei gleichzeitigen Aufrufen ist die *Reihenfolge* nicht
   zugesichert, nur die *Anzahl*. Prüfe genau das und nicht mehr — sonst schreibst du einen Test,
   der gelegentlich grundlos rot wird.

---

# Etappe 4 — Oberfläche (`ui`)

**Ziel:** Ein Fenster, das Messwerte aus dem Sensor-Thread anzeigt, ohne abzustürzen, und das
sich sauber schließen lässt.

**Fertig heißt:** Die Abnahmeliste unten ist einmal vollständig von Hand durchgegangen.

**Warum hier keine automatisierten Tests:** UI-Tests bräuchten ein eigenes Werkzeug und einen
laufenden Fenster-Server; der Aufwand steht in keinem Verhältnis zum Umfang. Das ist eine
bewusste Entscheidung, keine Lücke — und sie steht als solche im README. An ihre Stelle tritt
eine schriftliche Abnahmeliste, die reproduzierbar ist.

### Aufgabe 4.1 — Aufbau

**Spezifikation**

| Anforderung | Verhalten |
|---|---|
| U-1 | Grundgerüst `BorderPane`; Wertespalte als `VBox`; Bedienelemente in einer eigenen Leiste. Kein `FlowPane`. |
| U-2 | Große Anzeige des aktuellen Messwerts, Format `%.2f °C`. |
| U-3 | Statusanzeige: grün innerhalb der Toleranz, bernstein außerhalb. |
| U-4 | Eingabefeld für den Sollwert plus Schaltfläche zum Übernehmen. |
| U-5 | Ungültige Eingabe (leer, Buchstaben, außerhalb des Bereichs) markiert das Feld und ändert nichts. **Kein** Absturz, **keine** Ausnahme in der Konsole. |
| U-6 | Schaltfläche Start/Stopp für die Simulation. |

**Tipp**
1. `BorderPane` entspricht dem `DockPanel` aus WPF, `VBox` dem `StackPanel` mit vertikaler
   Ausrichtung. Die Layout-Logik überträgt sich; nur die Namen sind andere.

### Aufgabe 4.2 — Die Thread-Brücke

**Spezifikation**

| Anforderung | Verhalten |
|---|---|
| U-7 | Die Oberfläche abonniert `incubator.temperatureChanged()`. |
| U-8 | **Jeder** Zugriff auf Oberflächenelemente aus dem Sensor-Thread läuft über `Platform.runLater(...)`. |
| U-9 | Beim Schließen des Fensters: Abo beenden, Simulation schließen, Anwendungsprozess endet. |

**Tipps**
1. Mach den Fehler einmal absichtlich: Ruf `label.setText(...)` direkt aus dem Listener auf, ohne
   `Platform.runLater`. Die `IllegalStateException: Not on FX application thread` ist die
   Fehlermeldung, die du danach nie wieder suchen musst. **Danach zurückbauen.**
2. `Platform.runLater()` entspricht `Dispatcher.BeginInvoke()` — es stellt in die Warteschlange
   und kehrt sofort zurück. Ein Gegenstück zum synchronen `Dispatcher.Invoke()` gibt es in JavaFX
   nicht direkt, und du brauchst es hier auch nicht.
3. Zu U-9: Hier zeigt `Subscription` seinen Zweck. `stage.setOnCloseRequest(...)` ist der Ort
   dafür.

### Aufgabe 4.3 — Abnahme von Hand

Diese Liste wird einmal durchgegangen und das Ergebnis unten im Protokoll vermerkt.

- [ ] Fenster startet mit `gradlew run`.
- [ ] Ohne gestartete Simulation steht ein sinnvoller Anfangswert da, keine Platzhalter.
- [ ] Start: Der Wert ändert sich im Takt, ohne Ruckeln, ohne Ausnahme in der Konsole.
- [ ] Sollwert ändern: Der Wert wandert erkennbar zum neuen Sollwert.
- [ ] Statusfarbe wechselt beim Verlassen und Wiedererreichen der Toleranz.
- [ ] Ungültige Eingabe (`abc`, leer, `-40`): Feld markiert, sonst passiert nichts.
- [ ] Stopp: Der Wert steht still.
- [ ] Fenster schließen bei laufender Simulation: Der Prozess endet wirklich (im Task-Manager
      prüfen), keine Ausnahme beim Herunterfahren.
- [ ] Zehn Minuten laufen lassen: keine wachsende Speicherlast, keine Ausnahme.

---

# Etappe 5 — Abrunden

**Ziel:** Das Projekt ist von außen verständlich und läuft auf einem fremden Rechner.

**Fertig heißt:** Ein frisch geklonter Stand baut, testet und startet, ohne dass etwas von Hand
nachgelegt wird.

### Aufgabe 5.1 — README

**Spezifikation** — vier Abschnitte, mehr nicht:

1. **Was es ist** — drei Sätze.
2. **Wie man es startet** — `gradlew run`, `gradlew test`, benötigte Java-Version.
3. **Aufbau** — die drei Schichten und die Regel, dass Abhängigkeiten nur nach unten zeigen.
4. **Bewusste Entscheidungen und offene Punkte** — warum Java 21 LTS, warum
   `CopyOnWriteArrayList`, warum keine UI-Tests, und was bei mehr Zeit anders wäre.

**Tipps**
1. Abschnitt 4 ist der, der am meisten trägt. Die Grenzen des eigenen Codes benennen zu können
   wirkt souveräner als ein Projekt, das angeblich fertig ist.
2. Ein Screenshot des laufenden Fensters kostet zwei Minuten und macht das README ungleich
   greifbarer.

### Aufgabe 5.2 — Frischer Start

**Spezifikation**
- Repository an einen anderen Ort klonen.
- Dort `gradlew test` und `gradlew run` ausführen — beides muss ohne weitere Schritte laufen.
- Was fehlt, wird nachgetragen und committet.

**Tipp**
1. Der häufigste Fund dabei: eine Datei, die lokal existiert, aber nie committet wurde, oder eine
   zu weit gefasste `.gitignore`. Genau dafür ist der Schritt da.

---

## Offene Punkte

Bewusst nicht gelöst. Jeder Punkt hat einen Grund, und der Grund ist wichtiger als die Lösung.

| Punkt | Warum offen |
|---|---|
| **Reentranz beim Feuern** — löst ein Listener während `fire()` selbst eine Änderung aus, verschachteln sich die Ereignisse | Eine Warteschlange würde es lösen und den Baukasten deutlich vergrößern. Dokumentiertes Verhalten statt ungewollter Zufall. |
| **Reihenfolge bei gleichzeitigen Änderungen** — weil `fire()` außerhalb der Sperre läuft, können Ereignisse in anderer Reihenfolge ankommen als die Änderungen geschahen | Die Alternative wäre, fremden Code unter der Sperre laufen zu lassen. Das ist die schlechtere Wahl — siehe I-8. |
| **Kein Rückstau-Schutz** — ein langsamer Listener bremst den Sensor-Takt | Bei echter Hardware bräuchte es eine Entkopplung über eine Queue. Hier wäre das Aufwand ohne Erkenntnisgewinn. |
| **Fehlerbehandlung in der Oberfläche** ist minimal | Der Kern ist abgesichert, die Oberfläche nicht. Bewusste Gewichtung. |
| **Keine automatisierten UI-Tests** | Siehe Etappe 4. |

---

## Review-Protokoll

Wird bei jedem angeforderten Review fortgeschrieben. Zweck: Nachvollziehbarkeit — welcher Befund
kam wann, was wurde daraus, und **was wurde bewusst nicht übernommen**.

| Datum | Etappe | Befund | Entscheidung |
|---|---|---|---|
| — | — | *noch kein Review* | — |

---

## Änderungen an diesem Plan

Der Plan ist nicht in Stein. Wenn eine Spezifikation sich beim Bauen als falsch, unklar oder zu
umständlich erweist, wird sie geändert — mit Eintrag hier. Eine Anforderung stillschweigend zu
umgehen ist der einzige Fehler, den man dabei machen kann.

| Datum | Was geändert | Warum |
|---|---|---|
| 30.08.2026 | Plan angelegt | — |
| 31.08.2026 | Abschnitt „Formen der Zusammenarbeit" ergänzt | Der Plan kannte nur das Review nach getaner Arbeit. Die drei anderen Formen — Klären, Entwurf vorab, Steckenbleiben — greifen früher und sind mehr wert als eine Korrektur hinterher. |
| 31.08.2026 | Aufgabe 3.3 ohne Typvorgabe neu gefasst, neue Anforderung M-4 | Der Schnitt der Simulation war vorgegeben. Damit hätte eine Entwurfsentscheidung im Projekt gestanden, die nicht vom Autor kommt — und die sich hinterher nicht begründen ließe. Jetzt gibt der Plan nur das Verhalten vor. |
| 31.08.2026 | Fenstertitel auf „Incubator Monitor", Projektname `incubator-monitor` | Alles, was im Code oder als Bezeichner auftaucht, ist einheitlich englisch. Der deutsche Titel war die letzte Ausnahme und hätte im Fenster neben englischen Bezeichnern gestanden. Dieses Dokument bleibt deutsch. |
| 31.08.2026 | JUnit 6.0.0 statt JUnit 5 (Aufgabe 1.3) | Der IntelliJ-Assistent hat die neue Hauptversion eingetragen. Für die hier benutzten Zusicherungen ist der Unterschied null — die Paketnamen `org.junit.jupiter.api.*` sind unverändert. Am Build zu drehen, nur damit eine Zahl zur Doku passt, wäre der schlechtere Tausch. Die Abweichung steht dafür hier. |
