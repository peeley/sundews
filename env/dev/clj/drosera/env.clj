(ns drosera.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [drosera.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[drosera started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[drosera has shut down successfully]=-"))
   :middleware wrap-dev})
