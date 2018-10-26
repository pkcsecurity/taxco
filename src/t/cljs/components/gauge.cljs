(ns t.cljs.components.gauge
  (:require 
    [t.cljs.components :as c]
    [reagent.core :as r]))

(def css
  {::gauge {:position :relative}
   ::svg {:transform "rotate(-198deg)"}
   ::text {:position :absolute
           :top 0
           :left 0
           :z-index 1
           :text-align :center}})

(defn gauge [progress {:keys [height-px
                              stroke-width-px
                              color 
                              font-size
                              background-color]}]
  (let [outer-radius (/ height-px 2)
        inner-radius (- outer-radius (/ stroke-width-px 2))
        circumference (* (.-PI js/Math) 2 inner-radius)
        gray-circumference (* circumference 0.6)
        progress (* 0.6 progress)
        percent-circumference (* circumference progress)]
    [:div
     {:style (assoc (css ::gauge)
                    :height-px height-px 
                    :width height-px)}
     [:div {:style (assoc (css ::text)
                          :font-size font-size
                          :width height-px
                          :line-height (str height-px "px"))}
      (str (.round js/Math (* 100 (/ 1 0.6) progress)) "%")]
     [:svg {:style (css ::svg)
            :width height-px
            :height height-px
            :viewBox (str "0 0 " height-px " " height-px)}
      [:circle {:cx outer-radius
                :cy outer-radius
                :r inner-radius
                :fill :none
                :stroke background-color
                :stroke-width stroke-width-px
                :stroke-dasharray (str gray-circumference " " gray-circumference)}]
      [:circle {:cx outer-radius
                :cy outer-radius
                :r inner-radius
                :fill :none
                :stroke color 
                :stroke-width stroke-width-px
                :stroke-dasharray (str percent-circumference " " circumference)}]]]))
