<p align="center">
  <img src="web-advertise/favicon.jpeg" alt="OLAGU Symbol" width="160" />
</p>

# OLAGU


OLAGU is an offline-first Android application designed for fully on-device artificial intelligence inference, document parsing, and retrieval-augmented generation (RAG). Operating entirely on local hardware, OLAGU ensures total data privacy, deterministic execution, and complete independence from cloud services and network connectivity .

#### For contributors who like to test in local, use 

./gradlew installDebug && adb shell monkey -p com.olagu.ai 1

### after connecting your phone through usb


## Current Project Structure

```text
OLAGU/
│
├── app/
│   └── src/main/
│       ├── AndroidManifest.xml
│       │
│       ├── java/com/olagu/ai/
│       │   │
│       │   ├── MainActivity.kt
│       │   │
│       │   ├── navigation/
│       │   │
│       │   ├── ui/
│       │   │   ├── screens/
│       │   │   ├── components/
│       │   │   └── theme/
│       │   │
│       │   ├── data/
│       │   │   ├── model/
│       │   │   ├── local/
│       │   │   └── repository/
│       │   │
│       │   ├── document/
│       │   │   ├── picker/
│       │   │   └── parser/
│       │   │
│       │   ├── engine/
│       │   │   ├── runtime/
│       │   │   ├── embeddings/
│       │   │   └── rag/
│       │   │
│       │   ├── storage/
│       │   ├── security/
│       │   └── settings/
│       │
│       └── res/
│
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── gradlew
```
# Contributors keep in mind 
## Development stages 
```text
Stage 1	Complete frontend
Stage 2	File importing and document processing
Stage 3	Local storage and history
Stage 4	Offline AI engine
Stage 5	Embeddings and RAG
Stage 6	Security, optimization and testing
```