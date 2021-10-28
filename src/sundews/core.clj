(ns sundews.core
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [sundews.routes :as routes]
            ;; need to pull in for mount to start jobs
            [sundews.scheduler :as scheduler]
            [mount.core :as mount :refer [defstate]]))

(defstate server
  :start (jetty/run-jetty
          #'routes/handler
          {:port 4783 :join? false})
  :stop (.stop server))

(defn -main
  []
  (mount/start))

(comment
  (mount/start)
  (mount/stop))
