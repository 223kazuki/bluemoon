(ns bluemoon.views
  (:require
    [goog.dom :as gdom]
    [om.next :as om]
    [sablono.core :as sablono]))

(om/defui RootView
          static om/IQuery
          (query [_]
                 '[:current-view])
                 
          Object
          (render [this]
                  (let [{:keys [current-view]} (om/props this)]
                    (sablono/html
                      [:div
                       [:div.row {:style {:min-height "100%"}}
                        [:div.col]
                        [:div.col]
                        [:div.header]]
                       [:div.col]
                       [:div.row {:style {:min-height "100%"}}
                        [:div.col]
                        [:div.col
                         [:div.ctg 
                          ; {:transitionName "contents"
                          ; :component "div"
                          ; :transitionLeaveTimeout 500
                          ; :transitionEnterTimeout 500
                          ; :transitionLeave false}
                          ; ^{:key (str current-view)}
                          (current-view)]]
                        [:div.col]]]))))

(om/defui HelloWorld
          static om/IQueryParams
          (params [_]
                  {:limit 2})
          
          static om/IQuery
          (query [_]
                 '[:server-time
                   (:names {:limit ?limit})])
          
          Object
          (render [this]
                  (let [{:keys [names server-time]} (om/props this)]
                    (sablono/html
                      [:div
                       [:div (str "Hello, "
                                  (first names)
                                  (when (second names)
                                    (str " and " (second names))))]
                       [:button
                        {:onClick
                         (fn [e] (om/transact! this '[(prepend-name {:name "test"})]))}
                        "Click me!"]
                       [:div (str "Server time: " server-time)]]))))

(def hello-world (om/factory HelloWorld {:keyfn :db/id}))