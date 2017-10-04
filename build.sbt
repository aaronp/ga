enablePlugins(GitVersioning)

enablePlugins(DockerPlugin)

enablePlugins(SiteScaladocPlugin)

enablePlugins(PamfletPlugin)

sourceDirectory in Pamflet := sourceDirectory.value / "documentation"

siteSubdirName in SiteScaladoc := "api/latest"

mainClass in(Compile, run) := Some("com.porpoise.ga.countdown.Main")

val repo = "ga"

val username = "aaronp"

name := repo

organization := s"com.github.${username}"

scalaVersion := "2.11.11"

libraryDependencies += "com.google.guava" % "guava" % "22.0"

libraryDependencies += "junit"         % "junit"      % "4.12"  % "test"

// see https://leonard.io/blog/2017/01/an-in-depth-guide-to-deploying-to-maven-central/
pomIncludeRepository := (_ => false)

// To sync with Maven central, you need to supply the following information:
pomExtra in Global := {
  <url>https://github.com/{username}/{}</url>
    <licenses>
      <license>
        <name>Apache 2</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      </license>
    </licenses>
    <developers>
      <developer>
        <id>{username}</id>
        <name>Aaron Pritzlaff</name>
        <url>https://github.com/{username}/{repo}</url>
      </developer>
    </developers>
}


test in(assembly) := {}

dockerfile in docker := {
  // The assembly task generates a fat JAR file
  val artifact: File = assembly.value
  val artifactTargetPath = s"/app/${artifact.name}"

  new Dockerfile {
    from("java")
    expose(7770)
    run("mkdir", "-p", "/data")
    run("mkdir", "-p", "/config")
    env("DATA_DIR", "/data")
    volume("/data")
    volume("/config")
    add(artifact, artifactTargetPath)
    entryPoint("java", "-cp", "/config", "-jar", artifactTargetPath)
  }
}