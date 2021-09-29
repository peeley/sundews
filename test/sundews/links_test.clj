(ns sundews.links-test
  (:require [sundews.links :refer [encode-id-to-slug
                                   decode-slug-to-id
                                   slug-alphabet-length]]
            [clojure.test :refer [deftest is testing]]))

(deftest links-test
  (testing "Can encode ids to slugs"
    (is (= "" (encode-id-to-slug 0)))
    (is (= "b" (encode-id-to-slug 1)))
    (is (= "c" (encode-id-to-slug 2)))
    (is (= "ba" (encode-id-to-slug slug-alphabet-length))))
  (testing "Can decode slugs to ids"
    (is (= 0 (decode-slug-to-id "a")))
    (is (= 1 (decode-slug-to-id "b")))
    (is (= 2 (decode-slug-to-id "c")))
    (is (= slug-alphabet-length (decode-slug-to-id "ba"))))
  (testing "Encode -> decode is identity for arbitrary range"
    (let [test-range (range 0 1000)
          encoded-then-decoded (map #(-> % encode-id-to-slug decode-slug-to-id) test-range)]
      (is (map #(= %1 %2) test-range encoded-then-decoded)))))
