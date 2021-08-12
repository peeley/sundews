(ns drosera.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[drosera started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[drosera has shut down successfully]=-"))
   :middleware identity})
