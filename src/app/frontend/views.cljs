(ns app.frontend.views
  (:require [re-frame.core :as rf]
            [reagent-material-ui.components :as mui]
            [reagent.core :as r]
            [app.frontend.subs :as subs]
            [clojure.string :as str]
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
  [mui/table-row
   [mui/table-cell
    [mui/text-field {:type         "string"
                     :required     true
                     :placeholder  "Payable to..."
                     :defaultValue "Company Name"
                     :label        "Required"}]]
   [mui/table-cell
    [mui/text-field {:type         "number"
                     :required     true
                     :step         1
                     :placeholder  "Amount..."
                     :defaultValue "0"
                     :label        "Required"
                     :InputProps   {:end-adornment (r/as-element [mui/input-adornment {:position "end"} "₴"])}}]]
   [mui/table-cell
    [mui/text-field {:type         "date"
                     :required     true
                     :defaultValue (current-date)
                     :onChange     #(println (.. % -target -value))}]
    [mui/text-field {:type         "time"
                     :defaultValue (current-time)
                     :onChange     #(println (.. % -target -value))}]]
   [mui/table-cell
    [mui/button {:variant "outlined"
                 :type    "submit"}
     "Submit"]]])

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
           [mui/table-cell date]])])]))

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
