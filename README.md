# Incubator Monitor

**English** · [Deutsch](README.de.md)

> **Status: complete — all five stages done** — 2026-09-04

A simulated laboratory device written in Java: a CO₂ incubator that keeps cell cultures at a
target temperature, reports its readings from a thread of its own, and is monitored by a JavaFX
user interface.

<img src="docs/screenshot-window.png" width="620" alt="The application window: a large temperature reading, a green status light reading Within tolerance, a target input with Apply, and a Stop button">

---

## What this is about

There is **no real hardware**. The sensor is simulated — but the concurrency is real: readings are
produced on a different thread than the one drawing the interface. That boundary is where the
interesting part of this project lives.

The project is the hands-on half of a move from C# to Java. The goal is deliberately not "as many
features as possible" but **being traceable**: the decisions that carry weight are written down —
including the ones deliberately left open. A small project that is thought through is worth more
here than a large one that merely runs.

Four topics come together in their natural order: the observer pattern (Java has no `event`
keyword — if you want events, you build them), concurrency, binding to a UI that owns its own
thread, and testability.

---

## Running it

Requires a **JDK 21** — the Gradle toolchain is pinned to Java 21 LTS. JavaFX is fetched by Gradle;
nothing has to be installed by hand.

```
gradlew test    # all tests
gradlew run     # start the application
```

On Windows the wrapper is `gradlew.bat`, on Linux and macOS `./gradlew`.

`gradlew run` opens the window shown above. Press **Start** and the reading updates twice a second;
the status light turns amber once the value leaves the tolerance band around the target, and green
again when it returns.

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
| 5 | **Wrap-up** | README, clean clone, limits stated honestly | ✅ done |

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

