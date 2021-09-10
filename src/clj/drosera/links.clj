(ns drosera.links)

(def ^:private slug-alphabet
  (mapv char
        (concat (range (int \a) (int \z))
                (range (int \A) (int \Z))
                (range (int \0) 58))))

(def ^:private alphabet-length (count slug-alphabet))

(defn encode-id-to-slug
  [link-id]
  (loop [slug ""
         id-int link-id]
    (if (= id-int 0)
      slug
      (recur
       (str (nth slug-alphabet (dec (mod id-int alphabet-length))) slug)
       (quot id-int alphabet-length)))))

(defn decode-slug-to-id
  [slug-text]
  (loop [id 0
         slug-str slug-text]
    (if (empty? slug-str)
      (int id)
      (recur
       (+ id
          (* (inc (.indexOf slug-alphabet (last slug-str)))
             (Math/pow alphabet-length
                       (- (count slug-text) (count slug-str)))))
       (drop-last slug-str)))))
