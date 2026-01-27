# YUMeat 📱

YUMeat è l’app pensata per aiutarti a costruire abitudini alimentari sane, consapevoli e sostenibili, senza stress da calorie o numeri.  
Traccia i tuoi pasti con facilità, ricevi supporto motivazionale e segui il tuo percorso in modo empatico e personalizzato.

---

## Funzionalità principali 🛠️

- **Aggiunta dei pasti**  
  Inserisci gli alimenti manualmente, scattando una foto o scegliendo dalla galleria. YUMeat riconosce i cibi e ti aiuta a registrarli rapidamente.

- **Modalità Safe**  
  Nasconde numeri e calorie, e offre un’esperienza qualitativa per chi desidera focalizzarsi sul benessere e sulle proprie sensazioni, senza pressioni né conteggi.

- **Consigli personalizzati**  
  L’assistente AI integrato risponde alle tue domande su alimentazione, benessere e motivazione con messaggi empatici e adattati alle tue preferenze.

- **Ricette consigliate**  
  Scopri ricette sane, gustose e in linea con i tuoi obiettivi e preferenze alimentari.

- **Gestione delle preferenze alimentari**  
  Personalizza la tua esperienza: scegli se sei onnivoro, vegetariano, vegano e indica eventuali alimenti da evitare.

- **Diario del benessere**  
  Scrivi come ti senti ogni giorno, scegli un’emoji che rappresenta il tuo stato d’animo e tieni traccia del tuo percorso personale.

- **Challenge e motivazione**  
  Accetta sfide giornaliere e settimanali per migliorare le tue abitudini e ricevi citazioni motivazionali.

- **Supporto e aiuto rapido**  
  Accedi rapidamente a numeri utili, contatti di fiducia e una sezione di emergenza.

---

## Tecnologie utilizzate 🧠

- **Kotlin + Jetpack Compose**: interfaccia moderna e reattiva
- **Retrofit + Gson**: comunicazione con l’API di OpenAI per la chat AI
- **Android Architecture Components**: StateFlow, ViewModel, Navigation, ecc.
- **Material Design 3**

---

## Come iniziare 🏁

1. **Clona la repository**
   ```sh
   git clone https://github.com/Carm1neBread/YUMeat_25.git
   ```
2. **Aggiungi la tua API Key di OpenAI**
   - Crea un file `local.properties` nella root del progetto:
     ```
     OPENAI_API_KEY=sk-xxxxx
     ```
3. **Apri il progetto con Android Studio**
4. **Esegui l’app su un dispositivo o emulatore**

---

## Struttura del progetto 📁

- `data/`  
  Gestione dei dati, repository, modelli, chiamate API
- `ui/screens/`  
  Tutte le schermate dell'app divise per funzionalità
- `ui/navigation/`  
  Gestione delle rotte di navigazione tra le schermate
- `ui/theme/`  
  Temi e colori dell’app

---

## Autori

- [Cataldo Carmine](https://github.com/Carm1neBread)
