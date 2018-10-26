(ns t.cljs.components.nav
  (:require 
    [t.cljs.components :as c]
    [reagent.core :as r]))

(def logo-src "logo.png")

(def css 
  {::nav {:height "3rem"
          :width "100%"
          :background-color :white
          :border-bottom-width "1px"
          :border-style :solid
          :border-color (c/pkc-colors :light-gray)}
   ::width-container {:padding "0.5rem"
                      :height "100%"
                      :margin "0 auto"
                      :max-width "1200px"
                      :display :flex
                      :justify-content :space-between
                      :align-items :center}

   ::logo {}
   ::progress {:position :absolute
               :top 0
               :left 0}})

(defn logo [height]
  [:img {:src logo-src
         :style {:height height}}])

(defn nav [title]
  [:div {:style (css ::nav)}
   [:div {:style (css ::width-container)}
    [logo "1.5rem"]
    [c/icon :bars]]])
