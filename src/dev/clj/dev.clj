(ns dev
  (:refer-clojure :exclude [test])
  (:require [clojure.repl :refer :all]
            [clojure.pprint :refer [pprint]]
            [clojure.tools.namespace.repl :refer [refresh]]
            [clojure.java.io :as io]
            [com.stuartsierra.component :as component]
            [duct.generate :as gen]
            [duct.util.repl :refer [setup test cljs-repl migrate rollback]]
            [duct.util.system :refer [load-system]]
            [reloaded.repl :refer [system init start stop go reset]]
            [alembic.still :refer [load-project]]))

(defn new-system []
  (let [bindings {'db-uri (str "jdbc:h2:file:"
                               (.getAbsolutePath (io/file "kotohogi_dev.db"))
                               ";USER=sa;PASSWORD=")}]
    (load-system (keep io/resource ["bluemoon/system.edn" "dev.edn" "local.edn"]) bindings)))

(when (io/resource "local.clj")
  (load "local"))

(gen/set-ns-prefix 'bluemoon)

(reloaded.repl/set-init! new-system)
