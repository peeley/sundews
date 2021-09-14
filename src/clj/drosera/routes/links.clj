(ns drosera.routes.links
  (:require
   [drosera.db.core :as db]
   [drosera.links :as links]
   [drosera.middleware :as middleware]
   [ring.util.http-response :refer [content-type ok]]))

(defn redirect-slug
  [{{:keys [slug]} :path-params}]
  (let [link-id (links/decode-slug-to-id slug)
        link-row (db/get-link-by-id {:id link-id})]
    (content-type (ok
                   (if link-row
                     (str "Redirecting to " (:link link-row))
                     "Link has expired."))
                  "text/plain")))

(defn create-link
  [{{:keys [link]} :params}]
  (let [{id :id} (db/create-link! {:link link :user_id nil})
        slug (links/encode-id-to-slug id)]
  {:status 200 :headers {"Content-Type" "text/html"} :body slug}))

(defn links-routes
  []
  [
  ""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/link/create" {:post create-link}]
   ["/:slug" {:get redirect-slug}]])
