# Implementation Plan: Save Task Persistence

## Overview

Two production code changes unblock end-to-end task saving:

1. **`AuthService.login()`** — parse the JWT subject (`sub` claim) and write it to `localStorage` under the key `userId`. Everything downstream (`home.ts`, `TaskService`, the auth interceptor) already reads from that key and is correct.
2. **`ToDoController.createTask()`** — add an explicit null/blank guard for the `title` field before delegating to the service, so a missing title returns `400 Bad Request` instead of a `500`.

All other production code is already wired correctly. The remaining tasks install test dependencies and write the property-based and example-based tests that validate both fixes and the surrounding invariants.

---

## Tasks

- [ ] 1. Add test dependencies
  - [ ] 1.1 Install fast-check in the Angular project
    - Run `npm install --save-dev fast-check@3.23.2` from `angular/`
    - Verify `fast-check` appears under `devDependencies` in `angular/package.json`
    - _Requirements: (enables all frontend property tests)_

  - [ ] 1.2 Add jqwik to the Spring Gradle build
    - In `spring/todo/build.gradle.kts`, add to the `dependencies` block:
      `testImplementation("net.jqwik:jqwik:1.9.2")`
    - Run `gradlew.bat test` from `spring/todo/` to confirm the dependency resolves and the existing `contextLoads` test still passes
    - _Requirements: (enables all backend property tests)_

- [x] 2. Fix `AuthService.login()` — persist `userId` from JWT subject
  - [x] 2.1 Implement `extractUserIdFromToken()` and extend `login()` in `auth-service.ts`
    - File: `angular/src/app/services/auth-service.ts`
    - Add a private helper method `extractUserIdFromToken(token: string): string | null` that base64url-decodes the JWT payload segment (`token.split('.')[1]`) using `atob()`, JSON-parses it, and returns `payload.sub ?? null`; wrap the whole thing in a try/catch that returns `null` on any error
    - In the existing `login()` pipeline's `tap` callback, after `localStorage.setItem(this.TOKEN_KEY, token)`, call `extractUserIdFromToken(token)` and, if the result is non-null, call `this.setUserId(result)`
    - Do not change any method signatures, field names, or callers
    - _Requirements: 1.1, 2.2, 5.2_

  - [ ]* 2.2 Write example-based tests for `AuthService.login()` in `auth-service.spec.ts`
    - File: `angular/src/app/services/auth-service.spec.ts`
    - Provide `HttpTestingController` via `provideHttpClientTesting()` and `provideHttpClient()` in `TestBed`
    - Test 1 — successful login stores token: flush a fake JWT string, assert `localStorage.getItem('token')` equals that string
    - Test 2 — successful login parses and stores userId: flush a real-shaped JWT whose payload contains `{ "sub": "00000000-0000-0000-0000-000000000001" }`, assert `localStorage.getItem('userId')` equals that UUID
    - Test 3 — login with malformed JWT still stores token but does not store userId: flush a token without a valid base64 payload, assert `localStorage.getItem('userId')` is null
    - Clear `localStorage` in `afterEach`
    - _Requirements: 1.1, 2.2, 5.2_

- [x] 3. Checkpoint — verify the AuthService fix end-to-end
  - Ensure all tests pass, ask the user if questions arise.

- [x] 4. Fix `ToDoController.createTask()` — add title null-check
  - [x] 4.1 Add title guard to `createTask()` in `ToDoController.java`
    - File: `spring/todo/src/main/java/teambydefault/todo/controller/ToDoController.java`
    - After the existing `user == null || user.id == null` guard and before the `userRepo.findById()` call, add:
      ```java
      if (toDo.getTitle() == null || toDo.getTitle().isBlank()) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
      }
      ```
    - Also uncomment / add `toDo.setUser(user);` after the `userRepo.findById()` call so the full managed `User` entity (not just the stub from the request body) is attached before saving — this prevents a potential `TransientPropertyValueException`
    - No other changes to this file
    - _Requirements: 6.4_

  - [ ]* 4.2 Write example-based controller slice tests for `createTask()` in `ToDoControllerTest.java`
    - File: `spring/todo/src/test/java/teambydefault/todo/ToDoControllerTest.java` (create new)
    - Use `@WebMvcTest(ToDoController.class)` with `@MockitoBean ToDoService` and `@MockitoBean UserRepo`; disable the server-side `AuthInterceptor` by excluding it or setting `spring.security.enabled=false` in a test-scoped `application.properties`, or by providing a no-op `HandlerInterceptor` override
    - Test 1 — missing `user` field → 400
    - Test 2 — `user.id` present but `title` null → 400 (validates the new guard)
    - Test 3 — `title` is blank string → 400
    - Test 4 — valid body with existing userId → stub `userRepo.findById()` to return a `User`, stub `toDoService.createToDo()` to return a `Todo`; assert 201 and that the response body's `title` matches
    - _Requirements: 1.3, 1.4, 6.1, 6.4_

