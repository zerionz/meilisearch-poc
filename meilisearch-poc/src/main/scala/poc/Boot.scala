package poc


import cats.effect.{IO, IOApp}
import sttp.client3._
import sttp.client3.circe._
import io.circe._
import io.circe.syntax._
import io.circe.parser._
import io.circe.generic.auto._

import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets
import scala.concurrent.duration._
import scala.jdk.CollectionConverters._

object Boot extends IOApp.Simple {
  println("Booting up the MeiliSearch POC application...")

  // ----------------------------
  // Config (env-driven)
  // ----------------------------
  final case class Config(
                           meiliUrl: String,
                           meiliKey: String,
                           indexUid: String,
                           primaryKey: String,
                           synonymsPath: String,
                           docsPath: String,
                           testsPath: String,
                           resultsPath: String
                         )

  private def loadConfig: IO[Config] =
    IO {
      Config(
        meiliUrl = sys.env.getOrElse("MEILI_URL", "http://127.0.0.1:7700"),
        meiliKey = sys.env.getOrElse("MEILI_KEY", "masterKey"),
        indexUid = sys.env.getOrElse("MEILI_INDEX", "docs"),
        primaryKey = sys.env.getOrElse("MEILI_PK", "id"),
        synonymsPath = sys.env.getOrElse("MEILI_SYNONYMS_PATH", "src/main/resources/synonyms.json"),
        docsPath = sys.env.getOrElse("MEILI_DOCS_PATH", "src/main/resources/mock_docs.json"),
        testsPath = sys.env.getOrElse("MEILI_TESTS_PATH", "src/main/resources/synonym_tests.json"),
        resultsPath = sys.env.getOrElse("MEILI_TEST_RESULTS_PATH", "test_results.md")
      )
    }

  // ----------------------------
  // Domain model for docs
  // ----------------------------
  final case class Doc(
                        id: String,
                        title: String,
                        category: String,
                        content: String,
                        popularity: Int,
                        updated_at: Long,
                        tags: List[String]
                      )

  final case class SynonymEntry(thai_term: String, synonyms: List[String])

  final case class SynonymTestCase(
                                    name: String,
                                    query: String,
                                    expectTerm: Option[String],
                                    minHits: Int,
                                    maxHits: Int
                                  )

  // ----------------------------
  // Meilisearch Task response (minimal)
  // ----------------------------
  final case class TaskError(message: String, code: String, `type`: String, link: Option[String])
  final case class Task(uid: Long, indexUid: Option[String], status: String, `type`: String, error: Option[TaskError])

  implicit val taskDecoder: Decoder[Task] = Decoder.instance { c =>
    for {
      uid <- c.get[Long]("uid").orElse(c.get[Long]("taskUid"))
      indexUid <- c.get[Option[String]]("indexUid")
      status <- c.get[String]("status")
      tpe <- c.get[String]("type")
      error <- c.get[Option[TaskError]]("error")
    } yield Task(uid, indexUid, status, tpe, error)
  }

  // ----------------------------
  // Minimal Meili client (sttp)
  // ----------------------------
  final class MeiliClient(baseUrl: String, apiKey: String, backend: SttpBackend[Identity, Any]) {
    private val base = uri"$baseUrl"
    private val req0 = basicRequest.header("X-Meili-API-Key", apiKey)

    private def sendJson[A: Decoder](r: Request[Either[ResponseException[String, Error], A], Any]): Either[String, A] =
      try {
        r.send(backend).body.left.map(_.getMessage)
      } catch {
        case e: SttpClientException.ConnectException =>
          val causeMsg = Option(e.getCause).map(_.getMessage).getOrElse("no cause")
          Left(s"connect_error: ${e.getMessage}; cause=$causeMsg")
        case e: SttpClientException =>
          Left(s"sttp_error: ${e.getMessage}")
        case e: Exception =>
          Left(s"unexpected_error: ${e.getClass.getName}: ${e.getMessage}")
      }

    def createIndex(uid: String, primaryKey: String): Either[String, Task] = {
      val body = Json.obj("uid" -> uid.asJson, "primaryKey" -> primaryKey.asJson)
      val r = req0
        .post(base.addPath("indexes"))
        .contentType("application/json")
        .body(body)
        .response(asJson[Task])

      sendJson(r)
    }

    def indexExists(uid: String): Boolean = {
      val r = req0
        .get(base.addPath("indexes", uid))
        .send(backend)

      r.code.isSuccess
    }

