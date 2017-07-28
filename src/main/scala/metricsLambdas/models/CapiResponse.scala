package metricsLambdas.models

case class CapiResponse(response: Response)

case class Response(currentPage: Int, pages: Int, results: List[CapiResult])

case class CapiResult(webPublicationDate: String, fields: CapiFields, tags: List[Tag], debug: DebugFields)

case class CapiFields(internalComposerCode: String, internalOctopusCode: Option[String], creationDate: String)

case class Tag(id: String, `type`: String)

case class DebugFields(originatingSystem: Option[String])