(ns sundews.routes
  (:require [compojure.core :as compojure]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [sundews.handlers :refer [index-handler create-link-handler redirect-handler]]
            [sundews.db :as db]))

(compojure/defroutes routes
  (compojure/POST "/links/create" [link] (create-link-handler db/db link))
  (compojure/GET "/:slug" [slug] (redirect-handler db/db slug))
  (compojure/GET "/" [] (index-handler))
  (route/not-found "<h1>Page not found.</h1>"))

(def handler
  (wrap-defaults routes site-defaults))
