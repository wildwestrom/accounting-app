(ns app.frontend.events-test
  (:require [app.frontend.events :as sut]
            [app.frontend.subs :as subs]
            [app.frontend.db :as db]
            [re-frame.core :as re-frame
             :refer [subscribe dispatch-sync dispatch]]
            [day8.re-frame.test :as rf-test]
            [cljs.test :refer-macros [deftest testing is]]))
;; (dispatch [::sut/add-transaction ["some" "other" "data"]])

(deftest handlers
  (let [table (subscribe [::subs/table])
        tx {:payable-to "Some company" :amount "13546" :date 1629072000000}]
    (rf-test/run-test-sync
     (testing "Initial db works:"
       (dispatch [::sut/initialise-db])
       (is (= @table
              (:all-data db/default-db))))
     (testing "Adding to db works:"
       (dispatch [::sut/add-transaction tx])
       (is (= @table
              (conj (:all-data db/default-db) tx)))))))
