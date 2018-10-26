(ns t.cljs.components.button
  (:require 
    [t.cljs.components :as c]
    [reagent.core :as r]))

(def css
  {::button {:default
             {:padding "0.75rem 1rem"
              :background-color (c/pkc-colors :teal)
              :font-size (c/text-sizes :h6)
              :border-width "2px"
              :border-style :solid
              :border-radius "4px"
              :border-color (c/pkc-colors :teal)
              :outline :none
              :color (c/pkc-colors :white)
              :cursor :pointer
              :transition "all 0.2s ease-in-out"
              :display :flex
              :justify-content :center
              :align-items :center}

             ::dangerous?
             {:background-color (c/pkc-colors :red)
              :border-color (c/pkc-colors :red)
              :color (c/pkc-colors :white)}

             ::dangerous-hovered?
             {:background-color :red
              :border-color :red
              :color (c/pkc-colors :white)}

             ::ghosted?
             {:background-color :transparent
              :border-color :transparent
              :color (c/pkc-colors :teal)}

             ::secondary?
             {:background-color :transparent
              :border-color (c/pkc-colors :teal)
              :color (c/pkc-colors :teal)}
              
             ::hovered?
             {:color (c/pkc-colors :white)
              :border-color (c/pkc-colors :success)
              :background-color (c/pkc-colors :success)}

             ::disabled?
             {:color (c/pkc-colors :gray)
              :border-color (c/pkc-colors :light-gray)
              :background-color (c/pkc-colors :light-gray)
              :cursor :not-allowed}}

  
   ::icon-container {:display :inline-block
                     :margin-left "0.5rem"}
   
   ::icon {:default 
           {:color (c/pkc-colors :teal)}}})

(defn button [_ _]
  (let [hover-atom (r/atom false)]
    (fn [text {:keys [on-click
                      icon
                      style
                      disabled?
                      ghosted?
                      dangerous?
                      secondary?]}]
      (let [hovered? @hover-atom]
        [:button {:on-mouse-enter (c/on hover-atom)
                  :on-mouse-leave (c/off hover-atom)
                  :on-click (fn [e]
                              (when-not disabled?
                                (on-click e)))
                  :style (merge
                           (c/css-options (css ::button)
                                        ::ghosted? ghosted?
                                        ::secondary? secondary?
                                        ::hovered? hovered?
                                        ::dangerous? dangerous?
                                        ::dangerous-hovered? (and dangerous?
                                                                  hovered?)
                                        ::disabled? disabled?)
                           style)}
         text
         (when icon
           [:div {:style (css ::icon-container)}
             [c/icon (c/css-options (css ::icon)) icon]])]))))

(defn test-section []
  [c/section "Buttons"
   [:div
    [c/container "Primary Button"
     [button "Button Text"
      {:on-click #(js/alert "click")}]]
    [c/container "Button with Icon"
     [button "Button Text"
      {:icon :pen
       :on-click #(js/alert "click")}]]
    [c/container "Secondary Button"
     [button "Button Text"
      {:secondary? true
       :on-click #(js/alert "click")}]]    
    [c/container "Ghost Button"
     [button "Button Text"
      {:ghosted? true
       :on-click #(js/alert "click")}]]
    [c/container "Dangerous Button"
     [button "Button Text"
      {:dangerous? true
       :on-click #(js/alert "click")}]]
    [c/container "Disabled Button"
     [button "Button Text"
      {:disabled? true
       :on-click #(js/alert "click")}]]]])
