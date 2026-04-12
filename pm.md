# Pull request — opis zmian (Lab 6)

## Znaczące nazewnictwo (zadanie 1, 2 pkt / 2 przykłady)

### Cel

Ujednolicenie nazw klas, metod i parametrów tak, aby odzwierciedlały rzeczywiste pojęcia domenowe i nie wprowadzały synonimów dla tej samej operacji lub typu.

### Przykład 1 — „rental”, nie „loan”

W **rental-service** operacja przedłużenia wypożyczenia była nazwana `extendLoan`, podczas gdy reszta modułu (np. `CatalogServiceClient.extendRental`) używa pojęcia **rental**. Zmieniono nazewnictwo na spójne z domeną:

- `IRentalService.extendLoan` → `extendRental`
- `RentalService` — implementacja oraz prywatna metoda pomocnicza `extendLoanRecord` → `extendRentalRecord`
- `RentalController` — metoda obsługująca `PUT /api/rentals/{itemId}/extend` → `extendRental` (ścieżka REST bez zmian)

Otoczenie refaktoru markerami: `//Lab6 : Znaczące nazewnictwo 1 Start` / `Stop`.

**Frontend:** bez zmian — klient HTTP nadal wywołuje ten sam URL; zmiana dotyczy wyłącznie nazw w kodzie Java.

### Przykład 2 — parametr zgodny z typem

W fabrykach użytkownika parametr metody `createUser(User …)` nazywał się `userDto`, mimo że typem jest encja `User`. Zmieniono na `user` w:

- `IUserFactory`
- `UserFactory`, `AdminUserFactory`, `LibrarianUserFactory`

Otoczenie refaktoru markerami: `//Lab6 : Znaczące nazewnictwo 2 Start` / `Stop`.

### Commit

- `Refactor: Znaczące nazewnictwo`

---

## Długość metod (zadanie 2, 3 pkt / 3 przykłady)

### Cel

Metody publiczne wybrane do refaktoru mają co najwyżej **20 linii** ciała (od `{` do `}`); logika przeniesiona do prywatnych metod pomocniczych. **Zachowano** istniejące komentarze i markery wcześniejszych labów (np. Lab5 Mediator w `RentalService`, `//l2 Adapter usage` w `AuthService`, Lab4 OCP w `BranchInventoryService`).

### Przykład 1 — `RentalService.rentItem`

Wydzielono: `buildRentalHistoryForRent`, `saveNewRentalAndNotifyCreated`, `sendRentalConfirmationSafely` (w tym blok `//Lab5 Mediator Start` / `End` przy wysyłce potwierdzenia). Markery: `//Lab6 : Długość metod 1 Start` / `Stop`.

### Przykład 2 — `AuthService.register`

Wydzielono: `buildAddressFromRegisterRequest`, `buildNewUserDtoForRegistration`, `buildAuthResponseForRegisteredUser`. Markery: `//Lab6 : Długość metod 2 Start` / `Stop`.

### Przykład 3 — `BranchInventoryService.rentCopy`

Wydzielono: `applyRentDataAndClearReservation`, `persistRentedCopyAndBuildResponse`, `notifyRentObservers`, `fulfillReservationIfWasReserved`, `buildRentReservationResponse` (z zachowaniem komentarzy przy observerze i `fulfillReservation`). Markery: `//Lab6 : Długość metod 3 Start` / `Stop`.

### Commit

- `Refactor: Długość metod (max 20 linii)`

---

## Jedna rola funkcji / SRP (zadanie 3, 3 pkt / 3 przykłady)

### Cel

Rozdzielenie odpowiedzialności w wybranych metodach: krótka metoda publiczna jako orkiestracja, prywatne metody z jedną rolą (domena, persystencja/observer, integracja z mediatorem, uwierzytelnianie, diagnostyka, budowa odpowiedzi itd.). Zachowane komentarze i markery wcześniejszych labów (`L6`, `Lab5`, `L3 Iterator`).

### Przykład 1 — `RentalService.returnItem`

Wydzielono: `applyReturnDomainTransition` (walidacja stanu + pola zwrotu), `persistReturnedRentalAndNotifyObservers` (zapis + zdarzenie `RETURNED`), `finalizeReturnIntegration` (`MarkAsReturnedRequest` + blok Lab5 przy `SendReturnConfirmationNotification`). Markery: `//Lab6 : Jedna rola funkcji 1 Start` / `Stop`.

