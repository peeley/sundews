(ns drosera.links-test
  (:require [drosera.links :as links]
            [clojure.test :refer :all]))

(deftest test-slug-decode
  (testing "test slug can decode into id"
    (is (= 0 (links/decode-slug-to-id "a")))
    (is (= 1 (links/decode-slug-to-id "b")))
    (is (= 61 (links/decode-slug-to-id "9")))
    (is (= 62 (links/decode-slug-to-id "ba")))
    (is (every? identity
                (map #(= % (links/decode-slug-to-id (links/encode-id-to-slug %)))
                         (range 0 links/slug-alphabet-length))))))

(deftest test-id-encode
  (testing "test id can be encoded to slug"
    (is (= "b" (links/encode-id-to-slug 1)))
    (is (= "9" (links/encode-id-to-slug 61)))
    (is (= "ba" (links/encode-id-to-slug 62)))
    (is (= "b" (links/encode-id-to-slug (links/decode-slug-to-id "b"))))
    (is (= true (every? identity
                        (map #(= % (links/encode-id-to-slug (links/decode-slug-to-id %)))
                             (map str (rest links/slug-alphabet))))))))
