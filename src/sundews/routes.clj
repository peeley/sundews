(ns sundews.routes
  (:require [compojure.core :as compojure]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [sundews.handlers :as h]
            [sundews.db :as db]))

(compojure/defroutes routes
  (compojure/POST "/links/create" [link] (h/create-link-handler db/db link))
  (compojure/GET "/links/created/:short-link" [short-link] (h/link-created-handler short-link))
  (compojure/GET "/:slug" [slug] (h/redirect-handler db/db slug))
  (compojure/GET "/" [] (h/index-handler))
  (route/not-found (h/not-found-handler)))

(def handler
  (wrap-defaults routes site-defaults))
