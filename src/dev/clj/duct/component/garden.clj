(ns duct.component.garden
  "A component for running Garden CSS compiler."
  (:require [com.stuartsierra.component :as component]
            [suspendable.core :as suspendable]
            [clojure.pprint :refer [pprint]]
            [ns-tracker.core :as ns-tracker]
            [clojure.java.io :as io]
            [garden.core]
            [me.raynes.fs :as fs]))

(defn- builds [project]
  (-> project :garden :builds))

(defn- output-path [build]
  (-> build :compiler :output-to))

(defn- validate-builds [project]
  (doseq [{:keys [id stylesheet source-paths] :as build} (builds project)]
    (cond
      (nil? source-paths)
      (throw (Exception. (format "No source-paths specified in build %s. " (name id))))
      (nil? stylesheet)
      (throw (Exception. (format "No stylesheet specified in build %s. " (name id))))
      (not (symbol? stylesheet))
      (throw (Exception. (format ":stylesheet value must be a symbol in build %s." (name id))))
      (nil? (output-path build))
      (throw (Exception. (format "No :output-to file specified in build %s." (name id)))))))

(defn- ensure-output-directory-exists [build]
  (let [dir (-> (output-path build)
                io/file
                fs/absolute-path
                fs/parent)]
    (when-not (fs/exists? dir)
      (when-not (fs/mkdirs dir)
        (throw (Exception. (format "Could not create directory %s" dir)))))))

(defn- prepare-build [build]
  (ensure-output-directory-exists build))

(defn- compile-builds [builds]
  (doseq [build builds]
    (prepare-build build)
    (try
      (let [stylesheet (eval (:stylesheet build))
            flags (:compiler build)]
        (println (str "Compiling with Garden " (pr-str (:output-to flags)) "..."))
        (println stylesheet)
        (garden.core/css flags stylesheet)
        (println "CSS compilation successful."))
      (catch Exception e
        (println "Error:" (.getMessage e))))
    (flush)))

(defrecord Server [builds]
  component/Lifecycle
  (start [component]
         (compile-builds builds)
         component)
  (stop [component]
        component)
  suspendable/Suspendable
  (suspend [component]
           component)
  (resume [component old-component]
          (compile-builds builds)
          component))

(defn server [options]
  (map->Server options))
