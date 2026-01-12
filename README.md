# meilisearch-poc

POC for Meilisearch synonyms + query tests using Scala (cats-effect + sttp).

## Setup

1) Start Meilisearch with Docker

```sh
docker run -it --rm \
  -p 7700:7700 \
  -v $(pwd)/meili_data:/meili_data \
  getmeili/meilisearch:v1.16
```

2) Files to edit

- `src/main/resources/synonyms.json`
  - JSON array of entries with `thai_term` and `synonyms`.
  - Example shape:
    ```json
    [
      { "thai_term": "TERM_A", "synonyms": ["SYN_A1", "SYN_A2"] }
    ]
    ```

- `src/main/resources/mock_docs.json`
  - JSON array of documents to ingest. One doc per `thai_term` is recommended.
  - Fields: `id`, `title`, `category`, `content`, `popularity`, `updated_at`, `tags`.
  - Example shape:
    ```json
    [
      {
        "id": "syn-1",
        "title": "TERM_A",
        "category": "boi",
        "content": "Doc for TERM_A",
        "popularity": 100,
        "updated_at": 1737000001,
        "tags": ["synonym", "thai_term"]
      }
    ]
    ```

- `src/main/resources/synonym_tests.json`
  - JSON array of test cases.
  - Fields: `name`, `query`, `expectation`, `expectTerm`, `minHits`, `maxHits`.
  - Example shape:
    ```json
    [
      {
        "name": "positive-01",
        "query": "SYN_A1",
        "expectation": "should include 'TERM_A'",
        "expectTerm": "TERM_A",
        "minHits": 1,
        "maxHits": 10
      }
    ]
    ```

## Run

```sh
sbt run
```

The app will:
- Create the index if it does not exist.
- Apply settings (including synonyms).
- Ingest documents from `mock_docs.json`.
- Run all tests from `synonym_tests.json` and print a table with status, input, and output.
- Save the latest table to `test_results.md` (overwritten on each run).

### Sample output

```text
+--------+----------------------+------------------------------------------+---------------------------+----------------------------------------------+
| STATUS | TEST                 | INPUT                                    | EXPECT                    | OUTPUT                                       |
+--------+----------------------+------------------------------------------+---------------------------+----------------------------------------------+
| PASS   | positive-01          | การสนับสนุนการลงทุน                      | should include 'การส่งเสริมการลงทุน' | hits=1 titles=การส่งเสริมการลงทุน             |
| PASS   | positive-06          | Strategic Talent Center (STC)            | should include 'ช่างฝีมือและผู้ชำนาญการ' | hits=1 titles=ช่างฝีมือและผู้ชำนาญการ         |
| FAIL   | negative-01          | nonexistent-zzz-12345                    | should return 0 hits       | hits=0 titles=<none>                         |
+--------+----------------------+------------------------------------------+---------------------------+----------------------------------------------+
```

### Optional env vars

- `MEILI_URL` (default `http://127.0.0.1:7700`)
- `MEILI_KEY` (default `masterKey`)
- `MEILI_INDEX` (default `docs`)
- `MEILI_PK` (default `id`)
- `MEILI_SYNONYMS_PATH` (default `src/main/resources/synonyms.json`)
- `MEILI_DOCS_PATH` (default `src/main/resources/mock_docs.json`)
- `MEILI_TESTS_PATH` (default `src/main/resources/synonym_tests.json`)
- `MEILI_TEST_RESULTS_PATH` (default `test_results.md`)
