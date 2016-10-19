(ns bluemoon.endpoint.bluemoon-test
  (:require [com.stuartsierra.component :as component]
            [clojure.test :refer :all]
            [kerodon.core :refer :all]
            [kerodon.test :refer :all]
            [shrubbery.core :as shrub]
            [bluemoon.endpoint.bluemoon :as bluemoon]))

(def handler
  (bluemoon/bluemoon-endpoint {}))

(deftest smoke-test
  (testing "bluemoon page exists"
    (-> (session handler)
        (visit "/bluemoon")
        (has (status? 200) "page exists"))))