- [ ] 5. Backend property-based tests
  - [ ]* 5.1 Write Property 3 test — unknown user returns 400, known user proceeds
    - File: `spring/todo/src/test/java/teambydefault/todo/ToDoControllerPropertyTest.java` (create new)
    - Use `@WebMvcTest(ToDoController.class)` + `@MockitoBean` for service and repo
    - Use `@Property` (jqwik) with `@ForAll UUID unknownId` — stub `userRepo.findById(unknownId)` to return `Optional.empty()`, send a POST with that `user.id`, assert 400
    - In a second `@Property`, supply a valid `@ForAll String title` (assume non-blank) and a seeded known userId — assert 201
    - **Property 3: Task creation validates user existence and returns 400 for unknown users**
    - **Validates: Requirements 1.3, 6.1, 6.2**

  - [ ]* 5.2 Write Property 4 test — round-trip field matching via `@SpringBootTest` + H2
    - File: `spring/todo/src/test/java/teambydefault/todo/ToDoRoundTripPropertyTest.java` (create new)
    - Annotate with `@SpringBootTest` and `@AutoConfigureMockMvc`; H2 is already on the test classpath and replaces SQLite via `application-test.properties`
    - Add `spring.datasource.url=jdbc:h2:mem:testdb` and `spring.jpa.hibernate.ddl-auto=create-drop` to `src/test/resources/application-test.properties` (create if missing); activate with `@ActiveProfiles("test")`
    - Use `@Property` with `@ForAll @AlphaChars @StringLength(min=1, max=50) String title` and optional description; seed one real `User` via `UserRepo.save()` in `@BeforeEach`
    - POST each generated task, assert 201 and that the response body's `title`, `description` match the inputs; then GET by userId and assert the task is present in the list
    - **Property 4: Task creation is a round-trip — persisted task matches submitted fields**
    - **Validates: Requirements 1.4, 2.1**

  - [ ]* 5.3 Write Property 5 test — user isolation for GET /task
    - File: `spring/todo/src/test/java/teambydefault/todo/ToDoIsolationPropertyTest.java` (create new)
    - Use `@SpringBootTest` + H2 + `@AutoConfigureMockMvc`; same `@ActiveProfiles("test")` setup as above
    - Use `@Property` with two generated sets of task titles (non-overlapping by construction — prefix each with the userId); seed two users
    - After inserting all tasks, GET each user's task list and assert no task belonging to user B appears in user A's results and vice versa
    - **Property 5: User isolation — GET /task returns only the requesting user's tasks**
    - **Validates: Requirements 2.3**

- [ ] 6. Checkpoint — verify all backend tests pass
  - Run `gradlew.bat test` from `spring/todo/` and ensure all tests pass. Ask the user if questions arise.

- [ ] 7. Frontend property-based tests
  - [ ]* 7.1 Write Property 1 test — `TaskService.create()` body correctness in `task-service.spec.ts`
    - File: `angular/src/app/services/task-service.spec.ts`
    - Import `fc` from `fast-check` and `provideHttpClientTesting`, `HttpTestingController` from `@angular/common/http/testing`
    - Configure `TestBed` with `provideHttpClient()` and `provideHttpClientTesting()`
    - Use `fc.assert(fc.property(fc.record({ title: fc.string({ minLength: 1 }), description: fc.string(), dueDate: fc.string({ minLength: 1 }), userId: fc.uuidV(4) }), ({ title, description, dueDate, userId }) => { ... }), { numRuns: 100 })`
    - Inside the property: call `service.create({ title, description, dueDate, user: { id: userId } })`.subscribe(); use `httpMock.expectOne(...)` to capture the request; assert `req.request.body.title === title`, `.description === description`, `.dueDate === dueDate`, `.user.id === userId`; flush an empty `Todo`
    - **Property 1: Request body contains all form fields**
    - **Validates: Requirements 1.1, 2.2**

  - [ ]* 7.2 Write Property 2 test — auth interceptor attaches token in `auth-interceptor` spec
    - File: `angular/src/app/interceptors/auth-interceptor.spec.ts` (create new)
    - Configure `TestBed` with `provideHttpClient(withInterceptors([authInterceptor]))` and `provideHttpClientTesting()`
    - Use `fc.assert(fc.property(fc.string({ minLength: 1 }), (token) => { ... }), { numRuns: 100 })`
    - Inside the property: set `localStorage.setItem('token', token)`; inject `HttpClient` and make a GET to `http://localhost:8080/task`; capture the request via `HttpTestingController`; assert `req.request.headers.get('Authorization') === 'Bearer ' + token`; flush `[]`
    - Clear `localStorage` after each run
    - **Property 2: Auth interceptor attaches the stored token**
    - **Validates: Requirements 1.2**

  - [ ]* 7.3 Write Property 6 test — form invalid when title or dueDate is empty in `home.spec.ts`
    - File: `angular/src/app/home-view/home.spec.ts`
    - Import `fc` from `fast-check`; add `provideHttpClientTesting()` and `provideHttpClient()` and `provideRouter([])` to the `TestBed` providers
    - Property A: `fc.assert(fc.property(fc.string(), (dueDate) => { component.addForm.setValue({ title: '', description: '', dueDate }); expect(component.addForm.invalid).toBeTrue(); }), { numRuns: 100 })`
    - Property B: `fc.assert(fc.property(fc.string(), (title) => { component.addForm.setValue({ title, description: '', dueDate: '' }); expect(component.addForm.invalid).toBeTrue(); }), { numRuns: 100 })`
    - Property C: `fc.assert(fc.property(fc.string({ minLength: 1 }), fc.string({ minLength: 1 }), (title, dueDate) => { component.addForm.setValue({ title, description: '', dueDate }); expect(component.addForm.valid).toBeTrue(); }), { numRuns: 100 })`
    - **Property 6: Form is invalid whenever title or dueDate is empty**
    - **Validates: Requirements 3.1, 3.2**

  - [ ]* 7.4 Write Property 7 test — successful save resets form and hides it in `home.spec.ts`
    - File: `angular/src/app/home-view/home.spec.ts` (extend the same describe block)
    - Use `fc.assert(fc.property(fc.string({ minLength: 1 }), fc.string({ minLength: 1 }), (title, dueDate) => { ... }), { numRuns: 100 })`
    - Spy on `authService.getUserId()` to return a fixed UUID; set form values to `{ title, description: '', dueDate }`; show the form (`component.showAddForm = true`); call `component.submitAdd()`; capture the POST via `HttpTestingController` and flush a valid `Todo`; assert `component.addForm.value.title === null` (or reset state) and `component.showAddForm === false`
    - **Property 7: Successful save resets the form and hides it**
    - **Validates: Requirements 4.1, 4.2**

  - [ ]* 7.5 Write Property 8 test — task list sorted ascending by due date in `home.spec.ts`
    - File: `angular/src/app/home-view/home.spec.ts` (extend the same describe block)
    - Use `fc.assert(fc.property(fc.array(fc.record({ dueDate: fc.date({ min: new Date('2000-01-01'), max: new Date('2099-12-31') }) }), { minLength: 0, maxLength: 20 }), (todos) => { const sorted = [...todos].sort((a, b) => new Date(a.dueDate).getTime() - new Date(b.dueDate).getTime()); for (let i = 0; i < sorted.length - 1; i++) { expect(new Date(sorted[i].dueDate).getTime()).toBeLessThanOrEqual(new Date(sorted[i+1].dueDate).getTime()); } }), { numRuns: 200 })`
    - This is a pure algorithmic property that does not require HTTP mocking
    - **Property 8: Task list is sorted ascending by due date**
    - **Validates: Requirements 4.3**

