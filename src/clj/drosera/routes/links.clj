(ns drosera.routes.links
  (:require
   [drosera.middleware :as middleware]
   [ring.util.http-response :as response]))

(defn- redirect-slug
  [{{:keys [slug]} :path-params}]
  {:status 200 :headers {"Content-Type" "text/plain"} :body slug})

(defn links-routes
  []
  [
  ""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/:slug" {:get redirect-slug}]])
