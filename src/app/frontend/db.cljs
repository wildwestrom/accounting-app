(ns app.frontend.db
  (:require [tick.core :as t]))

(def default-db
  {:all-data [{:payable-to "Test Data Co. Ltd."
               :amount 12499
               :date #time/instant "2021-05-01T00:00:00.000Z"}
              {:payable-to "Test Data Co. Ltd."
               :amount 12499
               :date #time/instant "2021-06-01T00:00:00.000Z"}
              {:payable-to "Test Data Co. Ltd."
               :amount 12499
               :date #time/instant "2021-07-01T00:00:00.000Z"}
              {:payable-to "Test Data Co. Ltd."
               :amount 12499
               :date #time/instant "2021-08-01T00:00:00.000Z"}
              {:payable-to "Test Data Co. Ltd."
               :amount 12499
               :date #time/instant "2021-08-05T00:00:00.000Z"}
              {:payable-to "Test Data Co. Ltd."
               :amount 199
               :date #time/instant "2021-08-07T00:00:00.000Z"}
              {:payable-to "Test Data Co. Ltd."
               :amount 1099
               :date #time/instant "2021-08-21T06:14:00.000Z"}]})
