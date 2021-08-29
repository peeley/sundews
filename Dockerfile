FROM node:16 as install_npm

WORKDIR /usr/app
COPY package.json /usr/app/package.json

RUN npm install

FROM clojure:lein AS build_clj

COPY ./project.clj /app/project.clj
COPY ./shadow-cljs.edn /app/shadow-cljs.edn
COPY ./resources /app/resources
COPY ./src/ /app/src
COPY ./env/prod /app/env/prod

WORKDIR /app

RUN lein uberjar

FROM clojure:latest

COPY --from=build_clj /app/target/uberjar/drosera.jar .
COPY --from=install_npm /usr/app/node_modules .

CMD java -jar drosera.jar
