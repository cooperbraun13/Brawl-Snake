# Really simple wrapper on Maven's command to build, test, & run your project
# Maven is a package builder and runner tool for Java
#  https://maven.apache.org/

JAR_TARGET = target/farkle-*.jar

# Find all Java files recursively in the specified directory and its subdirectories
JAVA_FILES = $(shell find src/main/java/edu/gonzaga/ -name "*.java")

# The first target is the default one run by make
# By convention it's called 'all'
all: build

# Target for the actual jar file to build and how to build it
# Only rebuilds if you edit the *.java main/source files
$(JAR_TARGET): $(JAVA_FILES)
	@echo "Building MainGame package - results in target/ directory"
	mvn package -DskipTests

build: $(JAR_TARGET)

force-build: clean build

test:
	@echo "Running all tests"
	mvn test

mvn-run:
	@echo "Running Final Game main - see pom.xml for arguments passed"
	mvn exec:java

run: build
	@echo "Running Yahtzee main without maven overhead"
	java -jar target/finalgame-*.jar

fast-run:
	java -jar target/finalgame-*.jar

spellcheck:
	-codespell src/

setup-dependencies:
	apt update
	apt install -y maven python3-pip checkstyle
	pip3 install codespell

javadoc:
	@echo "Creating javadoc materials"
	@echo "These go into: target/site/apidocs/"
	@echo "Load up index.html to read them"
	-mvn javadoc:javadoc

lint:
	@echo "Running spotless linter to check source files"
	-mvn spotless:check

lint-autofix:
	@echo "Autofixing linting errors"
	mvn spotless:apply


# Generate the code coverage reports using maven/jacoco
# Results are in target/site/jacoco/*
coverage:
	@echo "Making code coverage reports"
	@echo "Output is in target/site/jacoco/index.html"
	mvn clean
	mvn jacoco:prepare-agent
	mvn test
	mvn jacoco:report

# Opens your coverage report using a web browser (works on Linux)
view-coverage:
	xdg-open target/site/jacoco/index.html
