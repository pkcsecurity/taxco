(ns t.cljs.components.input
  (:require 
    [t.cljs.components :as c]
    [reagent.core :as r]))

(def css
  {::container {:width "100%"}

   ::label {:margin-bottom "0.25rem"}

   ::text {:default
           {:width "100%"
            :font-size "1rem"
            :line-height "2rem"
            :height "2rem"
            :padding "0 0.5rem"
            :outline :none
            :border-top :none
            :border-top-left-radius "4px"
            :border-top-right-radius "4px"
            :border-left :none
            :border-right :none
            :border-bottom-width "2px"
            :border-bottom-color (c/pkc-colors :dark-gray)
            :transition "border-color 0.2s ease-in-out"}

           ::focused?
           {:border-bottom-color (c/pkc-colors :teal)}

           ::errored?
           {:border-bottom-color (c/pkc-colors :error)}

           ::disabled?
           {:user-select :none
            :cursor :not-allowed
            :background-color (c/pkc-colors :light-gray)
            :border-bottom-color (c/pkc-colors :dark-gray)}}

   ::helper-text {:margin "0.5rem 0 0.5rem"
                  :color (c/pkc-colors :dark-gray)}

   ::error-text {:color (c/pkc-colors :error)
                 :margin-top "0.25rem"}

   ::text-container {:position :relative
                     :height "2rem"}

   ::icon-left {:default 
                {:position :absolute
                 :top 0
                 :left 0
                 :height "2rem"
                 :width "2rem"
                 :color (c/pkc-colors :gray)
                 :transition "color 0.2s ease-in-out"
                 :cursor :pointer}

                ::hovered?
                {:color (c/pkc-colors :teal)}

                ::errored?
                {:color (c/pkc-colors :error)
                 :cursor :not-allowed}

                ::disabled?
                {:color (c/pkc-colors :light-gray)
                 :cursor :not-allowed}}

   ::icon-right {:default 
                 {:position :absolute
                  :top 0
                  :right 0
                  :height "2rem"
                  :width "2rem"
                  :justify-content :flex-end
                  :color (c/pkc-colors :gray)
                  :transition "color 0.2s ease-in-out"
                  :cursor :pointer}

                 ::hovered?
                 {:color (c/pkc-colors :teal)}

                 ::errored?
                 {:color (c/pkc-colors :error)
                  :cursor :not-allowed}

                 ::disabled?
                 {:color (c/pkc-colors :light-gray)
                  :cursor :not-allowed}}


   ::input-container {:default 
                      {:position :absolute
                       :top 0
                       :bottom 0
                       :left 0
                       :right 0}

                      ::icon-left? 
                      {:left "2rem"}

                      ::icon-right? 
                      {:right "2rem"}}})

(defn input [_ _]
  (let [focus-atom (r/atom false)
        left-hover-atom (r/atom false)
        right-hover-atom (r/atom false)]
    (fn [value-atom {:keys [helper-text
                            error-handler
                            type
                            label
                            placeholder
                            disabled?
                            icon-left
                            icon-left-on-click
                            icon-right
                            icon-right-on-click]
                     :or {error-handler c/nop
                          icon-left-on-click c/nop
                          icon-right-on-click c/nop}}]
      (let [value @value-atom
            focused? @focus-atom
            error-text (error-handler value)
            errored? error-text
            left-hover? @left-hover-atom
            right-hover? @right-hover-atom]
        [:div {:style (css ::container)}
         [:h5 {:style (css ::label)}
          label]
         (when helper-text
           [:h6 {:style (css ::helper-text)}
            helper-text])
         [:div {:style (css ::text-container)}
          (when icon-left
            [:div {:on-mouse-enter (c/on left-hover-atom)
                   :on-mouse-leave (c/off left-hover-atom)
                   :on-click (fn [e]
                               (when-not (or disabled? errored?)
                                 (icon-left-on-click value e)))
                   :style (c/css-options (css ::icon-left)
                                         ::hovered? left-hover?
                                         ::errored? errored?
                                         ::disabled? disabled?)}
             [c/icon icon-left]])
          [:div {:style (c/css-options (css ::input-container)
                                       ::icon-left? icon-left
                                       ::icon-right? icon-right)}
           [:input
            {:style (c/css-options (css ::text)
                                   ::focused? focused?
                                   ::errored? errored?
                                   ::disabled? disabled?)
             :readOnly disabled?
             :placeholder placeholder
             :value value
             :type type
             :on-focus (c/on focus-atom)
             :on-blur (c/off focus-atom)
             :on-change (fn [e]
                          (reset! value-atom (.. e -target -value)))}]]
          (when icon-right
            [:div {:on-mouse-enter (c/on right-hover-atom)
                   :on-mouse-leave (c/off right-hover-atom)
                   :on-click (fn [e]
                               (when-not (or disabled? errored?)
                                 (icon-right-on-click value e)))
                   :style (c/css-options (css ::icon-right)
                                         ::hovered? right-hover?
                                         ::errored? errored?
                                         ::disabled? disabled?)}
             [c/icon icon-right]])]
         (when (and error-text (not disabled?))
           [:h6 {:style (css ::error-text)}
            error-text])]))))

