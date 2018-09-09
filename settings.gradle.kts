rootProject.name = "camel-zeebe"
rootProject.buildFileName = "build.gradle.kts"

include("extension")

include("extension:api")
findProject(":extension:api")?.name = "camel-zeebe-api"

include("extension:core")
findProject(":extension:core")?.name = "camel-zeebe-core"


include("examples")
include("examples:calculator:server")
include("examples:calculator:worker")
