(ns drosera.routes.links
  (:require
   [drosera.db.core :as db]
   [drosera.links :as links]
   [drosera.middleware :as middleware]
   [ring.util.http-response :as response]))

(defn- redirect-slug
  [{{:keys [slug]} :path-params}]
  (let [link-id (links/decode-slug-to-id slug)
        link-row (db/get-link-by-id {:id link-id})]
    (if link-row
      (response/found {:link link-row})
      (response/not-found "This link has expired."))))

(defn links-routes
  []
  [
  ""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/:slug" {:get redirect-slug}]])
