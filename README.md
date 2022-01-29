# COP4520 Assignment1

## Usage
To compile and run, first ensure you have a properly setup and installed JDK with access to the `javac` and `java` commands. Then:
* Clone this repository
* Open a terminal
* Navigate to wherever you cloned the repository, then to Assignment1/src
* Run `javac Driver.java`
* Run `java Driver`

This should result in a file being created called "primes.txt" with contents similar to:
```
1.943s 5761455 279209790387276

99999787
99999821
99999827
99999839
99999847
99999931
99999941
99999959
99999971
99999989
```
## Efficiency Notes

On my machine, running this application with eight threads completes in about `1.943s`, or about two seconds. With one 
thread, it runs in `15.792s`, or about sixteen seconds. This shows rather good efficiency and speedup, with some overhead as expected.

## Correctness
This algorithm errs on the side of correctness, by splitting the workload up between threads by having each thread write 
to its own BitSet (A memory-efficient bit-bashing based boolean array implementation from the Java Library), then merging 
the results into a final BitSet after all are done for analysis and output.

Each thread starts at their assigned startpoint (Calcualted by evalutating `startPoint - (startPoint % p)` (where `p` is 
the current number to remove multiples of) and loops through MAX/THREADS elements of its BitSet.
This tests correctly at small sizes (though if too small, requries a single thread be used), and across multiple runs of many threads.
