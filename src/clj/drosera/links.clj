(ns drosera.links)

(def slug-alphabet
  (mapv char
        (concat (range (int \a) (inc (int \z)))
                (range (int \A) (inc (int \Z)))
                (range (int \0) (inc (int \9))))))

(def slug-alphabet-length (count slug-alphabet))

;; the slug "a" will never exist in the wild, as that represents an id of 0.
;; there are other odd interactions, where an id of 0 is encoded to the empty
;; string. these shouldn't ever occur (since serial ids start at 1), but
;; something to be aware of.

(defn encode-id-to-slug
  [link-id]
  (loop [slug ""
         id-int link-id]
    (if (= id-int 0)
      slug
      (recur
       (str (nth slug-alphabet (mod id-int slug-alphabet-length)) slug)
       (quot id-int slug-alphabet-length)))))

(defn decode-slug-to-id
  [slug-text]
  (loop [id 0
         slug-str slug-text]
    (if (empty? slug-str)
      (int id)
      (recur
       (+ id
          (* (.indexOf slug-alphabet (last slug-str))
             (Math/pow slug-alphabet-length
                       (- (count slug-text) (count slug-str)))))
       (drop-last slug-str)))))
