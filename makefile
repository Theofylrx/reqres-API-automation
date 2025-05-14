GRADLE_CLI = ./gradlew

.PHONY: clean build run

clean:
	$(GRADLE_CLI) clean

build:
	$(GRADLE_CLI) build

# run:
# 	$(GRADLE_CLI) run

test:
	$(GRADLE_CLI) test