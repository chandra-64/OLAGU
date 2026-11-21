# OLAGU


OLAGU is an offline-first Android application designed for fully on-device artificial intelligence inference, document parsing, and retrieval-augmented generation (RAG). Operating entirely on local hardware, OLAGU ensures total data privacy, deterministic execution, and complete independence from cloud services and network connectivity .

## Project Structure

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
