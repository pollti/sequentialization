# Bachelor thesis Tim Pollandt 2020

## How to Run

1. Install Maven and verify it is installed (`mvn --version`).
2. Run `mvn clean package`.
3. The JAR file is placed in `target/`.
4. Run the experiments using `java -jar bachelors-thesis.jar <Experiment> <DebugOutput> <MinSize> <MaxSize> <StepSize> <Runs>`.

* `Experiment`: Can be `Auctioneer` or `Needham`. More possibilities could be added in future releases.
* `DebugOutput`: Boolean. Is always `true` for Auctioneer example. If `true`, for each translation step the current terms are printed in files. Choose `false` for time measures.
* `MinSize`: Integer > 1. Only has an effect for Needham example. Chooses initial number of communicating pairs.
* `MaxSize`: Integer >= `MinSize`. Only has an effect if Experiment=Needham and `DebugOutput=false`. Specifies the last number of communicating pairs for a loop (for time measures). Big numbers can lead to a massive memory consumption.
* `StepSize`: Integer > 0. Only has an effect if Experiment=Needham and `DebugOutput=false`. Specifies the step size for the loop over number if pairs of communicating partners.
* `Runs`: Integer > 0. Only has an effect if Experiment=Needham and `DebugOutput=false`. Attempts per number of communication pairs for time measures.

`run.sh` provides en example execution as used in our experiments.

## Experiments

The experiment data is in the `experiments` folder. Our experiments used the Needham experiment for `n=1..600` with 15 attempts. With `experiments.py` the approximation functions can be determined from measured data.

## Bachelor thesis on G/Pi -> SGP -> Promela

Main parts (see thesis for more details):
* [x] AST Modelling
  * [x] Pi calculus
  * [x] Global types
  * [x] SGP
* [X] Converter
  * [X] P/G to SGP
  * [X] SGP to Promela
* [X] Tests
  * [X] Scalable Needham-Schroeder in G/P
  * [X] Speed tests

This is part of a Bachelor thesis at TU Darmstadt. Pi calculus terms with concurrency are translated to one sequential term following the global type's structure. Afterwards this is translated to Promela code.
