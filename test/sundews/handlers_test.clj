(ns sundews.handlers-test
  (:require [sundews.handlers :as handlers]
            [sundews.db :as db]
            [clojure.test :refer [deftest is testing]]
            [next.jdbc :refer [with-transaction]]))


(deftest create-link-test
  (testing "Can create links and store in DB"
    (with-transaction [test-db db/db {:rollback-only true}]
      (let [response (handlers/create-link-handler test-db "")]
        (is (:status response) 403))
      (let [response (handlers/create-link-handler test-db "abc.com")]
        (is (:status response) 200)
        (is (not-empty (db/get-link-by-url test-db "abc.com")))))))
