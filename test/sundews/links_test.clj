(ns sundews.links-test
  (:require [sundews.links :refer [encode-id-to-slug
                                   make-link-from-slug
                                   decode-slug-to-id
                                   slug-alphabet-length]]
            [clojure.test :refer [deftest is testing]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]))

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
  (testing "Can construct URLs from slugs and env vars"
    (is (= "http://sunde.ws/P" (make-link-from-slug "http://sunde.ws" "P")))
    (is (= "http://sunde.ws/" (make-link-from-slug "http://sunde.ws" "")))))

(defspec encode-then-decode-is-identity
  10000
  (prop/for-all [num gen/nat]
                (= num (-> num encode-id-to-slug decode-slug-to-id))))
