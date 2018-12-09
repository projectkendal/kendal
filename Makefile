version:
	mvn versions:set -DnewVersion=$(v)

prepare-release:
	mvn release:prepare -Dresume=false

perform-release:
	mvn release:perform

install:
	mvn clean install

negative-compile-tests:
	$$JTREG_HOME/bin/jtreg -cpa:handlers/target/classes:processor/target/classes -javacoption:-Xlint:none -agentvm -workDir tests-negative/target/JTwork -reportDir tests-negative/target/JTreport tests-negative/src/main/java/kendal/test/negative