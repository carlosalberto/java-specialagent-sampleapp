.PHONY: build clean run

ls_accesstoken := yourtokenhere

specialagent_jar := ../java-specialagent/opentracing-specialagent/target/opentracing-specialagent-0.0.1-SNAPSHOT.jar

build:
	mvn package

clean:
	mvn clean

run: $(specialagent_jar)
	java -Dls.accessToken=${ls_accesstoken} -cp target/java-specialagent-sampleapp-1.0-SNAPSHOT.jar -javaagent:$(specialagent_jar) io.opentracing.contrib.App
