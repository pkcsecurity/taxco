(ns t.clj.views.index
  (:require [environ.core :as environ]
            [hiccup.core :as html]))

(def title "Tazko")

(defn style [href & {:keys [integrity]}]
  [:link
   {:rel "stylesheet"
    :href href
    :integrity integrity
    :crossorigin :anonymous}])

(def app-js
  (if (= "development" (environ/env :environment)) 
    "/js/development/index.js" 
    "/js/release/index.js"))

(def styles
  ["https://cdnjs.cloudflare.com/ajax/libs/meyer-reset/2.0/reset.min.css"
   "https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.7.0/animate.min.css"
   "https://pro.fontawesome.com/releases/v5.4.1/css/all.css"])

(def index
  (html/html 
    {:mode :html}
    [:head
     [:title title]
     [:meta {:charset "utf-8"}]
     [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
     (for [src styles]
       (style src))]
    [:body
     [:div#reagent]
     [:script {:src app-js}]]))
