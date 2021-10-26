(ns sundews.db
  (:require [next.jdbc :as jdbc]
            [honey.sql :as sql]
            [mount.core :refer [defstate]]))

(def db-spec {:dbtype "postgresql"
              :dbname (System/getenv "DB_USER")
              :user (System/getenv "DB_USER")
              :host (System/getenv "DB_HOST")
              :port 5432
              :password (System/getenv "DB_PASSWORD")})

(defstate db
  :start (jdbc/get-datasource db-spec))

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

(defn get-link-by-url
  [db url]
  (jdbc/execute! db (sql/format {:select [:*]
                                 :from [:links]
                                 :where [:= :url url]})))

(defn delete-links-created-before!
  [db timestamp]
  (jdbc/execute! db (sql/format {:delete-from [:links]
                                 :where [:< :created_at timestamp]})))

(defn delete-all-links!
  [db]
  (jdbc/execute! db (sql/format {:delete-from [:links]})))

(defn update-link-created-time
  [db new-time link-id]
  (jdbc/execute! db (sql/format {:update [:links]
                                 :set {:created_at new-time}
                                 :where [:= :id link-id]})))

(comment
  (migrate-up)
  (migrate-down)
  )
