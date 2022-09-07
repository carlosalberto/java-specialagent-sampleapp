.PHONY: build clean run

SPECIALAGENT_JAR := opentracing-special-agent-1.7.4.jar

LS_ACCESSTOKEN := yourtokenhere
LS_AGENT_JAR := ../otel-launcher-java/agent/target/lightstep-opentelemetry-javaagent.jar

JMX_PORT := 999

build:
	mvn package

clean:
	mvn clean

run:
	java -cp target/java-specialagent-sampleapp-1.0-SNAPSHOT.jar io.opentracing.contrib.App

run-specialagent: $(LS_AGENT_JAR)
	java \
		-Dls.accessToken=${LS_ACCESSTOKEN} \
		-Dls.componentName=JavaSampleApp \
		-Dls.maxBufferedSpans=1000 \
		-Dls.maxReportingIntervalMillis=3000 \
		-cp target/java-specialagent-sampleapp-1.0-SNAPSHOT.jar -javaagent:$(LS_AGENT_JAR) io.opentracing.contrib.App

run-jmx:
	java \
		-Dcom.sun.management.jmxremote.port=${JMX_PORT} \
		-Dcom.sun.management.jmxremote.authenticate=false \
		-Dcom.sun.management.jmxremote.ssl=false \
		-cp target/java-specialagent-sampleapp-1.0-SNAPSHOT.jar io.opentracing.contrib.App
