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

(defn migrate-up
  []
  (jdbc/execute! db
              ["CREATE TABLE IF NOT EXISTS links (
                    id SERIAL,
                    url TEXT NOT NULL,
                    created_at TIMESTAMP NOT NULL DEFAULT NOW()
               );"]))

(defn migrate-down
  []
  (jdbc/execute! db ["DROP TABLE links;"]))

(defn insert-link!
  [db link-text]
  (jdbc/execute-one! db (sql/format {:insert-into "links"
                                 :values [{:url link-text}]
                                 :returning [:id]})))

(defn get-link-by-id
  [db id]
  (jdbc/execute-one! db (sql/format {:select [:*]
                                     :from [:links]
                                     :where [:= :id id]})))

(defn get-all-links
  [db]
  (jdbc/execute! db (sql/format {:select [:*]
                                 :from [:links]})))

(map :links/url (get-all-links db))