    // PATCH /indexes/:uid/settings
    def updateSettings(indexUid: String, settings: Json): Either[String, Task] = {
      val r = req0
        .patch(base.addPath("indexes", indexUid, "settings"))
        .contentType("application/json")
        .body(settings)
        .response(asJson[Task])

      sendJson(r)
    }

    // POST /indexes/:uid/documents
    def addDocuments(indexUid: String, docs: Json): Either[String, Task] = {
      val r = req0
        .post(base.addPath("indexes", indexUid, "documents"))
        .contentType("application/json")
        .body(docs)
        .response(asJson[Task])

      sendJson(r)
    }

    // POST /indexes/:uid/search
    def search(indexUid: String, payload: Json): Either[String, Json] = {
      val r = req0
        .post(base.addPath("indexes", indexUid, "search"))
        .contentType("application/json")
        .body(payload)
        .response(asJson[Json])

      sendJson(r)
    }

    // GET /tasks/:taskUid
    def getTask(taskUid: Long): Either[String, Json] = {
      val r = req0
        .get(base.addPath("tasks", taskUid.toString))
        .response(asJson[Json])

      sendJson(r)
    }
  }

  private def readFile(path: String): IO[String] =
    IO.blocking {
      val p = Paths.get(path)
      Files.readAllLines(p).asScala.mkString("\n")
    }

  private def decodeJsonFile[A: Decoder](path: String, label: String): IO[A] =
    readFile(path).flatMap { content =>
      parse(content) match {
        case Left(err) => IO.raiseError(new RuntimeException(s"Failed to parse $label JSON: ${err.message}"))
        case Right(js) =>
          js.as[A] match {
            case Left(err) => IO.raiseError(new RuntimeException(s"Failed to decode $label JSON: ${err.getMessage}"))
            case Right(value) => IO.pure(value)
          }
      }
    }

  private def loadSynonymEntries(path: String): IO[List[SynonymEntry]] =
    decodeJsonFile[List[SynonymEntry]](path, "synonyms")

  private def loadDocsFile(path: String): IO[List[Doc]] =
    decodeJsonFile[List[Doc]](path, "docs")

  private def loadTestCases(path: String): IO[List[SynonymTestCase]] =
    decodeJsonFile[List[SynonymTestCase]](path, "tests")

  // ----------------------------
  // Build settings JSON
  // ----------------------------
  private def buildSynonymsMap(entries: List[SynonymEntry]): Json = {
    def termVariants(term: String): List[String] = {
      val trimmed = term.trim
      val withoutParens = term.replace("(", " ").replace(")", " ").replaceAll("\\s+", " ").trim
      val parenContent = "\\(([^)]+)\\)".r
        .findAllMatchIn(term)
        .map(_.group(1).trim)
        .filter(_.nonEmpty)
        .toList

      List(trimmed, withoutParens).filter(_.nonEmpty).distinct ++ parenContent
    }

    val expanded = entries.foldLeft(Map.empty[String, List[String]]) { (acc, entry) =>
      val base = entry.thai_term :: entry.synonyms
      val group = base.flatMap(termVariants).distinct
      group.foldLeft(acc) { (innerAcc, term) =>
        val related = group.filterNot(_ == term)
        val merged = (innerAcc.getOrElse(term, List.empty) ++ related).distinct
        innerAcc.updated(term, merged)
      }
    }

    Json.obj(expanded.toSeq.sortBy(_._1).map { case (k, v) => (k, v.asJson) }: _*)
  }

  private def generateTestsFromSynonyms(entries: List[SynonymEntry]): List[SynonymTestCase] = {
    val maxHits = 20
    entries.zipWithIndex.flatMap { case (entry, i) =>
      val baseName = s"auto-${i + 1}"
      val primary = SynonymTestCase(
        name = s"$baseName-term",
        query = entry.thai_term,
        expectTerm = Some(entry.thai_term),
        minHits = 1,
        maxHits = maxHits
      )
      val syns = entry.synonyms.zipWithIndex.map { case (syn, j) =>
        SynonymTestCase(
          name = s"$baseName-syn-${j + 1}",
          query = syn,
          expectTerm = Some(entry.thai_term),
          minHits = 1,
          maxHits = maxHits
        )
      }
      primary :: syns
    }
  }

