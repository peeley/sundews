(ns sundews.views
  (:require [hiccup.core :refer [html]]
            [hiccup.form :as form]))

(def index
  (html [:body
         [:h1 "Sundews"]
         (form/form-to [:post "/links/create"]
                       (form/text-field {:placeholder "Shorten link"} "link")
                       (form/submit-button "Submit"))]))
