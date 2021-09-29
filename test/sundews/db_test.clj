(ns sundews.db-test
  (:require [sundews.db :as db]
            [next.jdbc :refer [with-transaction]]
            [clojure.test :refer [deftest is testing]]))

(deftest db-tests
  (with-transaction [test-db db/db {:rollback-only true}]
    (let [insert-result (db/insert-link! test-db "foo.com")]
      (testing "Can insert links into db"
        (is (contains? insert-result :links/id)))
      (testing "Can retrieve links from db"
        (is (= "foo.com" (->> insert-result
                             :links/id
                             (db/get-link-by-id test-db)
                             :links/url)))
        (is (= nil (db/get-link-by-id test-db Integer/MAX_VALUE)))))))
