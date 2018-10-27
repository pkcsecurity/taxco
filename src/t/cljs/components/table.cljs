(ns t.cljs.components.table
  (:require [t.cljs.components :as c]
            [reagent.core :as r]))

(defn table [title data]
  [:div {:style {:text-align :left
                 :margin "2rem 0"}}
   [:h4 title]
   (cond
     (map? data) [:div
                  (for [[k v] (sort data)]
                    ^{:key k} [:div 
                               [:h5 (name k)]
                               [:p (str v)]])]
     :else [:p (str data)])])
