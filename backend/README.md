# First backend API

Java 21 + Spring Boot, generated from Spring Initializr. Open `pom.xml` in IntelliJ as a Maven project, or use the included Maven wrapper.

## Run and test

From this directory:

```sh
./mvnw test
./mvnw spring-boot:run
```

The backend listens on http://localhost:8080. It is separate from the Vite form (5173) and demo websites (8081/8082). Set `PORT` to override the backend port. Stop with Ctrl+C.

## Try the endpoint

```sh
curl -i http://localhost:8080/api/entries \
  -H 'Content-Type: application/json' \
  -d '{"name":"Alex","email":"alex@example.com","company":"","message":"Hello","source":"website-1"}'
```

Valid input returns HTTP 200:

```json
{"validated":true,"saved":false,"message":"Validation passed. Database storage is not connected yet."}
```

Invalid fields return HTTP 400 with a `message` and an `errors` object keyed by field. Name, email and message are required; company and source are optional. Source, when provided, must be `direct`, `website-1`, or `website-2`. Source is a user-supplied label, not trusted proof of the embedding website.

## Learn the request flow

1. `EntryController`: receives `POST /api/entries`.
2. `EntryRequest`: Java record holding JSON fields; validation annotations describe the rules.
3. `@Valid`: asks Spring to validate the record before calling the controller method.
4. `ApiErrorHandler`: translates invalid requests into consistent JSON errors.
5. `EntryResponse`: Java record describing the response.

Records are appropriate here because these are immutable data-transfer objects. Database storage, frontend connection, authentication, abuse protection and email scheduling are future milestones. No submissions are stored or logged by this endpoint. There is deliberately no “created” status or generated entry ID yet.

Next: configure Vite to proxy `/api` to this backend, then replace the frontend console message with a fetch request. The iframe submits from the form's own origin, not from the parent demo website.