- [ ] 8. Frontend example-based tests for the AuthService fix and error paths
  - [ ]* 8.1 Extend `auth-service.spec.ts` with error-path examples
    - File: `angular/src/app/services/auth-service.spec.ts`
    - These extend the tests from task 2.2, specifically:
    - Test — `getUserId()` returns null when localStorage has no `userId` key (baseline guard for Requirement 5.2)
    - Test — `logout()` removes both `token` and `userId` from localStorage
    - _Requirements: 5.2, 5.3_

  - [ ]* 8.2 Extend `home.spec.ts` with form validation message examples
    - File: `angular/src/app/home-view/home.spec.ts`
    - Test — touching `title` and leaving it empty renders the text "Title is required." in the template
    - Test — touching `dueDate` and leaving it empty renders the text "Due date is required." in the template
    - Test — when `authService.getUserId()` returns null, `submitAdd()` sets `errorMessage` to "No logged-in user found." and does not send an HTTP request
    - Test — when the POST returns a 500, `submitAdd()` sets `errorMessage` to "Failed to create task. Please try again."
    - _Requirements: 3.3, 3.4, 5.1, 5.2_

- [ ] 9. Final checkpoint — all tests green
  - Run `gradlew.bat test` from `spring/todo/` and `ng test --watch=false` from `angular/` and ensure every test suite passes. Ask the user if questions arise.

---

## Notes

- Tasks marked with `*` are optional and can be skipped for faster MVP delivery
- Only tasks 2.1 and 4.1 modify production code — all other tasks are tests or dependency setup
- The frontend uses **Vitest** (not Karma/Jasmine) as configured in `package.json`; test files use `describe`/`it`/`expect` syntax compatible with both Vitest and Jasmine
- `fast-check` must be installed before any frontend property tests can run (task 1.1)
- `jqwik` must be added to `build.gradle.kts` before any backend property tests can run (task 1.2)
- The H2 test profile (`application-test.properties`) is needed for integration property tests (5.2, 5.3); the `@WebMvcTest` slice tests (4.2, 5.1) do not need it
- The `title` column has `unique=true` — backend property tests that insert multiple tasks must use distinct titles (e.g., prefix with a random UUID)
- The response body currently serializes the full `User` including `password`; this is a known out-of-scope issue documented in the design

## Task Dependency Graph

```json
{
  "waves": [
    { "id": 0, "tasks": ["1.1", "1.2"] },
    { "id": 1, "tasks": ["2.1", "4.1"] },
    { "id": 2, "tasks": ["2.2", "4.2"] },
    { "id": 3, "tasks": ["5.1", "5.2", "5.3", "7.1", "7.2", "7.3", "7.4", "7.5"] },
    { "id": 4, "tasks": ["8.1", "8.2"] }
  ]
}
```
