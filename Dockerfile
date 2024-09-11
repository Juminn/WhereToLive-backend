FROM azul/zulu-openjdk:17
ADD build/libs/whereToLive-0.0.1-SNAPSHOT.jar whereToLive-SNAPSHOT.jar
EXPOSE  8080

# 환경 변수로 Java 옵션 설정

# 엔트리 포인트에 환경 변수 추가

#원래버전
#ENV JAVA_OPTS="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/heap_dumps"
#ENTRYPOINT java -jar /costCalculrator-0.0.1-SNAPSHOT.jar $JAVA_OPTS

RUN mkdir -p /heap_dumps
ENV JAVA_OPTS="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/heap_dumps/heapdump.hprof -Xlog:gc*:file=/var/log/gc.log"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /costCalculrator-0.0.1-SNAPSHOT.jar"]