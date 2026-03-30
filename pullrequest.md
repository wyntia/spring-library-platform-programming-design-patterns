# Pull Request – SRP + OCP (Lab4)

## Cel zmian
W ramach zadania zostały wdrożone refaktoryzacje pod:
- **SRP (Single Responsibility Principle)** – rozdzielenie odpowiedzialności usług,
- **OCP (Open/Closed Principle)** – dwa jawne podejścia:
  1. **przez abstrakcję**,
  2. **przez sterowanie danymi**.

W zmianach zachowano istniejące implementacje wzorców oznaczone komentarzami `//LabX ...` (bez usuwania markerów).

---

## 1) Single Responsibility Principle (SRP)

## SRP 1 – `user-service`

### Problem przed refaktorem
`UserService` łączył wiele odpowiedzialności naraz:
- CRUD profilu użytkownika,
- logika bezpieczeństwa (hasła, reset, walidacja credentials),
- logika przypisań bibliotek/oddziałów,
- publikacja zdarzeń.

### Rozwiązanie
Rozdzielono obowiązki do wyspecjalizowanych klas:
- `user-service/src/main/java/org/pollub/user/service/UserProfileService.java`
- `user-service/src/main/java/org/pollub/user/service/UserSecurityService.java`
- `user-service/src/main/java/org/pollub/user/service/UserBranchService.java`
- `user-service/src/main/java/org/pollub/user/service/UserEventPublisher.java`
- `user-service/src/main/java/org/pollub/user/facade/UserFacade.java` (warstwa orkiestracji)

Dodatkowo:
- `UpdateUserCommand` został przepięty na `UserFacade`.
- usunięto legacy:
  - `user-service/src/main/java/org/pollub/user/service/UserService.java`
  - `user-service/src/main/java/org/pollub/user/service/IUserService.java`

### Efekt SRP
Każda klasa ma jeden główny powód do zmiany:
- profil,
- bezpieczeństwo,
- relacje z oddziałem,
- event publishing,
- orkiestracja.

---

## SRP 2 – `branch-service`

### Problem przed refaktorem
`BranchService` łączył odczyt, modyfikacje, wyszukiwanie i integrację z user-service.

### Rozwiązanie
Rozdzielono odpowiedzialności do:
- `branch-service/src/main/java/org/pollub/branch/service/BranchQueryService.java`
- `branch-service/src/main/java/org/pollub/branch/service/BranchCommandService.java`
- `branch-service/src/main/java/org/pollub/branch/service/BranchSearchService.java`
- `branch-service/src/main/java/org/pollub/branch/service/BranchEmployeesService.java`
- `branch-service/src/main/java/org/pollub/branch/facade/BranchFacade.java`

### Efekt SRP
Separacja read/write/search/integration ogranicza sprzężenie i zmniejsza zakres zmian przy rozwoju funkcji.

---

## SRP 3 – `feedback-service`

### Problem przed refaktorem
Monolityczny `FeedbackService` łączył:
- przyjmowanie feedbacku,
- rate limiting,
- moderację,
- powiadomienia,
- eventy.

### Rozwiązanie
Rozdzielono odpowiedzialności do:
- `feedback-service/src/main/java/org/pollub/feedback/service/FeedbackSubmissionService.java`
- `feedback-service/src/main/java/org/pollub/feedback/service/FeedbackRateLimitService.java`
- `feedback-service/src/main/java/org/pollub/feedback/service/FeedbackModerationService.java`
- `feedback-service/src/main/java/org/pollub/feedback/service/FeedbackNotificationService.java`
- `feedback-service/src/main/java/org/pollub/feedback/service/FeedbackEventPublisher.java`
- `feedback-service/src/main/java/org/pollub/feedback/facade/FeedbackFacade.java`

### Efekt SRP
Każdy obszar (`submit`, `rate-limit`, `moderation`, `notification`, `events`) jest rozwijany niezależnie.

---

## 2) Open/Closed Principle (OCP)

Poniżej opisano oba podejścia: abstrakcja + sterowanie danymi.

## OCP 1 – wyszukiwanie użytkowników (`user-service`)

### A) Podejście przez abstrakcję
Zamiast sztywnego tworzenia konkretnych expression w serwisie, użyto fabryk przez interfejs:
- `user-service/src/main/java/org/pollub/user/service/search/UserSearchExpressionFactory.java`
- `.../UsernameSearchExpressionFactory.java`
- `.../EmailSearchExpressionFactory.java`
- `.../NameSearchExpressionFactory.java`
- `.../SurnameSearchExpressionFactory.java`

`UserProfileService.searchUsers(...)` buduje listę kryteriów z listy fabryk DI.

**Efekt OCP (abstrakcja):**
Nowe kryterium dodaje się przez nową implementację fabryki, bez modyfikacji logiki serwisu.



## OCP 2 – notyfikacje feedbacku (`feedback-service`)

