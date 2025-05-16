GRADLE_CLI = ./gradlew

.PHONY: clean build test

clean:
	$(GRADLE_CLI) clean

build:
	$(GRADLE_CLI) build

test:
	$(GRADLE_CLI) test -P env=prod

report:
	$(GRADLE_CLI) allureReport

serve-report:
	$(GRADLE_CLI) allureServe