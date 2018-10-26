(ns t.cljs.components
  (:require 
    [garden.core :as css]
    [reagent.core :as r]))

(def text-sizes
  {:h1 "3.157rem"
   :h2 "2.369rem"
   :h3 "1.777rem"
   :h4 "1.333rem"
   :h5 "1rem"
   :p "1rem"
   :h6 "0.75rem"
   :small "0.563rem"})

(def pkc-colors
  {:bg "#fbfaf3"
   :white "#fbfaf3"

   :teal "#1fb3a2"
   :accent "#1fb3a2"

   :gold "#fba217"
   :warning "#fba217"

   :green "#158764"
   :success "#158764"

   :red "#f34e39"
   :error "#f34e39"

   :light-gray "#b0b0ad"
   :gray "#838383"
   :dark-gray "#666666"

   :text "#353535"
   :black "#222222"})

(def text-css
  (for [[k sz] text-sizes]
    [k {:font-size sz}]))

(def core-css
  (css/css
    {:pretty-print? false}
    [:* {:box-sizing :border-box
         :font-family ["Work Sans" :sans-serif]}]
    [:html {:font-size "18px"}]
    [:html :body :#reagent :#app :#body {:height "100%"
                                         :width "100%"}]
    [:body {:background-color "#fbfaf3"
            :font-family ["Work Sans" :sans-serif]
            :color "#353535"}]
    [:p {:font-family [:georgia :serif]
         :line-height 1.35}]
    [".measure p:not(:first-of-type)" {:text-indent "1em"}]
    [:h2 {:line-height 1.2
          :font-weight 700
          :margin-bottom "0.25em"}]
    [:h3 {:line-height 1.3
          :font-weight 700
          :margin-bottom "0.2em"}]
    [:h4 {:line-height 1.3
          :font-weight 700
          :margin-bottom "0.2em"}]
    [:h5 {:line-height 1.3
          :font-weight 700
          :margin-bottom "0.2em"}]
    [:h6 {:line-height 1.3
          :margin-bottom "0.2em"}]
    [:.measure {:max-width "500px"
                :width "100%"}]
    text-css))

(defn style []
  [:style core-css])

(defn on [atom]
  (fn [_] (reset! atom true)))

(defn off [atom]
  (fn [_] (reset! atom false)))

(defn css-options [{:keys [default] :as elem-css} & kvs]
  (let [valid-options (map first (filter second (partition 2 kvs)))]
    (apply merge 
           default
           (keep elem-css valid-options))))

(def css
  {::container {:margin "1rem 0"}

   ::component-preview {:padding "1rem"
                        :border "1px solid"
                        :border-color (pkc-colors :light-gray)}
   
   ::section {:margin "2rem 0"}})

(defn container [title body]
  [:div.measure {:style (css ::container)}
   [:h4 title]
   [:div {:style (css ::component-preview)}
    body]])

(defn section [title body]
  [:div.measure {:style (css ::section)}
   [:h3 title]
   body])

(defn icon 
  ([icon-class] [icon {} icon-class])
  ([style icon-class]
   [:span.fa-stack 
    [:i.fas.fa-square.fa-stack-2x]
    [:i.far.fa-stack-1x.fa-inverse 
     {:style style
      :class (str "fa-" (name icon-class))}]]))

(def nop (constantly nil))
