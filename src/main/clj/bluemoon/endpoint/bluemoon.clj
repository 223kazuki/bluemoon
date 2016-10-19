(ns bluemoon.endpoint.bluemoon
  (:refer-clojure :exclude [read])
  (:require [compojure.core :refer :all]
            [clojure.java.io :as io]
            [bluemoon.dep :refer [single-page-application]]
            [om.next.server :as om]
            [cognitect.transit :as transit])
  (:import [java.io ByteArrayOutputStream]))

(defmulti read om/dispatch)

(defmethod read :server-time
  [env k _]
  {:value (str "Party time! " (java.util.Date.))})

(defmulti mutate om/dispatch)

(defmethod mutate 'prepend-name
  [env k _]
  {:action #(println "test to call remote mutate.")})

(def remote-parser
  (om/parser {:read read :mutate mutate}))

(defn transit-write [clj-obj]
  (let [out-stream (ByteArrayOutputStream.)]
    (transit/write (transit/writer out-stream :json) clj-obj)
    (.toString out-stream)))

(defn handle-om-query [req]
  (let [query (transit/read (transit/reader (:body req) :json))
        result (remote-parser nil query)]
    {:status 200
     :body (transit-write result)}))

(defn bluemoon-endpoint [{{db :spec} :db}]
  (context "/bluemoon" []
           (GET "/" [] (single-page-application))
           (POST "/om-query" req (handle-om-query req))))
