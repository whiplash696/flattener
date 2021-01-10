# Flattener
Converts a hierarchical JSON into a flattened version, with keys as the path to every terminal value in the JSON structure.

Assumptions (mentioned in the task description and discussed over phone):
- the input always fits in RAM
- the input JSON does not contain arrays
- a library can be used for JSON parsing

### Build and execution

```
mvn clean package
cat src/test/resources/deep-nested-input.json | java -jar target/flattener-1.0.0.jar
```
