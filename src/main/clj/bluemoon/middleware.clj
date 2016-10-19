(ns bluemoon.middleware
  (:require [clojure.tools.logging :as log]
            [clojure.edn :as edn]
            [environ.core :refer [env]]
            [ring.util.response :refer [content-type redirect]]
            [ring.util.response :refer [redirect]])
  (:import [javax.servlet ServletContext]))

(defn wrap-logging
  [handler]
  (fn [request]
    (log/debug "request : " (pr-str request))
    (let [response (handler request)]
      (log/debug "response : " (pr-str response))
      response)))

(defn wrap-edn-response
  [handler]
  (fn [request]
    (try
      (let [response (handler request)]
        (if (coll? (:body response))
          (let [edn-response (update-in response [:body] pr-str)]
            (if (contains? (:headers response) "Content-Type")
              edn-response
              (content-type edn-response "application/edn; charset=utf-8")))
          response))
      (catch Exception e
        (.printStackTrace e)
        {:status 500 :headers {"Content-Type" "text/plain"} :body "Innternal server error."}))))

(defn wrap-edn-body
  [handler]
  (fn [{:keys [content-type body] :as request}]
    (if (and (not (nil? content-type))
             (not (== (.indexOf content-type "application/edn") -1))
             (not (nil? body)))
      (if-let [edn (try
                     (edn/read-string (slurp body))
                     (catch Exception ex false))]
        (handler (assoc request :body {:edn edn}))
        {:status 400 :headers {"Content-Type" "text/plain"} :body "Malformed edn in request body."})
      (handler request))))

(defn wrap-error-response
  [handler]
  (fn [request]
    (let [response (handler request)]
      (if (coll? (:body response))
        (let [edn-response (update-in response [:body] pr-str)]
          (if (contains? (:headers response) "Content-Type")
            edn-response
            (content-type edn-response "application/edn; charset=utf-8")))
        response))))