  private def buildSettings(synonyms: Json): Json =
    Json.obj(
      "searchableAttributes" -> Json.arr("title".asJson, "category".asJson, "content".asJson, "tags".asJson),
      "filterableAttributes" -> Json.arr("category".asJson, "tags".asJson),
      "sortableAttributes" -> Json.arr("popularity".asJson, "updated_at".asJson),
      "rankingRules" -> Json.arr(
        "words".asJson,
        "typo".asJson,
        "proximity".asJson,
        "attribute".asJson,
        "sort".asJson,
        "exactness".asJson
      ),
      "synonyms" -> synonyms
    )

  // ----------------------------
  // Optional: naive task polling (POC)
  // ----------------------------
  private def waitTaskDone(
                            meili: MeiliClient,
                            taskUid: Long,
                            maxTries: Int = 30,
                            allowedFailedCodes: Set[String] = Set.empty
                          ): IO[Unit] = {
    def loop(n: Int): IO[Unit] =
      if (n <= 0) IO.raiseError(new RuntimeException(s"Task $taskUid did not finish in time"))
      else
        IO.sleep(500.millis) *>
          IO(meili.getTask(taskUid)).flatMap {
            case Left(err) => IO.raiseError(new RuntimeException(s"getTask failed: $err"))
            case Right(js) =>
              val status = js.hcursor.get[String]("status").getOrElse("unknown")
              status match {
                case "succeeded" => IO.println(s"Task $taskUid succeeded")
                case "failed" =>
                  val code = js.hcursor.downField("error").get[String]("code").toOption
                  if (code.exists(allowedFailedCodes.contains)) {
                    IO.println(s"Task $taskUid ended with ${code.get} (continuing)")
                  } else {
                    IO.raiseError(new RuntimeException(s"Task $taskUid failed: ${js.noSpaces}"))
                  }
                case _ => loop(n - 1)
              }
          }

    loop(maxTries)
  }

  private def runSynonymTests(
                               meili: MeiliClient,
                               indexUid: String,
                               tests: List[SynonymTestCase],
                               resultsPath: String
                             ): IO[Unit] = {
    val defaultLimit = 50

    final case class TestResult(status: String, name: String, input: String, output: String)

    def renderOutput(titles: List[String], hitCount: Int): String = {
      val titleStr = if (titles.isEmpty) "<none>" else titles.mkString(", ")
      s"hits=$hitCount titles=$titleStr"
    }

    def runOne(test: SynonymTestCase): IO[TestResult] = {
      val limit = Math.max(defaultLimit, test.maxHits)
      val payload = Json.obj(
        "q" -> test.query.asJson,
        "limit" -> limit.asJson
      )

      IO(meili.search(indexUid, payload)).flatMap {
        case Left(err) =>
          IO.pure(TestResult("ERROR", test.name, test.query, s"error=$err"))
        case Right(js) =>
          val hits = js.hcursor.downField("hits").focus.flatMap(_.asArray).getOrElse(Vector.empty)
          val hitCount = hits.length
          val titles = hits.flatMap(_.hcursor.get[String]("title").toOption).toList
          val hasExpected = test.expectTerm.forall(term => titles.contains(term))
          val minOk = hitCount >= test.minHits
          val maxOk = hitCount <= test.maxHits

          if (hasExpected && minOk && maxOk) {
            IO.pure(TestResult("PASS", test.name, test.query, renderOutput(titles, hitCount)))
          } else {
            val termInfo = test.expectTerm.map(t => s"expect=$t").getOrElse("expect=<none>")
            IO.pure(TestResult("FAIL", test.name, test.query, s"$termInfo ${renderOutput(titles, hitCount)}"))
          }
      }
    }

    def truncate(value: String, max: Int): String =
      if (value.length <= max) value else value.take(max - 3) + "..."

    def padRight(value: String, width: Int): String =
      value + (" " * (width - value.length))

    def renderTable(results: List[TestResult]): String = {
      val headers = List("STATUS", "TEST", "INPUT", "OUTPUT")
      val maxCaps = Map("STATUS" -> 7, "TEST" -> 20, "INPUT" -> 40, "OUTPUT" -> 80)

      val widths = headers.map { header =>
        val values = header match {
          case "STATUS" => results.map(_.status)
          case "TEST" => results.map(_.name)
          case "INPUT" => results.map(_.input)
          case "OUTPUT" => results.map(_.output)
        }
        val maxLen = (header :: values).map(_.length).max
        val cap = maxCaps.getOrElse(header, maxLen)
        val width = math.min(maxLen, cap)
        header -> math.max(header.length, width)
      }.toMap

      val sep = "+-" + headers.map(h => "-" * widths(h)).mkString("-+-") + "-+"
      val headerRow = "| " + headers.map(h => padRight(h, widths(h))).mkString(" | ") + " |"
      val rows = results.map { r =>
        val cells = List(
          padRight(truncate(r.status, widths("STATUS")), widths("STATUS")),
          padRight(truncate(r.name, widths("TEST")), widths("TEST")),
          padRight(truncate(r.input, widths("INPUT")), widths("INPUT")),
          padRight(truncate(r.output, widths("OUTPUT")), widths("OUTPUT"))
        )
        "| " + cells.mkString(" | ") + " |"
      }

      (sep :: headerRow :: sep :: rows ::: List(sep)).mkString("\n")
    }

    tests.foldLeft(IO.pure(List.empty[TestResult])) { (acc, test) =>
      acc.flatMap(results => runOne(test).map(results :+ _))
    }.flatMap { results =>
      val table = renderTable(results)
      val md = s"# Test Results\n\n```\n$table\n```\n"
      IO.blocking(Files.write(Paths.get(resultsPath), md.getBytes(StandardCharsets.UTF_8))) *>
        IO.println(table) *>
        IO.println(s"[test] results saved to $resultsPath")
    }
  }