(defn test-section []
  [c/section "Inputs"
   [:div
    [c/container "Basic Input"
     [input (r/atom "")
      {:type :text
       :label "Full Name"
       :placeholder "John Doe"}]]
    [c/container "Helper Text Input"
     [input (r/atom "")
      {:type :text
       :label "Full Name"
       :placeholder "John Doe"
       :helper-text "Here is some optional helper text explaining the input field, but
                    it should never exceed 100 chars."}]]
    [c/container "Disabled Input"
     [input (r/atom "")
      {:type :text
       :label "Full Name"
       :placeholder "John Doe"
       :disabled? true
       :helper-text "Here is some optional helper text explaining the input field, but
                    it should never exceed 100 chars."}]]
    [c/container "Error Input"
     [input (r/atom "")
      {:type :text
       :label "Full Name"
       :placeholder "John Doe"
       :error-handler (fn [v]
                        (when-not (= v "foobar")
                          "Field must be equal to \"foobar\""))}]]
    [c/container "Icon Left"
     [input (r/atom "")
      {:type :text
       :label "Full Name"
       :placeholder "John Doe"
       :icon-left :arrow-alt-circle-down
       :icon-left-on-click #(js/alert "icon left")}]]

    [c/container "Icon Right"
     [input (r/atom "")
      {:type :text
       :label "Full Name"
       :placeholder "John Doe"
       :icon-right :arrow-alt-circle-down
       :icon-right-on-click #(js/alert "icon right")}]]

    [c/container "Icon Left and Right"
     [input (r/atom "")
      {:type :text
       :label "Full Name"
       :placeholder "John Doe"
       :icon-left :pen
       :icon-left-on-click #(js/alert "icon left")
       :icon-right :arrow-alt-circle-down
       :icon-right-on-click #(js/alert "icon right")}]]

    [c/container "Icon Left with Error"
     [input (r/atom "")
      {:type :text
       :label "Full Name"
       :placeholder "John Doe"
       :icon-left :pen
       :icon-left-on-click #(js/alert "icon left")
       :error-handler (fn [v]
                        (when-not (= v "foobar")
                          "Field must be equal to \"foobar\""))}]]

    [c/container "Icon Left and Right with Error"
     [input (r/atom "")
      {:type :text
       :label "Full Name"
       :placeholder "John Doe"
       :icon-left :pen
       :icon-left-on-click #(js/alert "icon left")
       :icon-right :arrow-alt-circle-down
       :icon-right-on-click #(js/alert "icon right")
       :error-handler (fn [v]
                        (when-not (= v "foobar")
                          "Field must be equal to \"foobar\""))}]]

    [c/container "Kitchen Sink"
     [input (r/atom "")
      {:type :text
       :label "Full Name"
       :placeholder "John Doe"
       :helper-text "This component has everything in it, all the time."
       :icon-left :pen
       :icon-left-on-click #(js/alert "icon left")
       :icon-right :arrow-alt-circle-down
       :icon-right-on-click #(js/alert "icon right")
       :error-handler (fn [v]
                        (when-not (= v "foobar")
                          "Field must be equal to \"foobar\""))}]]

    [c/container "Kitchen Sink Disabled"
     [input (r/atom "")
      {:type :text
       :label "Full Name"
       :placeholder "John Doe"
       :helper-text "This component is disabled all the time, really a shame. Defaults
                    to hiding the error on disabled."
       :disabled? true
       :icon-left :pen
       :icon-left-on-click #(js/alert "icon left")
       :icon-right :arrow-alt-circle-down
       :icon-right-on-click #(js/alert "icon right")
       :error-handler (fn [v]
                        (when-not (= v "foobar")
                          "Field must be equal to \"foobar\""))}]]]])