### Przykład 2 — `AuthService.login`

Wydzielono: `validateLoginCredentials`, `logLoginDebugHints`, `createAccessTokenForUser`, `buildAuthResponseForLogin`, `logAuthResponseDebugHint` — bez zmiany kolejności logów i komunikatów. Markery: `//Lab6 : Jedna rola funkcji 2 Start` / `Stop`.

### Przykład 3 — `ReservationService.cleanupExpiredReservations`

Pętla z iteratorem L3 wywołuje: `expireReservationRecord` (walidacja stanu + zapis wygasłej rezerwacji), `publishExpiredReservationIntegrationEvents` (observer + `UpdateCatalogStatusRequest`). Markery: `//Lab6 : Jedna rola funkcji 3 Start` / `Stop`.

### Commit

- `Refactor: Jedna rola funkcji (SRP)`

---

## Poziom abstrakcji top-down (zadanie 4, 3 pkt / 3 przykłady)

### Cel

Orkiestracja na **jednym poziomie abstrakcji**: metoda publiczna opisuje kroki domenowe; szczegóły (DTO, `getDueDate()`, `mediator.send` z konstrukcją żądania) schodzą do głębszych metod prywatnych. Zachowane wcześniejsze markery Lab6 (długość metod, nazewnictwo) oraz `L4 OCP` przy fabrykach wyszukiwania.

### Przykład 1 — `RentalService.rentItem`

Dodano `notifyUserOfRentalConfirmation` oraz `completeRentalInCatalog`, żeby `rentItem` nie mieszał szczegółów z sąsiednimi krokami. Markery: `//Lab6 : Poziom abstrakcji 1 Start` / `Stop` (wewnątrz bloku długości metod 1).

### Przykład 2 — `RentalService.extendRental`

Kroki: `validateRentalExtensionAllowed`, `applyRentalDueDateExtension`, `persistExtendedRentalAndNotifyObservers`, `syncCatalogAfterRentalExtension`. Markery: `//Lab6 : Poziom abstrakcji 2 Start` / `Stop` wewnątrz `//Lab6 : Znaczące nazewnictwo 1`; `extendRentalRecord` pozostaje w bloku nazewnictwa.

### Przykład 3 — `UserProfileService.searchUsers`

Wydzielono: `loadAllUsersForSearch`, `buildCombinedSearchExpression` (z blokiem `// L4 - OCP 1`), `executeUserSearch`. Markery: `//Lab6 : Poziom abstrakcji 3 Start` / `Stop`.

### Commit

- `Refactor: Poziom abstrakcji (top-down)`

---

## Maksymalnie 3 argumenty (zadanie 5, 3 pkt / 3 przykłady)

### Cel

Metody przyjmują co najwyżej **3 parametry**; dłuższe listy zastąpione rekordami kontekstu. Wybrane miejsca: **catalog-service** (wypożyczenie egzemplarza, aktualizacja stanu wypożyczenia) oraz **user-service** (publikacja zdarzenia po usunięciu użytkownika).

### Przykład 1 — `BranchInventoryService` (rent copy)

Rekordy `RentCopyParties`, `PendingRentPersistence`; `persistRentedCopyAndBuildResponse(PendingRentPersistence)`, `notifyRentObservers(RentCopyParties)`, `fulfillReservationIfWasReserved(boolean, RentCopyParties)`, `buildRentReservationResponse(RentCopyParties, RentalHistoryDto)`. `applyRentDataAndClearReservation` — 3 argumenty. Markery: `//Lab6 : Maksymalnie 3 argumenty 1 Start` / `Stop`.

### Przykład 2 — `BranchInventoryService` (stan egzemplarza)

Rekord `RentAssignmentDetails`; `updateInventoryRecordWithRentData(BranchInventory, CopyStatus, RentAssignmentDetails)` — 3 argumenty (wcześniej 5). Markery: `//Lab6 : Maksymalnie 3 argumenty 2 Start` / `Stop`.

### Przykład 3 — `UserEventPublisher` + `UserProfileService.deleteUser`

Rekord [UserEventSnapshot](c:/Users/Black/Desktop/spring-library-platform-programming-design-patterns/user-service/src/main/java/org/pollub/user/dto/UserEventSnapshot.java); `publish(String eventType, UserEventSnapshot snapshot, String message)` zamiast pięciu parametrów primitivów. Markery: `//Lab6 : Maksymalnie 3 argumenty 3 Start` / `Stop` w `UserEventPublisher`.

