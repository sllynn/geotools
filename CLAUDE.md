# GeoTools Development Guide

## Build Commands
- Build: `mvn clean install` 
- Full build with checks: `mvn clean install -Dqa -Dall`
- Run single test: `mvn -Dtest=TestName test`
- QA checks: `mvn verify -Ppmd -Perrorprone -DskipTests`

## Code Style
- **Formatting**: Google style variant (Palantir) with 120 columns, 4 spaces indent
- **Naming**:
  - Interfaces: `DataStore` (CamelCase)
  - Implementations: `ShapefileDataStore` (append interface name)
  - Abstract classes: `AbstractDataStore`
  - Tests: `SampleTest`, `ServerOnlineTest` (online tests)
- **Imports**: Organize imports as Java standard, avoid wildcards
- **Exceptions**: Never break exception chain, include context, log before assumptions
- **Null Handling**: Don't return null, use empty collections/Optional

## Architecture Guidelines
- Use interfaces to decouple components
- Follow factory pattern for object creation
- Prefer composition over inheritance
- Isolate geometry code in JTS library
- Make extension points and metadata clear

## Testing
- JUnit for unit tests in src/test
- Maven profiles for online/integration tests
- Coverage with JaCoCo
- Mark tests with appropriate annotations