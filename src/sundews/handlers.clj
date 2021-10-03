(ns sundews.handlers
  (:require [sundews.db :as db]
            [sundews.links :as links]
            [sundews.views :as views]
            [ring.util.response :as response]))

(defn index-handler
  []
  (-> {:status 200 :body (views/index)}
      (response/content-type "text/html")
      (response/charset "utf-8")))

(defn create-link-handler
  [link]
  (let [link-id (:links/id (db/insert-link! link))
        link-slug (links/encode-id-to-slug link-id)]
    (-> {:status 200 :body link-slug}
        (response/content-type "text/html")
        (response/charset "utf-8"))))

(defn redirect-handler
  [slug]
  (let [link-id (links/decode-slug-to-id slug)
        redirect-url (db/get-link-by-id link-id)]
    (response/redirect redirect-url)))
