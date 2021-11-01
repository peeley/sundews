(ns sundews.db-test
  (:require [sundews.db :as db]
            [next.jdbc :refer [with-transaction]]
            [clojure.test :as t :refer [deftest is testing]]
            [java-time :as time]
            [mount.core :as mount]))

;; TODO refactor, should only need to migrate once
(defn db-fixture
  [f]
  (mount/start #'db/db)
  (db/migrate-up db/db)
  (f)
  (mount/stop #'db/db))

(t/use-fixtures :once db-fixture)

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
        (is (= nil (db/get-link-by-id test-db Integer/MAX_VALUE))))
      (testing "Can delete links created before arbitrary time"
        (let [now (time/instant->sql-timestamp (time/instant))]
          (db/delete-links-created-before! test-db now)
          (is (empty? (db/get-all-links test-db))))
        (let [new-link-id (:links/id (db/insert-link! test-db "abc.com"))
              future (-> (time/instant)
                         (time/plus (time/minutes 5))
                         time/instant->sql-timestamp)]
          (db/update-link-created-time test-db future new-link-id)
          (is (= (count (db/get-all-links test-db)) 1))
          (is (not-empty (db/get-link-by-url test-db "abc.com"))))))))
