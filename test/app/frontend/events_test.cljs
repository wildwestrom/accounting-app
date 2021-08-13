(ns app.frontend.events-test
  (:require [app.frontend.events :as sut]
            [app.frontend.subs :as subs]
            [app.frontend.db :as db]
            [re-frame.core :as rf]
            [day8.re-frame.test :as rf-test]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest dummy-test
  (is (= true true)))

(defn test-fixtures
  []
  ;; change this coeffect to make tests start with nothing
  (rf/reg-cofx
   :local-store-todos
   (fn [cofx _]
     "Read in todos from localstore, and process into a map we can merge into app-db."
     (assoc cofx :local-store-todos
            (sorted-map)))))

(deftest handler
  (testing "Make sure the add-to-db handler adds an item to the db"
    (is (let [test-db [["some" "data" "here"]]
              test-tx ["some" "other" "data"]]
          (= [["some" "data" "here"]
              ["some" "other" "data"]]
             (rf/dispatch-sync [::sut/add-transcation test-db test-tx]))))))

;; (deftest init
;;   (rf-test/run-test-sync               ;; <-- add this
;;     ;; with the above macro this becomes a dispatch-sync
;;     ;; and app-db is isolated between tests
;;    (rf/dispatch [:initialise-db])
;;     ;; Define subscriptions to the app state
;;    (let [table (rf/subscribe [::subs/table])]
;;       ;;Assert the initial state
;;      (is (= db/default-db @table)))))

;; (deftest add-to-db
;;   (testing "Add a piece of data to the table"
;;     (let [db {:some "data"}
;;           event {:more "data"}
;;           result-db (sut/add-items db event)]
;;       (is (= {:some "data"
;;               :more "data"}
;;              result-db)))))
