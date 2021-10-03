(ns sundews.routes
  (:require [compojure.core :as compojure]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [sundews.handlers :refer [index-handler create-link-handler redirect-handler]]
            [sundews.db :as db]))

(compojure/defroutes routes
  (compojure/POST "/links/create" [link] (create-link-handler db/db link))
  (compojure/GET "/:slug" [slug] (redirect-handler slug))
  (compojure/GET "/" [] (index-handler)))

(def handler
  (wrap-defaults routes site-defaults))