### Commit

- `Refactor: Maksymalnie 3 argumenty`

---

## Wyjątki zamiast kodów błędów (zadanie 6, 3 pkt / 3 odrębne przykłady)

### Cel

Zamiast sygnalizować błąd wartością zwracaną (`success: false` w DTO, `Optional.empty()` przy dowolnym błędzie HTTP, `null` z klienta), warstwa serwisu/kilent rzuca wyjątki; tam gdzie kontrakt HTTP musi pozostać bez zmian, dedykowany `@RestControllerAdvice` mapuje wyjątek z powrotem na ten sam body/status co wcześniej.

### Przykład 1 — reset hasła (user-service + auth-service)

- `UserSecurityService.resetPassword`: przy niezgodności email+PESEL — `PasswordResetIdentityNotVerifiedException`; `PasswordResetExceptionHandler` zwraca **HTTP 200** + `ResetPasswordResponseDto` z `success=false` i dotychczasowym komunikatem (WebClient w auth nadal deserializuje odpowiedź).
- `AuthService.resetPassword`: pusty `Optional` z klienta — `PasswordResetUserServiceFailedException`; błąd wysyłki maila — `PasswordResetEmailDeliveryException`; `PasswordResetExceptionHandler` w auth-service zwraca te same DTO co wcześniej.

Markery: `//Lab6 : Wyjątki zamiast kodów błędów — przykład 1 (reset hasła)`.

### Przykład 2 — `BranchServiceClient` (user-service)

404 z branch-service → nadal `Optional.empty()`; inne błędy (w tym sieć) → `ServiceException` zamiast milczącego `Optional.empty()`. Uwaga w `CachingBranchServiceProxy` i `UserBranchService.getEmployeeBranch`, że awaria usługi propaguje wyjątek.

Markery: `//Lab6 : Wyjątki zamiast kodów błędów — przykład 2`.

### Przykład 3 — email użytkownika (rental-service)

`UserServiceClient.getUserEmail` rzuca `ServiceException`, gdy nie da się pobrać adresu (wcześniej zwracał `null` i handlery robiły `if (email == null)`). Mediator wywołuje `GetUserEmailHandler`, który woła ten klient — wyjątek leci do góry. **Obsługa** „nie ma emaila → zaloguj ostrzeżenie i nie wysyłaj maila” była najpierw w każdym z trzech handlerów (`try/catch`); w **zadaniu 7 (DRY)** ten sam `try/catch` jest **w jednym miejscu** — `NotificationUserEmailResolver` (handlery tylko przekazują własny komunikat `log.warn` jako `Runnable`).

Markery: `//Lab6 : Wyjątki zamiast kodów błędów — przykład 3` (klient); DRY — resolver i handlery (zadanie 7).

### Commit

- `Refactor: Wyjątki zamiast kodów błędów (Lab6)`

---

## Brak powtórzeń / DRY (zadanie 7, 1 pkt)

### Cel

Jeden wspólny fragment logiki zamiast trzech kopii w kodzie (zasada **DRY** — *Don’t Repeat Yourself*).

### Co to robi w praktyce (łańcuch wywołań)

Powiadomienia e-mail (przypomnienie o terminie, potwierdzenie wypożyczenia, potwierdzenie zwrotu) potrzebują **adresu użytkownika** z **user-service**.

1. Handler (np. `RentalConfirmationHandler`) woła `notificationUserEmailResolver.resolveEmail(userId, ...)`.
2. Resolver przez **mediator** wysyła `GetUserEmailRequest` → wykonuje się `GetUserEmailHandler` → ten woła [UserServiceClient.getUserEmail](c:/Users/Black/Desktop/spring-library-platform-programming-design-patterns/rental-service/src/main/java/org/pollub/rental/client/UserServiceClient.java) (HTTP do user-service).
3. Gdy coś pójdzie nie tak (brak użytkownika, błąd sieci, pusty email), klient rzuca **`ServiceException`** (to jest efekt **zadania 6**, przykład 3 — błąd jako wyjątek, nie jako `null`).
4. **Resolver** łapie `ServiceException`, wywołuje przekazany **`Runnable`** (w praktyce lambda z `log.warn` z handlera), zwraca **`Optional.empty()`**.
5. Handler widzi pusty `Optional`, robi `return null` i **nie** wysyła maila — tak samo jak wcześniej po `try/catch` albo po `email == null`.

