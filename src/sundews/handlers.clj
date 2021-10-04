(ns sundews.handlers
  (:require [sundews.db :as db]
            [sundews.links :as links]
            [sundews.views :as views]
            [ring.util.response :as response]))

(defn index-handler
  []
  (views/index 200))

(defn create-link-handler
  [db link]
  (if (empty? link)
    (views/index 403 :invalid)
    (let [link-id (:links/id (db/insert-link! db link))
          link-slug (links/encode-id-to-slug link-id)]
      (views/index 200 :success link-slug))))

(defn redirect-handler
  [db slug]
  (let [link-id (links/decode-slug-to-id slug)
        redirect-url (:links/url (db/get-link-by-id db link-id))]
    (if redirect-url
      (response/redirect redirect-url)
      (response/not-found "This link is either expired or invalid."))))
