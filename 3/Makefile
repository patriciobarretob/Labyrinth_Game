.PHONY: build

build: 
	@mvn -e -f ../Maze/Common/pom.xml package -q -Dmaven.test.skip=true

prerequisites: build

target: prerequisites 
