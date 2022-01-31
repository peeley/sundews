(ns sundews.views
  (:require [hiccup.def :refer [defhtml]]
            [hiccup.form :as form]
            [ring.util.response :as response]
            [ring.util.anti-forgery :refer [anti-forgery-field]]))

(defhtml index-template
  [link-validity]
  [:body [:h1 [:a {:href "/"} "Sundews"]]
   (when (= link-validity :invalid)
     [:p "Your submitted link is invalid."])
   (form/form-to [:post "/links/create"]
                 (anti-forgery-field)
                 (form/text-field {:placeholder "Shorten link"} "link")
                 (form/submit-button "Submit"))])

(defhtml not-found-template
  []
  [:body [:h1 "Page not found."]])

(defhtml link-created-template
  [shortened-link]
  [:p "Your link was shortened successfully: "
   [:a {:href (str "/" shortened-link)} shortened-link]])

(defn- make-view
  [view & {:keys [status] :or {status 200}}]
  (-> {:status status :body view}
      (response/content-type "text/html")
      (response/charset "utf-8")))

(defn link-created
  [shortened-link]
  (make-view (link-created-template shortened-link)))

(defn index
  [status & [link-validity]]
  (make-view (index-template link-validity) :status status))

(defn not-found
  []
  (make-view (not-found-template) :status 404))
