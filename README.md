## Description
Java Spring Boot API that returns:
1. The 5 heaviest Pokémon.
2. The 5 tallest Pokémon.
3. The 5 Pokémon with the highest base experience.

Data source: [PokéAPI](https://pokeapi.co/api/v2/).

## Technologies
- Java 17
- Spring Boot 3
- WebFlux (WebClient)
- Caffeine Cache
- JUnit 5 + Mockito + WireMock (integration)
- JaCoCo (coverage)
- Docker

## Run with Docker

### Build the image
```bash
mvn clean package -DskipTests
docker build -t alea-pokemon-api .
```

### Run the container
```bash
docker run -p 8080:8080 alea-pokemon-api
```

### Test the endpoints
```bash
curl http://localhost:8080/api/pokemons/heaviest
curl http://localhost:8080/api/pokemons/tallest
curl http://localhost:8080/api/pokemons/experienced
```

## Cache
- Implemented with Caffeine (spring.cache.caffeine.spec=maximumSize=100,expireAfterWrite=10m)
- Reduces repetitive calls to PokéAPI.

## Integration Testing
- Simulates PokéAPI using WireMock.
- Verifies that the endpoints respond correctly.