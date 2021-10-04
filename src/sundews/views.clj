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

(defn index
  [status & [link-status shortened-link]]
  (-> {:status status :body (index-template link-status shortened-link)}
      (response/content-type "text/html")
      (response/charset "utf-8")))
