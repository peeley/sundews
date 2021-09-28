(ns sundews.db
  (:require [next.jdbc :as jdbc]
            [honey.sql :as sql]))

(def db-spec {:dbtype "postgresql"
              :dbname (System/getenv "DB_USER")
              :user (System/getenv "DB_USER")
              :host (System/getenv "DB_HOST")
              :port 5432
              :password (System/getenv "DB_PASSWORD")})

(def db (jdbc/get-datasource db-spec))

(defn- db-execute
  [db sql]
  (jdbc/execute! db sql))

(defn migrate-up
  []
  (db-execute db
              ["CREATE TABLE IF NOT EXISTS links (
                    id SERIAL,
                    url TEXT NOT NULL,
                    created_at TIMESTAMP NOT NULL DEFAULT NOW()
               );"]))

(defn migrate-down
  []
  (db-execute db ["DROP TABLE links;"]))

(defn insert-link!
  [db link-text]
  (db-execute db (sql/format {:insert-into "links"
                              :values [{:url link-text}]})))
