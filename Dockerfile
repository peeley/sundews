FROM clojure:lein AS build

COPY ./src /usr/src/app/src
COPY ./project.clj /usr/src/app/project.clj

WORKDIR /usr/src/app

RUN lein uberjar

FROM clojure:latest AS run

COPY --from=build /usr/src/app/target/uberjar/*-standalone.jar .

CMD java -jar ./*.jar
