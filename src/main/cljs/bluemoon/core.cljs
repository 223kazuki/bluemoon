(ns bluemoon.core
  (:require [goog.dom :as gdom]
            [om.next :as om]
            [accountant.core :as accountant]
            [bluemoon.routes]
            [bluemoon.views :as views]
            [bluemoon.parsers :refer [parser send]]))

(def names-list
  '("Rebecca" "Jesse" "Molly"))

(def reconciler
  (om/reconciler
    {:state {:names names-list :current-view views/hello-world}
     :parser parser
     :send send}))

(defn mount-root []
  (om/add-root! reconciler views/RootView (gdom/getElement "app")))

(defn init! []
  (accountant/configure-navigation!)
  (accountant/dispatch-current!)
  (mount-root))
