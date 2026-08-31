# Inkubator-Monitor

> **Stand: Etappe 2 von 5 abgeschlossen** — 01.09.2026

Ein simuliertes Laborgerät in Java: ein CO₂-Inkubator, der Zellkulturen auf einer Zieltemperatur
hält, seine Messwerte aus einem eigenen Thread meldet und von einer JavaFX-Oberfläche überwacht
wird.

---

## Worum es geht

Es gibt **keine echte Hardware**. Der Sensor ist simuliert — aber die Nebenläufigkeit ist echt:
Der Messwert entsteht auf einem anderen Thread als dem, der die Oberfläche zeichnet. Genau an
dieser Grenze liegt der interessante Teil.

Das Projekt ist der praktische Teil eines Umstiegs von C# nach Java. Der Anspruch ist bewusst
nicht „möglichst viele Features", sondern **jede Entscheidung im Code begründen können**. Ein
kleines Projekt, das durchdacht ist, ist hier mehr wert als ein großes, das nur läuft.

Vier Themen kommen dabei in ihrer natürlichen Reihenfolge zusammen: das Observer-Muster (Java hat
kein `event`-Schlüsselwort — wer Ereignisse will, baut sie), Nebenläufigkeit, die Anbindung an
eine UI mit eigenem Thread, und Testbarkeit.

---

## Aufbau

Drei Pakete, Abhängigkeiten **nur nach unten**:

```
ui        JavaFX. Kennt device und events.
          ↑ Thread-Grenze
device    Inkubator + Sensor-Simulation. Kennt events, kennt die UI nicht.
events    Observer-Baukasten. Kennt nichts.
```

`events` und `device` haben keine UI-Abhängigkeit. Deshalb laufen ihre Tests ohne laufendes
Fenster, und deshalb wäre die Oberfläche austauschbar, ohne die Gerätelogik anzufassen.

---

## Die fünf Etappen

| # | Etappe | Ergebnis | Stand |
|---|---|---|---|
| 1 | **Gerüst** | Gradle-Projekt, das startet und testet | ✅ fertig |
| 2 | **Observer-Baukasten** | Ereignisse zustellen und abbestellen, getestet | ✅ fertig |
| 3 | **Gerät und Nebenläufigkeit** | Ein Inkubator, der aus einem eigenen Thread meldet | ⏳ als Nächstes |
| 4 | **Oberfläche** | JavaFX-Fenster, das Messwerte anzeigt | ⬜ offen |
| 5 | **Abrunden** | README, frischer Klon, Grenzen benannt | ⬜ offen |

**Etappe 1 — Gerüst.** Gradle mit Kotlin-DSL, Java-21-Toolchain, JavaFX und JUnit. Die drei Pakete
und ein leeres Fenster, das sich sauber schließt.

**Etappe 2 — Observer-Baukasten.** `Event`, `EventSupport` und `Subscription`. Der Code stammt aus
einem Vorläuferprojekt und wurde **unverändert übernommen**, dann wurden Tests dagegen geschrieben,
dann wurde repariert — in dieser Reihenfolge. Zwei echte Fehler kamen dabei ans Licht: Das
Abmelden konnte fremde Abos beenden, und Fehler aus Listenern verschwanden ohne Stacktrace. Beide
wurden erst als roter Test sichtbar gemacht und dann behoben.

**Etappe 3 — Gerät und Nebenläufigkeit.** Der `Incubator` mit Zieltemperatur und Toleranz, dazu
eine Sensor-Simulation, die im Sekundentakt neue Messwerte erzeugt. Hier geht es um Atomarität
statt bloßer Sichtbarkeit, um Sperren, die niemals fremden Code umschließen, und um einen
Zeitgeber, der sich sauber beenden lässt.

**Etappe 4 — Oberfläche.** Ein JavaFX-Fenster mit Messwertanzeige, Sollwert-Eingabe und
Start/Stopp. Jeder Zugriff aus dem Sensor-Thread läuft über `Platform.runLater(…)` — die Brücke
zwischen den Threads ist der eigentliche Inhalt dieser Etappe.

**Etappe 5 — Abrunden.** README, ein frischer Klon, der ohne Nacharbeit baut und startet, und eine
ehrliche Liste dessen, was bewusst offen blieb.

Jede Etappe endet in einem vorzeigbaren Zustand. Was da ist, läuft; die Tests sind grün.

---

## Technik

Java 21 LTS · Gradle (Kotlin-DSL) · JavaFX 21 · JUnit

**Keine Fremdbibliotheken** außer diesen. Alles andere würde den Eigenanteil verwischen.

Ebenfalls bewusst außen vor: Netzwerk, Datenbank, Persistenz, Multi-Modul-Aufbau.

---

## Starten

```
gradlew test    # alle Tests
gradlew run     # Anwendung starten
```

`gradlew run` öffnet derzeit noch ein leeres Fenster — die Oberfläche entsteht in Etappe 4.

---

## Mehr Details

- **[`CLAUDE.md`](CLAUDE.md)** — warum das Projekt so geschnitten ist, welche Entscheidungen
  getroffen wurden und was bewusst offen bleibt
- **[`ENTWICKLUNGSPLAN.md`](ENTWICKLUNGSPLAN.md)** — was gebaut wird, in welcher Reihenfolge,
  mit Anforderungen, Testlisten und den Protokollen über Abweichungen
