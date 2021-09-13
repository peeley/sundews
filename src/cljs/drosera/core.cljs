(ns drosera.core)

(defn ^:dev/after-load mount-components []
  (let [content (js/document.getElementById "app")]
    (.appendChild content (js/document.createTextNode "Welcome to poo"))))

(defn init! []
  (mount-components))