  // ----------------------------
  // Main POC
  // ----------------------------
  override def run: IO[Unit] =
    for {
      cfg <- loadConfig
      _ <- IO.println(s"[boot] Meili URL=${cfg.meiliUrl} index=${cfg.indexUid}")

      synonymEntries <- loadSynonymEntries(cfg.synonymsPath)
      synonyms = buildSynonymsMap(synonymEntries)
      settings = buildSettings(synonyms)

      docs <- loadDocsFile(cfg.docsPath)
      tests <- loadTestCases(cfg.testsPath)
      generatedTests = generateTestsFromSynonyms(synonymEntries)
      allTests = generatedTests ++ tests

      backend = HttpClientSyncBackend()
      meili = new MeiliClient(cfg.meiliUrl, cfg.meiliKey, backend)

      // Create index (skip if already exists)
      exists <- IO(meili.indexExists(cfg.indexUid))
      _ <- if (exists) {
        IO.println(s"[boot] createIndex skipped: index '${cfg.indexUid}' already exists")
      } else {
        for {
          createRes <- IO(meili.createIndex(cfg.indexUid, cfg.primaryKey))
          _ <- createRes match {
            case Right(task) =>
              task.status match {
                case "failed" if task.error.exists(_.code == "index_already_exists") =>
                  IO.println(s"[boot] createIndex: index '${cfg.indexUid}' already exists (continuing)")
                case "failed" =>
                  IO.raiseError(new RuntimeException(s"[boot] createIndex failed: ${task.error.map(_.message).getOrElse("unknown")}"))
                case "succeeded" =>
                  IO.println(s"[boot] createIndex succeeded task=${task.uid}")
                case _ =>
                  IO.println(s"[boot] createIndex enqueued task=${task.uid} status=${task.status}") *>
                    waitTaskDone(meili, task.uid, allowedFailedCodes = Set("index_already_exists"))
                      .handleErrorWith(e => IO.println(s"[boot] createIndex wait warning: ${e.getMessage}"))
              }
            case Left(err) =>
              IO.println(s"[boot] createIndex: $err (continuing)")
          }
        } yield ()
      }

      // Apply settings (includes synonyms)
      settingsRes <- IO(meili.updateSettings(cfg.indexUid, settings))
      settingsTaskUid <- settingsRes match {
        case Right(task) => IO.println(s"[boot] updateSettings task=${task.uid}") *> IO.pure(task.uid)
        case Left(err) => IO.raiseError(new RuntimeException(s"updateSettings failed: $err"))
      }
      _ <- waitTaskDone(meili, settingsTaskUid)

      // Ingest docs
      docsJson = docs.asJson
      addRes <- IO(meili.addDocuments(cfg.indexUid, docsJson))
      addTaskUid <- addRes match {
        case Right(task) => IO.println(s"[boot] addDocuments task=${task.uid}") *> IO.pure(task.uid)
        case Left(err) => IO.raiseError(new RuntimeException(s"addDocuments failed: $err"))
      }
      _ <- waitTaskDone(meili, addTaskUid)

      _ <- runSynonymTests(meili, cfg.indexUid, allTests, cfg.resultsPath)
    } yield ()

}
