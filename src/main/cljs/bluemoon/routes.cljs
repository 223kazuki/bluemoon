(ns bluemoon.routes
  (:require-macros [secretary.core :refer [defroute]])
 (:require [bluemoon.views :as view]
  	        [secretary.core]))

(defroute "/bluemoon" []
  (println "test!"))