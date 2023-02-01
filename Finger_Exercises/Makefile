.PHONY: build

build: 
	@mvn -f ./Other/pom.xml package -q -Dmaven.test.skip=true

prerequisites: build

target: prerequisites 