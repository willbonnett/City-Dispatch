# Important parts from spec

Submission deadline: 5th March

### 1,1 What you are building
o A 2D grid city map with obstacles.
o Stations placed at fixed locations on the grid.
o Emergency units (ambulances, fire engines, police cars) based at stations.
o Incidents that are reported, dispatched, handled on-scene, and resolved as time ticks forward.
o Deterministic behaviour: the same input must always lead to the same output

### 2.1 Required classes
• CityRescueImpl — your main class that implements the CityRescue interface.
• CityMap — knows the grid size, which cells are blocked, and whether a move is legal.
• Station — stores station details and the units based there (with a capacity).
• Incident — stores incident details and its lifecycle status.
• Unit (abstract) — common unit behaviour shared by all emergency vehicles.
• At least three Unit subclasses — e.g., Ambulance, FireEngine, PoliceCar.

<b>Should make use of polmorphism</b>

### 3 Enums
IncidentType =  MEDICAL, FIRE, CRIME
UnitType = AMBULANCE, FIRE_ENGINE, POLICE_CAR
IncidentStatus = REPORTED, DISPATCHED, IN_PROGRESS, RESOLVED,
CANCELLED
UnitStatus = IDLE, EN_ROUTE, AT_SCENE, OUT_OF_SERVICE

### 4.2 Choosing the ‘best’ unit (tie-breakers)
Among eligible units (correct type, not OUT_OF_SERVICE, and not already assigned):
1. Shortest Manhattan distance (|𝑥! − 𝑥"| + |𝑦! − 𝑦"|)
2. If tied: lowest unitId
3. If tied: lowest homeStationId

### 4.3 Movement preference
When multiple moves are possible, use fixed direction order: N, E, S, W.

### Movement rule (applied once per tick for each EN_ROUTE unit)
1. List the four candidate moves in order N, E, S, W.
2. Ignore moves that go out of bounds or into a blocked cell.
3. Take the first legal move that reduces Manhattan distance to the target.
4. If none reduce distance, take the first legal move in N, E, S, W order.
5. If no legal move exists, the unit stays put this tick

### 6. Incident lifecycle (from call to calm)
Incidents move through a predictable lifecycle. Your job is to enforce the legal transitions and update
everything in the right order each tick.
o REPORTED — the incident exists, but no unit has been assigned yet.
o DISPATCHED — a unit has been assigned and is travelling.
o IN_PROGRESS — the unit has arrived and is working on scene.
o RESOLVED — the incident is complete and the unit becomes available again.
o CANCELLED — the incident was withdrawn before completion.

### 6.1 How long it takes for an incident take to resolve

Ambulance = 2 ticks
Police_Car = 3 ticks
Fire_Engine = 4 ticks

### 6.2 Cancellation and escalation
Rules:
1. cancelIncident is allowed only when an incident is REPORTED or DISPATCHED.
2. If a DISPATCHED incident is cancelled, the unit is immediately released back to IDLE (it stays at its
current location). Idle units remain stationary at their last location until dispatched again (they do
not automatically return to their home station).
3. escalateIncident may change severity (1–5) unless the incident is RESOLVED or CANCELLED.

# Beyond this point in Spec is layout for exceptions and formats for method outputs

