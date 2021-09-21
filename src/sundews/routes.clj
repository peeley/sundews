(ns sundews.routes
  (:require [compojure.core :as compojure]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :as response]
            [sundews.views :as views]))


(compojure/defroutes routes
  (compojure/POST "/links/create"
                  {{:keys [link]} :params}
                  link)
  (compojure/GET "/" _ (-> {:status 200 :body views/index}
                            (response/content-type "text/html")
                            (response/charset "utf-8"))))

(def handler
  (wrap-defaults routes
                 (assoc site-defaults
                        :security
                        {:anti-forgery false})))

(comment
  )
