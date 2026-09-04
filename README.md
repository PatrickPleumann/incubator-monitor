# Incubator Monitor

**English** · [Deutsch](README.de.md)

> **Status: stages 1–4 done, stage 5 in progress** — 2026-09-04

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

## Diagrams

Click either one to open it full size.

| [**The path of a reading**](docs/flow-reading-path.svg) | [**Types and their dependencies**](docs/class-diagram.svg) |
|:--|:--|
| [<img src="docs/flow-reading-path.svg" width="420" alt="Flow: how a reading travels from the sampler thread to the interface">](docs/flow-reading-path.svg) | [<img src="docs/class-diagram.svg" width="420" alt="Class diagram of the three packages">](docs/class-diagram.svg) |
| What happens on every tick — where the value is guarded by a lock, where a call is abandoned, and where the thread boundary sits. | Every type in the three packages, its fields and methods, and which of them knows which. |

---

## The five stages

| # | Stage | Result | Status |
|---|---|---|---|
| 1 | **Scaffolding** | A Gradle project that builds, runs and tests | ✅ done |
| 2 | **Observer toolkit** | Delivering and cancelling events, under test | ✅ done |
| 3 | **Device and concurrency** | An incubator reporting from its own thread | ✅ done |
| 4 | **User interface** | A JavaFX window showing live readings | ✅ done |
| 5 | **Wrap-up** | README, clean clone, limits stated honestly | ⏳ in progress |

**Stage 1 — Scaffolding.** Gradle with the Kotlin DSL, a Java 21 toolchain, JavaFX and JUnit. The
three packages, plus an empty window that closes cleanly.

**Stage 2 — Observer toolkit.** `Event`, `EventSupport` and `Subscription`. The code came from an
earlier project and was **adopted unchanged**; then tests were written against it; then it was
repaired — in that order. Two real defects surfaced along the way: cancelling one subscription
could cancel someone else's, and exceptions thrown by listeners vanished without a stack trace.
Each was first made visible as a failing test, then fixed.

**Stage 3 — Device and concurrency.** The `Incubator` with its target temperature and tolerance,
plus a sensor simulation producing a new reading at a fixed interval. This stage is about atomicity
rather than mere visibility, about locks that never enclose foreign code, and about a scheduler
that can be shut down cleanly.

**Stage 4 — User interface.** A JavaFX window with a reading display, a target-temperature input
and a start/stop control. Every access from the sensor thread goes through `Platform.runLater(…)`
— that bridge between threads is what this stage is really about.

The written acceptance list was walked through by hand on 2026-09-04 and passed on all nine points,
memory behaviour included: over ten minutes the heap climbed steadily without ever collecting, and
a forced collection then dropped it below its own starting floor. A rising curve on a generously
sized heap says nothing — only forced low points can be compared.

**Stage 5 — Wrap-up.** README, a fresh clone that builds and runs with no extra steps, and an
honest list of what was deliberately left open.

Every stage ends in a presentable state. Whatever exists, runs; the tests are green.

---

## Acceptance

There are **no automated UI tests** here. They would need their own tooling and a running window
server, which is out of proportion to a window this size. In their place stands a written
acceptance list of nine points, walked through by hand and recorded in the development plan.

The last of those nine — *ten minutes of running, no growing memory* — turned out to be the one
worth keeping.

[<img src="docs/acceptance-heap.png" alt="Heap usage over twenty minutes, with two forced collections and one automatic collection marked by arrows">](docs/acceptance-heap.png)

*Heap usage over roughly twenty minutes. **Green: a collection forced by hand. Red: one the JVM
ran on its own.***

For the first ten minutes the curve only climbs — 17 MB to 59 MB, not a sawtooth in sight. That
looks like a leak and is not one. The heap here may grow to 8 GB, so the collector had no reason
to act, and the counters confirm it barely did.

The second forced collection drops the heap to 9 MB and shrinks the committed memory from 110 MB
to 41 MB. That smaller heap is what makes the third valley possible: the young region now fills in
minutes instead of a quarter of an hour, and at 17:11 the JVM collects **on its own**, down to
10.5 MB. The two arrows also mark two different mechanisms — the forced ones are full collections,
the automatic one is a young-generation collection, an ordinary tooth of the sawtooth we had been
waiting for.

Two floors, arrived at independently, minutes apart, at the same height. Nothing accumulates.

Left running for another quarter of an hour, the picture becomes plain:

[<img src="docs/acceptance-heap-30min.png" alt="Heap usage over thirty minutes: after the forced collection the curve settles into a repeating sawtooth between 10 and 22 MB">](docs/acceptance-heap-30min.png)

*The same run, half an hour in. No arrows needed — everything after 17:06 is the JVM's own doing.*

From there on it collects entirely by itself, roughly every five minutes: up to about 22 MB, down
to about 10 MB, and again, and again. Three cycles, three floors, all at the same height — this is
the sawtooth that was missing from the first ten minutes, and its shape is the whole answer. **A
leak would lift the floor a little with every cycle.** This one does not move.

What this really taught was how to read the instrument rather than the code: **on a generously
sized heap, a rising curve says nothing.** Only low points can be compared — and one the runtime
chose by itself is worth more than one squeezed out by hand.

---

## One design decision: a model, not a sensor

Readings come from a replaceable type behind an interface. Its method reads

```java
double nextTemperature(double currentCelsius, double targetCelsius);
```

It **computes** the next value rather than reading one, and it remembers nothing — together that
makes it testable with no scheduler and no thread involved. The seam is therefore a **model of
temperature behaviour**, not a sensor: what can be swapped are different curves (calm, sluggish,
noisy), not real hardware.

That is deliberate. An interface with room for real hardware behind it would have to carry four
more things: a method without arguments (a sensor reads, it does not compute), an answer to read
failures (a checked exception or `OptionalDouble`), a lifetime (`AutoCloseable`, since a
connection is opened and must be closed) — and possibly the opposite direction altogether, because
real sensors tend to report on their own rather than being polled. None of that would buy anything
here: arithmetic does not fail, and what sits behind the interface is a random-walk formula.

What settled it is that the decision stays **reversible**. The interface has exactly one user —
the type that holds the clock. Moving to real sensing later touches one class and adds a second
one next to it. Abstractions do not get expensive by being introduced late; they get expensive
when they have to be cut back out of ten places.

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

`gradlew run` opens the window. Press **Start** and the reading updates twice a second; the status
light turns amber once the value leaves the tolerance band around the target, and green again when
it returns.

---

## Further reading

Both documents below are written in German.

- **[`CLAUDE.md`](CLAUDE.md)** — why the project is cut the way it is, which decisions were made,
  and what is knowingly left open
- **[`ENTWICKLUNGSPLAN.md`](ENTWICKLUNGSPLAN.md)** — what gets built and in which order, with
  requirements, test lists, and the logs recording every deviation
