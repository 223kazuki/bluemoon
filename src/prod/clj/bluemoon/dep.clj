(ns bluemoon.dep
  (:require [hiccup.page :as hiccup]))

(defn single-page-application
  []
  (hiccup/html5
    [:head
     [:meta {:http-equiv "Pragma" :content "no-cache"}]
     [:meta {:http-equiv "Cache-Control" :content "no-cache"}]
     [:meta {:http-equiv "content-style-type" :content "text/css"}]
     [:meta {:http-equiv "content-script-type" :content "text/javascript"}]
     [:meta {:name "viewport" :content "width=device-width,user-scalable=no,maximum-scale=1"}]
     [:link {:rel "stylesheet" :media "all" :type "text/css" :href "/css/screen.css"}]
     [:title "Bluemoon"]]
    [:body
     [:div {:id "app" :class "container"}]
     [:script {:src "/js/main.js"}]]))