### A) Podejście przez abstrakcję
`FeedbackNotificationService` zależy od interfejsu handlera:
- `feedback-service/src/main/java/org/pollub/feedback/service/FeedbackNotificationHandler.java`
- `feedback-service/src/main/java/org/pollub/feedback/service/FeedbackNotificationService.java`

Domyślna implementacja:
- `feedback-service/src/main/java/org/pollub/feedback/service/DecoratedFeedbackNotificationHandler.java`

**Efekt OCP (abstrakcja):**
Nowy kanał notyfikacji = nowa implementacja handlera, bez zmian w `FeedbackNotificationService`.

### B) Podejście przez sterowanie danymi
W `DecoratedFeedbackNotificationHandler` łańcuch Decorator+Composite jest budowany na podstawie danych konfiguracyjnych:
- `feedback-service/src/main/java/org/pollub/feedback/service/FeedbackNotificationDataDrivenProperties.java`
- `feedback-service/src/main/resources/application.yml` (`feedback.notification.*`)

Konfigurowane są m.in.:
- kanały (`email`, `sms`),
- odbiorcy,
- nagłówek/stopka,
- flaga logowania.

**Efekt OCP (data-driven):**
Zmiana routingów notyfikacji odbywa się przez dane, bez modyfikacji kodu serwisu orkiestrującego.

---

## OCP 3 – feedback (sanitacja + limity + moderacja)

### A) Podejście przez abstrakcję
Sanitacja została odseparowana do polityki:
- `feedback-service/src/main/java/org/pollub/feedback/service/FeedbackSanitizationPolicy.java`
- `feedback-service/src/main/java/org/pollub/feedback/service/VisitorFeedbackSanitizationPolicy.java`
- użycie w `FeedbackSubmissionService`

Wzorzec Visitor (`SecuritySanitizationVisitor` + `feedback.accept(...)`) pozostał zachowany.

**Efekt OCP (abstrakcja):**
Nowa polityka sanitacji = nowa implementacja interfejsu, bez zmiany `FeedbackSubmissionService`.

### B) Podejście przez sterowanie danymi
Rate-limiting oparto o słowniki reguł w kodzie (data-driven w warstwie domenowej):
- `feedback-service/src/main/java/org/pollub/feedback/service/FeedbackRateLimitRuleDictionary.java`
- `feedback-service/src/main/java/org/pollub/feedback/service/FeedbackRateLimitService.java`

Reguły są wybierane krokowo na podstawie danych runtime:
1. kontekst auth z `SecurityContext`,
2. rozróżnienie `GUEST` vs `AUTHENTICATED`,
3. nadpisanie przez regułę roli (`ROLE_*`) gdy istnieje.

Dodatkowo moderacja korzysta z danych reguł (`resolveAtStatuses`):
- `feedback-service/src/main/java/org/pollub/feedback/service/FeedbackModerationDataDrivenProperties.java`
- `feedback-service/src/main/java/org/pollub/feedback/service/FeedbackModerationService.java`

W `FeedbackModerationService` (OCP3) reguła ustawiania `resolvedAt` pochodzi z danych `feedback.moderation.resolve-at-statuses`
udostępnianych przez `FeedbackModerationDataDrivenProperties`, zamiast hardcoded warunku `RESOLVED || DISMISSED`.

**Efekt OCP (data-driven):**
Dobór limitu i części reguł moderacji jest sterowany danymi, bez zmian API i bez ingerencji w logikę wywołań kontrolera/proxy.

---

## OCP 4 – cykl życia kopii (`catalog-service`)

### A) Podejście przez sterowanie danymi (domenowe, bez config)
W `catalog-service` logika przejść statusów kopii została przeniesiona do reguł data-driven:
- `catalog-service/src/main/java/org/pollub/catalog/service/InventoryOperation.java`
- `catalog-service/src/main/java/org/pollub/catalog/service/InventoryTransitionRuleDictionary.java`
- `catalog-service/src/main/java/org/pollub/catalog/service/InventoryTransitionPolicy.java`

`BranchInventoryService` korzysta z `InventoryTransitionPolicy` zamiast hardcoded warunków statusowych w operacjach:
- `rentCopy`, `returnCopy`, `reserveCopy`, `cancelReservation`, `extendRental`

### B) Adaptacja istniejącego wzorca (State)
Nie usunięto wzorca State – został zaadaptowany:
- `catalog-service/src/main/java/org/pollub/catalog/state/CopyStateFactory.java`

`CopyStateFactory` nadal tworzy implementacje `CopyState`, ale wybór stanu odbywa się przez data-driven rejestr
`Map<CopyStatus, Supplier<CopyState>>` zamiast hardcoded `switch`.

**Efekt OCP:**
Nowe operacje/przejścia i mapowanie stanów rozszerza się przez dane (reguły/rejestr),
bez modyfikowania głównej logiki serwisu i bez usuwania istniejących wzorców projektowych.

---