**Efekt biznesowy:** bez zmian — nadal „albo mamy email i wysyłamy powiadomienie, albo logujemy ostrzeżenie i kończymy”.

### Wcześniej vs teraz

| | **Wcześniej (po zadaniu 6, przed DRY)** | **Teraz (zadanie 7)** |
|---|----------------------------------------|------------------------|
| Gdzie jest `try/catch (ServiceException)` | W **każdym** z trzech handlerów osobno (ta sama struktura skopiowana 3×) | **Raz** w `NotificationUserEmailResolver` |
| Skąd handler bierze email | `try { mediator.send(new GetUserEmailRequest(...)) } catch ...` | `resolveEmail(...)` → `Optional`; jeśli pusty → `return null` |
| Logi | Każdy handler miał własny `log.warn` w swoim `catch` | Ten sam tekst logu co wcześniej, ale przekazany do resolwera jako **`Runnable`**, żeby nie scalać trzech różnych zdań w jedną wspólną wiadomość |

### Po co `Runnable` w `resolveEmail`?

Trzy handlery muszą nadal pisać **inne** komunikaty (np. „skipping **rental** confirmation” vs „skipping **return** confirmation”). Wspólna metoda nie może na sztywno wypisać jednego stringa — dlatego handler przekazuje **co zalogować przy błędzie** jako `Runnable` (najczęściej `() -> log.warn("...", ...)`).

### Pliki

- [NotificationUserEmailResolver.java](c:/Users/Black/Desktop/spring-library-platform-programming-design-patterns/rental-service/src/main/java/org/pollub/rental/mediator/support/NotificationUserEmailResolver.java) — wspólna obsługa `ServiceException` + `Optional`.
- [OverdueReminderHandler.java](c:/Users/Black/Desktop/spring-library-platform-programming-design-patterns/rental-service/src/main/java/org/pollub/rental/mediator/handler/OverdueReminderHandler.java), [RentalConfirmationHandler.java](c:/Users/Black/Desktop/spring-library-platform-programming-design-patterns/rental-service/src/main/java/org/pollub/rental/mediator/handler/RentalConfirmationHandler.java), [ReturnConfirmationHandler.java](c:/Users/Black/Desktop/spring-library-platform-programming-design-patterns/rental-service/src/main/java/org/pollub/rental/mediator/handler/ReturnConfirmationHandler.java) — wywołanie resolwera zamiast powielonego `try/catch`.

Markery: `//Lab6 : Brak powtórzeń (DRY) Start` / `Stop`.

### Commit

- `Refactor: Brak powtórzeń w kodzie (DRY)`

---

## Eliminacja magicznych liczb (zadanie 8, 1 pkt.)

### Cel

Zastąpienie literałów w kodzie nazwanymi abstrakcjami (stała, `TimeUnit`, `HttpStatus`), żeby **intencja** była czytelna bez zgadywania znaczenia liczby.

### Zmiany

1. **auth-service — [AuthService](c:/Users/Black/Desktop/spring-library-platform-programming-design-patterns/auth-service/src/main/java/org/pollub/auth/service/AuthService.java):** `expiresIn` — zamiast `/ 1000` metoda `accessTokenExpiresInSeconds()` z `TimeUnit.MILLISECONDS.toSeconds(jwtTokenProvider.getExpirationMs())` (sekundy z ważności JWT bez magicznej „tysiączki”).
2. **user-service — [BranchServiceClient](c:/Users/Black/Desktop/spring-library-platform-programming-design-patterns/user-service/src/main/java/org/pollub/user/client/BranchServiceClient.java):** rozpoznanie braku oddziału — `status.isSameCodeAs(HttpStatus.NOT_FOUND)` zamiast `== 404`.
3. **feedback-service — [AbstractFeedbackModerator](c:/Users/Black/Desktop/spring-library-platform-programming-design-patterns/feedback-service/src/main/java/org/pollub/feedback/service/moderator/AbstractFeedbackModerator.java):** próg długości treści — `MIN_CONTENT_LENGTH_CHARS` zamiast literału `5`.

Markery: `//Lab6 : Magiczne liczby Start` / `Stop`.

### Commit

- `Refactor: Eliminacja magicznych liczb (Lab6)`
