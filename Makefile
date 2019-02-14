.PHONY: build clean run

ls_accesstoken := yourtokenhere

specialagent_jar := ../java-specialagent/opentracing-specialagent/target/opentracing-specialagent-0.0.1-SNAPSHOT.jar
lighstep_tracer_jar := ../java-specialagent/tracers/lightstep/target/java-specialagent-lightstep-0.0.1-SNAPSHOT.jar

build:
	mvn package

clean:
	mvn clean

run: $(specialagent_jar)
	java \
		-Dls.accessToken=${ls_accesstoken} \
		-Dls.componentName=MyInstrumentedApp \
		-cp target/java-specialagent-sampleapp-1.0-SNAPSHOT.jar:$(lighstep_tracer_jar) -javaagent:$(specialagent_jar) io.opentracing.contrib.App

run-no-agent:
	java -cp target/java-specialagent-sampleapp-1.0-SNAPSHOT.jar io.opentracing.contrib.App
