.PHONY: build

build: 
	@mvn -e -f pom.xml package -q -Dmaven.test.skip=true

prerequisites: build

target: prerequisites 
