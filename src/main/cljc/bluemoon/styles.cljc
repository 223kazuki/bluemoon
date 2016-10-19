(ns bluemoon.styles
  (:require [garden.core :refer [css]]
            [garden.units :refer [px percent]]
            [garden.stylesheet :refer [at-keyframes rule]]
            #?(:clj [garden.def :refer [defstyles]])))

(def body
  [[:html :body {:font-family "'Helvetica Neue', Helvetica, Arial, sans-serif"
                 :height (percent 100)
                 :padding-top (px 40)}]
   [:body {:font-family "'メイリオ', Meiryo, Osaka, 'ＭＳ Ｐゴシック', 'MS PGothic', Helvetica, Arial, sans-serif"
           :font-size (px 16)
           :margin (px 0)
           :height (percent 100)
           :_overflow "hidden"}]

   ;; nav bar
   [:nav.navbar
    [:.container-fluid
     [:.navbar-header
      [:a.navbar-brand
       [:span {:color "white" :border-radius "5px" :background-color "black"
               :padding "2px 7px"}]]]]]

   ;;    [:#content-area
   ;;     [:.content-area-enter {:opacity 0.01
   ;;                            :transition "opacity .5s ease-in"}]
   ;;     [:.content-area-enter.content-area-enter-active {:opacity 1 }]
   ;;     [:.content-area-leave {:opacity 1
   ;;                            :transition "opacity .5s ease-in" }]
   ;;     [:.content-area-leave.content-area-leave-active {:opacity 0.01}]]

   ;; message
   [:.flash-message {:opacity 0
                     :z-index 100
                     :position "absolute" :top "70px" :right "15px"
                     :-webkit-animation "fadi 5s 1"
                     :-moz-animation "fadi 5s 1"
                     :-o-animation "fadi 5s 1"
                     :-webkit-animation-timing-function "ease-in"
                     :-moz-animation-timing-function "ease-in"
                     :-o-animation-timing-function "ease-in"}]
   [(at-keyframes :fadi [[(keyword (str "0.1%")) {:opacity 1 :display "block"}]
                         [(keyword "100%") {:opacity 0 :display "none"}]])]

   ;; wbs
   [:#wbs {:padding-left (px 0)
           :margin-left (px 0)}
    [:table {:width (percent 100)
             :table-layout "fixed"}
     [:tr {:border "solid"
           :width (percent 100)
           :border-width (px 1)
           :height (px 25)}
      [[:td {:border "solid"
             :overflow-x "hidden"
             :padding-left (px 5)
             :padding-right (px 5)
             :border-width (px 1)}]
       [:&#wbsBody
        [:td {:margin (px 0)
              :padding (px 0)}
         [:.scrollArea {:width (percent 100)
                        :overflow-x "scroll"
                        :overflow-y "scroll" ;;TODO modosu
                        :margin-left 0
                        :padding-left 0}
          [:.scrollInner {:padding 0
                          :margin 0}

           [:#dateHeader {:margin 0
                          :margin-bottom (px 2)
                          :padding 0}
            [:ul {:margin 0
                  :padding 0}
             [:li {:margin 0
                   :padding 0
                   :float "left"
                   :list-style-type "none"}]]]

           [:#taskBody {:position "relative"
                        :padding (px 0)}
            [[:.back {:position "absolute"
                      :z-index -1
                      :overflow "hidden"}]
             [:.sortable {:position "absolute"
                          :list-style-type "none"
                          :margin 0
                          :padding 0}
              [:li {:margin 0
                    :padding 0
                    :float "left"
                    :cursor "pointer"
                    :white-space "nowrap"}
               [:&:hover {:background-color "gray"
                          :opacity 0.5
                          :border-right "solid red 2px"
                          :z-index 1}]
               [:p {:margin 0
                    :height (px 25)
                    :padding 0}
                [:&.task {:border-radius (px 1)
                          :opacity 1
                          :background-color "#136FFF"
                          :border-right "solid red 2px"}]]]]]]]]]]
       [:&.selected {:background-color "#136FFF"
                     :opacity 0.7}]]]]]])

#?(:clj (defstyles screen body))
