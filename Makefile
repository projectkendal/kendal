version:
	mvn versions:set -DnewVersion=$(v)

prepare-release:
	mvn release:prepare -Dresume=false

perform-release:
	mvn release:perform