version:
	mvn versions:set -DnewVersion=$(v)

prepare-release:
	mvn release:prepare -Dresume=false

perform-release:
	mvn release:perform

install:
	mvn clean install

compilation-tests:
	$$JTREG_HOME/bin/jtreg -cpa:handlers/target/classes:processor/target/classes -javacoption:-Xlint:none -agentvm -workDir tests/target/JTwork -reportDir tests/target/JTreport tests/src/main/java/kendal/test/