The build/test environment used is Maven/JUnit 5
The quality tools that should be run before work is considered complete are JUnit tests, SpotBugs Findings, and PIT mutation tesing.
Preserve existing behavior unless a change is explicitly requested.
Do not weaken, remove, or disable tests simply to make the build pass.
Production-code changes should be clearly identified for later human review.
Do not push or merge changes automatically.