The written acceptance list was walked through by hand and passed on all nine points — see
[Acceptance](#acceptance) below.

**Stage 5 — Wrap-up.** README, a fresh clone that builds and runs with no extra steps, and an
honest list of what was deliberately left open. The clone was made on 2026-09-04 with a *fresh*
Gradle home, so JavaFX and JUnit were downloaded again rather than taken from a local cache: tests
green, window starts, shutdown clean. Nothing had to be added afterwards.

Every stage ends in a presentable state. Whatever exists, runs; the tests are green.

---

## Acceptance

There are **no automated UI tests** here — they would need their own tooling and a running window
server, out of proportion to a window this size. In their place stands a written acceptance list of
nine points, walked through by hand on 2026-09-04 and passed in full. The last of them turned out
to be the one worth keeping: *ten minutes of running, no growing memory*.

[<img src="docs/acceptance-heap.png" alt="Heap usage over twenty minutes, with two forced collections and one automatic collection marked by arrows">](docs/acceptance-heap.png)

*Twenty minutes. **Green: a collection forced by hand. Red: one the JVM ran on its own.***

For the first ten minutes the curve only climbs — 17 MB to 59 MB, not a sawtooth in sight. That
looks like a leak and is not one: this heap may grow to 8 GB, so the collector had no reason to act
and barely did. The forced collection then drops it to 9 MB and shrinks the committed memory from
110 MB to 41 MB. Only after that does the JVM collect on its own, in the third valley — the likely
reason being that the young region of a smaller heap fills sooner. That last part is an inference
from the numbers, not something measured here.

[<img src="docs/acceptance-heap-30min.png" alt="Heap usage over thirty minutes: after the forced collection the curve settles into a repeating sawtooth between 10 and 22 MB">](docs/acceptance-heap-30min.png)

*The same run, half an hour in. Everything after 17:06 is the JVM's own doing.*

Left alone it settles into a rhythm: up to about 22 MB, down to about 10 MB, roughly every five
minutes. Three cycles, three floors, all at the same height. **A leak would lift the floor a little
with every cycle** — this one does not move.

The lesson was about reading the instrument rather than the code: on a generously sized heap a
rising curve says nothing, and only low points can be compared. The full measurement, with the
collector's own counters, is in the review log of [`ENTWICKLUNGSPLAN.md`](ENTWICKLUNGSPLAN.md).

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

---

## Deliberate decisions and open points

### Why the code looks the way it does

**A private lock object, not `synchronized(this)`.** With `this` as the monitor, any outside caller
could lock the incubator from the outside and deadlock it, and nothing in the class would show why.
A private field can only be locked from inside.

**`fire()` runs outside the lock.** The event is built inside the guarded block and delivered
outside it. Listener code is foreign code, and foreign code must never execute while this object
holds its own lock — that is how a deadlock is built. The price is stated under the open points:
events can arrive in a different order than the changes happened.

**The sampler keeps no state of its own.** On every tick it asks the incubator for the current
reading instead of remembering its own copy. Two places claiming the same truth drift apart as soon
as anyone else calls `updateTemperature`.

**`scheduleWithFixedDelay`, not `scheduleAtFixedRate`.** Fixed rate catches up on missed runs. A
late reading is worthless, and catching up would only pile more work on a listener that is already
too slow.

**Daemon threads for the sampler.** A `ScheduledExecutorService` does not create them by itself.
Without a `ThreadFactory` setting `setDaemon(true)`, the process keeps running after the window is
closed.

**Device values are read before `Platform.runLater(…)`, never inside it.** The block runs later, on
the FX thread. A question asked inside it would be answered with the state of that later moment —
the number would come from one reading and the status colour from the next.

**`MonitorView` does not know the `Incubator`.** It is handed numbers and booleans and reports input
back through `DoubleConsumer` and `Runnable`. This is not about object lifetimes — the incubator
would live just as long either way — but about who may know whom: delete the whole `ui` package and
`device` and `events` still compile and their tests still run. That is also why the tests need no
window on screen.

**Cleanup in `Application.stop()`, not in `stage.setOnCloseRequest(…)`.** The runtime calls `stop()`
whenever the application ends regularly — closing the last window as well as `Platform.exit()`; a
hard `System.exit()` bypasses it, but then nothing is cleaned up anyway. A close request, by
contrast, fires only when the user closes that one window, and another handler can swallow it.

**`CopyOnWriteArrayList` for the listeners.** A listener may cancel its own subscription while
`fire()` is still walking the list. Copy-on-write iterates over a snapshot, so the removal cannot
disturb the loop. Copying happens only when the list **changes** — on subscribe and unsubscribe,
never on firing: here that is two copies over the whole run of the program, while the device
reports twice a second without touching the list. It becomes the wrong choice where listeners come
and go constantly; the length of the list then merely multiplies the cost of each single copy.

**A model rather than a sensor behind the seam** — the longest of these decisions has
[its own section](#one-design-decision-a-model-not-a-sensor) above.

### Tooling

**Java 21 LTS, not the newest release.** 21 is the version companies actually run. It costs the
short `void main()` of newer releases — in exchange, the classic
`public static void main(String[] args)` gets practised.

**The interface is built in Java code, not FXML.** One tool fewer that can go wrong. FXML buys
nothing at this size.

**No third-party libraries.** Only the JDK, JavaFX and JUnit. Anything else would blur how much of
this is my own work.

**No automated UI tests** — see [Acceptance](#acceptance) for what stands in their place.

**Out of scope on purpose:** networking, databases, persistence, a multi-module layout. None of
them would show anything the four core topics do not already show.

### What would be different with more time

- **Reentrancy while firing.** If a listener triggers another change during `fire()`, the events
  nest. A queue would resolve it; for now it is documented behaviour rather than an accident.
- **Ordering under concurrent changes.** Because `fire()` runs outside the lock, events can arrive
  in a different order than the changes happened. The alternative — foreign listener code running
  under this object's own lock — is the worse trade.
- **No backpressure.** A slow listener slows the sampler thread down. Real hardware would need a
  queue between the two.
- **Two answers to listener failures in one package.** `EventSupport` takes an error handler,
  `TemperatureSampler` prints the stack trace itself. Consistency would cost a constructor
  parameter that no caller needs yet.
- **A listener that closes the sampler blocks for a second.** It waits for a lock held by the
  thread that is itself waiting for that listener to return. It resolves on its own after the
  timeout, but it is a real second of standstill.
- **Error handling in the interface is minimal.** The core is guarded, the UI is not — a deliberate
  weighting rather than an oversight.

---

## Where AI was used

Claude Code took part in this project as a reviewer, not as a code generator. The rule was written
down before the first line was typed: *generated code is not adopted until it is understood.* What
that looks like in practice, taking the most recent round as the example — `TemperatureSampler`,
handed over with a deliberately narrow question: **are there redundant local variables, or missing
values?**

Two answers came back, and only one of them turned into a change.

- **No redundant locals.** The named `ThreadFactory` and `Runnable` in `start()` could be inlined
  into their calls. They are not redundant — the names are what keep the method readable. Nothing
  changed here.
- **A missing value.** The sampling interval was checked for `null` but never for being
  **positive**. `Duration.ZERO` passed the constructor and only failed later inside `start()`,
  where `scheduleWithFixedDelay` rejects a delay of zero — an exception raised far from its cause.
  Alongside it, `interval.toMillis()` truncated: an interval below a millisecond became zero and
  failed the same way.

The repair took the route everything in `device` takes: the failing tests first — both were seen
red for the right reasons, one for a missing exception, one for an exception thrown in the wrong
place — then the check in the constructor and the switch to `toNanos()`. A third, purely cosmetic
remark (two `else` branches after a `return`) was applied by hand before the rest was touched.

That split is the point of the section. The findings came from the review; the decision about which
of them was worth acting on did not. Both are written down in the review log of
[`ENTWICKLUNGSPLAN.md`](ENTWICKLUNGSPLAN.md), the same place that records what was **not** adopted.

---

## Further reading

Both documents below are written in German.

- **[`CLAUDE.md`](CLAUDE.md)** — why the project is cut the way it is, which decisions were made,
  and what is knowingly left open
- **[`ENTWICKLUNGSPLAN.md`](ENTWICKLUNGSPLAN.md)** — what gets built and in which order, with
  requirements, test lists, and the logs recording every deviation
