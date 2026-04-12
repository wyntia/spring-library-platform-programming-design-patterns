# Pull request — opis zmian (Lab 9)

## Celowo zła klasa ~100 linii (5 pkt.)

### Co robi feature (biznesowo)

Endpoint `GET /api/feedback/admin/text-digest` (ADMIN/LIBRARIAN) zwraca **jeden blok tekstu** (`text/plain`) z listą zgłoszeń feedbacku — wygodne do skopiowania (mail, notatka). Opcjonalnie: filtr `status`, limit `maxLines` (domyślnie 50).

### Klasa `FeedbackIngressMixer`

- Pakiet: `org.pollub.feedback.digest`, bean **`@Component`** (świadomie nie „service” z interfejsem).
- **Anty-wzorce (celowe):** mieszanka repozytorium + składanie tekstu + walidacja limitów + logi w jednej klasie; długa metoda `buildWholeThing` z **6 parametrami**; pola/metody typu `tmp`, `buf`, `go`, `x`; nadmiar komentarzy; nazwa klasy ogólnikowa („Ingress”, „Mixer”).
- Markery: `//Lab9 : Celowo zła klasa (anty-wzorce) Start` / `Stop`.

### Integracja

- `FeedbackController` — cienka delegacja do `buildWholeThing(...)`.

### Branch i commit

- Branch: **`Lab9`**
- Commit: `Lab9: FeedbackIngressMixer (celowo zła klasa)`
