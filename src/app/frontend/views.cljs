(ns app.frontend.views
  (:require [re-frame.core :as rf]
            [reagent-material-ui.components :as mui]
            [reagent.core :as r]
            [app.frontend.subs :as subs]
            [app.frontend.events :as events]
            [goog.string :as gstring]
            [clojure.string :as string]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Components
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn leading-zero
  [num]
  (gstring/format "%02d" num))

(defn current-datetime
  []
  (string/join ":" (drop-last (string/split (.toISOString (js/Date.)) ":"))))

(defn input-fields []
  (r/with-let [payable-to (r/atom "Company Name")
               amount     (r/atom "0")
               datetime   (r/atom (current-datetime))]
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
                                      [mui/input-adornment "₴"])}}]]
     [mui/table-cell
      [mui/grid {:container true
                 :justifyContent "space-between"
                 :style #js {:gridTemplateColumns "repeat (2, 1fr)"}}
       [mui/text-field {:type      "datetime-local"
                        :required  true
                        :label       "Required"
                        :value     @datetime
                        :on-change #(reset! datetime (.. % -target -value))}]
       [mui/button
        {:variant "outlined"
         :type    "submit"
         :on-click #(rf/dispatch
                     [::events/add-transaction
                      {:payable-to @payable-to
                       :amount     @amount
                       :date       (.valueOf (js/Date. @datetime))}])} "Submit"]]]]))

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
           [mui/table-cell (.toLocaleString (js/Date. date))]])])]))

(defn table
  []
  [mui/table-container
   [mui/table
    [mui/table-head
     [input-fields]]
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
