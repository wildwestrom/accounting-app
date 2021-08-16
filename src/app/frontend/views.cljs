(ns app.frontend.views
  (:require [re-frame.core :as rf]
            [reagent-material-ui.components :as mui]
            [reagent.core :as r]
            [app.frontend.subs :as subs]
            [app.frontend.events :as events]
            [goog.string :as gstring]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Components
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn leading-zero
  [num]
  (gstring/format "%02d" num))

(defn current-date
  []
  (let [inst (js/Date.)]
    (str (.getFullYear inst)
         "-"
         (leading-zero (.getMonth inst))
         "-"
         (leading-zero (.getDate inst)))))

(defn current-time
  []
  (let [inst (js/Date.)]
    (str (leading-zero (.getHours inst))
         ":"
         (leading-zero (.getMinutes inst)))))

(defn input-fields []
  (r/with-let [payable-to (r/atom "Company Name")
               amount     (r/atom "0")
               date       (r/atom (current-date))
               time       (r/atom (current-time))]
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
                       :on-change   #(reset! amount (.. % -target -value))
                       :label       "Required"
                       :InputProps  {:end-adornment
                                     (r/as-element
                                      [mui/input-adornment {:position "end"}
                                       "₴"])}}]]
     [mui/table-cell
      [mui/text-field {:type      "date"
                       :required  true
                       :value     @date
                       :on-change #(reset! date (.. % -target -value))}]
      [mui/text-field {:type      "time"
                       :value     @time
                       :on-change #(reset! time (.. % -target -value))}]]
     [mui/table-cell
      [mui/button {:variant "outlined"
                   :type    "submit"
                   :on-click #(rf/dispatch [::events/add-transaction
                                            {:payable-to @payable-to
                                             :amount     @amount
                                             :date       (js/Date.parse (str @date " " @time))}])}
       "Submit"]]]))

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
     [mui/card [mui/card-header {:title "Accounts Payable:"}]]
     [table]]]])
