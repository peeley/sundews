(ns sundews.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [sundews.routes :as routes]))

(deftest router-tests
  (testing "Index page successfully routes"
    (let [response (routes/handler {:uri "/" :request-method :get})
          response-status (:status response)
          response-body (:body response)]
      (is (= response-status 200))
      (is (re-find #"Sundews" response-body))))
  (testing "Cross-site forgery token needed on /links/create"
    (let [response (routes/handler {:uri "/links/create"
                                    :request-method :post
                                    :form-params {"link" "_"}})]
      (is (= (:status response) 403))))
  (testing "Missing pages return 404s"
    (let [response (routes/handler {:uri "/gocrazy/gostupid"
                                    :request-method :get})]
      (is (= (:status response) 404)))))
