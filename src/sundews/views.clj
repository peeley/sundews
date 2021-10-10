(ns sundews.views
  (:require [hiccup.def :refer [defhtml]]
            [hiccup.form :as form]
            [ring.util.response :as response]
            [ring.util.anti-forgery :refer [anti-forgery-field]]))

;; needed to suppress clj-kondo "Unresolved symbol" error when using macros
(declare index-template)

(defhtml index-template
  [status shortened-link]
  [:body [:h1 [:a {:href "/"} "Sundews"]]
   (when (= status :success)
     [:p "Your shortened link is " [:a {:href (str "/" shortened-link)} shortened-link]])
   (when (= status :invalid)
     [:p "Your submitted link is invalid."])
   (form/form-to [:post "/links/create"]
                 (anti-forgery-field)
                 (form/text-field {:placeholder "Shorten link"} "link")
                 (form/submit-button "Submit"))])

(declare not-found-template)

(defhtml not-found-template
  []
  [:body [:h1 "Page not found."]])

(defn- make-view
  [status view]
  (-> {:status status :body view}
      (response/content-type "text/html")
      (response/charset "utf-8")))

(defn index
  [status & [link-status shortened-link]]
  (make-view status (index-template link-status shortened-link)))

(defn not-found
  []
  (make-view 404 (not-found-template)))
