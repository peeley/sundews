(ns sundews.core
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [sundews.routes :as routes]))

(defonce server (atom nil))

(defn start-server
  []
  (reset! server
         (jetty/run-jetty
          #'routes/handler
          {:port 4783 :join? false})))

(defn stop-server
  []
  (when @server (.stop @server))
  (reset! server nil))

(defn -main
  []
  (start-server))
