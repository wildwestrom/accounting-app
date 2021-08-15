(ns app.frontend.events-test
  (:require [app.frontend.events :as sut]
            [app.frontend.subs :as subs]
            [app.frontend.db :as db]
            [re-frame.core :as re-frame
             :refer [subscribe dispatch-sync dispatch]]
            [day8.re-frame.test :as rf-test]
            [cljs.test :refer-macros [deftest testing is]]
            [re-frame.core :as rf]))

(deftest handlers
  (rf-test/run-test-sync
   (let [table (subscribe [::subs/table])]
     (dispatch [::sut/initialise-db])
     (dispatch [::sut/add-transaction ["some" "other" "data"]])
     (is (= @table
            (conj (:all-data db/default-db) ["some" "other" "data"]))))))
