(ns app.frontend.views
  (:require [re-frame.core :as rf]
            [reagent-material-ui.components :as mui]
            [reagent-material-ui.cljs-time-utils :refer [cljs-time-utils]]
            [reagent-material-ui.pickers.mui-pickers-utils-provider :refer [mui-pickers-utils-provider]]
            [reagent-material-ui.pickers.date-time-picker :refer [date-time-picker]]
            [reagent.core :as r]
            [app.frontend.subs :as subs]
            [app.frontend.events :as events]
            [clojure.string :as string])
  (:import (goog.i18n DateTimeSymbols)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Components
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn input-fields []
  (r/with-let [payable-to (r/atom "Company Name")
               amount     (r/atom "0")
               datetime   (r/atom (js/Date.))]
    [mui/table-row
     [mui/table-cell
      [mui/text-field {:type        "string"
                       :required    true
                       :placeholder "Payable to..."
                       :value       @payable-to
                       :on-change   #(reset! payable-to (.. % -target -value))
                       :label       "Required"}]]
     [mui/table-cell
      [mui/text-field {:type        "number"
                       :required    true
                       :step        1
                       :placeholder "Amount..."
                       :value       @amount
                       :on-change   #(reset! amount (int (.. % -target -value)))
                       :label       "Required"
                       :InputProps  {:end-adornment
                                     (r/as-element
                                      [mui/input-adornment {:position "end"} "₴"])}}]]
     [mui/table-cell
      [mui-pickers-utils-provider {:utils  cljs-time-utils
                                   :locale DateTimeSymbols}
       [mui/grid {:container true
                  :justifyContent "space-between"
                  :style #js {:gridTemplateColumns "repeat (2, 1fr)"}}
        [date-time-picker {:value @datetime
                           :variant "inline"
                           :onChange #(reset! datetime (.. % -date))}]
        [mui/button
         {:variant "outlined"
          :type    "submit"
          :on-click #(rf/dispatch
                      [::events/add-transaction
                       {:payable-to @payable-to
                        :amount     @amount
                        :date       (.valueOf (js/Date. @datetime))}])} "Submit"]]]]]))

(defn table-body
  []
  (let [table (rf/subscribe [::subs/table])]
    [mui/table-body
     (for [row @table]
       [mui/table-row {:key (random-uuid)}
        (let [{:keys [payable-to amount date]} row]
          [:<>
           [mui/table-cell payable-to]
           [mui/table-cell
            [mui/grid {:container      true
                       :justifyContent "space-between"}
             [:span amount]
             [:span "₴"]]]
           [mui/table-cell (.toLocaleString (js/Date. date))]])])
     [input-fields]]))

(defn table
  []
  [mui/table-container
   [mui/table
    [mui/table-head
     [mui/table-row
      [mui/table-cell "Payable to:"]
      [mui/table-cell "Amount:"]
      [mui/table-cell "Date:"]]]
    [table-body]]])

(defn app
  []
  [:<>
   [mui/css-baseline]
   [mui/container
    [:h1 "Accounting App Demo"]
    [mui/paper {:elevation 3}
     [mui/container
      [mui/card-header {:title "Accounts Payable:"}]
      [mui/box
       [:span "Current spending for this month: "]
       [:span @(rf/subscribe [::subs/current-month-total]) "₴"]]]
     [table]]]])
