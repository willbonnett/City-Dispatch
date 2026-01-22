# Important parts from spec

Submission deadline: 5th March

### 1,1 What you are building
o A 2D grid city map with obstacles. <br>
o Stations placed at fixed locations on the grid.<br>
o Emergency units (ambulances, fire engines, police cars) based at stations.<br>
o Incidents that are reported, dispatched, handled on-scene, and resolved as time ticks forward.<br>
o Deterministic behaviour: the same input must always lead to the same output<br>

### 2.1 Required classes
• CityRescueImpl — your main class that implements the CityRescue interface.<br>
• CityMap — knows the grid size, which cells are blocked, and whether a move is legal.<br>
• Station — stores station details and the units based there (with a capacity).<br>
• Incident — stores incident details and its lifecycle status.<br>
• Unit (abstract) — common unit behaviour shared by all emergency vehicles.<br>
• At least three Unit subclasses — e.g., Ambulance, FireEngine, PoliceCar.<br>

<b>Should make use of polmorphism</b><br>

### 3 Enums
IncidentType =  MEDICAL, FIRE, CRIME<br>
UnitType = AMBULANCE, FIRE_ENGINE, POLICE_CAR<br>
IncidentStatus = REPORTED, DISPATCHED, IN_PROGRESS, RESOLVED,
CANCELLED<br>
UnitStatus = IDLE, EN_ROUTE, AT_SCENE, OUT_OF_SERVICE<br>

### 4.2 Choosing the ‘best’ unit (tie-breakers)
Among eligible units (correct type, not OUT_OF_SERVICE, and not already assigned):
1. Shortest Manhattan distance (|𝑥! − 𝑥"| + |𝑦! − 𝑦"|)<br>
2. If tied: lowest unitId<br>
3. If tied: lowest homeStationId<br>

### 4.3 Movement preference
When multiple moves are possible, use fixed direction order: N, E, S, W.

### Movement rule (applied once per tick for each EN_ROUTE unit)
1. List the four candidate moves in order N, E, S, W. <br>
2. Ignore moves that go out of bounds or into a blocked cell. <br>
3. Take the first legal move that reduces Manhattan distance to the target.<br>
4. If none reduce distance, take the first legal move in N, E, S, W order.<br>
5. If no legal move exists, the unit stays put this tick<br>

### 6. Incident lifecycle (from call to calm)
Incidents move through a predictable lifecycle. Your job is to enforce the legal transitions and update
everything in the right order each tick.<br>
o REPORTED — the incident exists, but no unit has been assigned yet.<br>
o DISPATCHED — a unit has been assigned and is travelling.<br>
o IN_PROGRESS — the unit has arrived and is working on scene.<br>
o RESOLVED — the incident is complete and the unit becomes available again.<br>
o CANCELLED — the incident was withdrawn before completion.<br>

### 6.1 How long it takes for an incident take to resolve

Ambulance = 2 ticks<br>
Police_Car = 3 ticks<br>
Fire_Engine = 4 ticks<br>

### 6.2 Cancellation and escalation
Rules:<br>
1. cancelIncident is allowed only when an incident is REPORTED or DISPATCHED.<br>
2. If a DISPATCHED incident is cancelled, the unit is immediately released back to IDLE (it stays at its
current location). Idle units remain stationary at their last location until dispatched again (they do
not automatically return to their home station).<br>
3. escalateIncident may change severity (1–5) unless the incident is RESOLVED or CANCELLED.<br>

# Beyond this point in Spec is layout for exceptions and formats for method outputs

