(ns sundews.handlers-test
  (:require [sundews.handlers :as handlers]
            [sundews.db :as db]
            [sundews.links :as links]
            [clojure.test :refer [deftest is testing]]
            [next.jdbc :refer [with-transaction]]))


(deftest create-link-handler-test
  (with-transaction [test-db db/db {:rollback-only true}]
    (testing "Empty links are rejected"
      (let [response (handlers/create-link-handler test-db "")]
        (is (= (:status response) 403))))
    (testing "Can store submitted links in the database"
      (let [response (handlers/create-link-handler test-db "abc.com")]
        (is (= (:status response) 200))
        (is (not-empty (db/get-link-by-url test-db "abc.com")))))))

(deftest redirect-handler-test
  (with-transaction [test-db db/db {:rollback-only true}]
    (testing "Can retrieve links by slug and redirect"
      (let [link-slug (->> "abc.com"
                         (db/insert-link! test-db)
                         :links/id
                         links/encode-id-to-slug)
            found-response (handlers/redirect-handler test-db link-slug)]
        (is (= (:status found-response) 302))))
    (testing "Missing links return a 404"
      (let [missing-response (handlers/redirect-handler test-db "ZZZZZZ")]
        (is (= (:status missing-response) 404))))))
