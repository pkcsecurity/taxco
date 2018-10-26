(ns t.cljs.body
  (:require [reagent.core :as r]
            [t.cljs.components :as c]
            [t.cljs.components.button :as btn]
            [t.cljs.components.gauge :as g]
            [t.cljs.components.nav :as nav]
            [t.cljs.components.progress :as p]))

(def css
  {::body {:height "100%"}})

(defn card [title progress]
  (let [timer-id (atom nil)
        show? (r/atom false)]
    (r/create-class
      {:component-did-mount
       (fn [this]
         (js/setTimeout (c/on show?) 5000))

       :reagent-render
       (fn [title progress]
         [:div {:style {:margin "1rem 0"
                        :display :inline-block
                        :padding "1rem 2rem"
                        :background-color :white
                        :text-align :center
                        :border-width "1px"
                        :border-style "solid"
                        :border-color (c/pkc-colors :light-gray)}}
          [:h5 {:style {:margin "1rem 0 2rem"}} title]
          (if @show?
            [g/gauge
             progress
             {:height-px 150
              :stroke-width-px 20
              :font-size (c/text-sizes :h3)
              :color (c/pkc-colors (cond
                                     (> progress 0.75) :success
                                     (> progress 0.50) :warning
                                     :else :error))
              :background-color (c/pkc-colors :light-gray)}]
            [:div
             [p/progress 150]])
          (when (< progress 0.75)
            [btn/button "Explode"
             {:style {:margin "0 auto"}
              :icon :fire}])])})))

(defn body []
  [:div {:style (css ::body)}
   [nav/nav]
   [:div
    {:style {:max-width "900px"
             :width "100%"
             :margin "0 auto"
             :padding-top "3rem"}}
    [:h4 "Josefina"]
    [:p "This machine could be compromised."]
    [:div {:style {:display :flex
                   :justify-content :space-around
                   :align-items :center}}
     [card  "Overall Health" 0.8]
     [card  "Martini Strength" 0.65]
     [card  "Flyness" 0.3]
     [card  "Freshness" 0.9]]]])


