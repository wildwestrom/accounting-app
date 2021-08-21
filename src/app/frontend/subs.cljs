(ns app.frontend.subs
  (:require [re-frame.core :as re-frame :refer [reg-sub]]))

(reg-sub
 ::table
 (fn [db _]
            (sort-by :date (:all-data db))))

(reg-sub
 ::current-month-total
 (fn [db _]
            (let [beginning-of-month (.valueOf
                                      (let [date (js/Date.)]
                                        (js/Date. (.getFullYear date)
                                                  (.getMonth date)
                                                  1)))
                  end-of-month       (.valueOf
                                      (let [date (js/Date.)]
                                        (js/Date. (.getFullYear date)
                                                  (+ 1 (.getMonth date))
                                                  1
                                                  0
                                                  0
                                                  -1)))]
              (apply + (map :amount
                            (filter #(<= beginning-of-month
                                         (:date %)
                                         end-of-month)
                                    (:all-data db)))))))
