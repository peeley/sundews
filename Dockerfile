FROM clojure:lein AS build

COPY ./project.clj /app/project.clj
COPY ./shadow-cljs.edn /app/shadow-cljs.edn
COPY ./resources /app/resources
COPY ./src/ /app/src
COPY ./env/prod /app/env/prod

WORKDIR /app

RUN lein uberjar

FROM clojure:latest

COPY --from=build /app/target/uberjar/drosera.jar .

CMD java -jar drosera.jar
