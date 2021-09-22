(ns sundews.views
  (:require [hiccup.def :refer [defhtml]]
            [hiccup.form :as form]
            [ring.util.anti-forgery :refer [anti-forgery-field]]))

;; needed to suppress clj-kondo "Unresolved symbol" error when using macros
(declare index)

(defhtml index
  []
  [:body [:h1 "Sundews"]
         (form/form-to [:post "/links/create"]
                       (anti-forgery-field)
                       (form/text-field {:placeholder "Shorten link"} "link")
                       (form/submit-button "Submit"))])
