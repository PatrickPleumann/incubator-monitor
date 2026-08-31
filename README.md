# Incubator Monitor

**English** · [Deutsch](README.de.md)

> **Status: stage 2 of 5 complete** — 2026-09-01

A simulated laboratory device written in Java: a CO₂ incubator that keeps cell cultures at a
target temperature, reports its readings from a thread of its own, and is monitored by a JavaFX
user interface.

---

## What this is about

There is **no real hardware**. The sensor is simulated — but the concurrency is real: readings are
produced on a different thread than the one drawing the interface. That boundary is where the
interesting part of this project lives.

The project is the hands-on half of a move from C# to Java. The goal is deliberately not "as many
features as possible" but **being able to justify every decision in the code**. A small project
that is thought through is worth more here than a large one that merely runs.

Four topics come together in their natural order: the observer pattern (Java has no `event`
keyword — if you want events, you build them), concurrency, binding to a UI that owns its own
thread, and testability.

---

## Structure

Three packages, dependencies pointing **downwards only**:

```
ui        JavaFX. Knows device and events.
          ↑ thread boundary
device    Incubator + sensor simulation. Knows events, never the UI.
events    Observer toolkit. Knows nothing.
```

`events` and `device` carry no UI dependency. That is why their tests run without a window on
screen, and why the interface could be replaced without touching the device logic.

---

## The five stages

| # | Stage | Result | Status |
|---|---|---|---|
| 1 | **Scaffolding** | A Gradle project that builds, runs and tests | ✅ done |
| 2 | **Observer toolkit** | Delivering and cancelling events, under test | ✅ done |
| 3 | **Device and concurrency** | An incubator reporting from its own thread | ⏳ next |
| 4 | **User interface** | A JavaFX window showing live readings | ⬜ open |
| 5 | **Wrap-up** | README, clean clone, limits stated honestly | ⬜ open |

**Stage 1 — Scaffolding.** Gradle with the Kotlin DSL, a Java 21 toolchain, JavaFX and JUnit. The
three packages, plus an empty window that closes cleanly.

**Stage 2 — Observer toolkit.** `Event`, `EventSupport` and `Subscription`. The code came from an
earlier project and was **adopted unchanged**; then tests were written against it; then it was
repaired — in that order. Two real defects surfaced along the way: cancelling one subscription
could cancel someone else's, and exceptions thrown by listeners vanished without a stack trace.
Each was first made visible as a failing test, then fixed.

**Stage 3 — Device and concurrency.** The `Incubator` with its target temperature and tolerance,
plus a sensor simulation producing a new reading every second. This stage is about atomicity
rather than mere visibility, about locks that never enclose foreign code, and about a scheduler
that can be shut down cleanly.

**Stage 4 — User interface.** A JavaFX window with a reading display, a target-temperature input
and a start/stop control. Every access from the sensor thread goes through `Platform.runLater(…)`
— that bridge between threads is what this stage is really about.

**Stage 5 — Wrap-up.** README, a fresh clone that builds and runs with no extra steps, and an
honest list of what was deliberately left open.

Every stage ends in a presentable state. Whatever exists, runs; the tests are green.

---

## Technology

Java 21 LTS · Gradle (Kotlin DSL) · JavaFX 21 · JUnit

**No third-party libraries** beyond these. Anything else would blur how much of this is my own work.

Also deliberately out of scope: networking, databases, persistence, a multi-module layout.

---

## Running it

```
gradlew test    # all tests
gradlew run     # start the application
```

`gradlew run` currently opens an empty window — the interface is built in stage 4.

---

## Further reading

Both documents below are written in German.

- **[`CLAUDE.md`](CLAUDE.md)** — why the project is cut the way it is, which decisions were made,
  and what is knowingly left open
- **[`ENTWICKLUNGSPLAN.md`](ENTWICKLUNGSPLAN.md)** — what gets built and in which order, with
  requirements, test lists, and the logs recording every deviation
